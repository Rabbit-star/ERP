package com.cxy.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.cxy.erp.dao.IReportDao;

public class ReportDao extends HibernateDaoSupport implements IReportDao {

	@Override
	public List ordersReport(Date startDate, Date endDate) {
		String hql = "select new Map(gt.name as name,sum(ol.money) as y) "
				+ "from Goodstype gt, Goods gs, Orderdetail ol, Orders o "
				+ "where "
				+ "gs.goodstype=gt and ol.orders = o "
				+ "and ol.goodsuuid=gs.uuid and o.type='2' ";
		List<Date> dateList = new ArrayList<Date>();
		if(null != startDate){
			hql += "and o.createtime>=? ";
			dateList.add(startDate);
		}
		if(null != endDate){
			hql += "and o.createtime<=? ";
			dateList.add(endDate);
		}
		hql += "group by gt.name";
		//指定转换的类型
		Date[] param = new Date[0];
		//转成新的数组
		Date[] params = dateList.toArray(param);
		return getHibernateTemplate().find(hql,params);
	}
	
	@Override
	public List returnReport(Date startDate, Date endDate) {
		String hql = "select new Map(gt.name as name,sum(rl.num) as y) "
				+ "from Goodstype gt, Goods gs, Returnorderdetail rl, Returnorders r "
				+ "where "
				+ "gs.goodstype=gt and rl.returnorders = r "
				+ "and rl.goodsuuid=gs.uuid and r.type='2' ";
		List<Date> dateList = new ArrayList<Date>();
		if(null != startDate){
			hql += "and r.createtime>=? ";
			dateList.add(startDate);
		}
		if(null != endDate){
			hql += "and r.createtime<=? ";
			dateList.add(endDate);
		}
		hql += "group by gt.name";
		//指定转换的类型
		Date[] param = new Date[0];
		//转成新的数组
		Date[] params = dateList.toArray(param);
		return getHibernateTemplate().find(hql,params);
	}
	
	/**
	 * 销售趋势图
	 * @param year
	 * @return
	 */
	public List<Map<String,Object>> getSumMoney(int year){
		String hql = "select new Map(month(o.createtime) as name,sum(ol.money) as y) "
				+ "from Orderdetail ol, Orders o "
				+ "where ol.orders=o "
				+ "and o.type='2' and year(o.createtime)=? "
				+ "group by month(o.createtime)";
		/*String hql = "select new Map(month(o.createtime) as name,sum(o.totalmoney) as y) "
				+ "from Orders o "
				+ "where "
				+ "o.type='2' and year(o.createtime)=? "
				+ "group by month(o.createtime)";*/
		
		return (List<Map<String, Object>>) getHibernateTemplate().find(hql, year);
	}

}
