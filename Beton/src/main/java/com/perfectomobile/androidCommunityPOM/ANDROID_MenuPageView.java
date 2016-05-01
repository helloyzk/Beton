package com.perfectomobile.androidCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfectomobile.utils.PerfectoUtils;

/**
 * The Class ANDROID_MenuPageView.
 * 	- This page view class describes the Perfecto Community Android application's main menu. 
 * 	- This class contains page elements of the Menu page view.
 * 	- Methods interact with mapped page elements and return the appropriate page object.
 */
public class ANDROID_MenuPageView extends ANDROID_CommunityBaseView{
	
		/** Page element: The Close Menu button. */
	// Page elements
		By btnCloseMenu = By.xpath("//*[@text='Perfecto Mobile']");
		
		/** Page element: The Dashboard menu item. */
		By btnDashboard = By.xpath("//*[@text='Dashboard']");
		
		/** Page element: The Stash menu item. */
		By btnStash = By.xpath("//*[@text='Stash']");
		
		/** Page element: The Tags menu item. */
		By btnTags = By.xpath("//*[@text='Tags']");		
		
		/** Page element: The Categories menu item. */
		By btnCategories = By.xpath("//*[@text='Categories']");
		
		/** Page element: The Posts menu item. */
		By btnPosts = By.xpath("//*[@text='Posts']");
		
		/** Page element: The Series menu item. */
		By btnSeries = By.xpath("//*[@text='Series']");
		
		/** Page element: The Members menu item. */
		By btnMembers = By.xpath("//*[@text='Members']");
		
		/** Page element: The Profile menu item. */
		By btnProfile = By.xpath("//*[@text='Profile']");
		
		/** Page element: The Communities menu item. */
		By btnCommunities = By.xpath("//*[@text='Your Communities']");
		
		/** Page element: The Settings menu item. */
		By btnSettings = By.xpath("//*[@text='Settings']");
	
	

	/**
	 * Instantiates a new ANDROID_MenuPageView.
	 *
	 * @param driver the driver
	 */
	public ANDROID_MenuPageView(RemoteWebDriver driver) {
		super(driver);
		PerfectoUtils.sleep(3000);
		
	}
	
	/**
	 * Closes the menu and returns to the previous view.
	 *
	 * @return the ANDROID_CommunityBaseVieww
	 */
	public ANDROID_CommunityBaseView closeMenu(){
		this.driver.findElement(btnCloseMenu).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}
	
	/**
	 * Opens Dashboard view.
	 *
	 * @return the ANDROID_CommunityBaseView
	 */
	public ANDROID_CommunityBaseView gotoDashboard(){
		this.driver.findElement(btnDashboard).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}
	
	/**
	 * Opens profile page view.
	 *
	 * @return the ANDROID_ProfilePageView
	 */
	public ANDROID_ProfilePageView gotoProfile(){
		this.driver.findElement(btnProfile).click();
		return new ANDROID_ProfilePageView(this.driver);
	}
//	public PCStashView gotoStash(){
//		this.driver.findElement(btnStash).click();
//		return new PCStashView(this.driver);
//	}
//	
//	public PCTagsView gotoTags(){
//		this.driver.findElement(btnTags).click();
//		return new PCTagsView(this.driver);
//	}
	
	/**
 * Opens Settings page view.
 *
 * @return the ANDROID_SettingsPageView
 */
public ANDROID_SettingsPageView gotoSettings(){
		this.driver.findElement(btnSettings).click();
		return new ANDROID_SettingsPageView(this.driver);
	}
}
