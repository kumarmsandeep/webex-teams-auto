package com.webexteams.auto.driver;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("Firefox")
@Lazy
@Qualifier("Firefox")
public class FirefoxBrowser extends WebexTeamsLoginDriver {

	@Value("${firefox.driver.path}")
	private String geckoDriverPath;

	@Override
	public WebDriver getDriver() {
		Path path = FileSystems.getDefault().getPath(geckoDriverPath);
		System.setProperty("webdriver.gecko.driver", path.toString());

		
		
		
		FirefoxOptions opts = new FirefoxOptions();
		opts.addArguments("-private");

		return new FirefoxDriver(opts);
	}

}
