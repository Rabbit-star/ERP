package com.cxy.erp.entity;

import java.util.List;

/**
 * 退货订单实体类
 * @author Administrator *
 */
public class Returnorders {	

	
	/**退货未审核*/
	public static final String STATE_CREATE ="0";
	/**退货已审核*/
	public static final String STATE_CHECK ="1";
	/**退货已入库*/
	public static final String STATE_END ="2";
	
	/**
	 * 退货已入库
	 */
	public static final String STATE_OUT = "3";
	/**
	 * 退货未入库
	 */
	public static final String STATE_NOT_OUT = "1";
	
	//类型  1：采购退货 2：销售退货
	/**采购退货*/
	public static final String TYPE_IN="1";
	/**销售退货*/
	public static final String TYPE_OUT="2";
	
	/** 采购退货未出库 */
	public static final String RETURN_NOT_OUT = "0";
	/** 采购退货已出库 */
	public static final String RETURN_OUT = "2";

	
	private Long uuid;//编号
	private java.util.Date createtime;//生成日期
	private java.util.Date checktime;//检查日期
	private java.util.Date endtime;//结束日期
	private String type;//订单类型
	
	private Long creater;//下单员
	private String createrName; //下单员姓名
	
	private Long checker;//审核员工编号
	private String checkerName; //审核员姓名
	
	private Long ender;//库管员
	private String enderName;//库管员姓名
	
	private Long supplieruuid;//供应商及客户编号
	private String supplierName;//供应商及客户名称
	
	private Double totalmoney;//合计金额
	private String state;//订单状态
	private Long waybillsn;//运单号
	private Long ordersuuid;//原订单编号
	
	//订单明细
	private List<Returnorderdetail> returnorderdetails;
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getEnderName() {
		return enderName;
	}
	public void setEnderName(String enderName) {
		this.enderName = enderName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	
	public List<Returnorderdetail> getReturnorderdetails() {
		return returnorderdetails;
	}
	public void setReturnorderdetails(List<Returnorderdetail> returnorderdetails) {
		this.returnorderdetails = returnorderdetails;
	}
	public Long getUuid() {		
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
	public java.util.Date getChecktime() {		
		return checktime;
	}
	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}
	public java.util.Date getEndtime() {		
		return endtime;
	}
	public void setEndtime(java.util.Date endtime) {
		this.endtime = endtime;
	}
	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getCreater() {		
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Long getChecker() {		
		return checker;
	}
	public void setChecker(Long checker) {
		this.checker = checker;
	}
	public Long getEnder() {		
		return ender;
	}
	public void setEnder(Long ender) {
		this.ender = ender;
	}
	public Long getSupplieruuid() {		
		return supplieruuid;
	}
	public void setSupplieruuid(Long supplieruuid) {
		this.supplieruuid = supplieruuid;
	}
	public Double getTotalmoney() {		
		return totalmoney;
	}
	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}
	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Long getWaybillsn() {		
		return waybillsn;
	}
	public void setWaybillsn(Long waybillsn) {
		this.waybillsn = waybillsn;
	}
	public Long getOrdersuuid() {		
		return ordersuuid;
	}
	public void setOrdersuuid(Long ordersuuid) {
		this.ordersuuid = ordersuuid;
	}

}
