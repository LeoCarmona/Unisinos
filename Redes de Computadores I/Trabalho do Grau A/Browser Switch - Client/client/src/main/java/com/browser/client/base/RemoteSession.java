package com.browser.client.base;

import java.net.Socket;

import com.browser.client.executor.CommandExecutor;

import lombok.Data;

@Data
public abstract class RemoteSession {

	public static final String		CREATE_SESSION_COMMAND	= "create-session";

	private final CommandExecutor	commandExecutor;

	public RemoteSession(CommandExecutor commandExecutor) throws NullPointerException {
		if (commandExecutor == null)
			throw new NullPointerException("commandExecutor");

		this.commandExecutor = commandExecutor;

		if (commandExecutor.getSession() == null)
			commandExecutor.setSession((String) commandExecutor.execute(CREATE_SESSION_COMMAND));
	}

	public RemoteSession(Socket socket, String session) throws NullPointerException {
		if (socket == null)
			throw new NullPointerException("socket");

		this.commandExecutor = new CommandExecutor(socket, session);

		if (session == null)
			commandExecutor.setSession((String) commandExecutor.execute(CREATE_SESSION_COMMAND));
	}

	public RemoteSession(Socket socket) throws NullPointerException {
		this(socket, null);
	}

}
