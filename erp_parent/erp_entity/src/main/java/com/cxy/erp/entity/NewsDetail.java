package com.cxy.erp.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 信息详情
 * 
 * @author Administrator
 *
 */
public class NewsDetail {

	private Long uuid; // 信息id
	private String remark; // 信息具体详情
	private String url; // 需要访问的地址
	private Date requestData; // 请求的日期

	@JSONField(serialize = false)
	private News news;

	// 没有浏览
	public final static String STATE_NO_READ = "0";
	// 已经浏览过
	public final static String STATE_READ = "1";

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getRequestData() {
		return requestData;
	}

	public void setRequestData(Date requestData) {
		this.requestData = requestData;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

}
