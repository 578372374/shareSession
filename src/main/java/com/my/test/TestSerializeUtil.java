package com.my.test;

import com.base.bean.User;
import com.util.SerializeUtil;

public class TestSerializeUtil {
	public static void main(String[] args) {
		User user = new User();
		user.setUsername("abc");
		user.setPassword("def");
		
		byte[] bytes = SerializeUtil.objToBytes(user);		
		
		User user2 = (User)SerializeUtil.bytesToObj(bytes);
		user2.setUsername("abc2");
		user2.setPassword("def2");
		
		System.out.println(user2);
	}

}
