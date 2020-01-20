package com.cxy.erp.biz.impl;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cxy.erp.biz.IMenuBiz;
import com.cxy.erp.dao.IMenuDao;
import com.cxy.erp.entity.Menu;

import redis.clients.jedis.Jedis;
/**
 * 菜单业务逻辑类
 * @author Administrator
 *
 */
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

	private IMenuDao menuDao;
	private Jedis jedis;
	public Jedis getJedis() {
		return jedis;
	}
	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}
	public void setMenuDao(IMenuDao menuDao) {
		this.menuDao = menuDao;
		super.setBaseDao(this.menuDao);
	}
	/**
	 * 取得对应的ID
	 * @param empUuid
	 * @return
	 */
	public List<Menu> getMenusByEmpuuid(Long empUuid){
		//为了避免多次访问数据库,用redis来提高应用性能
		String menus = jedis.get("menuList_"+empUuid);//从缓存中取出值
		List<Menu> menuList = null;
		//判断是否存在值
			if(menus!=null&&menus.length()!=0){
				 menuList = JSON.parseArray(menus,Menu.class);
			}else{
				//不存的话访问数据库
				menuList = menuDao.getEmpRoleMenu(empUuid);
				//把值再次存到缓存中
				jedis.set("menuList_"+empUuid,JSON.toJSONString(menuList));
			}
		return menuList;
	}
}
