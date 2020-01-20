package com.cxy.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.cxy.erp.biz.IEmpBiz;
import com.cxy.erp.biz.IMenuBiz;
import com.cxy.erp.entity.Emp;
import com.cxy.erp.entity.Menu;

public class ErpRealm extends AuthorizingRealm {
	// 注入empbiz
		private IEmpBiz empBiz;
		// 注入 menuBiz
		private IMenuBiz menuBiz;

		public void setMenuBiz(IMenuBiz menuBiz) {
			this.menuBiz = menuBiz;
		}

		public void setEmpBiz(IEmpBiz empBiz) {
			this.empBiz = empBiz;
		}

		// 认证的方法
		@Override
		protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
			// 得到令牌
			UsernamePasswordToken token = (UsernamePasswordToken) arg0;
			// 得到用户和密码
			String username = token.getUsername();
			String password = new String(token.getPassword());
			// 调用业务层的
			Emp emp = empBiz.findByusernameAndPwd(username, password);
			// 判断是不是为空
			if (emp == null) {
				System.out.println("认证失败");
				return null;
			}
			// 设置到缓冲中
			this.empBiz.doSetJedis(emp.getUuid());

			// 认证成功
			return new SimpleAuthenticationInfo(emp, password, getName());
		}

		// 授权的方法
		@Override
		protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
			// 取得当前的用户信息
			Emp emp = (Emp) arg0.getPrimaryPrincipal();
			// 实例化授权信息
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 遍历用户的菜单
			List<Menu> menus = menuBiz.getMenusByEmpuuid(emp.getUuid());
			for (Menu menu : menus) {
				// 授权指定的菜单
				info.addStringPermission(menu.getMenuname());
			}
			return info;
		}


}
