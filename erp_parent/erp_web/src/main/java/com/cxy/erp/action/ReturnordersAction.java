package com.cxy.erp.action;
import java.util.List;

import org.apache.shiro.authz.UnauthorizedException;

import com.alibaba.fastjson.JSON;
import com.cxy.erp.biz.IReturnordersBiz;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Returnorderdetail;
import com.cxy.erp.entity.Returnorders;
import com.cxy.erp.exception.ErpException;

/**
 * 退货订单Action 
 * @author Administrator
 *
 */
public class ReturnordersAction extends BaseAction<Returnorders> {

	private IReturnordersBiz returnordersBiz;

	public void setReturnordersBiz(IReturnordersBiz returnordersBiz) {
		this.returnordersBiz = returnordersBiz;
		super.setBaseBiz(this.returnordersBiz);
	}

	/**
	 * 退货订单审核
	 */
	public void doCheck(){
		//获取当前用户
		Emp loginUser = getLoginUser();
		if (null == loginUser) {
			//用户没有登陆，session已失效
			ajaxReturn(false, "亲,您还未登入");
			return;
		}
		
		try {
			//调用业务层审核业务
			returnordersBiz.doCheck(getId(),loginUser.getUuid());
			ajaxReturn(true, "审核成功");
		} catch(UnauthorizedException u){
			ajaxReturn(false, "权限不足");
		} catch (ErpException e){
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ajaxReturn(false, "审核失败");
			e.printStackTrace();
		}
	}
	/**
	 * 销售订单退货审核
	 */
	public void doSaleCheck(){
		//获取当前用户
		Emp loginUser = getLoginUser();
		if (null == loginUser) {
			//用户没有登陆，session已失效
			ajaxReturn(false, "亲,您还未登录");
			return;
		}
		
		try {
			//调用业务层审核业务
			returnordersBiz.doSaleCheck(getId(),loginUser.getUuid());
			ajaxReturn(true, "审核成功");
		} catch(UnauthorizedException u){
			ajaxReturn(false, "权限不足");
		} catch (ErpException e){
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ajaxReturn(false, "审核失败");
			e.printStackTrace();
		}
	}
	
	//保存退货订单时所需要的后端传过来的json数据
	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	public void add(){
		//判断用户是否登录
		Emp loginUser = getLoginUser();
		if(loginUser==null){
			ajaxReturn(false, "亲！您还没有登陆");
			return ;
		}
		try {
			//获取订单
			Returnorders returnorders=getT();
			//设置创建者
			returnorders.setCreater(loginUser.getUuid());
			//把json对象转化为数组
			List<Returnorderdetail> detailList= JSON.parseArray(json, Returnorderdetail.class);
			returnorders.setReturnorderdetails(detailList);
			returnordersBiz.add(returnorders);
			ajaxReturn(true, "保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "保存失败!");
		}
		
		
	}

	/**
	 * 我的订单
	 */
	public void myListByPage(){
		if(null == getT1()){
			//构建查询条件
			setT1(new Returnorders());
		}
		Emp loginUser = getLoginUser();
		//登陆用户的编号查询
		getT1().setCreater(loginUser.getUuid());
		
		super.listByPage();
	}

	/**
	 * 添加订单
	 */
	public void addReturn(){
		
		Emp loginUser = getLoginUser();
		if(null == loginUser){
			//用户没有登陆，session已失效
			ajaxReturn(false, "亲！您还没有登陆");
			return;
		}
		try {
			//System.out.println(json);
			Returnorders returnOrders = getT();
			System.out.println("退货类型:"+returnOrders.getType());
			//订单创建者
			returnOrders.setCreater(loginUser.getUuid());
			
			List<Returnorderdetail> returnDetailList = JSON.parseArray(json,Returnorderdetail.class);
			//订单明细
			returnOrders.setReturnorderdetails(returnDetailList);
			//System.out.println(detailList.size());
			returnordersBiz.addReturn(returnOrders);
			ajaxReturn(true, "添加订单成功");
		} catch (Exception e) {
			ajaxReturn(false, "添加订单失败");
			e.printStackTrace();
		}
	}
	/**
	 * 退货订单审核
	 */
	public void returnOrdersCheck(){
		//获取当前登陆用户
		Emp loginUser = getLoginUser();
		if(null == loginUser){
			ajaxReturn(false, "亲！您还没有登陆");
			return;
		}
		try {
			//调用审核业务
			returnordersBiz.doreturnOrdersCheck(getId(), loginUser.getUuid());
			ajaxReturn(true, "退货审核成功");
		} catch(UnauthorizedException u){
			ajaxReturn(false, "抱歉,你暂时权限不足!");
		} catch (ErpException e){
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ajaxReturn(false, "退货审核失败");
			e.printStackTrace();
		}
	}
}
