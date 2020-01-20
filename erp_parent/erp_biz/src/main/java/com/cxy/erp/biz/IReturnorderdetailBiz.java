package com.cxy.erp.biz;
import com.cxy.erp.entity.Returnorderdetail;
/**
 * 退货订单明细业务逻辑层接口
 * @author Administrator
 *
 */
public interface IReturnorderdetailBiz extends IBaseBiz<Returnorderdetail>{
	
	/**
	 * 采购退货出库
	 */
	void doreturnOutStore(Long id, Long storeuuid, Long uuid);
	
	/**
	 * 入库
	 * @param uuid 明细编号
	 * @param storeuuid 仓库编号
	 * @param empuuid 库管员工编号
	 */
	void doInStore(Long uuid,Long storeuuid, Long empuuid);
	
	/**
	 * 出库
	 * @param uuid 明细编号
	 * @param storeuuid 仓库编号
	 * @param empuuid 库管员工编号
	 */
	void doOutStore(Long uuid,Long storeuuid, Long empuuid);
}

