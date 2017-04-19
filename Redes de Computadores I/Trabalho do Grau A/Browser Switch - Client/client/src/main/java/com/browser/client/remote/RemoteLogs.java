package com.browser.client.remote;

import java.net.Socket;
import java.util.Set;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteLogs extends RemoteSession implements Logs {

	public RemoteLogs(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteLogs(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteLogs(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public LogEntries get(String logType) {
		return (LogEntries) this.getCommandExecutor().execute("Logs.get", logType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getAvailableLogTypes() {
		return (Set<String>) this.getCommandExecutor().execute("Logs.getAvailableLogTypes");
	}

}
