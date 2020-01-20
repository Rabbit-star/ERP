package com.cxy.erp.biz;
import com.cxy.erp.entity.Goods;
import com.cxy.erp.entity.View_Num;
/**
 * 商品业务逻辑层接口
 * @author Administrator
 *
 */
public interface IGoodsBiz extends IBaseBiz<Goods>{
	/**
	 * 通过商品id和供应商id查询仓库中对应商品的数量
	 * @param uuid
	 * @param supplieruuid
	 * @return
	 */
	View_Num findCount(Long uuid,Long supplieruuid);
}

