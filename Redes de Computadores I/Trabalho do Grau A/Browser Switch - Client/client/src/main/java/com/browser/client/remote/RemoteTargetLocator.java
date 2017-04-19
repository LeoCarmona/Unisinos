package com.browser.client.remote;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import com.browser.client.RemoteBrowser;
import com.browser.client.base.RemoteSession;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteTargetLocator extends RemoteSession implements TargetLocator {

	protected final RemoteBrowser browser;

	public RemoteTargetLocator(RemoteBrowser browser) throws NullPointerException {
		super(browser.getCommandExecutor());

		this.browser = browser;
	}

	@Override
	public WebDriver frame(int index) {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.frame", index);

		return browser;
	}

	@Override
	public WebDriver frame(String nameOrId) {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.frame", nameOrId);

		return browser;
	}

	@Override
	public WebDriver frame(WebElement frameElement) {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.frame", ((RemoteWebElement) frameElement).locator);

		return browser;
	}

	@Override
	public WebDriver parentFrame() {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.parentFrame");

		return browser;
	}

	@Override
	public WebDriver window(String nameOrHandle) {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.window", nameOrHandle);

		return browser;
	}

	@Override
	public WebDriver defaultContent() {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.defaultContent");

		return browser;
	}

	@Override
	public WebElement activeElement() {
		this.getCommandExecutor().execute("WebDriver.TargetLocator.activeElement");

		return new RemoteWebElement(this.getCommandExecutor(), null);
	}

	@Override
	public Alert alert() {
		return new RemoteAlert(this.getCommandExecutor());
	}

}
