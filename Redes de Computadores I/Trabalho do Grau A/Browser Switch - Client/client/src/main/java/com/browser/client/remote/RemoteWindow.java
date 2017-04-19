package com.browser.client.remote;

import java.net.Socket;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver.Window;

import com.browser.client.base.RemoteSession;
import com.browser.client.executor.CommandExecutor;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RemoteWindow extends RemoteSession implements Window {

	public RemoteWindow(CommandExecutor commandExecutor) throws NullPointerException {
		super(commandExecutor);
	}

	public RemoteWindow(Socket socket, String session) throws NullPointerException {
		super(socket, session);
	}

	public RemoteWindow(Socket socket) throws NullPointerException {
		super(socket);
	}

	@Override
	public void setSize(Dimension targetSize) {
		this.getCommandExecutor().execute("WebDriver.Window.targetSize", targetSize);
	}

	@Override
	public void setPosition(Point targetPosition) {
		this.getCommandExecutor().execute("WebDriver.Window.targetPosition", targetPosition);
	}

	@Override
	public Dimension getSize() {
		return (Dimension) this.getCommandExecutor().execute("WebDriver.Window.getSize");
	}

	@Override
	public Point getPosition() {
		return (Point) this.getCommandExecutor().execute("WebDriver.Window.getPosition");
	}

	@Override
	public void maximize() {
		this.getCommandExecutor().execute("WebDriver.Window.maximize");
	}

	@Override
	public void fullscreen() {
		this.getCommandExecutor().execute("WebDriver.Window.fullscreen");
	}
}
