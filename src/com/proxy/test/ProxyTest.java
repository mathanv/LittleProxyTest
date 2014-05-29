package com.proxy.test;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.proxy.utils.ProxyServer;
import com.proxy.utils.WebDriverBuilder;

public class ProxyTest {
	private static final String TAG = "ProxyTest";
	private static ProxyServer proxyServer;
	private static WebDriverBuilder wdBuilder;
	private static WebDriver wDriver;
	private static final int port = 7070;
	private static String httpsUrl = "https://ssl.vizury.com/analyze/analyze.php?account_id=VIZVRM299&param=e500&orderid=11141048851&orderprice=&pid1=132-7121-406&currency=false&crmid=3005147345&section=1&level=1";
	//private static String httpUrl = "http://www.vizury.com/analyze/analyze.php?account_id=VIZVRM299&section=1&level=1&param=e300&pid=004-2685-046&catid=Camisa&subcat1id=Camisa_Nike&pname=Camisa%20Nike%20Sele%C3%A7%C3%A3o%20Brasil%20I%202014%20n%C2%BA%2010&image=http%3A%2F%2Fstatic1.netshoes.net%2FProdutos%2F46%2F004-2685-046%2F004-2685-046_detalhe1.jpg&lp=http%3A%2F%2Fwww.netshoes.com.br%2Fttp%3A%2F%2Fwww.netshoes.com.br%2Fproduto%2Fcamisa-nike-selecao-brasil-i-2014-n-10--torcedor-004-2685&old=&new=239%2C90&misc=9&misc1=26%2C66";
	private static String httpUrl = "https://ssl.vizury.com/analyze/analyze.php?account_id=VIZVRM299&param=e500&orderid=11141048851&orderprice=&pid1=132-7121-406&currency=false&crmid=3005147345&section=1&level=1";
	
	@BeforeSuite
	public void setUp () {
		proxyServer = new ProxyServer(port);
		wdBuilder = new WebDriverBuilder("chrome", true, port);
		// Uncomment the below lines for reproducing the second issue i mentioned in the mail
		// Change the value of httpUrl to that of httpsUrl in the initialization above
		wDriver = wdBuilder.getDriver();
		wDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	}
	
	@BeforeMethod
	public void initProxyAndBrowser () {
		proxyServer.clearSessionInfo();
		proxyServer.initSessionInfo();
		// Comment the below two lines for reproducing the second issue i mentioned in the mail
		//wDriver = wdBuilder.getDriver();
		//wDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		wDriver.get("http://www.vizury.com");
		wDriver.manage().deleteAllCookies();
		wDriver.get("about:blank");
	}
	
	@Test(dataProvider="generateUrls")
	public void simpleProxyTest (String url) {
		wDriver.get(url);
		// In our actual tests, we monitor and collect all the session information collected in the CustomHttpFilters
		// And use the same for performing certain validations
	}
	
	@DataProvider
	public Object[][] generateUrls () {
		return new Object [][] {{httpUrl},{httpsUrl}};
	}
	
	@AfterMethod
	public void clearProxyAndBrowser () {
		// Comment the below lines for reproducing the second issue i mentioned in the mail
		/*if (wDriver != null) {
			wDriver.quit();			
		}*/
	}
	
	@AfterSuite
	public void cleanUp () {
		if (wDriver != null)
			wDriver.quit();
		if (proxyServer != null)
			proxyServer.stop();
	}	
}
