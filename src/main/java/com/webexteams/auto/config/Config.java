package com.webexteams.auto.config;

import java.util.concurrent.Executors;

import javax.servlet.ServletContextListener;
import javax.validation.constraints.NotNull;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import com.webexteams.auto.controller.CustomAsyncUncaughtExceptionHandler;
import com.webexteams.auto.controller.ExampleServletContextListener;
import com.webexteams.auto.driver.WebexTeamsLoginDriver;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAsync
@Configuration
@ComponentScan
@EnableRetry
@EnableSwagger2
public class Config implements AsyncConfigurer {

	@Bean(name = "ConcurrentTaskExecutor")
	public TaskExecutor asyncExecutor(@Value("${login.threadpool.size:2}") int threadPoolSize) {
		return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(threadPoolSize));
	}

	@NotNull
	@Bean
	public WebexTeamsLoginDriver webexLoginDriver(@Value("${login.browser:Chrome}") String browser,
			ApplicationContext context) {
		return BeanFactoryAnnotationUtils.qualifiedBeanOfType(context, WebexTeamsLoginDriver.class, browser);
	}

	@NotNull
	@Bean
	ServletListenerRegistrationBean<ServletContextListener> myServletListener(
			@Autowired ExampleServletContextListener exampleServletContextListener) {
		ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
		srb.setListener(exampleServletContextListener);
		return srb;
	}

	@Autowired
	private CustomAsyncUncaughtExceptionHandler customAsyncUncaughtExceptionHandler;

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return customAsyncUncaughtExceptionHandler;
	}

}
