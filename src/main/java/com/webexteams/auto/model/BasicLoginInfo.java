package com.webexteams.auto.model;

import io.swagger.annotations.ApiModelProperty;

public class BasicLoginInfo {

	@ApiModelProperty(allowEmptyValue = false, value = "The username", position = 1, required = true)
	private String name;

	@ApiModelProperty(allowEmptyValue = false, value = "The password", position = 2, required = true)
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
