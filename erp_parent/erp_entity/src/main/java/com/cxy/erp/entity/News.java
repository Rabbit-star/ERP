package com.cxy.erp.entity;

import java.util.Date;
import java.util.List;

/**
 * 信息通知
 * 
 * @author Administrator
 *
 */
public class News {

	private Long uuid; // 信息id
	private Long requester;// 请求人
	private Long operer; // 操作人
	private String requesterName;
	private String opererName;
	private String opererobjectName;
	private Long opererobject;// 操作对象
	private String state;// 信息的状态 0代表 未读取 1 代表已经读取 2代表已经删除
	private String newstype; // 属于哪个类型
	private Date lastdate; // 最后通知时间

	private String newstate;// 代表是通知 还是回复

	// 一个信息通知下面有多个信息详情
	private List<NewsDetail> newsdetails;

	public static final String NEW_NOTIFY = "0"; // 代表为通知信息
	public static  final String NEW_REPLAY="1"; // 代表为回复信息

	// 代表入库通知
	public static final String NEW_STYPE_IN = "1";
	// 没有浏览
	public final static String STATE_NO_READ = "0";
	// 已经浏览过
	public final static String STATE_READ = "1";

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getOpererName() {
		return opererName;
	}

	public void setOpererName(String opererName) {
		this.opererName = opererName;
	}

	public String getOpererobjectName() {
		return opererobjectName;
	}

	public void setOpererobjectName(String opererobjectName) {
		this.opererobjectName = opererobjectName;
	}

	public List<NewsDetail> getNewsdetails() {
		return newsdetails;
	}

	public void setNewsdetails(List<NewsDetail> newsdetails) {
		this.newsdetails = newsdetails;
	}

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public Long getRequester() {
		return requester;
	}

	public void setRequester(Long requester) {
		this.requester = requester;
	}

	public Long getOperer() {
		return operer;
	}

	public void setOperer(Long operer) {
		this.operer = operer;
	}

	public Long getOpererobject() {
		return opererobject;
	}

	public void setOpererobject(Long opererobject) {
		this.opererobject = opererobject;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNewstype() {
		return newstype;
	}

	public void setNewstype(String newstype) {
		this.newstype = newstype;
	}

	public Date getLastdate() {
		return lastdate;
	}

	public void setLastdate(Date lastdate) {
		this.lastdate = lastdate;
	}

	public String getNewstate() {
		return newstate;
	}

	public void setNewstate(String newstate) {
		this.newstate = newstate;
	}

}
