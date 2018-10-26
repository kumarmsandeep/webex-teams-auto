package com.webexteams.auto.driver;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Qualifier("HtmlUnit")
public class HtmlUnitBrowser extends WebexTeamsLoginDriver {

	@Override
	public WebDriver getDriver() {
		return null;
	}

}
