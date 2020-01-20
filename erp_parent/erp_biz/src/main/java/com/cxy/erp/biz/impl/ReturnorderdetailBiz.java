package com.cxy.erp.biz.impl;
import java.util.Date;
import java.util.List;

import com.cxy.erp.biz.IReturnorderdetailBiz;
import com.cxy.erp.dao.IReturnorderdetailDao;
import com.cxy.erp.dao.IStoredetailDao;
import com.cxy.erp.dao.IStoreoperDao;
import com.cxy.erp.dao.ISupplierDao;
import com.cxy.erp.entity.Orderdetail;
import com.cxy.erp.entity.Orders;
import com.cxy.erp.entity.Returnorderdetail;
import com.cxy.erp.entity.Returnorders;
import com.cxy.erp.entity.Storedetail;
import com.cxy.erp.entity.Storeoper;
import com.cxy.erp.exception.ErpException;
/**
 * 退货订单明细业务逻辑类
 * @author Administrator
 *
 */
public class ReturnorderdetailBiz extends BaseBiz<Returnorderdetail> implements IReturnorderdetailBiz {

	private IReturnorderdetailDao returnorderdetailDao;
	private IStoreoperDao storeoperDao;//库存操作记录
	private IStoredetailDao storedetailDao;//库存
	private ISupplierDao supplierDao;
	

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}

	public void setReturnorderdetailDao(IReturnorderdetailDao returnorderdetailDao) {
		this.returnorderdetailDao = returnorderdetailDao;
		super.setBaseDao(this.returnorderdetailDao);
	}
	
	/**
	 * 销售退货入库
	 */
	public void doInStore(Long uuid,Long storeuuid,Long empuuid){
		// TODO Auto-generated method stub
				System.out.println("销售退货查询的id为:"+uuid);
				Returnorderdetail orderdetail = this.returnorderdetailDao.get(uuid);
				System.out.println(orderdetail);
				if(!orderdetail.getState().equals(Returnorderdetail.STATE_NOT_IN)){
					throw new ErpException("已入过库了  不要在重复入库了");
				}
				//设置状态为已入库
				orderdetail.setState(Returnorderdetail.STATE_IN);
				//设置入库时间
				orderdetail.setEndtime(new Date());
				//设置入库的操作人员
				orderdetail.setEnder(empuuid);
				//设置入到那个仓库
				orderdetail.setStoreuuid(storeuuid);
				
				
				/**要更新仓库库存表**/
				Storedetail storedetail = new Storedetail();
				storedetail.setGoodsuuid(orderdetail.getGoodsuuid());
				storedetail.setStoreuuid(storeuuid);
				
				List<Storedetail> slist = storedetailDao.getList(storedetail, null, null);
				//判断库存表中是否存在此商品的记录   存在则增加商品的数量    不存在则新增记录
				if(slist.size()>0){
					slist.get(0).setNum(slist.get(0).getNum() == null?0:slist.get(0).getNum()+orderdetail.getNum());
				}else{
					storedetail.setNum(orderdetail.getNum());
					storedetailDao.add(storedetail);
				}
				
				/**要更新仓库操作记录表**/
				Storeoper storeoper = new Storeoper();
				storeoper.setEmpuuid(empuuid);
				storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
				storeoper.setNum(orderdetail.getNum());
				storeoper.setStoreuuid(storeuuid);
				storeoper.setType(Returnorderdetail.STATE_IN);
				storeoper.setOpertime(orderdetail.getEndtime());
				storeoperDao.add(storeoper);
				
				/**要更新订单表**/
				Returnorderdetail queryParam = new Returnorderdetail();
				Returnorders orders = orderdetail.getReturnorders();
				queryParam.setReturnorders(orders);
				//2. 查询订单下是否还存在状态为0的明细
				queryParam.setState(Orderdetail.STATE_NOT_IN);
				//3. 调用 getCount方法，来计算是否存在状态为0的明细
				long count = returnorderdetailDao.getCount(queryParam, null, null);
				if(count == 0){
					//4. 所有的明细都已经入库了，则要更新订单的状态，关闭订单
					orders.setState(Returnorders.STATE_END);
					orders.setEndtime(orderdetail.getEndtime());
					orders.setEnder(empuuid);
				}
				
				
	}
	//采购退货出库
	public void doreturnOutStore(Long id, Long storeuuid, Long empuuid) {
		//******** 第1步 更新商品明细**********
		//1. 获取明细信息
		Returnorderdetail returnDetail = returnorderdetailDao.get(id);
		//2. 判断明细的状态，一定是未入库的
		if(!Returnorderdetail.STATE_NOT_OUT.equals(returnDetail.getState())){
			throw new ErpException("亲！该明细已经出库了，不能重复出库");
		}
		//3. 修改状态为已出库
		returnDetail.setState(Returnorderdetail.STATE_OUT);
		//4. 出库时间
		returnDetail.setEndtime(new Date());
		//5. 库管员
		returnDetail.setEnder(empuuid);
		//6. 从哪个仓库出
		returnDetail.setStoreuuid(storeuuid);
		
		//*******第2 步 更新商品仓库库存*********
		//1. 构建查询的条件
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(returnDetail.getGoodsuuid());
		storedetail.setStoreuuid(storeuuid);
		//2. 通过查询 检查是否存在库存信息
		List<Storedetail> storeList = storedetailDao.getList(storedetail, null, null);
		if(storeList.size()>0){
			//存在的话，则应该减去它的数量
			Storedetail sd = storeList.get(0);
			sd.setNum(sd.getNum() - returnDetail.getNum());
			if(sd.getNum() < 0){
				throw new ErpException("库存不足,无法退货");
			}
		}else{
			throw new ErpException("库存不足");
		}
		
		//****** 第3步 添加操作记录*****
		//1. 构建操作记录
		Storeoper log = new Storeoper();
		log.setEmpuuid(empuuid);
		log.setGoodsuuid(returnDetail.getGoodsuuid());
		log.setNum(returnDetail.getNum());
		log.setOpertime(returnDetail.getEndtime());
		log.setStoreuuid(storeuuid);
		log.setType(Storeoper.TYPE_OUT);
		//2. 保存到数据库中
		storeoperDao.add(log);
		
		//**** 第4步 是否需要更新订单的状态********
	
		//1. 构建查询条件
		Returnorderdetail queryParam = new Returnorderdetail();
		Returnorders returnorders = returnDetail.getReturnorders();
		queryParam.setReturnorders(returnorders);
		//2. 查询订单下是否还存在状态为0的明细
		queryParam.setState(Returnorderdetail.STATE_NOT_OUT);
		//3. 调用 getCount方法，来计算是否存在状态为0的明细
		long count = returnorderdetailDao.getCount(queryParam, null, null);
		if(count == 0){
			//4. 所有的明细都已经出库了，则要更新订单的状态，关闭订单
			returnorders.setState(Returnorders.STATE_END);
			returnorders.setEndtime(returnDetail.getEndtime());
			returnorders.setEnder(empuuid);
			/*//客户
			Supplier supplier = supplierDao.get(returnorders.getSupplieruuid());
			//在线预约下单,获取运单号
			Long waybillsn = waybillWs.addWaybill(1l, supplier.getAddress(), supplier.getContact(), supplier.getTele(), "--");
			//更新运单号
			returnorders.setWaybillsn(waybillsn);*/
		}
		
	}

	@Override
	public void doOutStore(Long uuid, Long storeuuid, Long empuuid) {
		//******** 第1步 更新商品明细**********
		//1. 获取明细信息
		Returnorderdetail detail = returnorderdetailDao.get(uuid);
		//2. 判断明细的状态，一定是未入库的
		if(!Returnorderdetail.STATE_NOT_OUT.equals(detail.getState())){
			throw new ErpException("亲！该明细已经出库了，不能重复出库");
		}
		//3. 修改状态为已出库
		detail.setState(Returnorderdetail.STATE_OUT);
		//4. 出库时间
		detail.setEndtime(new Date());
		//5. 库管员
		detail.setEnder(empuuid);
		//6. 从哪个仓库出
		detail.setStoreuuid(storeuuid);
		
		//*******第2 步 更新商品仓库库存*********
		//1. 构建查询的条件
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(detail.getGoodsuuid());
		storedetail.setStoreuuid(storeuuid);
		//2. 通过查询 检查是否存在库存信息
		List<Storedetail> storeList = storedetailDao.getList(storedetail, null, null);
		if(storeList.size()>0){
			//存在的话，则应该累加它的数量
			Storedetail sd = storeList.get(0);
			sd.setNum(sd.getNum() - detail.getNum());
			if(sd.getNum() < 0){
				throw new ErpException("库存不足");
			}
		}else{
			throw new ErpException("库存不足");
		}
		
		//****** 第3步 添加操作记录*****
		//1. 构建操作记录
		Storeoper log = new Storeoper();
		log.setEmpuuid(empuuid);
		log.setGoodsuuid(detail.getGoodsuuid());
		log.setNum(detail.getNum());
		log.setOpertime(detail.getEndtime());
		log.setStoreuuid(storeuuid);
		log.setType(Storeoper.TYPE_OUT);
		//2. 保存到数据库中
		storeoperDao.add(log);
		
		//**** 第4步 是否需要更新订单的状态********
	
		//1. 构建查询条件
		Returnorderdetail queryParam = new Returnorderdetail();
		Returnorders orders = detail.getReturnorders();
		queryParam.setReturnorders(orders);
		//2. 查询订单下是否还存在状态为0的明细
		queryParam.setState(Returnorderdetail.STATE_NOT_OUT);
		//3. 调用 getCount方法，来计算是否存在状态为0的明细
		long count = returnorderdetailDao.getCount(queryParam, null, null);
		if(count == 0){
			//4. 所有的明细都已经出库了，则要更新订单的状态，关闭订单
			orders.setState(Returnorders.STATE_OUT);
			orders.setEndtime(detail.getEndtime());
			orders.setEnder(empuuid);
			
		}
		
	}
	
}
