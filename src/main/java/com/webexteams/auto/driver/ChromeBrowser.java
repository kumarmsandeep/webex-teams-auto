package com.webexteams.auto.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("Chrome")
@Lazy
@Qualifier("Chrome")
public class ChromeBrowser extends WebexTeamsLoginDriver {

	@Value("${chrome.driver.path}")
	private String driverPath;
	
	@Value("${chrome.mode.headless:true}")
	private boolean isHeadless;

	@Override
	public WebDriver getDriver() {
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(isHeadless);
		options.addArguments("--incognit");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		return new ChromeDriver(options);
	}
}
