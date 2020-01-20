package com.cxy.erp.dao;

import com.cxy.erp.entity.Goods;
import com.cxy.erp.entity.View_Num;
/**
 * 商品数据访问接口
 * @author Administrator
 *
 */
public interface IGoodsDao extends IBaseDao<Goods>{
	/**
	 * 通过供应商id查询仓库中对应商品的数量
	 * @param uuid
	 * @param supplieruuid
	 * @return
	 */
	View_Num findCount(Long uuid,Long supplieruuid);
}
