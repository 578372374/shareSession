package com.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor{
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			Object attribute = request.getSession().getAttribute("user");
			if(attribute == null) {//登录已经过期,跳转到登录页面
				response.sendRedirect("login.jsp");
				return false;
			}
			return true;
		}
}
