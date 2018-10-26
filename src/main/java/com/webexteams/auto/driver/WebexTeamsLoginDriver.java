package com.webexteams.auto.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

import com.webexteams.auto.model.LoginInfo;

public abstract class WebexTeamsLoginDriver {

	@Async("ConcurrentTaskExecutor")
	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public void login(LoginInfo info) {

		WebDriver driver = null;
		try {

			System.out.println("Login : " + info.getName());

			String url = "https://developer.webex.com";

			if (info.getDriver() != null) {
				info.getDriver().quit();
			}

			driver = this.getDriver();

			driver.get(url);

			WebDriverWait wait = new WebDriverWait(driver, 20);

			By loginAnchorTag = By.cssSelector("a[class='login-signup login sign-in']");

			driver.findElement(loginAnchorTag).click();

			waitForDocumentReady(driver);

			By userMail = By.id("IDToken1");

			By passwordElement = By.id("IDToken2");

			By UserEmailBtn = By.id("IDButton2");

			By passwordBtn = By.id("Button1");

			wait.until(ExpectedConditions.visibilityOfElementLocated(userMail));

			driver.findElement(userMail).sendKeys(info.getName());

			wait.until(ExpectedConditions.elementToBeClickable(UserEmailBtn));

			driver.findElement(UserEmailBtn).click();

			waitForDocumentReady(driver);

			wait.until(ExpectedConditions.visibilityOfElementLocated(passwordElement));

			driver.findElement(passwordElement).sendKeys(info.getPassword());

			wait.until(ExpectedConditions.elementToBeClickable(passwordBtn));

			driver.findElement(passwordBtn).click();

			waitForDocumentReady(driver);

			info.setDriver(driver);
		} catch (Exception e) {
			if (driver != null) {
				driver.quit();
			}
			throw e;
		}
	}

	private void waitForDocumentReady(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		while (true) {

			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			} else {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public String getAccessToken(WebDriver driver) {
		try {
			if (driver != null) {
				Cookie cookieNamed2 = driver.manage().getCookieNamed("csw-access-token");
				if (cookieNamed2 != null) {
					return cookieNamed2.getValue();
				}
			}
		} catch (Exception e) {
		}

		return null;
	}

	public abstract WebDriver getDriver();
}
