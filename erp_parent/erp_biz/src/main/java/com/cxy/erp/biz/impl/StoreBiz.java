package com.cxy.erp.biz.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxy.erp.biz.IStoreBiz;
import com.cxy.erp.dao.IEmpDao;
import com.cxy.erp.dao.IStoreDao;
import com.cxy.erp.entity.Store;
/**
 * 仓库业务逻辑类
 * @author Administrator
 *
 */
public class StoreBiz extends BaseBiz<Store> implements IStoreBiz {

	private IStoreDao storeDao;
	private IEmpDao empDao;
	
	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
		super.setBaseDao(this.storeDao);
	}
	public IEmpDao getEmpDao() {
		return empDao;
	}
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	public List<Store> getListByPage(Store Store1,Store Store2,Object param,int firstResult, int maxResults){
		List<Store> list = super.getListByPage(Store1, Store2, param, firstResult, maxResults);
		Map<Long,String> empNameMap = new HashMap<Long, String>();
		for (Store store : list) {
			store.setEmpName(getEmpName(store.getEmpuuid(), empNameMap, empDao));
		}
		return list;
	}
	
}
