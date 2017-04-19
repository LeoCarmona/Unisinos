package com.browser.client.remote;

import java.net.Socket;
import java.util.List;

import org.openqa.selenium.WebDriver.ImeHandler;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteImeHandler extends RemoteSession implements ImeHandler {

	public RemoteImeHandler(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteImeHandler(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteImeHandler(Socket socket) throws NullPointerException {
		super(socket);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAvailableEngines() {
		return (List<String>) this.getCommandExecutor().execute("WebDriver.ImeHandler.getAvailableEngines");
	}

	@Override
	public String getActiveEngine() {
		return (String) this.getCommandExecutor().execute("WebDriver.ImeHandler.getActiveEngine");
	}

	@Override
	public boolean isActivated() {
		return (boolean) this.getCommandExecutor().execute("WebDriver.ImeHandler.isActivated");
	}

	@Override
	public void deactivate() {
		this.getCommandExecutor().execute("WebDriver.ImeHandler.deactivate");
	}

	@Override
	public void activateEngine(String engine) {
		this.getCommandExecutor().execute("WebDriver.ImeHandler.activateEngine", engine);
	}

}
