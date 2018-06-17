package com.util;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 操作redis的工具
 *
 */
public class RedisPoolUtil {
	static JedisPool jedisPool = null;
	static{
		JedisPoolConfig config = new JedisPoolConfig();
		//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
        config.setMaxTotal(500); 
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
        config.setMaxIdle(5);  
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
        config.setMaxWaitMillis(1000 * 100);  
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
        config.setTestOnBorrow(true);  
		jedisPool= new JedisPool(config,"192.168.128.135",6379);
	}
	
	public static Jedis getJedis(){
		return jedisPool.getResource();
	}
	
	public static void releaseJedis(Jedis jedis){
		jedis.close();
	}
	
	//测试使用JedisPool
	public static void main(String[] args) {
		Jedis jedis = getJedis();
		
		//jedis.set(SerializeUtil.objToBytes("k1"),SerializeUtil.objToBytes("v1"));
		//jedis.expire(SerializeUtil.objToBytes("k1"), 30);

		Set<String> keys = jedis.keys("*");
		System.out.println(keys.size());
		
		System.out.println(jedis.ttl(SerializeUtil.objToBytes("k1")));
		System.out.println(jedis.get(SerializeUtil.objToBytes("k1")));
		releaseJedis(jedis);
	}
}
