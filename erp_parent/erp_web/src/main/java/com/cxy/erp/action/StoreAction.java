package com.cxy.erp.action;
import com.cxy.erp.biz.IStoreBiz;
import com.cxy.erp.biz.impl.BaseBiz;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Store;

import oracle.net.aso.l;

/**
 * 仓库Action 
 * @author Administrator
 *
 */
public class StoreAction extends BaseAction<Store> {

	private IStoreBiz storeBiz;

	public void setStoreBiz(IStoreBiz storeBiz) {
		this.storeBiz = storeBiz;
		super.setBaseBiz(this.storeBiz);
	}
	/**
	 * 只显示当前登陆用户下的仓库
	 */
	public void myList(){
		if(null == getT1()){
			//构建查询条件
			setT1(new Store());
		}
		Emp loginUser = getLoginUser();
		//查找当前登陆用户下的仓库
		getT1().setEmpuuid(loginUser.getUuid());
		System.out.println(getT1());
		System.out.println(loginUser.getUuid());
		super.list();
	}
	public void add() {
		Emp loginuser = getLoginUser();
		if(loginuser==null) {
			return ;
		}else {
			getT().setEmpuuid(loginuser.getUuid());
			super.add();
		}
	}
}
