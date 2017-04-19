package com.browser.server.api;

import static com.browser.server.api.DriverManager.DRIVERS;
import static com.browser.server.api.DriverManager.checkSession;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.Credentials;

import com.browser.server.api.support.DriverFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Leonardo Carmona da Silva
 *         <ul>
 *         <li><a href="https://br.linkedin.com/in/l3ocarmona">https://br.linkedin.com/in/l3ocarmona</a></li>
 *         <li><a href="mailto:lcdesenv@gmail.com">lcdesenv@gmail.com</a></li>
 *         </ul>
 *
 */
@Data
@Slf4j
public class Driver implements Runnable, Closeable {

	private final ServerSocket	serverSocket;
	private Socket				socket;

	public Driver(ServerSocket serverSocket) {
		if (serverSocket == null)
			throw new NullPointerException("serverSocket");

		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		final ObjectOutputStream out;
		final ObjectInputStream in;

		try {
			this.setSocket(this.getServerSocket().accept());
			out = new ObjectOutputStream(this.getSocket().getOutputStream());
			in = new ObjectInputStream(this.getSocket().getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		while (true) {
			try {
				@SuppressWarnings("unchecked")
				List<Object> commands = (List<Object>) in.readObject();
				int size = commands.size();

				if (size < 2)
					throw new IllegalArgumentException("{session} {command} {args}");

				String session = commands.get(0) != null ? commands.get(0).toString() : null;
				String command = commands.get(1).toString();

				StringBuilder sb = new StringBuilder(128);
				sb.append("[" + session + ", " + command);

				for (int i = 2; i < size; i++)
					sb.append(", " + beautify(commands.get(i)));

				sb.append("]");

				log.info(this.getSocket() + " <<< " + sb);

				WebDriver driver;
				Object arg;

				if ("create-session".equals(command)) {
					driver = DRIVERS.get(commands.get(0));

					if (driver != null) {
						out.writeObject(driver.toString());
						continue;
					}

					driver = DriverFactory.start();
					DRIVERS.put(driver.toString(), driver);

					out.writeObject(driver.toString());
				} else {
					driver = checkSession(commands);

					switch (command) {

						// ==================================================
						// WebDriver commands
						// ==================================================

						case "WebDriver.get":
							driver.get(commands.get(2).toString());

							out.writeObject("success");

							break;

						case "WebDriver.getTitle":
							out.writeObject(driver.getTitle());

							break;

						case "WebDriver.findElements":
							out.writeInt(driver.findElements((By) commands.get(2)).size());

							break;

						case "WebDriver.getPageSource":
							out.writeObject(driver.getPageSource());

							break;

						case "WebDriver.close":
							driver.close();

							out.writeObject("success");

							if (driver.toString().contains("null")) {
								DRIVERS.remove(session);
								this.getSocket().close();

								continue;
							}

							break;

						case "WebDriver.quit":
							DRIVERS.remove(session);

							driver.quit();

							out.writeObject("success");

							this.getSocket().close();
							continue;

						case "WebDriver.getWindowHandles":
							out.writeObject(driver.getWindowHandles());

							break;

						case "WebDriver.getWindowHandle":
							out.writeObject(driver.getWindowHandle());

							break;

						// ==================================================
						// WebDriver -> TargetLocator commands
						// ==================================================

						case "WebDriver.TargetLocator.frame":
							arg = commands.get(2);

							if (arg instanceof Integer)
								driver.switchTo().frame((int) arg);
							else if (arg instanceof String)
								driver.switchTo().frame((String) arg);
							else if (arg instanceof By)
								driver.switchTo().frame(driver.findElement((By) arg));
							else
								throw new IllegalArgumentException("Unknown entity: " + arg);

							out.writeObject("success");

							break;

						case "WebDriver.TargetLocator.parentFrame":
							driver.switchTo().parentFrame();

							out.writeObject("success");

							break;

						case "WebDriver.TargetLocator.window":
							driver.switchTo().window((String) commands.get(2));

							out.writeObject("success");

							break;

						case "WebDriver.TargetLocator.defaultContent":
							driver.switchTo().defaultContent();

							out.writeObject("success");

						case "WebDriver.TargetLocator.activeElement":
							driver.switchTo().activeElement();

							out.writeObject("success");

							break;

						// ==================================================
						// WebDriver -> TargetLocator -> Alert commands
						// ==================================================

						case "Alert.dismiss":
							driver.switchTo().alert().dismiss();

							out.writeObject("success");

							break;

						case "Alert.accept":
							driver.switchTo().alert().accept();

							out.writeObject("success");

							break;

						case "Alert.getText":
							out.writeObject(driver.switchTo().alert().getText());

							break;

						case "Alert.sendKeys":
							driver.switchTo().alert().sendKeys((String) commands.get(2));

							out.writeObject("success");

							break;

						case "Alert.setCredentials":
							driver.switchTo().alert().setCredentials((Credentials) commands.get(2));

							out.writeObject("success");

							break;

						case "Alert.authenticateUsing":
							driver.switchTo().alert().authenticateUsing((Credentials) commands.get(2));

							out.writeObject("success");

							break;

						// ==================================================
						// WebDriver -> Navigation commands
						// ==================================================

						case "WebDriver.Navigation.back":
							driver.navigate().back();

							out.writeObject("success");

							break;

						case "WebDriver.Navigation.forward":
							driver.navigate().forward();

							out.writeObject("success");

							break;

						case "WebDriver.Navigation.to":
							arg = commands.get(2);

							if (arg instanceof String)
								driver.navigate().to((String) arg);
							else if (arg instanceof URL)
								driver.navigate().to((URL) arg);
							else
								throw new IllegalArgumentException("Unknown entity: " + arg);

							out.writeObject("success");

							break;

						case "WebDriver.Navigation.refresh":
							driver.navigate().refresh();

							out.writeObject("success");

							break;

						// ==================================================
						// WebDriver -> Options commands
						// ==================================================

						case "WebDriver.Options.addCookie":
							driver.manage().addCookie((Cookie) commands.get(2));

							out.writeObject("success");
							break;

						case "WebDriver.Options.deleteCookieNamed":
							driver.manage().deleteCookieNamed((String) commands.get(2));

							out.writeObject("success");
							break;

						case "WebDriver.Options.deleteCookie":
							driver.manage().deleteCookie((Cookie) commands.get(2));

							out.writeObject("success");
							break;

						case "WebDriver.Options.deleteAllCookies":
							driver.manage().deleteAllCookies();

							out.writeObject("success");
							break;

						case "WebDriver.Options.getCookies":
							out.writeObject(driver.manage().getCookies());

							break;

						case "WebDriver.Options.getCookieNamed":
							out.writeObject(driver.manage().getCookieNamed((String) commands.get(2)));

							break;

						// ==================================================
						// WebDriver -> Options -> Timeouts commands
						// ==================================================

						case "WebDriver.Timeouts.implicitlyWait":
							driver.manage().timeouts().setScriptTimeout((long) commands.get(2), (TimeUnit) commands.get(3));

							out.writeObject("success");

							break;

						case "WebDriver.Timeouts.setScriptTimeout":
							driver.manage().timeouts().setScriptTimeout((long) commands.get(2), (TimeUnit) commands.get(3));

							out.writeObject("success");

							break;

						case "WebDriver.Timeouts.pageLoadTimeout":
							driver.manage().timeouts().pageLoadTimeout((long) commands.get(2), (TimeUnit) commands.get(3));

							out.writeObject("success");

							break;

						// ==================================================
						// WebDriver -> Options -> ImeHandler commands
						// ==================================================

						case "WebDriver.ImeHandler.getAvailableEngines":
							out.writeObject(driver.manage().ime().getAvailableEngines());

							break;

						case "WebDriver.ImeHandler.getActiveEngine":
							out.writeObject(driver.manage().ime().getActiveEngine());

							break;

						case "WebDriver.ImeHandler.isActivated":
							out.writeObject(driver.manage().ime().isActivated());

							break;

						case "WebDriver.ImeHandler.deactivate":
							driver.manage().ime().deactivate();

							out.writeObject("success");

							break;

						case "WebDriver.ImeHandler.activateEngine":
							driver.manage().ime().activateEngine((String) commands.get(2));

							out.writeObject("success");

							break;

						// ==================================================
						// WebDriver -> Options -> Window commands
						// ==================================================

						case "WebDriver.Window.setSize":
							driver.manage().window().setSize((Dimension) commands.get(2));

							out.writeObject("success");

							break;

						case "WebDriver.Window.setPosition":
							driver.manage().window().setPosition((Point) commands.get(2));

							out.writeObject("success");

							break;

						case "WebDriver.Window.getSize":
							out.writeObject(driver.manage().window().getSize());

							break;

						case "WebDriver.Window.getPosition":
							out.writeObject(driver.manage().window().getPosition());

							break;

						case "WebDriver.Window.maximize":
							driver.manage().window().maximize();

							out.writeObject("success");

							break;

						case "WebDriver.Window.fullscreen":
							driver.manage().window().fullscreen();

							out.writeObject("success");

							break;

						// ==================================================
						// WebDriver -> Options -> Log commands
						// ==================================================

						case "Logs.get":
							out.writeObject(driver.manage().logs().get((String) commands.get(2)));

							break;

						case "Logs.getAvailableLogTypes":
							out.writeObject(driver.manage().logs().getAvailableLogTypes());

							break;

						// ==================================================
						// WebElement commands
						// ==================================================

						case "WebElement.getScreenshotAs":
							out.writeObject(driver.findElement((By) commands.get(2)).getScreenshotAs((OutputType<?>) commands.get(3)));

							break;

						case "WebElement.click":
							driver.findElement((By) commands.get(2)).click();

							out.writeObject("success");

							break;

						case "WebElement.submit":
							driver.findElement((By) commands.get(2)).submit();

							out.writeObject("success");

							break;

						case "WebElement.sendKeys":
							driver.findElement((By) commands.get(2)).sendKeys((CharSequence[]) commands.get(3));

							out.writeObject("success");

							break;

						case "WebElement.clear":
							driver.findElement((By) commands.get(2)).clear();

							out.writeObject("success");

							break;

						case "WebElement.getTagName":
							out.writeObject(driver.findElement((By) commands.get(2)).getTagName());
							break;

						case "WebElement.getAttribute":
							out.writeObject(driver.findElement((By) commands.get(2)).getAttribute((String) commands.get(3)));

							break;

						case "WebElement.isSelected":
							out.writeObject(driver.findElement((By) commands.get(2)).isSelected());

							break;

						case "WebElement.isEnabled":
							out.writeObject(driver.findElement((By) commands.get(2)).isEnabled());

							break;

						case "WebElement.getText":
							out.writeObject(driver.findElement((By) commands.get(2)).getText());

							break;

						case "WebElement.findElements":
							out.writeInt(driver.findElement((By) commands.get(2)).findElements((By) commands.get(3)).size());

							break;

						case "WebElement.isDisplayed":
							out.writeObject(driver.findElement((By) commands.get(2)).isDisplayed());

							break;

						case "WebElement.getLocation":
							out.writeObject(driver.findElement((By) commands.get(2)).getLocation());

							break;

						case "WebElement.getSize":
							out.writeObject(driver.findElement((By) commands.get(2)).getSize());

							out.writeObject("success");

							break;

						case "WebElement.getRect":
							out.writeObject(driver.findElement((By) commands.get(2)).getRect());

							out.writeObject("success");

							break;

						case "WebElement.getCssValue":
							out.writeObject(driver.findElement((By) commands.get(2)).getCssValue((String) commands.get(3)));

							out.writeObject("success");

							break;

						default:
							throw new IllegalArgumentException("Invalid command: " + commands.get(1).toString());

					}
				}
			} catch (Throwable e) {
				try {
					out.writeObject(e);
				} catch (IOException e1) {
					break;
				}
			}
		}

	}

	@Override
	public synchronized void close() {
		if (this.getSocket() != null && !this.getSocket().isClosed()) {
			try {
				this.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected static String beautify(Object object) {
		if (object.getClass().isArray()) {
			StringBuilder builder = new StringBuilder("[");

			final int length = Array.getLength(object);

			if (length > 0) {
				builder.append(Array.get(object, 0));

				for (int i = 1; i < length; i++)
					builder.append(", " + beautify(Array.get(object, i)));
			}

			return builder + "]";
		}

		return object != null ? object.toString() : "null";
	}

}
