package com.webexteams.auto.controller;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import com.webexteams.auto.model.LoginInfo;

@Component
public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler{
	
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
		if (objects != null && objects.length > 0 && objects[0] instanceof LoginInfo) {
			LoginInfo loginInfo = (LoginInfo) objects[0];
			WebDriver webDriver = loginInfo.getDriver();
			if(webDriver != null) {
				webDriver.quit();
			}
		}
	}
}
