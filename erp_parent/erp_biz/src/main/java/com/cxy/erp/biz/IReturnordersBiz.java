package com.cxy.erp.biz;
import java.util.List;

import com.cxy.erp.entity.Returnorders;
/**
 * 退货订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IReturnordersBiz extends IBaseBiz<Returnorders>{
	/**
	 * 审核
	 * @param uuid 订单编号
	 * @param empUuid 审核员
	 */
	void doCheck(long uuid, Long empUuid);
	
	/**
	 * 销售退货审核
	 * @param id
	 * @param uuid
	 */
	void doSaleCheck(long uuid, Long empUuid);
	
	//获取分页后的退货订单列表
		List<Returnorders> getListByPage(Returnorders t1,Returnorders t2,Object param,int firstResult, int maxResults);
		//添加退货订单
		void addReturn(Returnorders returnorders);
		//审核
		void doreturnOrdersCheck(Long uuid, Long empUuid);
}

