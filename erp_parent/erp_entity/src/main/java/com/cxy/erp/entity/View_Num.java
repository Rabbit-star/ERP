package com.cxy.erp.entity;

public class View_Num {
	private Long uuid;  //商品id
	private Long supplieruuid; //供应商id
	private Integer num;	//总数量
	
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public Long getSupplieruuid() {
		return supplieruuid;
	}
	public void setSupplieruuid(Long supplieruuid) {
		this.supplieruuid = supplieruuid;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
}
