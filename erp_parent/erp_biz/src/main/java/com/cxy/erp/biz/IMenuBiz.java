package com.cxy.erp.biz;
import java.util.List;

import com.cxy.erp.entity.Menu;
/**
 * 菜单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{
	/**
	 * 查询用户的菜单ID
	 * @param empUuid
	 * @return
	 */
	public List<Menu> getMenusByEmpuuid(Long empUuid);
}

