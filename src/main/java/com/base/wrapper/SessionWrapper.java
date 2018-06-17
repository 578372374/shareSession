package com.base.wrapper;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.base.constant.Constant;
import com.util.RedisPoolUtil;
import com.util.SerializeUtil;

import redis.clients.jedis.Jedis;

/**
 * 自己的session
 * 直接把整个session存到redis里
 */
public class SessionWrapper implements HttpSession,Serializable {

	private static final long serialVersionUID = -5067759511085172852L;
	HttpSession session = null;
	//session.setAttribute()的容器 原生session反序列化后，不能调用setAttr 所以自己定义一个来做Attr的容器
	Map<String,Object> attrs = new ConcurrentHashMap<String,Object>();
	
	public Map<String, Object> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}
	
	public SessionWrapper() {
		
	}
	
	public SessionWrapper(HttpSession session) {
		this.session = session;
		//设置原生session有效期为配置文件中指定值
		session.setMaxInactiveInterval(Constant.EXPIRE_TIME);
	}
	
	//更新redis中的session
	public void flushRedis() {
		Jedis jedis = RedisPoolUtil.getJedis();
		jedis.set(SerializeUtil.objToBytes(session.getId()),SerializeUtil.objToBytes(this));
		jedis.close();
	}
	
	/**获取属性**/
	@Override
	public Object getAttribute(String name) {
		return attrs.get(name);
	}
	/**设置属性**/
	@Override
	public void setAttribute(String name, Object value) {
		attrs.put(name, value);
		flushRedis();
	}
	/**删除属性**/
	@Override
	public void removeAttribute(String name) {
		attrs.remove(name);
		flushRedis();
	}
	
	/**手动使session失效**/
	@Override
	public void invalidate() {
		session.invalidate();
		Jedis jedis = RedisPoolUtil.getJedis();
		jedis.del(SerializeUtil.objToBytes(session.getId()));
		jedis.close();
	}

	/**TODO 也需要同步操作redis,但非重要属性,也没找到set方法,后面再补充吧  session是否新建的**/
	@Override
	public boolean isNew() {
	   return session.isNew();
	}
	
	/**获取所有属性名**/
	@Override
	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}
	
	/**设置session有效期**/
	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
		//同步更新redis
		Jedis jedis = RedisPoolUtil.getJedis();
		jedis.expire(SerializeUtil.objToBytes(session.getId()), interval);
		jedis.close();
	}
	
	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	/**TODO 也需要同步操作redis,但没找到set方法,暂时返回当前时间吧**/
	@Override
	public long getLastAccessedTime() {
		return System.currentTimeMillis();
	}

	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Override
	public Object getValue(String name) {
		return session.getValue(name);
	}

	@Override
	public String[] getValueNames() {
		return session.getValueNames();
	}

	@Override
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	@Override
	public void removeValue(String name) {
		session.removeValue(name);
	}
}
