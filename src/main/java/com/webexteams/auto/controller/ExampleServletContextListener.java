package com.webexteams.auto.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleServletContextListener implements ServletContextListener {

	@Autowired
	private LoginManager loginMgr;

	@Override
	public void contextInitialized(ServletContextEvent sce) {

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		loginMgr.closeAllSessions();
	}
}
