package com.cxy.erp.biz.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import com.cxy.erp.biz.IReturnordersBiz;
import com.cxy.erp.dao.IEmpDao;
import com.cxy.erp.dao.IReturnordersDao;
import com.cxy.erp.dao.ISupplierDao;
import com.cxy.erp.entity.Returnorderdetail;
import com.cxy.erp.entity.Returnorders;
import com.cxy.erp.exception.ErpException;
/**
 * 退货订单业务逻辑类
 * @author Administrator
 *
 */
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

	private IReturnordersDao returnordersDao;
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void setReturnordersDao(IReturnordersDao returnordersDao) {
		this.returnordersDao = returnordersDao;
		super.setBaseDao(this.returnordersDao);
	}

	/**
	 * 审核
	 * @param uuid 订单编号
	 * @param empUuid 审核员
	 */
	@RequiresPermissions("采购退货审核")
	public void doCheck(long uuid, Long empUuid) {
		//获取订单，进入持久化状态
		Returnorders rs = returnordersDao.get(uuid);
		//订单状态
		if (Returnorders.STATE_CHECK.equals(rs.getState())) {
			throw new ErpException("订单已审核不能重复审核");
		}
		//修改订单状态
		rs.setState(Returnorders.STATE_CHECK);
		//审核时间
		rs.setChecktime(new Date());
		//审核员
		rs.setChecker(empUuid);
	}
	
	/**
	 * 销售订单退货审核
	 */
	@RequiresPermissions("销售退货订单审核")
	public void doSaleCheck(long uuid, Long empUuid) {
		//获取订单，进入持久化状态
		Returnorders rs = returnordersDao.get(uuid);
		//订单状态
		if (Returnorders.STATE_CHECK.equals(rs.getState())) {
			throw new ErpException("订单已审核不能重复审核");
		}
		//修改订单状态
		rs.setState(Returnorders.STATE_CHECK);
		//审核时间
		rs.setChecktime(new Date());
		//审核员
		rs.setChecker(empUuid);
		
	}
	
	/**
	 * 新增退货订单
	 */
	public void add(Returnorders returnorders){
		//设置退货订单的状态,未审核
		returnorders.setState(Returnorders.STATE_CREATE);
		//设置创建时间
		returnorders.setCreatetime(new Date());
		
		//设置订单总金额
		double total=0;
		for (Returnorderdetail detail: returnorders.getReturnorderdetails()) {
			total+=detail.getMoney();
			//设置订单明细状态为未出库状态
			detail.setState(Returnorderdetail.STATE_NOT_OUT);
			
			//设置订单明细与订单的关系
			detail.setReturnorders(returnorders);
		}
		returnorders.setTotalmoney(total);
		
		returnordersDao.add(returnorders);
	}
	
	/**
	 * 重新getListByPage方法,显示员工姓名
	 */
	public List<Returnorders> getListByPage(Returnorders t1,Returnorders t2,Object param,int firstResult, int maxResults){
		//获取分页后的订单列表
		List<Returnorders> returnordersList =  super.getListByPage(t1,t2,param,firstResult, maxResults);
		//缓存员工编号与员工的名称, key=员工的编号，value=员工的名称
		Map<Long, String> empNameMap = new HashMap<Long, String>();
		//缓存供应商编号与员工的名称, key=供应商的编号，value=供应商的名称
		Map<Long, String> supplierNameMap = new HashMap<Long, String>();
		//循环，获取员工的名称
		for(Returnorders o : returnordersList){
			//从缓存中取出员工名称
			o.setCreaterName(getEmpName(o.getCreater(),empNameMap,empDao));
			o.setCheckerName(getEmpName(o.getChecker(),empNameMap,empDao));
			o.setEnderName(getEmpName(o.getEnder(),empNameMap,empDao));
			
			//供应商
			o.setSupplierName(getSupplierName(o.getSupplieruuid(),supplierNameMap,supplierDao));
		}
		return returnordersList;
	}
	@Override
	public void addReturn(Returnorders returnorders) {
		
		//1. 设置退货订单的状态
		returnorders.setState(Returnorders.STATE_CREATE);
		
		//3. 退单时间
		returnorders.setCreatetime(new Date());
		
		// 合计金额
		double total = 0;
		
		for(Returnorderdetail returndetail : returnorders.getReturnorderdetails()){
			System.out.println(" ");
			System.out.println(returndetail);
			System.out.println(" ");
			//累计金额
			total += returndetail.getMoney();
			//明细的状态
			returndetail.setState(Returnorderdetail.STATE_NOT_IN);
			//跟订单的关系
			returndetail.setReturnorders(returnorders);
		}
		//设置订单总金额
		returnorders.setTotalmoney(total);
		
		//保存到DB
		returnordersDao.add(returnorders);
		
	}
	@Override
	public void doreturnOrdersCheck(Long uuid, Long empUuid) {
		//获取订单，进入持久化状态
		Returnorders orders = returnordersDao.get(uuid);
		//订单的状态
		if(!Returnorders.STATE_CREATE.equals(orders.getState())){
			throw new ErpException("亲！该订单已经审核过了");
		}
		//1. 修改订单的状态
		orders.setState(Returnorders.STATE_CHECK);
		//2. 审核的时间
		orders.setChecktime(new Date());
		//3. 审核人
		orders.setChecker(empUuid);
		
	}
	
}
