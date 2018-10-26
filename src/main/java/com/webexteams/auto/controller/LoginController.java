package com.webexteams.auto.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webexteams.auto.driver.WebexTeamsLoginDriver;
import com.webexteams.auto.exception.ResourceNotFoundException;
import com.webexteams.auto.model.BasicLoginInfo;
import com.webexteams.auto.model.LoginInfo;
import com.webexteams.auto.model.LoginInfoResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController()
@RequestMapping("/login")
@Api(tags = { "Login" })
public class LoginController {

	@Autowired
	private LoginManager loginManager;

	@Resource(name = "webexLoginDriver")
	private WebexTeamsLoginDriver driver;

	@ApiOperation(value = "List available logins", response = LoginInfoResponse.class, responseContainer = "List")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<LoginInfoResponse> getAllLogins(@RequestParam("username") String username) {
		List<LoginInfoResponse> responses = new ArrayList<LoginInfoResponse>();
		if (username != null && username.trim().length() > 0) {
			LoginInfo loginInfo = loginManager.getLoginInfo(username);
			if (loginInfo == null) {
				throw new ResourceNotFoundException();
			} else {
				LoginInfoResponse resp = new LoginInfoResponse();
				resp.setUsername(loginInfo.getName());
				resp.setAccessToken(driver.getAccessToken(loginInfo.getDriver()));
				responses.add(resp);
			}
		} else {
			Collection<LoginInfo> loginInfoList = loginManager.getLoginInfo();
			for (LoginInfo loginInfo : loginInfoList) {
				LoginInfoResponse resp = new LoginInfoResponse();
				resp.setUsername(loginInfo.getName());
				resp.setAccessToken(driver.getAccessToken(loginInfo.getDriver()));
				responses.add(resp);
			}
		}

		return responses;
	}

	@ApiOperation(nickname = "New Login", value = "Add new login")
	@RequestMapping(method = RequestMethod.PUT)
	public void createLogin(BasicLoginInfo basicLoginInfo) {
		LoginInfo info = new LoginInfo();
		info.setName(basicLoginInfo.getName());
		info.setPassword(basicLoginInfo.getPassword());
		boolean status = loginManager.add(info);
		if (!status) {
			throw new ResourceNotFoundException();
		}
	}

	@ApiOperation(nickname = "Delete Login", value = "Delete login")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{username}")
	public void deleteLoginInfo(String username) {
		boolean status = loginManager.delete(username);
		if (!status) {
			throw new ResourceNotFoundException();
		}
	}

	@ApiOperation(nickname = "Refresh Login", value = "Refresh existing login")
	@RequestMapping(method = RequestMethod.PATCH, path = "/{username}")
	public void refreshLoginInfo(String username) {
		boolean status = loginManager.refresh(username);
		if (!status) {
			throw new ResourceNotFoundException();
		}
	}
}
