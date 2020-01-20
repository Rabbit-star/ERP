package com.cxy.erp.biz.impl;
import com.cxy.erp.biz.IDepBiz;
import com.cxy.erp.dao.IDepDao;
import com.cxy.erp.entity.Dep;
/**
 * 部门业务逻辑类
 * @author Administrator
 *
 */
public class DepBiz extends BaseBiz<Dep> implements IDepBiz {

	private IDepDao depDao;
	
	public void setDepDao(IDepDao depDao) {
		this.depDao = depDao;
		super.setBaseDao(this.depDao);
	}
	
}
