package com.cxy.erp.dao;

import java.util.List;

import com.cxy.erp.entity.Menu;
/**
 * 菜单数据访问接口
 * @author Administrator
 *
 */
public interface IMenuDao extends IBaseDao<Menu>{
	/**
	 * 查询用户的权限菜单
	 * @param empUuid
	 * @return
	 */
	public List<Menu> getEmpRoleMenu(Long empUuid);
}
