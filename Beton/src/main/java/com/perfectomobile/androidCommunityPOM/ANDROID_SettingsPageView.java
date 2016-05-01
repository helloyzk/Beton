package com.perfectomobile.androidCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;


// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_SettingsPageView.
 */
public class ANDROID_SettingsPageView extends ANDROID_CommunityBaseView{
	
	/** The btn logout. */
	By btnLogout = By.xpath("//*[@text='Logout']");
	
	/**
	 * Instantiates a new ANDROI d_ settings page view.
	 *
	 * @param driver the driver
	 */
	public ANDROID_SettingsPageView(RemoteWebDriver driver) {
		super(driver);
	}
	
	/**
	 * Logout.
	 *
	 * @return the ANDROI d_ community base view
	 */
	public ANDROID_CommunityBaseView logout(){
		this.driver.findElement(btnLogout).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}

}
