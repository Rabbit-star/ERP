package com.cxy.erp.biz;
import java.util.List;

import com.cxy.erp.entity.Storealert;
import com.cxy.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑层接口
 * @author Administrator
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{
	/**
	 * 获得仓库的实体
	 */
	Storedetail storedetailNum(Long goodsuuid, Long storeuuid);

	/**
	 * 获取库存预警列表
	 * @return
	 */
	List<Storealert> getStorealertList();
	
	/**
	 * 发送库存预警邮件
	 */
	void sendStorealertMail();
	
}

