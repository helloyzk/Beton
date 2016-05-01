package com.perfectomobile.androidCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfectomobile.utils.*;
import com.perfectomobile.utils.PerfectoUtils.*;


// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_SearchResultsView.
 */
public class ANDROID_SearchResultsView extends ANDROID_CommunityBaseView{
	
	/** The btn nav up. */
	By btnNavUp = By.id("android:id/up");
//	By btnNavUp = By.id("android:id/action_bar_title");
	
	/**
 * Instantiates a new ANDROI d_ search results view.
 *
 * @param driver the driver
 */
public ANDROID_SearchResultsView(RemoteWebDriver driver) {
		super(driver);
	}
	
	/**
	 * Select search result.
	 *
	 * @param findText the find text
	 * @return the ANDROI d_ post page view
	 */
	public ANDROID_PostPageView selectSearchResult(String findText){
		VisualDriverControl.clickVisualText(this.driver, findText, true, ScrollNext.SWIPE_UP, 30, 1, 90, MatchMode.CONTAIN,96);
		return new ANDROID_PostPageView(this.driver);
	}
	
	/**
	 * Back to set search.
	 *
	 * @return the ANDROI d_ community base view
	 */
	public ANDROID_CommunityBaseView backToSetSearch(){
		//System.out.println(btnNavUp);
		this.driver.findElement(btnNavUp).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}
}
