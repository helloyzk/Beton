/*
 * 
 */
package com.perfectomobile.androidCommunityPOM_Appium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.perfectomobile.utils.PerfectoUtils;

import io.appium.java_client.android.AndroidDriver;



// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_CommunityBaseView. 
 * 	- This page view class describes the Perfecto Community Android application's initial launch view, the Dashboard page view.
 * 	- This is the Base POM class that is initially created and extended by all other page view classes in the androidCommunityPOM package. 
 * 	- This class contains page elements of the Dashboard page view, as well as those existing on all other pages
 * 	- Methods interact with mapped page elements
 */
public class ANDROID_CommunityBaseView {
	
	/** The driver. */
	AndroidDriver<WebElement> driver;	
		
	/** Page element: The action bar title. */
	
	By actionBarTitle = By.id("android:id/action_bar_title");
	
	/** Page element: The dashboard title. */
	By dashboardTitle = By.xpath("//*[@text='Perfecto Mobile Community']");
	
	/** Page element: The txt sign in. */
	By txtSignIn = By.xpath("//*[@text='Sign In']");
	
	/** Page element: The close search button. */
	By closeSearch = By.id("android:id/up");		
			
	/** Page element: The input email address. */
	By inputEmailAddress = By.id("com.bloomfire.android.perfecto:id/email_address");
	
	/** Page element: The input password. */
	By inputPassword = By.id("com.bloomfire.android.perfecto:id/password");
	
	/** Page element: The btn done. */
	By btnDone = By.xpath("//*[@text='Done']");
	
	/** Page element: The btn search. */
	By btnSearch = By.id("com.bloomfire.android.perfecto:id/menu_search");
	
	/** Page element: The input search. */
	By inputSearch = By.id("android:id/search_src_text");
	
	/**
	 * Instantiates a new ANDROID_CommunityBaseView.
	 *
	 * @param driver the AppiumDriver
	 */
	// Class constructor
	public ANDROID_CommunityBaseView(AndroidDriver<WebElement> driver){
		this.driver = driver;
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);			
	}
	
	/**
	 * Logs into the Perfecto Community Android application. 
	 * If the application is in a currently logged in state, it will first log out and then log in with the provided credentials
	 * Throws an Illegal state exception if the login attempt fails
	 *
	 * @param appUser the application user
	 * @param appUserPwd the application user password
	 * @return the ANDROID_CommunityBaseView object
	 */
	// init to check for BasePage load and log-in if necessary	
	public ANDROID_CommunityBaseView login(String appUser, String appUserPwd){		
		//Check if login view is open
		try{				
			this.driver.findElement(txtSignIn);			
		}catch (Exception e){	//already signed in -- logout		
			openMenuDrawer().gotoSettings().logout();			
		}
		//enter credentials and submit
		try {			
			this.driver.findElement(inputEmailAddress).sendKeys(appUser);
			this.driver.findElement(inputPassword).click();
			this.driver.findElement(inputPassword).sendKeys(appUserPwd);
			this.driver.findElement(btnDone).click();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Check for dashboard page load
		try{
			PerfectoUtils.fluentWait(actionBarTitle, driver, 10);
        	this.driver.findElement(dashboardTitle);
        	return new ANDROID_CommunityBaseView(this.driver);
        }
        catch(Exception e){
        	Reporter.log("<br>" + "---------------------->>>Login attempt failed " +  "<br>");
        	String errorFile = PerfectoUtils.takeScreenshot(driver);
			Reporter.log("Error screenshot saved in file: " + errorFile);
			
        	throw new IllegalStateException();
        }
		
	}
		
	/**
	 * Open the Menu drawer.
	 *
	 * @return the ANDROID_MenuPageView object
	 */
	//Open menu drawer	
	public ANDROID_MenuPageView openMenuDrawer(){
		this.driver.findElement(actionBarTitle).click();
		return new ANDROID_MenuPageView(this.driver);
	}
	
	/**
	 * Click the search button.
	 *
	 * @return the ANDROID_MenuPageView object
	 */
	// click top menu search button 
	public ANDROID_MenuPageView clickSearchButton(){
		this.driver.findElement(btnSearch).click();
		return new ANDROID_MenuPageView(this.driver);
	}
	
	/**
	 * Close the Search interface.
	 *
	 * @return the ANDROID_MenuPageView
	 */
	// close the search field 
	public ANDROID_MenuPageView closeSearch(){
		PerfectoUtils.sleep(1000);
		this.driver.findElement(closeSearch).click();
		if(this.driver.findElement(closeSearch).isDisplayed()){ //click again if search bar has only cleared
			this.driver.findElement(closeSearch).click();
		}
		return new ANDROID_MenuPageView(this.driver);
	}
	
	/**
	 * Input search text.
	 *
	 * @param q the text to search
	 * @return the ANDROID_SearchResultsView
	 */
	//input text into exposed search field after clicking on the search button
	public ANDROID_SearchResultsView inputSearchText(String q){
		this.driver.findElement(btnSearch).sendKeys(q);
		// click keyboard enter key (KEYCODE_ENTER = 66)
		this.driver.sendKeyEvent(66);
		//VisualDriverControl.clickByTextOffset(this.driver, "DEL", LabelPosition.BELOW, "10%");

		return new ANDROID_SearchResultsView(this.driver);
	}

	
}
