package com.cxy.erp.biz.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.crypto.hash.Md5Hash;

import com.alibaba.fastjson.JSON;
import com.cxy.erp.biz.IEmpBiz;
import com.cxy.erp.dao.IDepDao;
import com.cxy.erp.dao.IEmpDao;
import com.cxy.erp.dao.IMenuDao;
import com.cxy.erp.dao.IRoleDao;
import com.cxy.erp.entity.Dep;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Menu;
import com.cxy.erp.entity.Role;
import com.cxy.erp.entity.Tree;
import com.cxy.erp.exception.ErpException;

import redis.clients.jedis.Jedis;
/**
 * 员工业务逻辑类
 * @author Administrator
 *
 */
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {

	private int hashIterations = 2;
	private String isCache = "1"; // 代表有jedis记录该用户存在新的信息
	private IEmpDao empDao;
	private IRoleDao roleDao;
	public int getHashIterations() {
		return hashIterations;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}

	public IRoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public IMenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public Jedis getJedis() {
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public IDepDao getDepDao() {
		return depDao;
	}

	public void setDepDao(IDepDao depDao) {
		this.depDao = depDao;
	}

	public IEmpDao getEmpDao() {
		return empDao;
	}

	private IMenuDao menuDao;
	private Jedis jedis;
	private IDepDao depDao;
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
	}
	
	/**
	 * 用户登陆
	 * @param username
	 * @param pwd
	 * @return
	 */
	public Emp findByUsernameAndPwd(String username, String pwd){
		//查询前先加密
		pwd = encrypt(pwd, username);
		System.out.println(pwd);
		return empDao.findByUsernameAndPwd(username, pwd);
	}

	/**
	 * 修改密码
	 */
	public void updatePwd(Long uuid, String oldPwd, String newPwd) {
		//取出员工信息
		Emp emp = empDao.get(uuid);
		//加密旧密码
		String encrypted = encrypt(oldPwd, emp.getUsername());
		//旧密码是否正确的匹配
		if(!encrypted.equals(emp.getPwd())){
			//抛出 自定义异常
			throw new ErpException("旧密码不正确");
		}		
		empDao.updatePwd(uuid, encrypt(newPwd,emp.getUsername()));
	}
	
	/**
	 * 新增员工
	 */
	public void add(Emp emp){
		//String pwd = emp.getPwd();
		// source: 原密码
		// salt:   盐 =》扰乱码
		// hashIterations: 散列次数，加密次数
		//Md5Hash md5 = new Md5Hash(pwd, emp.getUsername(), hashIterations);
		//取出加密后的密码
		//设置初始密码
		String newPwd = encrypt(emp.getPwd(), emp.getUsername());
		System.out.println(newPwd);
		//设置成加密后的密码
		emp.setPwd(newPwd);
		//保存到数据库中
		super.add(emp);
	}
	
	/**
	 * 重置密码
	 */
	public void updatePwd_reset(Long uuid, String newPwd){
		//取出员工信息
		Emp emp = empDao.get(uuid);
		empDao.updatePwd(uuid, encrypt(newPwd,emp.getUsername()));
	}
	
	
	/**
	 * 获取用户角色
	 * @param uuid
	 * @return
	 */
	public List<Tree> readEmpRoles(Long uuid){
		List<Tree> treeList = new ArrayList<Tree>();
		//获取用户信息
		Emp emp = empDao.get(uuid);
		//获取用户下的角色列表
		List<Role> empRoles = emp.getRoles();
		//获取所有角色列表
		List<Role> rolesList = roleDao.getList(null, null, null);
		Tree t1 = null;
		for(Role role : rolesList){
			t1 = new Tree();
			//转换成string类型
			t1.setId(String.valueOf(role.getUuid()));
			t1.setText(role.getName());
			//判断是否需要勾选中,用户是否拥有这个角色
			if(empRoles.contains(role)){
				t1.setChecked(true);
			}
			treeList.add(t1);
		}
		return treeList;
	}
	
	/**
	 * 更新用户的角色
	 * @param uuid
	 * @param checkedStr
	 */
	public void updateEmpRoles(Long uuid, String checkedStr){
		//获取用户信息
		Emp emp = empDao.get(uuid);
		//清空该用户下的所有角色
		emp.setRoles(new ArrayList<Role>());
		
		String[] ids = checkedStr.split(",");
		Role role = null;
		for(String id : ids){
			role = roleDao.get(Long.valueOf(id));
			//设置用户的角色
			emp.getRoles().add(role);
		}
		try {
			//清除缓存中当前用户的菜单权限，为了让它重新从数据库获取最新的权限信息
			jedis.del("menuList_" + uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 加密
	 * @param source
	 * @param salt
	 * @return
	 */
	private String encrypt(String source, String salt){
		Md5Hash md5 = new Md5Hash(source, salt, hashIterations);
		return md5.toString();
	}
	/**
	 * 复制menu
	 * @param src
	 * @return
	 */
	private Menu cloneMenu(Menu src){
		Menu menu = new Menu();
		menu.setIcon(src.getIcon());
		menu.setMenuid(src.getMenuid());
		menu.setMenuname(src.getMenuname());
		menu.setUrl(src.getUrl());
		menu.setMenus(new ArrayList<Menu>());
		return menu;
	}

	/**
	 * 导出
	 */
	@Override
	public void export(OutputStream os, Emp t1) {
		// 根据查询条件获取商品表
				List<Emp> empList = super.getList(t1, null, null);
				// 创建excel工作簿
				HSSFWorkbook wk = new HSSFWorkbook();
				HSSFSheet sheet = wk.createSheet("员工");
				// 写入表头
				HSSFRow row = sheet.createRow(0);
				// 定义好每一列的标题
				String[] headerNames = { "登陆名", "真实姓名", "性别", "邮件地址", "联系电话", "地址", "出生年月日","部门编号"};
				// 指定每一列的宽度
				int[] columnWidths = { 4000, 4000, 8000, 4000, 4000, 4000, 4000, 4000 };
				// 创建一个单元格
				HSSFCell cell = null;
				for (int i = 0; i < headerNames.length; i++){
					cell = row.createCell(i);
					// 这是单元格里面的值,根据下标设置
					cell.setCellValue(headerNames[i]);
					// 设置单元格的宽度
					sheet.setColumnWidth(i, columnWidths[i]);
				}
				// 写入内容
				int i = 1;
				for (Emp emp : empList) {
					row = sheet.createRow(i);
					// 必须按照hderarNames 的顺序来
					row.createCell(0).setCellValue(emp.getUsername());//登陆名
					row.createCell(1).setCellValue(emp.getName());// 真实姓名
					if(emp.getGender()==1){
						row.createCell(2).setCellValue("男");//性别
					}
					if(emp.getGender()==0){
						row.createCell(2).setCellValue("女");//性别
					}
					row.createCell(3).setCellValue(emp.getEmail());//邮件地址
					row.createCell(4).setCellValue(emp.getTele());//联系电话
					row.createCell(5).setCellValue(emp.getAddress());//地址
					row.createCell(6).setCellValue(emp.getBirthday());//出生年月日
					row.createCell(7).setCellValue(emp.getDep().getName());//部门编号
					i++;
				}
				try {
					// 写入到输出中
					wk.write(os);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						// 关闭工作薄
						wk.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	}

	/**
	 * 导入数据
	 */
	@Override
	public void doImport(InputStream is) throws IOException {
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(is);
			HSSFSheet sheet = wb.getSheetAt(0);
			String type = "";
			if ("员工".equals(sheet.getSheetName())) {
				type = "员工";
			} else {
				throw new ErpException("名字不对,请重新提交");
			}
			// 读取数据
			// 最后一行的行号
			int lastRow = sheet.getLastRowNum();
			Emp emp = null;
			for (int i = 1; i < lastRow; i++) {
				emp = new Emp();
				emp.setUsername(sheet.getRow(i).getCell(0).getStringCellValue());//设置登陆名
				// 判断是否已经存在,通过名称来判断
				List<Emp> list = empDao.getList(null, emp, null);
				if (list.size() > 0) {
					//存在该用户
					emp = list.get(0);
				}
				List<Dep> depList = depDao.getList(null, null, null);
				Map<String, Dep> map = new HashMap<String,Dep>();
				for (Dep dep2 : depList) {
					map.put(dep2.getName(), dep2);
				}
				emp.setName(sheet.getRow(i).getCell(1).getStringCellValue());//设置真实姓名
				String gender = sheet.getRow(i).getCell(2).getStringCellValue();
				if ("男".equals(gender)) {//设置性别
					emp.setGender(1l);
				}else {
					emp.setGender(0l);
				}
				emp.setEmail(sheet.getRow(i).getCell(3).getStringCellValue());//设置邮箱
				emp.setTele(sheet.getRow(i).getCell(4).getStringCellValue());//设置电话
				emp.setAddress(sheet.getRow(i).getCell(5).getStringCellValue());//设置地址
				emp.setBirthday(sheet.getRow(i).getCell(6).getDateCellValue());
				String name = sheet.getRow(i).getCell(7).getStringCellValue();
				emp.setDep(map.get(name));
				if (list.size() == 0) {
					empDao.add(emp);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (null != wb) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<Menu> getMenusByEmpuuid(Long uuid) {
		//1. 尝试着从缓存里取出menuList
				String menuListJson = jedis.get("menuList_" + uuid);
				List<Menu> menuList = null;
				if(null != menuListJson){
					System.out.println("从缓存中取出menuList");
					//3. 缓存中已经存在，取出后再转成List对象
					menuList = JSON.parseArray(menuListJson, Menu.class);
				}else{
					System.out.println("从数据库查询出menuList");
					//第一次查询
					//2. Jedis不支持对象的存储，支持字符串的存储，所以，当第一次存入缓存的时候，转成JSON字符串存进去
					menuList = empDao.getMenusByEmpuuid(uuid);
					jedis.set("menuList_" + uuid, JSON.toJSONString(menuList));
				}
				return menuList;
	}

	@Override
	public Menu readMenusByEmpuuid(Long uuid) {
		//获取所有的菜单，做模板用的
		Menu root = menuDao.get("0");
		//用户下的菜单集合
		List<Menu> empMenus = empDao.getMenusByEmpuuid(uuid);
		//根菜单
		Menu menu = cloneMenu(root);
		
		//循环匹配模板
		//一级菜单
		Menu _m1 = null;
		Menu _m2 = null;
		for(Menu m1 : root.getMenus()){
			_m1 = cloneMenu(m1);
			//二级菜单循环
			for(Menu m2 : m1.getMenus()){
				//用户包含有这个菜单
				if(empMenus.contains(m2)){
					//复制菜单
					_m2 = cloneMenu(m2);
					//加入到上级菜单下
					_m1.getMenus().add(_m2);
				}
			}
			//有二级菜单我们才加进来
			if(_m1.getMenus().size() > 0){
				//把一级菜单加入到根菜单下
				menu.getMenus().add(_m1);
			}
		}
		return menu;
	}

	@Override
	public Emp findByusernameAndPwd(String username, String password) {
		// 加密
		password = getMD5Hash(password, username);
		return empDao.findByusernameAndPwd(username, password);
	}

	@Override
	public void doSetJedis(Long uuid) {
		String jedisis = String.valueOf(uuid);
		// 判断jedis 有没有缓冲数据
		Long i = this.empDao.getStateCount(uuid);
		if (i > 0) {
			jedis.set(jedisis, isCache);
		}
	}

	@Override
	public boolean DoIsNews(Long uuid) {
		// jedis 中的缓冲的数据
				String jedisis = String.valueOf(uuid);
				// 判断jedis 有没有缓冲数据
				String is = jedis.get(jedisis);
				// 假如jets记录该用户存在新的信息
				/*
				 * if (is != null && is.equals(isCache)) { return true; }
				 */
				// 假如jets记录该用户存在新的信息
				if (is != null && is.equals(isCache)) {
					return true;
				}
				return false;

				// 假如存在数据 那么代表有新的信息
				/*
				 * Long i = this.empDao.getStateCount(uuid); if (i > 0) {
				 * jedis.set(jedisis, isCache); }
				 */
	}
	/**
	 * 通过shrio加密密码
	 * 
	 * @param pwd
	 * @param username
	 * @return
	 */
	public String getMD5Hash(String pwd, String username) {
		// 加密算法 第二个参数表示加的盐.第三个表示散列的次数
		Md5Hash md5Hash = new Md5Hash(pwd, username, hashIterations);
		return md5Hash.toString();
	}
}
