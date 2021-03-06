package com.fox.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕捉类
 * 
 * @author Bean
 *
 */
@ControllerAdvice
public class AllException {

	/// 角色權权限异常捕捉
	@ExceptionHandler(value = NullPointerException.class)
	@ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
	public String roleException(NullPointerException e) {
		System.out.println("---------------------->" + e);
		return "系统--数据出现异常！！！";
		// return "/abc";
	}

	// 其它异常异常捕捉
	@ExceptionHandler(value = Exception.class)
	@ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
	public String allException(Exception e) {
		System.out.println("---------------------->" + e);
		return "系統--出现异常！！！";
	}

}