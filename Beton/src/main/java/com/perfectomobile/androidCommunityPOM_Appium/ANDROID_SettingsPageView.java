package com.perfectomobile.androidCommunityPOM_Appium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;


// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_SettingsPageView.
 */
public class ANDROID_SettingsPageView extends ANDROID_CommunityBaseView{
	
	/** The btn logout. */
	By btnLogout = By.xpath("//*[@text='Logout']");
	
	/**
	 * Instantiates a new AANDROID_SettingsPageView.
	 *
	 * @param driver the AppiumDriver
	 */
	public ANDROID_SettingsPageView(AndroidDriver<WebElement> driver) {
		super(driver);
	}
	
	/**
	 * Logs the current user out of the application. This automatically launches the login dialog.
	 *
	 * @return the ANDROID_CommunityBaseView object
	 */
	public ANDROID_CommunityBaseView logout(){
		this.driver.findElement(btnLogout).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}

}
