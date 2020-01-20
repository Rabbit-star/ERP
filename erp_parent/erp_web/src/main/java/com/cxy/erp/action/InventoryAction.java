package com.cxy.erp.action;
import java.util.List;

import org.apache.shiro.authz.UnauthorizedException;

import com.cxy.erp.biz.IInventoryBiz;
import com.cxy.erp.biz.IStoredetailBiz;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Inventory;
import com.cxy.erp.entity.Storedetail;
import com.cxy.erp.exception.ErpException;

/**
 * 盘盈盘亏Action 
 * @author Administrator
 *
 */
public class InventoryAction extends BaseAction<Inventory> {

	private IInventoryBiz inventoryBiz;

	public void setInventoryBiz(IInventoryBiz inventoryBiz) {
		this.inventoryBiz = inventoryBiz;
		super.setBaseBiz(this.inventoryBiz);
	}
	/**
	 * 采购订单审核
	 */
	public void doCheck() {
		// 获取当前登陆用户
		Emp loginUser = getLoginUser();
		if (null == loginUser) {
			// 用户没有登陆，session已失效
			ajaxReturn(false, "亲！您还没有登陆");
			return;
		}
		try {
			// 调用审核业务
			inventoryBiz.doCheck(getId(), loginUser.getUuid());
			
			//修改库存数量
			editStoredetail();
			ajaxReturn(true, "审核成功");
		} catch (UnauthorizedException u) {
			ajaxReturn(false, "权限不足");
		} catch (ErpException e) {
			ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			ajaxReturn(false, "审核失败");
			e.printStackTrace();
		}
	}
	
	//修改库存数量
	private void editStoredetail() {
		//拿到这个审核的的实体
		Inventory inventory = inventoryBiz.get(getId());
		//通过拿到这个仓库的 
		Storedetail storedetail = storedetailBiz.storedetailNum(inventory.getGoodsuuid(), inventory.getStoreuuid());
		Long num = 0L;
		//如果状态是 盘盈
		if (inventory.getType().toString().equals(1 + "")) {
			try {
				num = storedetail.getNum() + inventory.getNum();
				storedetail.setNum(num);
				storedetailBiz.update(storedetail);
			} catch (NullPointerException e) {
				Storedetail storedetail1 = new Storedetail();
				storedetail1.setGoodsuuid(inventory.getGoodsuuid());
				storedetail1.setStoreuuid(inventory.getStoreuuid());
				storedetail1.setNum(inventory.getNum());
				storedetailBiz.add(storedetail1);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果状态是 盘亏
		if (inventory.getType().toString().equals(2 + "")) {
			num = storedetail.getNum() - inventory.getNum();
			storedetail.setNum(num);
			storedetailBiz.update(storedetail);
		}
	}

	private IStoredetailBiz storedetailBiz;

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
	}

	/**
	 * 添加订单
	 */
	@Override
	public void add() {
		Emp loginUser = getLoginUser();
		// 校验用户是否登录,订单的创建人就是当前用户
		if (null == loginUser) {
			ajaxReturn(false, "亲,你还没有登录!");
			return;
		}
		try {
			// 获取提交的订单
			Inventory inventory = getT();
			if (inventory.getNum() < 0) {
				ajaxReturn(false, "输入的数量小于0");
				return;
			}
			// 盘亏时,先判断数量是否出现大于库存其情况,如果是,抛出错误
			// System.out.println(inventory.getType().toString().equals(2+""));
			if (inventory.getType().toString().equals(2 + "")) {
				Storedetail storedetail = new Storedetail();
				storedetail.setGoodsuuid(inventory.getGoodsuuid());
				storedetail.setStoreuuid(inventory.getStoreuuid());
				// 查询storedetail表中对应仓库中对应商品的数量
				
				List<Storedetail> list = storedetailBiz.getList(storedetail, null, null);
				if (list.size() <= 0) {
					ajaxReturn(false, "库存不足!");
					return;
				} else {
					Long num = list.get(0).getNum();
					System.out.println("仓库取得的数量为:" + num);
					System.out.println("盘亏取得的数量为:" + inventory.getNum());
					// 盘亏:数量大于库存
					if (num < inventory.getNum()) {
						ajaxReturn(false, "库存不足!");
						return;
					}
				}
			}

			// 设置订单的创建人
			inventory.setCreater(loginUser.getUuid());
			// 提交订单
			inventoryBiz.add(inventory);
			Long uuid = inventory.getUuid();
			System.out.println("添加订单编号为"+uuid);
			String message = "成功添加编号为"+uuid.toString()+"的订单";
			ajaxReturn(true, message);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "添加订单失败");
		}
	}
}
