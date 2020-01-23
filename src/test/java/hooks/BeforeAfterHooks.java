package hooks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class BeforeAfterHooks{
	
	private static final String Android = null;
	//define public variable
	public static AndroidDriver<AndroidElement> androiddriver;
	//public static WebDriver chromedriver;
	
	@BeforeMethod(alwaysRun=true)
	@Parameters({"port", "deviceName", "udid", "app", "appPackage", "appActivity"})
	public void setCaps(String port, String deviceName, String udid, String app, String appPackage, String appActivity){

		String rPath= System.getProperty("user.dir"); //resource path
		/*
		//define Chrome Capabilities
		DesiredCapabilities chromeCaps = new DesiredCapabilities();
		chromeCaps.setBrowserName("browserName");
		chromeCaps.setPlatform(Platform.ANY);	
		
		ChromeOptions chromeoptions = new ChromeOptions();
		chromeoptions.merge(chromeCaps);
		*/
		//define Android Capabilities
		DesiredCapabilities androidCaps = new DesiredCapabilities();
		androidCaps.setCapability("automationName", "UiAutomator2");
		androidCaps.setCapability("platformName", "Android");
		androidCaps.setCapability("deviceName", deviceName);
		androidCaps.setCapability("udid", udid);
		androidCaps.setCapability("app", rPath + app );
		androidCaps.setCapability("appPackage", appPackage);
		androidCaps.setCapability("appActivity", appActivity);
		androidCaps.setCapability("noReset", "true");
		androidCaps.setCapability("fullReset", "false");
		
		//Url to hub
		URL url = null;
		try {
			url = new URL ("http://localhost:"+port+"/wd/hub");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//creating automation driver
		androiddriver= new AndroidDriver<AndroidElement> (url,androidCaps);
		System.out.println ("Android Driver Started");
		//chromedriver= new RemoteWebDriver(url,chromeoptions);
	
		androiddriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}
		
	@AfterMethod(alwaysRun = true)
	public static void endTest() {
		androiddriver.close();
		System.out.println("Test Ended");
		//chromedriver.quit();
	}

}
