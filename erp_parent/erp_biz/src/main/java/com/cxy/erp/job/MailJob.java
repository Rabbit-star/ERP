package com.cxy.erp.job;

import com.cxy.erp.biz.IStoredetailBiz;

/**
 * 自动发预警邮件
 * @author Administrator
 *
 */
public class MailJob {
	
	private IStoredetailBiz storedetailBiz;

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
	}
	private TestMail testMail;

	public TestMail getTestMail() {
		return testMail;
	}

	public void setTestMail(TestMail testMail) {
		this.testMail = testMail;
	}

	/**
	 * 发送预警邮件调用 的方法
	 */
	public void sendStorealertMail(){
		try {
			//storedetailBiz.sendStorealertMail();
			testMail.sendMail();
			System.out.println("发送预警邮件!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
