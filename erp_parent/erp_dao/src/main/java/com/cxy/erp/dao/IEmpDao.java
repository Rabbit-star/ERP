package com.cxy.erp.dao;

import java.util.List;

import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Menu;
/**
 * 员工数据访问接口
 * @author Administrator
 *
 */
public interface IEmpDao extends IBaseDao<Emp>{

	/**
	 * 用户登陆
	 * @param username
	 * @param pwd
	 * @return
	 */
	Emp findByUsernameAndPwd(String username, String pwd);
	
	void updatePwd(Long uuid, String newPwd);
	
	/**
	 * 查询用户下的菜单权限
	 * @param uuid
	 * @return
	 */
	List<Menu> getMenusByEmpuuid(Long uuid);

	Emp findByusernameAndPwd(String username, String password);

	Long getStateCount(Long uuid);
}
