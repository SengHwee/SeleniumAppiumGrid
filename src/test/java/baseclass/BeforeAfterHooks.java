package baseclass;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import setup.CMD;

public class BeforeAfterHooks {

	public AndroidDriver<AndroidElement> androidDriver;
	public WebDriver browserDriver;
	public String automationType = ""; 

	@BeforeSuite
	public void runSetup() throws IOException, InterruptedException {
		CMD.startHUB();
	}

	@BeforeTest(alwaysRun=true)
	@Parameters({"browserName","port","deviceName","udid","systemPort","app","appPackage","appActivity"})
	public void deviceCaps(String browserName, @Optional String port, 
			@Optional String deviceName, @Optional String udid, @Optional String systemPort,
			@Optional String app, @Optional String appPackage, @Optional String appActivity) throws IOException, InterruptedException {

		String rPath= System.getProperty("user.dir"); //User Directory

		if(port==null) {
			port= "4444";
		}

		switch (browserName.toUpperCase()) {

		case("CHROME"):
			CMD.registerBROWSER(browserName);
		DesiredCapabilities chromeCaps = new DesiredCapabilities();
		chromeCaps.setBrowserName("chrome");
		chromeCaps.setPlatform(Platform.ANY);	

		ChromeOptions chromeoptions = new ChromeOptions();
		chromeoptions.addArguments("--window-position=0,0");
		chromeoptions.addArguments("--window-size=800,450");
		chromeoptions.merge(chromeCaps);

		URL chromeURL = new URL ("http://localhost:"+port+"/wd/hub");
		WebDriver chromeDriver = new RemoteWebDriver(chromeURL,chromeoptions);
		browserDriver= chromeDriver;
		automationType= "browser";
		break;


		case("FIREFOX"):
			CMD.registerBROWSER(browserName);
		DesiredCapabilities firefoxCaps = new DesiredCapabilities();
		firefoxCaps.setBrowserName("firefox");
		firefoxCaps.setPlatform(Platform.ANY);

		//			FirefoxOptions firefoxoptions = new FirefoxOptions();
		//			firefoxoptions.addArguments(arguments)
		//			firefoxoptions.addArguments("--window-size=800,450");
		//			firefoxoptions.merge(firefoxCaps);

		URL firefoxURL = new URL ("http://localhost:"+port+"/wd/hub");
		WebDriver firefoxDriver = new RemoteWebDriver(firefoxURL,firefoxCaps);
		browserDriver = firefoxDriver;
		firefoxDriver.manage().window().setSize(new Dimension(800,450));
		firefoxDriver.manage().window().setPosition(new Point(800,0));
		automationType= "browser";
		break;

		case("ANDROIDLOCAL"):
			CMD.loadscrcpy(udid);
		CMD.registerMOBILE(deviceName);
		DesiredCapabilities androidCaps = new DesiredCapabilities();
		androidCaps.setCapability("automationName", "UiAutomator2");
		androidCaps.setCapability("platformName", "Android");
		androidCaps.setCapability("deviceName", deviceName);
		androidCaps.setCapability("udid", udid);
		androidCaps.setCapability("systemPort", systemPort);
		//androidCaps.setCapability("app", rPath + app); 		//uncomment if app is not installed
		androidCaps.setCapability("appPackage", appPackage);
		androidCaps.setCapability("appActivity", appActivity);
		androidCaps.setCapability("noReset", "false");
		androidCaps.setCapability("fullReset", "false");

		URL androidURL = new URL ("http://localhost:"+port+"/wd/hub");
		androidDriver = new AndroidDriver<AndroidElement>(androidURL,androidCaps);
		automationType= "android";
		break;

		}
	}


	@AfterTest(alwaysRun=true)
	public void tearDown() {
		switch(automationType.toUpperCase()){

		case("BROWSER"): 		
			browserDriver.quit();
		System.out.println("Browser Test Ended");
		break;

		case("ANDROID"): 		
			androidDriver.closeApp();
		System.out.println("App Closed");
		androidDriver.quit();
		System.out.println("Android Test Ended");
		break;
		}
	}

}
