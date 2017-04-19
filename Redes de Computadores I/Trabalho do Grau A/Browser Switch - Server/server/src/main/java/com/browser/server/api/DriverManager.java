package com.browser.server.api;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

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
@Slf4j
public abstract class DriverManager {

	public static final int				DEFAULT_PORT				= 4567;
	public static final int				DEFAULT_QUANTITY_DRIVERS	= 1000;

	static final Map<String, WebDriver>	DRIVERS						= new HashMap<>();

	public static void main(String[] args) throws IOException {
		log.info("Starting");

		int port, drivers;

		switch (args.length) {
			case 0:
				port = DEFAULT_PORT;
				drivers = DEFAULT_QUANTITY_DRIVERS;

				break;
			case 1:
				port = Integer.parseInt(args[0]);
				drivers = DEFAULT_QUANTITY_DRIVERS;

				break;

			default:
				port = Integer.parseInt(args[0]);
				drivers = Integer.parseInt(args[1]);

				break;
		}
		
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			ExecutorService service = Executors.newFixedThreadPool(drivers);
			final int m = drivers + 1;
			
			for (int i = 1; i < m; i++) {
				Driver driver = new Driver(serverSocket);

				log.info("Creating socket nº " + i + ": " + driver.toString());
				
				service.submit(driver);
			}

			service.shutdown();
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("Finished");
	}

	public static WebDriver checkSession(List<Object> commands) {
		WebDriver driver = DRIVERS.get(commands.get(0).toString());

		if (driver == null)
			throw new NullPointerException("not connected");

		return driver;
	}

	protected DriverManager() {

	}

}
