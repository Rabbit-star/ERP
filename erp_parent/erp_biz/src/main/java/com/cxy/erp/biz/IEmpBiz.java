package com.cxy.erp.biz;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Menu;
import com.cxy.erp.entity.Tree;
/**
 * 员工业务逻辑层接口
 * @author Administrator
 *
 */
public interface IEmpBiz extends IBaseBiz<Emp>{

	/**
	 * 用户登陆
	 * @param username
	 * @param pwd
	 * @return
	 */
	Emp findByUsernameAndPwd(String username, String pwd);
	
	/**
	 * 修改密码
	 */
	void updatePwd(Long uuid, String oldPwd, String newPwd);
	
	/**
	 * 重置密码
	 */
	void updatePwd_reset(Long uuid, String newPwd);
	/**
	 * 获取用户角色
	 * @param uuid
	 * @return
	 */
	List<Tree> readEmpRoles(Long uuid);
	
	/**
	 * 更新用户的角色
	 * @param uuid
	 * @param checkedStr
	 */
	void updateEmpRoles(Long uuid, String checkedStr);
	
	/**
	 * 查询用户下的菜单权限
	 * @param uuid
	 * @return
	 */
	List<Menu> getMenusByEmpuuid(Long uuid);
	
	/**
	 * 获取用户菜单
	 * @param uuid
	 * @return
	 */
	Menu readMenusByEmpuuid(Long uuid);
	
	/**
	 * 导出数据
	 * @param os
	 */
	void export(OutputStream os, Emp t1);
	
	/**
	 * 数据导入
	 * @param is
	 */
	void doImport(InputStream is) throws IOException;

	Emp findByusernameAndPwd(String username, String password);

	void doSetJedis(Long uuid);
	/**
	 * 查看该用户下面是不是拥有新的信息
	 */
	boolean DoIsNews(Long uuid);
}

