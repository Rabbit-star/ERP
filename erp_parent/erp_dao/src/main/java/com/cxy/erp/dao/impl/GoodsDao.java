package com.cxy.erp.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.cxy.erp.dao.IGoodsDao;
import com.cxy.erp.entity.Goods;
import com.cxy.erp.entity.View_Num;
/**
 * 商品数据访问类
 * @author Administrator
 *
 */
public class GoodsDao extends BaseDao<Goods> implements IGoodsDao {

	/**
	 * 构建查询条件
	 * @param dep1
	 * @param dep2
	 * @param param
	 * @return
	 */
	public DetachedCriteria getDetachedCriteria(Goods goods1,Goods goods2,Object param){
		DetachedCriteria dc=DetachedCriteria.forClass(Goods.class);
		
		if(goods1!=null){
			if(null != goods1.getName() && goods1.getName().trim().length()>0){
				dc.add(Restrictions.like("name", goods1.getName(), MatchMode.ANYWHERE));
			}
			if(null != goods1.getOrigin() && goods1.getOrigin().trim().length()>0){
				dc.add(Restrictions.like("origin", goods1.getOrigin(), MatchMode.ANYWHERE));
			}
			if(null != goods1.getProducer() && goods1.getProducer().trim().length()>0){
				dc.add(Restrictions.like("producer", goods1.getProducer(), MatchMode.ANYWHERE));
			}
			if(null != goods1.getUnit() && goods1.getUnit().trim().length()>0){
				dc.add(Restrictions.like("unit", goods1.getUnit(), MatchMode.ANYWHERE));
			}
			//根据商品类型查询
			if(null != goods1.getGoodstype() && goods1.getGoodstype().getUuid() != null){
				dc.add(Restrictions.eq("goodstype", goods1.getGoodstype()));
			}
			if(null!=goods1.getOutprice()) {
				dc.add(Restrictions.ge("outprice",goods1.getOutprice()));
			}
			if(null!=goods2.getOutprice()) {
				dc.add(Restrictions.le("outprice",goods2.getOutprice()));
			}
			if(null!=param&&param!="") {
				Integer i = (Integer) param;
				if(i==1) {
					dc.addOrder(Order.asc("outprice"));
				}else {
					
					dc.addOrder(Order.desc("outprice"));
					
				}
					
			}

		}
		return dc;
	}
	
	/**
	 * 通过商品id和供应商id查询仓库中对应商品的数量
	 * @param uuid
	 * @param supplieruuid
	 * @return
	 */
	public View_Num findCount(Long uuid,Long supplieruuid){
		List<View_Num> list = (List<View_Num>) this.getHibernateTemplate().find("from View_Num where uuid=? and supplieruuid=?", uuid,supplieruuid);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
