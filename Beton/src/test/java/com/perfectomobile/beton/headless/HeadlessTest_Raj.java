package com.perfectomobile.beton.headless;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class HeadlessTest_Raj {
	public static AndroidDriver<WebElement> andDriver;
	public static IOSDriver<WebElement> iosDriver;
	
	@Test
	public void test_IOS(ITestContext context) throws Exception{
		
//	 	WebElement el = driver.findElement(By.name("Animation"));
//	
//	 	Assert.assertEquals("Animation", el.getText());
//
//        el = driver.findElementByClassName("android.widget.TextView");
//        
//        Assert.assertEquals("API Demos", el.getText());
//        
//        el = driver.findElement(By.name("App"));
//        el.click();
//        List<WebElement> els = driver.findElementsByClassName("android.widget.TextView");
//        
//        Assert.assertEquals("Activity", els.get(2).getText());
       // driver.closeApp();
		andDriver.get("www.perfectomobile.com");
		


		

	}


	@BeforeTest
    public void setUp(ITestContext context) throws Exception {
	    //    File classpathRoot = new File(System.getProperty("user.dir"));
		//    File appDir = new File(classpathRoot, "../../../apps/ApiDemos/bin");
	    //    File app = new File(appDir, "ApiDemos-debug.apk");
        		 DesiredCapabilities cap = new DesiredCapabilities();
		//        capabilities.setCapability("deviceName","Android Emulator");
		//        capabilities.setCapability("platformVersion", "4.4");
		//        capabilities.setCapability("app", app.getAbsolutePath());
		//        capabilities.setCapability("appPackage", "io.appium.android.apis");
		//        capabilities.setCapability("appActivity", ".ApiDemos");
		//        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        
        /* works for Android */
        if (context.getCurrentXmlTest().getParameter("Run_Mode").equals("Android")) {
	        cap.setCapability("user", context.getCurrentXmlTest().getParameter("user"));
			cap.setCapability("password", context.getCurrentXmlTest().getParameter("password"));		
			cap.setCapability("udid", context.getCurrentXmlTest().getParameter("deviceID_android"));
			cap.setCapability("deviceName", context.getCurrentXmlTest().getParameter("deviceID_android"));	
			cap.setCapability(CapabilityType.BROWSER_NAME, "chrome");
			//driver = new AndroidDriver(new URL("https://" + context.getCurrentXmlTest().getParameter("URL") + "/nexperience/perfectomobile/wd/hub"), cap);
			System.out.println("Driver Created Succesfully");
        }
		   /* works for IOS */
        if (context.getCurrentXmlTest().getParameter("Run_Mode").equals("IOS")) {
	        cap.setCapability("user", context.getCurrentXmlTest().getParameter("user"));
			cap.setCapability("password", context.getCurrentXmlTest().getParameter("password"));		
		//	cap.setCapability("udid", context.getCurrentXmlTest().getParameter("deviceID_iphone"));
			cap.setCapability(CapabilityType.BROWSER_NAME, "safari");
			cap.setCapability("deviceName", context.getCurrentXmlTest().getParameter("deviceID_iphone"));
			//cap.setCapability("automationName","Appium");
			//cap.setCapability("app", "Settings");
		//	cap.setCapability("bundleID", "Settings");
			
			//cap.setCapability(CapabilityType.BROWSER_NAME, "safari");  /* Tried Safari and it doesn't work */
			//driver = new IOSDriver(new URL("https://" + context.getCurrentXmlTest().getParameter("URL") + "/nexperience/perfectomobile/wd/hub"), cap);
			System.out.println("IOS Iphone Driver Created Succesfully");
        }
	}

    @AfterTest
    public void tearDown() throws Exception {
	//	driver.quit();
    }
	
	
	
}
