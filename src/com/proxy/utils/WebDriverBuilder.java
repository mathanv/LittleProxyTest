package com.proxy.utils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverBuilder {	
	private WebDriver driver;
	private static String chromeDriverPath = "lib/chromedriver";
	private static final String TAG = "WebDriverBuilder";
	private static Proxy _proxy;
	private static DesiredCapabilities currentCapability;
	private static String currentBrowser;
	
	public WebDriverBuilder (String browser, boolean isProxyRequired, int proxyPort) {
		currentBrowser = browser;
		if (System.getProperty("os.name").contains("Windows"))
			chromeDriverPath = "lib/chromedriver.exe";		
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		
		if (isProxyRequired && proxyPort > 0)
			_proxy = buildProxy("localhost", proxyPort);
	
		if (browser.equalsIgnoreCase("chrome")) {
			DesiredCapabilities capability = DesiredCapabilities.chrome();
			
			ChromeOptions options = new ChromeOptions();
	        options.addArguments("--incognito");
	        options.addArguments("test-type");
	        capability.setCapability(ChromeOptions.CAPABILITY, options);
	        
	        if (isProxyRequired)
	        	capability.setCapability(CapabilityType.PROXY, _proxy);
	        
	        LoggingPreferences logs = new LoggingPreferences();
	  	  	logs.enable(LogType.BROWSER, Level.ALL);
	        capability.setCapability(CapabilityType.LOGGING_PREFS, logs);
	        
	        currentCapability = capability;	        
		} else if (browser.equalsIgnoreCase("firefox")) {
			DesiredCapabilities capability = DesiredCapabilities.firefox();
			
			if (isProxyRequired)
	        	capability.setCapability(CapabilityType.PROXY, _proxy);
			
			currentCapability = capability;
		} else if (browser.equalsIgnoreCase("internet explorer")) {
			DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
			
			if (isProxyRequired)
	        	capability.setCapability(CapabilityType.PROXY, _proxy);
						
			currentCapability = capability;
		} else {
			Log.d(TAG, "Unsupported browser: " + browser);
		}				
	}
	
	public WebDriver getDriver () {
		Log.d(TAG, "Building driver for: " + currentBrowser);
		
		if (currentBrowser.equalsIgnoreCase("chrome"))
			this.driver = new ChromeDriver(currentCapability);
		else if (currentBrowser.equalsIgnoreCase("firefox"))
			this.driver  = new FirefoxDriver(currentCapability);
		else if (currentBrowser.equalsIgnoreCase("internet explorer"))
			this.driver = new InternetExplorerDriver(currentCapability);	
		
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		return this.driver;			
	}
	
	public WebDriver getDriverInstance () {
		return this.driver;
	}
	
	public JavascriptExecutor getJSDriver () {
		if (this.driver == null)
			this.getDriver();
		
		if (this.driver instanceof JavascriptExecutor)        	
				return (JavascriptExecutor) driver;
		else
			return null;
	}		
	
	private Proxy buildProxy (String host, int port) {
		Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        String proxyStr = String.format("%s:%d", host, port);
        proxy.setHttpProxy(proxyStr);
        proxy.setSslProxy(proxyStr);
        return proxy;
	}
}
