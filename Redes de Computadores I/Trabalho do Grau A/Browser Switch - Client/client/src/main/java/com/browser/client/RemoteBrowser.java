package com.browser.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;
import com.browser.client.remote.RemoteNavigation;
import com.browser.client.remote.RemoteOptions;
import com.browser.client.remote.RemoteTargetLocator;
import com.browser.client.remote.RemoteWebElement;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteBrowser extends RemoteSession implements WebDriver {

	public RemoteBrowser(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteBrowser(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteBrowser(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public void get(String url) {
		this.getCommandExecutor().execute("WebDriver.get", url);
	}

	@Override
	public String getCurrentUrl() {
		return (String) this.getCommandExecutor().execute("WebDriver.getCurrentUrl");
	}

	@Override
	public String getTitle() {
		return (String) this.getCommandExecutor().execute("WebDriver.getTitle");
	}

	@Override
	public List<WebElement> findElements(By by) {
		int size = (int) this.getCommandExecutor().execute("WebDriver.findElements", by);

		List<WebElement> webElements = new ArrayList<>(size);

		for (int i = 0; i < size; i++)
			webElements.add(new RemoteWebElement(this.getCommandExecutor(), by, i));

		return webElements;
	}

	@Override
	public WebElement findElement(By by) {
		return new RemoteWebElement(this.getCommandExecutor(), by);
	}

	@Override
	public String getPageSource() {
		return (String) this.getCommandExecutor().execute("WebDriver.getPageSource");
	}

	@Override
	public void close() {
		this.getCommandExecutor().execute("WebDriver.close");
	}

	@Override
	public void quit() {
		this.getCommandExecutor().execute("WebDriver.quit");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getWindowHandles() {
		return (Set<String>) this.getCommandExecutor().execute("WebDriver.getWindowHandles");
	}

	@Override
	public String getWindowHandle() {
		return (String) this.getCommandExecutor().execute("WebDriver.getWindowHandle");
	}

	@Override
	public TargetLocator switchTo() {
		return new RemoteTargetLocator(this);
	}

	@Override
	public Navigation navigate() {
		return new RemoteNavigation(this.getCommandExecutor());
	}

	@Override
	public Options manage() {
		return new RemoteOptions(this.getCommandExecutor());
	}

}
