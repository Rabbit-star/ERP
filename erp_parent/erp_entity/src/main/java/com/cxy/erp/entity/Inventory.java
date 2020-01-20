package com.cxy.erp.entity;
/**
 * 盘盈盘亏实体类
 * @author Administrator *
 */
public class Inventory {	
	private String goodname;// 商品名称
	private String storename;// 仓库名称
	private String creatername;// 登记人名称
	private String checkername;// 审核人名称

	public String getGoodname() {
		return goodname;
	}
	public void setGoodname(String goodname) {
		this.goodname = goodname;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getCreatername() {
		return creatername;
	}
	public void setCreatername(String creatername) {
		this.creatername = creatername;
	}
	public String getCheckername() {
		return checkername;
	}
	public void setCheckername(String checkername) {
		this.checkername = checkername;
	}
	/** 未审核 */
	public static final String STATE_CREATE = "0";
	/** 已审核 */
	public static final String STATE_CHECK = "1";
	/** 盘盈 */
	public static final String TYPE_IN = "1";
	/** 盘亏 */
	public static final String TYPE_OUT = "2";
	private Long uuid;//编号
	private Long goodsuuid;//商品
	private Long storeuuid;//仓库
	private Long num;//数量
	private String type;//类型
	private java.util.Date createtime;//登记日期
	private java.util.Date checktime;//审核日期
	private Long creater;//登记人
	private Long checker;//审核人
	private String state;//状态
	private String remark;//备注

	public Long getUuid() {		
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public Long getGoodsuuid() {		
		return goodsuuid;
	}
	public void setGoodsuuid(Long goodsuuid) {
		this.goodsuuid = goodsuuid;
	}
	public Long getStoreuuid() {		
		return storeuuid;
	}
	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	public Long getNum() {		
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemark() {		
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
