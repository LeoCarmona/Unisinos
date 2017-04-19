package com.browser.client.remote;

import java.net.Socket;

import org.openqa.selenium.Alert;
import org.openqa.selenium.security.Credentials;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteAlert extends RemoteSession implements Alert {

	public RemoteAlert(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteAlert(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteAlert(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public void dismiss() {
		this.getCommandExecutor().execute("Alert.dismiss");
	}

	@Override
	public void accept() {
		this.getCommandExecutor().execute("Alert.accept");
	}

	@Override
	public String getText() {
		return (String) this.getCommandExecutor().execute("Alert.getText");
	}

	@Override
	public void sendKeys(String keysToSend) {
		this.getCommandExecutor().execute("Alert.sendKeys", keysToSend);
	}

	@Override
	public void setCredentials(Credentials credentials) {
		this.getCommandExecutor().execute("Alert.setCredentials", credentials);
	}

	@Override
	public void authenticateUsing(Credentials credentials) {
		this.getCommandExecutor().execute("Alert.authenticateUsing", credentials);
	}
}
