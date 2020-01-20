package com.cxy.erp.dao;

import java.util.List;

import com.cxy.erp.entity.Storealert;
import com.cxy.erp.entity.Storedetail;
/**
 * 仓库库存数据访问接口
 * @author Administrator
 *
 */
public interface IStoredetailDao extends IBaseDao<Storedetail>{
	/**
	 * 获得仓库的具体数量
	 */
	Storedetail storedetailNum(Long goodsuuid, Long storeuuid);
	/**
	 * 获取库存预警列表
	 * 
	 * @return
	 */
	List<Storealert> getStorealertList();
}
