package com.cxy.erp.action;
import com.alibaba.fastjson.JSON;
import com.cxy.erp.biz.IGoodsBiz;
import com.cxy.erp.entity.Goods;
import com.cxy.erp.entity.View_Num;

/**
 * 商品Action 
 * @author Administrator
 *
 */
public class GoodsAction extends BaseAction<Goods> {

	private IGoodsBiz goodsBiz;

	public void setGoodsBiz(IGoodsBiz goodsBiz) {
		this.goodsBiz = goodsBiz;
		super.setBaseBiz(this.goodsBiz);
	}
	
	private Long goodsuuid;
	public Long getGoodsuuid() {
		return goodsuuid;
	}
	public void setGoodsuuid(Long goodsuuid) {
		this.goodsuuid = goodsuuid;
	}
	/**
	 * 通过商品id和供应商id查询商品的数量
	 */
	public void findCount(){
		View_Num findCount = goodsBiz.findCount(goodsuuid, getId());
		write(JSON.toJSONString(findCount));
	}

}
