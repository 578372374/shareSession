package com.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.bean.User;
import com.base.wrapper.SessionWrapper;
import com.util.SerializeUtil;

@Controller
public class TestController {
	@RequestMapping("/login")
	public String login(HttpServletRequest request,User user) {
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		return "main";
	}
	
	@RequestMapping("/setAttr")
	public String setAttr(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("abc", "我是abc");
		return "main";
	}
	
	@ResponseBody
	@RequestMapping("/getAttr")
	public Object getAttr(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object attribute = session.getAttribute("abc");
		return attribute;
	}
	
	@RequestMapping("/removeAttr")
	public String removeAttr(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("abc");
		return "main";
	}
	
	@RequestMapping("/invalidate")
	public String invalidate(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "main";
	}
	
	@RequestMapping("/test")
	public void testSerialize(HttpServletRequest request) {
		HttpSession session = new SessionWrapper(request.getSession());
		
		session.setAttribute("abc", "def");
		
		byte[] bytes = SerializeUtil.objToBytes(session);
		
		SessionWrapper session2 = (SessionWrapper)SerializeUtil.bytesToObj(bytes);
		System.out.println(session2.getId());
		System.out.println(session2.getAttribute("abc"));
		//此处有问题，无法调用setAttribute方法，需要进行封装
		session2.setAttribute("abc2", "def2");
		
		System.out.println(session2.getAttribute("abc"));
		System.out.println(session2.getAttribute("abc2"));
	}
}
