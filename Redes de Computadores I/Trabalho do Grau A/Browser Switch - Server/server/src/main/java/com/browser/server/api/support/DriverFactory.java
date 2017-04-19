package com.browser.server.api.support;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Leonardo Carmona da Silva
 *         <ul>
 *         <li><a href= "https://br.linkedin.com/in/l3ocarmona">https://br.linkedin.com/in/l3ocarmona</a></li>
 *         <li><a href="mailto:lcdesenv@gmail.com">lcdesenv@gmail.com</a></li>
 *         </ul>
 *
 */
@Slf4j
public final class DriverFactory {

	private static final String				DRIVERS_PATH			= System.getProperty("java.io.tmpdir") + "selenium" + File.separator + "drivers" + File.separator;
	private static final File				DRIVERS_FOLDER_TARGET	= new File(System.getProperty("java.io.tmpdir") + "selenium" + File.separator + "drivers" + File.separator);

	private static final List<WebDriver>	OPENED_DRIVERS			= new ArrayList<>();

	private static final ChromeOptions		DEFAULT_CHROME_OPTIONS	= new ChromeOptions();

	static {
		DEFAULT_CHROME_OPTIONS.addArguments("--start-maximized");

		log.info("Iniciando a extração dos navegadores");

		DRIVERS_FOLDER_TARGET.mkdirs();

		extractDriver("chromedriver_v2.27.exe");

		log.info("A extração dos navegadores foi concluída com sucesso");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for (WebDriver driver : OPENED_DRIVERS) {
				try {
					log.debug("Encerrando o driver " + driver);
					driver.quit();
					log.debug("O driver " + driver + " foi encerrado com sucesso");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			OPENED_DRIVERS.clear();
		}));
	}

	/**
	 * Método responsável por criar um {@link WebDriver}.
	 * 
	 * @param availableDriver
	 *            {@link WebDriver} disponível.
	 * 
	 * @param setDefault
	 *            Se {@code true}, indica ao {@link ConcurrentDriverManager} que o {@link WebDriver} será o padrão utilizado pelo {@link Driver} nesta {@link Thread}. Caso contrário, não faz nada.
	 * 
	 * @return O {@link WebDriver} criado.
	 */
	public static WebDriver start() {
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, DRIVERS_PATH + "/chromedriver_v2.27.exe");

		final WebDriver driver = new ChromeDriver(DEFAULT_CHROME_OPTIONS);

		driver.manage().timeouts().pageLoadTimeout(60, SECONDS);
		driver.manage().timeouts().implicitlyWait(15, SECONDS);

		OPENED_DRIVERS.add(driver);

		return driver;
	}

	private static final void extractDriver(String driverName) {
		final File target = new File(DRIVERS_FOLDER_TARGET, driverName);

		if (!target.exists()) {
			log.debug("Extraindo o driver " + driverName);

			try {
				Files.copy(DriverFactory.class.getResourceAsStream("/drivers/" + driverName), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}

			log.debug("Driver extraído com sucesso");
		}
	}

	/**
	 * Construtor responsável por prevenir a instanciação incorreta da classe.
	 */
	private DriverFactory() {

	}

}
