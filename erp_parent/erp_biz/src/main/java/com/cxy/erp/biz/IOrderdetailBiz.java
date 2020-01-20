package com.cxy.erp.biz;
import com.cxy.erp.entity.Orderdetail;
/**
 * 订单明细业务逻辑层接口
 * @author Administrator
 *
 */
public interface IOrderdetailBiz extends IBaseBiz<Orderdetail>{
	
	/**
	 * 采购入库
	 * @param uuid
	 * @param storeuuid
	 * @param empuuid
	 */
	void doInStore(Long uuid,Long storeuuid,Long empuuid);
	
	/**
	 * 销售出库
	 * @param uuid
	 * @param storeuuid
	 * @param empuuid
	 */
	void doOutStore(Long uuid,Long storeuuid,Long empuuid);


}

