package com.cxy.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.cxy.erp.dao.IStoredetailDao;
import com.cxy.erp.entity.Storealert;
import com.cxy.erp.entity.Storedetail;
/**
 * 仓库库存数据访问类
 * @author Administrator
 *
 */
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Storedetail storedetail1,Storedetail storedetail2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Storedetail.class);
		//根据商品编号查询
		if(storedetail1!=null){
			dc.add(Restrictions.eq("goodsuuid",storedetail1.getGoodsuuid()));
			
		}
		//根据仓库编号查询
		if(storedetail1!=null) {
			dc.add(Restrictions.eq("storeuuid", storedetail1.getStoreuuid()));
		}
		return dc;
	}
	/**
	 * 获得仓库的实体
	 */
	public Storedetail storedetailNum(Long goodsuuid, Long storeuuid) {
		String hql = "from Storedetail where goodsuuid = ? and storeuuid = ?";
		List<Storedetail> list = (List<Storedetail>) this.getHibernateTemplate().find(hql,goodsuuid,storeuuid);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获取库存预警列表
	 * 
	 * 
	 */
	public List<Storealert> getStorealertList() {
		String hql = "from Storealert where storenum<outnum";
		return (List<Storealert>) this.getHibernateTemplate().find(hql);
	}

}
