package com.browser.client.remote;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteWebElement extends RemoteSession implements WebElement {

	protected final By	locator;
	protected final int	index;

	public RemoteWebElement(CommandExecutor commandExecutor, By locator, int index) throws NullPointerException {
		super(commandExecutor);

		this.locator = locator;
		this.index = index;
	}

	public RemoteWebElement(CommandExecutor commandExecutor, By locator) throws NullPointerException {
		this(commandExecutor, locator, 0);
	}

	public RemoteWebElement(Socket socket, String session, By locator, int index) throws NullPointerException {
		super(socket, session);

		this.locator = locator;
		this.index = index;
	}

	public RemoteWebElement(Socket socket, String session, By locator) throws NullPointerException {
		this(socket, session, locator, 0);
	}

	public RemoteWebElement(Socket socket, By locator, int index) throws NullPointerException {
		super(socket);

		this.locator = locator;
		this.index = index;
	}

	public RemoteWebElement(Socket socket, By locator) throws NullPointerException {
		this(socket, locator, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return (X) this.getCommandExecutor().execute("WebElement.getScreenshotAs", locator, target);
	}

	@Override
	public void click() {
		this.getCommandExecutor().execute("WebElement.click", locator);
	}

	@Override
	public void submit() {
		this.getCommandExecutor().execute("WebElement.submit", locator);
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		this.getCommandExecutor().execute("WebElement.sendKeys", locator, keysToSend);
	}

	@Override
	public void clear() {
		this.getCommandExecutor().execute("WebElement.clear", locator);
	}

	@Override
	public String getTagName() {
		return (String) this.getCommandExecutor().execute("WebElement.getTagName", locator);
	}

	@Override
	public String getAttribute(String name) {
		return (String) this.getCommandExecutor().execute("WebElement.getAttribute", locator, name);
	}

	@Override
	public boolean isSelected() {
		return (boolean) this.getCommandExecutor().execute("WebElement.isSelected", locator);
	}

	@Override
	public boolean isEnabled() {
		return (boolean) this.getCommandExecutor().execute("WebElement.isEnabled", locator);
	}

	@Override
	public String getText() {
		return (String) this.getCommandExecutor().execute("WebElement.getText", locator);
	}

	@Override
	public List<WebElement> findElements(By by) {
		final int size = (int) this.getCommandExecutor().execute("WebElement.findElements", locator, by);

		List<WebElement> elementos = new ArrayList<>(size);

		for (int i = 0; i < size; i++)
			elementos.add(new RemoteWebElement(this.getCommandExecutor(), new ByChained(locator, by), i++));

		return elementos;
	}

	@Override
	public WebElement findElement(By by) {
		return new RemoteWebElement(this.getCommandExecutor(), new ByChained(locator, by));
	}

	@Override
	public boolean isDisplayed() {
		return (boolean) this.getCommandExecutor().execute("WebElement.isDisplayed", locator);
	}

	@Override
	public Point getLocation() {
		return (Point) this.getCommandExecutor().execute("WebElement.getLocation", locator);
	}

	@Override
	public Dimension getSize() {
		return (Dimension) this.getCommandExecutor().execute("WebElement.getSize", locator);
	}

	@Override
	public Rectangle getRect() {
		return (Rectangle) this.getCommandExecutor().execute("WebElement.getRect", locator);
	}

	@Override
	public String getCssValue(String propertyName) {
		return (String) this.getCommandExecutor().execute("WebElement.getCssValue", locator, propertyName);
	}

}
