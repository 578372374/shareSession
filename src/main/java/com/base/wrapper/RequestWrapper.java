package com.base.wrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.base.constant.Constant;
import com.util.RedisPoolUtil;
import com.util.SerializeUtil;

import redis.clients.jedis.Jedis;

/**
 * 对原生Request进行包装
 *
 */
public class RequestWrapper extends HttpServletRequestWrapper{

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public HttpSession getSession() {
		//获取用户提交的jsessionId
		String jsessionId = null;
		Cookie[] cookies = getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("JSESSIONID".equals(cookie.getName())) {
					jsessionId = cookie.getValue();
				}
			}
		}
		
		if(jsessionId == null) {//用户第一次访问，之前没有任何session生成
			//创建原生session
			HttpSession session = super.getSession();
			SessionWrapper sessionWrapper = new SessionWrapper(session);
			return sessionWrapper;
		}else{//用户之前访问过
			//去redis取指定session
			Jedis jedis = RedisPoolUtil.getJedis();
			byte[] objToBytes = SerializeUtil.objToBytes(jsessionId);
			byte[] bs = jedis.get(objToBytes);
			if(bs == null) {//指定id的session不存在，可能session过期了，可能就没有登录
				jedis.close();
				//返回一个新的session
				HttpSession session = super.getSession();
				SessionWrapper sessionWrapper = new SessionWrapper(session);
				return sessionWrapper;
			}else {//指定的session存在
				HttpSession session = (HttpSession)SerializeUtil.bytesToObj(bs);
				//更新session过期时间，只要这里更新了，其他位置就不需要更新了，因为这里是拿session的入口
				jedis.expire(SerializeUtil.objToBytes(session.getId()), Constant.EXPIRE_TIME);
				jedis.close();
				return session;
			}
		}
	}
}
