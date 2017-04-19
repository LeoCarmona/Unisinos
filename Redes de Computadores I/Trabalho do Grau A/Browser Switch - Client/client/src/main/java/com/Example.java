package com;

import java.net.InetAddress;
import java.net.Socket;

import org.openqa.selenium.By;

import com.browser.client.RemoteBrowser;

public class Example {

	public static void main(String[] args) throws Exception {		
		Socket server = new Socket(InetAddress.getLocalHost(), 4567);
		
		RemoteBrowser browser = new RemoteBrowser(server);
		
		browser.get("http://www.google.com.br");
		browser.findElement(By.id("lst-ib")).sendKeys("Unisinos");
		
		Thread.sleep(5000);
		
		browser.quit();
	}

}
