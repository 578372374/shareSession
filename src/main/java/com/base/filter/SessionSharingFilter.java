package com.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.wrapper.RequestWrapper;

/**
 * filter 将所有请求的request都装饰成我自己的request对象
 *
 */
public class SessionSharingFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
        try{
        	//将原生request转换成我的封装request
        	chain.doFilter(requestWrapper, httpServletResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		
	}

}
