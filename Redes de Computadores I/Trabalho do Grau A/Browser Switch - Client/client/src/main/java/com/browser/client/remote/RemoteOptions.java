package com.browser.client.remote;

import java.net.Socket;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.logging.Logs;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteOptions extends RemoteSession implements Options {

	public RemoteOptions(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteOptions(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteOptions(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public void addCookie(Cookie cookie) {
		this.getCommandExecutor().execute("WebDriver.Options.addCookie", cookie);
	}

	@Override
	public void deleteCookieNamed(String name) {
		this.getCommandExecutor().execute("WebDriver.Options.deleteCookieNamed", name);
	}

	@Override
	public void deleteCookie(Cookie cookie) {
		this.getCommandExecutor().execute("WebDriver.Options.deleteCookie", cookie);
	}

	@Override
	public void deleteAllCookies() {
		this.getCommandExecutor().execute("WebDriver.Options.deleteAllCookies");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Cookie> getCookies() {
		return (Set<Cookie>) this.getCommandExecutor().execute("WebDriver.Options.getCookies");
	}

	@Override
	public Cookie getCookieNamed(String name) {
		return (Cookie) this.getCommandExecutor().execute("WebDriver.Options.getCookieNamed");
	}

	@Override
	public Timeouts timeouts() {
		return new RemoteTimeouts(this.getCommandExecutor());
	}

	@Override
	public ImeHandler ime() {
		return new RemoteImeHandler(this.getCommandExecutor());
	}

	@Override
	public Window window() {
		return new RemoteWindow(this.getCommandExecutor());
	}

	@Override
	public Logs logs() {
		return new RemoteLogs(this.getCommandExecutor());
	}

}
