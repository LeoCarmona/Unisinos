package com.browser.client.remote;

import java.net.Socket;
import java.net.URL;

import org.openqa.selenium.WebDriver.Navigation;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteNavigation extends RemoteSession implements Navigation {

	public RemoteNavigation(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteNavigation(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteNavigation(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public void back() {
		this.getCommandExecutor().execute("WebDriver.Navigation.back");
	}

	@Override
	public void forward() {
		this.getCommandExecutor().execute("WebDriver.Navigation.forward");
	}

	@Override
	public void to(String url) {
		this.getCommandExecutor().execute("WebDriver.Navigation.to", url);
	}

	@Override
	public void to(URL url) {
		this.getCommandExecutor().execute("WebDriver.Navigation.to", url);
	}

	@Override
	public void refresh() {
		this.getCommandExecutor().execute("WebDriver.Navigation.refresh");
	}

}
