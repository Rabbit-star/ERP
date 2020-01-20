package com.cxy.erp.biz;
import com.cxy.erp.entity.Inventory;
/**
 * 盘盈盘亏业务逻辑层接口
 * @author Administrator
 *
 */
public interface IInventoryBiz extends IBaseBiz<Inventory>{
	/**
	 * 审核
	 * @param uuid 订单编号
	 * @param empUuid 审核员
	 */
	void doCheck(Long uuid, Long empUuid);

}

