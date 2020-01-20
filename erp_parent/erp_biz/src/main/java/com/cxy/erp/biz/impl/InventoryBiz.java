package com.cxy.erp.biz.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxy.erp.biz.IInventoryBiz;
import com.cxy.erp.dao.IEmpDao;
import com.cxy.erp.dao.IGoodsDao;
import com.cxy.erp.dao.IInventoryDao;
import com.cxy.erp.dao.IStoreDao;
import com.cxy.erp.entity.Inventory;
import com.cxy.erp.entity.Orders;
import com.cxy.erp.exception.ErpException;
/**
 * 盘盈盘亏业务逻辑类
 * @author Administrator
 *
 */
public class InventoryBiz extends BaseBiz<Inventory> implements IInventoryBiz {

	private IInventoryDao inventoryDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;
	private IEmpDao empDao;
	
	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}
	
	public void setInventoryDao(IInventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
		super.setBaseDao(this.inventoryDao);
	}
	/**
	 * 审核
	 * @param uuid 编号
	 * @param empUuid 审核员
	 */
	/*@RequiresPermissions("盘盈盘亏审核")*/
	public void doCheck(Long uuid, Long empUuid){
		
		//获取订单，进入持久化状态
		Inventory inventory = inventoryDao.get(uuid);
		//订单的状态
		if(!Inventory.STATE_CREATE.equals(inventory.getState())){
			throw new ErpException("亲！已经审核过了");
		}
		//1. 修改盘盈盘亏的状态
		inventory.setState(Orders.STATE_CHECK);
		//2. 审核的时间
		inventory.setChecktime(new Date());
		//3. 审核人
		inventory.setChecker(empUuid);
	}
	
	/**
	 * 添加订单
	 */
	@Override
	public void add(Inventory inventory) {
		// 新增的盘盈盘亏订单都是未审核的
		inventory.setState(inventory.STATE_CREATE);
		// 设置订单的时间为当前服务器的时间
		inventory.setCreatetime(new Date());
		// 保存订单
		inventoryDao.add(inventory);
	}
	
	public List<Inventory> getListByPage(Inventory t1,Inventory t2,Object param,int firstResult, int maxResults){
		//获取分页后的盘盈盘亏列表
		List<Inventory> inventoryList =  super.getListByPage(t1,t2,param,firstResult, maxResults);
		//缓存员工编号与员工的名称, key=员工的编号，value=员工的名称
		Map<Long, String> empNameMap = new HashMap<Long, String>();
		//商品编号与商品名称
		Map<Long, String> goodNameMap = new HashMap<Long, String>();
		//仓库编号与仓库名称
		Map<Long, String> storeNameMap = new HashMap<Long, String>();
		
		//循环，获取员工的名称
		for(Inventory i : inventoryList){
			//从缓存中取出员工名称
			i.setCreatername(getEmpName(i.getCreater(),empNameMap,empDao));
			i.setCheckername(getEmpName(i.getChecker(),empNameMap,empDao));
			i.setGoodname(getGoodsName(i.getGoodsuuid(), goodNameMap, goodsDao));
			i.setStorename(getStoreName(i.getStoreuuid(), storeNameMap, storeDao));		
		}
		return inventoryList;
	}
}
