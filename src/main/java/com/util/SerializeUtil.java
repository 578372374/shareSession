package com.util;

import java.io.IOException;

import org.nustaq.serialization.FSTConfiguration;

import com.base.bean.User;
import com.base.wrapper.SessionWrapper;

/**
 * fst序列化工具
 * https://github.com/RuedigerMoeller/fast-serialization
 *
 */
public class SerializeUtil{
	
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	/**
	 * 请将常用的序列化/反序列化 对象注册在这里，可以加快速度
	 */
	static {
		conf.registerClass(SessionWrapper.class,String.class,User.class);
		//不实现Serializable接口也能序列化
		conf.setForceSerializable(true);
	}
	/**
	 * 对象转换成byte[]
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] objToBytes(Object obj){
		/*FSTObjectOutput fstOut = null;
		try {
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			fstOut = new FSTObjectOutput(bytesOut);
			fstOut.writeObject(value);
			fstOut.flush();
			return bytesOut.toByteArray();
		}finally {
			fstOut.close();
		}*/
		return conf.asByteArray(obj);
	}
	
	/**
	 * byte[]转换成对象
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object bytesToObj(byte[] bytes) {
		/*FSTObjectInput fstInput = null;
		try {
			fstInput = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return fstInput.readObject();
		}
		finally {
			fstInput.close();
		}*/
		return conf.asObject(bytes);
	}
}
