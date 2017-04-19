package com.browser.client.executor;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriverException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CommandExecutor implements Closeable {

	private final Socket			socket;
	private final ObjectOutput		out;
	private final ObjectInputStream	in;

	private String					session;

	public CommandExecutor(Socket socket, String session) {
		if (socket == null)
			throw new NullPointerException("socket");

		this.socket = socket;
		this.session = session;

		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public CommandExecutor(Socket socket) {
		this(socket, null);
	}

	public synchronized Object execute(String command, Object... arguments) {
		List<Object> commands = new ArrayList<>(2 + arguments.length);

		StringBuilder args = new StringBuilder(128);
		args.append("[" + this.getSession() + ", " + command);

		commands.add(this.getSession());
		commands.add(command);

		for (Object argument : arguments) {
			commands.add(argument);
			args.append(", " + beautify(argument));
		}

		args.append("]");

		log.info(this.getSocket() + " >>> " + args);

		Object response;

		try {
			this.getOut().writeObject(commands);
			response = this.getIn().readObject();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		if (response instanceof WebDriverException || response instanceof IllegalArgumentException || response instanceof NullPointerException) {
			log.error(((Exception) response).getMessage());

			throw ((RuntimeException) response);
		}

		log.info(this.getSocket() + " <<< " + response.toString());

		return response;
	}

	@Override
	public synchronized void close() {
		if (!this.getSocket().isClosed()) {
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
