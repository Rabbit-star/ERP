package com.cxy.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.cxy.erp.dao.IMenuDao;
import com.cxy.erp.entity.Menu;
/**
 * 菜单数据访问类
 * @author Administrator
 *
 */
public class MenuDao extends BaseDao<Menu> implements IMenuDao {

	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Menu menu1,Menu menu2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Menu.class);
		return dc;
	}
	//查询用户权限的菜单
		@Override
		public List<Menu> getEmpRoleMenu(Long empUuid) {
			List<Menu> list = (List<Menu>) this.getHibernateTemplate().find("select m from Emp e join e.roles r join r.menus m where e.uuid=?",empUuid);
			return list;
		}
}
