package com.browser.client.remote;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver.Timeouts;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteTimeouts extends RemoteSession implements Timeouts {

	public RemoteTimeouts(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteTimeouts(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteTimeouts(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public Timeouts implicitlyWait(long time, TimeUnit unit) {
		this.getCommandExecutor().execute("WebDriver.Timeouts.implicitlyWait", time, unit);

		return this;
	}

	@Override
	public Timeouts setScriptTimeout(long time, TimeUnit unit) {
		this.getCommandExecutor().execute("WebDriver.Timeouts.setScriptTimeout", time, unit);

		return this;
	}

	@Override
	public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
		this.getCommandExecutor().execute("WebDriver.Timeouts.pageLoadTimeout", time, unit);

		return this;
	}

}
