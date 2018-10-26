package com.webexteams.auto.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.webexteams.auto.driver.WebexTeamsLoginDriver;
import com.webexteams.auto.model.LoginInfo;

@Component
public class LoginManager {

	@Value("${loginInfo.csv.fileName}")
	private String fileName;

	@Value("${loginInfo.csv.seperator}")
	private char seperator;

	private Map<String, LoginInfo> loginInfoList = new LinkedHashMap<String, LoginInfo>();

	@Resource(name = "webexLoginDriver")
	private WebexTeamsLoginDriver driver;

	@PostConstruct
	public void init() throws IOException {
		@SuppressWarnings("deprecation")
		CSVReader csvReader = new CSVReader(new FileReader(new File(fileName)), seperator);
		List<String[]> allData = csvReader.readAll();

		boolean isFirst = true;
		for (String[] row : allData) {
			if (isFirst) {
				isFirst = false;
				continue;
			}
			LoginInfo info = new LoginInfo();
			info.setName(row[0]);
			info.setPassword(row[1]);
			add(info);
		}
		csvReader.close();
	}

	public boolean add(LoginInfo loginInfo) {
		/*if (!loginInfoList.containsKey(loginInfo.getName())) {
			loginInfoList.put(loginInfo.getName(), loginInfo);
			driver.login(loginInfo);
			return true;
		}*/
		return false;
	}
	
	public boolean delete(String username) {
		if (loginInfoList.containsKey(username)) {
			LoginInfo loginInfo = loginInfoList.get(username);
			if(loginInfo != null && loginInfo.getDriver() != null) {
				loginInfo.getDriver().quit();
			}
			loginInfoList.remove(username);
		}
		return false;
	}
	
	public boolean refresh(String username) {
		if (loginInfoList.containsKey(username)) {
			LoginInfo loginInfo = loginInfoList.get(username);
			if(loginInfo != null && loginInfo.getDriver() != null) {
				loginInfo.getDriver().quit();
			}
			driver.login(loginInfo);
		}
		return false;
	}

	public Collection<LoginInfo> getLoginInfo() {
		return loginInfoList.values();
	}
	
	public LoginInfo getLoginInfo(String username) {
		return loginInfoList.get(username);
	}

	public void reTry(LoginInfo loginInfo) {
		WebDriver webDriver = loginInfo.getDriver();
		if (webDriver != null) {
			webDriver.quit();
		}
		driver.login(loginInfo);
	}

	// @PreDestroy
	public void closeAllSessions() {
		for (LoginInfo loginInfo : loginInfoList.values()) {
			WebDriver webDriver = loginInfo.getDriver();
			if (webDriver != null) {
				webDriver.quit();
			}
		}
	}

}
