package com.cxy.erp.biz.impl;
import com.cxy.erp.biz.IGoodsBiz;
import com.cxy.erp.dao.IGoodsDao;
import com.cxy.erp.entity.Goods;
import com.cxy.erp.entity.View_Num;
/**
 * 商品业务逻辑类
 * @author Administrator
 *
 */
public class GoodsBiz extends BaseBiz<Goods> implements IGoodsBiz {

	private IGoodsDao goodsDao;
	
	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
		super.setBaseDao(this.goodsDao);
	}
	/**
	 * 通过商品id和供应商id查询仓库中对应商品的数量
	 * @param uuid
	 * @param supplieruuid
	 * @return
	 */
	public View_Num findCount(Long uuid,Long supplieruuid){
		return goodsDao.findCount(uuid,supplieruuid);
	}

	
}
