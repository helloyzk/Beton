package com.perfectomobile.androidCommunityPOM_Appium;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;

import com.perfectomobile.utils.PerfectoUtils;

import io.appium.java_client.android.AndroidDriver;


// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_SearchResultsView.
 */
public class ANDROID_SearchResultsView extends ANDROID_CommunityBaseView{
	
	/** The btn nav up. */
	By btnNavUp = By.id("android:id/up");
//	By btnNavUp = By.id("android:id/action_bar_title");
	
	/**
 * Instantiates a new ANDROID_SearchResultsView.
 *
 * @param driver the driver
 */
public ANDROID_SearchResultsView(AndroidDriver<WebElement> driver) {
		super(driver);
	}
	
	/**
	 * Select search result.
	 *
	 * @param findResult the search result to select
	 * @return the ANDROID_PostPageView object
	 */
	public ANDROID_PostPageView selectSearchResult(String findResult) throws ElementNotVisibleException{
//		VisualDriverControl.clickVisualText(this.driver, findResult, true, ScrollNext.SWIPE_UP, 30, 1, 90, MatchMode.CONTAIN,96);
		int maxScroll = 10;
		By findText = By.xpath("//*[contains(@text,'" + findResult+"')]");
		for (int i = 0; i < maxScroll; i++) {
			if (this.driver.findElements(findText).size() > 0) {
				this.driver.findElement(findText).click();
			} else if (i == maxScroll-1){
				throw new ElementNotVisibleException("Element with the text: "+ findResult + " was not found within "+maxScroll+" page scrolls.");
			} else {
				this.driver.swipe(500, 1600, 500, 800, 400);
			} 
		}
		
		return new ANDROID_PostPageView(this.driver);
	}
	
	/**
	 *  Go Back to set the search query.
	 *
	 * @return the ANDROID_CommunityBaseView
	 */
	public ANDROID_CommunityBaseView backToSetSearch(){
		//System.out.println(btnNavUp);
		PerfectoUtils.sleep(1000);
		this.driver.findElement(btnNavUp).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}
}
