package com.cxy.erp.action;

import com.cxy.erp.biz.IReturnorderdetailBiz;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Returnorderdetail;
import com.cxy.erp.exception.ErpException;

/**
 * 退货订单明细Action 
 * @author Administrator
 *
 */
public class ReturnorderdetailAction extends BaseAction<Returnorderdetail> {

	private IReturnorderdetailBiz returnorderdetailBiz;

	public void setReturnorderdetailBiz(IReturnorderdetailBiz returnorderdetailBiz) {
		this.returnorderdetailBiz = returnorderdetailBiz;
		super.setBaseBiz(this.returnorderdetailBiz);
	}
	
	//仓库编号
	private Long storeuuid;
	public Long getStoreuuid() {
		return storeuuid;
	}
	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	
	/**
	 * 销售退货入库
	 */
	public void doInStore(){
		Emp loginUser = getLoginUser();
		if(null == loginUser){
			//用户没有登陆，session已失效
			ajaxReturn(false, "亲！您还没有登陆");
			return;
		}
		try {
			//调用明细入库业务
			returnorderdetailBiz.doInStore(getId(), storeuuid, loginUser.getUuid());
			ajaxReturn(true, "入库成功");
		}catch (ErpException e){
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ajaxReturn(false, "入库失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 采购退货出库
	 */
	public void returnOutStore(){
		Emp loginUser = getLoginUser();
		if(null == loginUser){
			//用户没有登陆，session已失效
			ajaxReturn(false, "亲！您还没有登陆");
			return;
		}
		try {
			//调用明细出库业务
			returnorderdetailBiz.doreturnOutStore(getId(), storeuuid, loginUser.getUuid());
			ajaxReturn(true, "出库成功");
		}catch (ErpException e){
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ajaxReturn(false, "出库失败");
			e.printStackTrace();
		}
		
	}
	/**
	 * 退货入库的action
	 */
	public void doreturnInStore(){
		try {
			returnorderdetailBiz.doInStore( getId(),getLoginUser().getUuid(), storeuuid);
			ajaxReturn(true, "入库成功");
		} catch (ErpException ee) {
			ajaxReturn(false, ee.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ajaxReturn(false, "入库失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 退货出库的action
	 */
	public void doreturnOutStore(){
		try {
			returnorderdetailBiz.doOutStore(getId(), storeuuid, getLoginUser().getUuid());
			ajaxReturn(true, "出库成功");
		} catch (ErpException ee) {
			ajaxReturn(false, ee.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ajaxReturn(false, "出库失败");
			e.printStackTrace();
		}
	}

}
