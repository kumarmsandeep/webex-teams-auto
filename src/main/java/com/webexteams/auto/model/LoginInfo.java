package com.webexteams.auto.model;

import org.openqa.selenium.WebDriver;

public class LoginInfo extends BasicLoginInfo {

	private WebDriver driver;

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
}
