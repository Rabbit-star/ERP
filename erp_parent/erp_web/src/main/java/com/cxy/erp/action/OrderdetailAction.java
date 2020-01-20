package com.cxy.erp.action;
import com.cxy.erp.biz.IOrderdetailBiz;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Orderdetail;
import com.cxy.erp.exception.ErpException;

/**
 * 订单明细Action 
 * @author Administrator
 *
 */
public class OrderdetailAction extends BaseAction<Orderdetail> {

	private IOrderdetailBiz orderdetailBiz;

	public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
		this.orderdetailBiz = orderdetailBiz;
		super.setBaseBiz(this.orderdetailBiz);
	}
	public Long storeuuid;
	public Long getStoreuuid() {
		return storeuuid;
	}
	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	public void doInStore() {
		Emp loginUser = getLoginUser();
		if(null==loginUser) {
			ajaxReturn(false, "亲!请您先登录");
			return;
		}
		try {
			orderdetailBiz.doInStore(getId(),storeuuid,loginUser.getUuid());
			ajaxReturn(true, "入库成功");
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			ajaxReturn(false, "入库失败");
		}
		
	}
	public void doOutStore() {
		Emp loginUser = getLoginUser();
		if(null==loginUser) {
			ajaxReturn(false, "亲,请您先登录");
			return;
		}
		try {
			orderdetailBiz.doOutStore(getId(),storeuuid,loginUser.getUuid());
			ajaxReturn(true, "出库成功");
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			ajaxReturn(false, "出库失败");
		}
		
	}

}
