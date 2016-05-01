package com.perfectomobile.IOSCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.perfectomobile.utils.PerfectoUtils.MatchMode;
import com.perfectomobile.utils.PerfectoUtils.ScrollNext;
import com.perfectomobile.utils.VisualDriverControl;

public class IOS_SearchResultsView extends IOS_CommunityBaseView {
	
	By btnNavUp = By.id("android:id/up");
//	By btnNavUp = By.id("android:id/action_bar_title");
	
	public IOS_SearchResultsView(RemoteWebDriver driver) {
		super(driver);
	}
	
	public IOS_PostPageView selectSearchResult(String findText){
		VisualDriverControl.clickVisualText(this.driver, findText, true, ScrollNext.SWIPE_UP, 30, 1, 90, MatchMode.CONTAIN,96);
		return new IOS_PostPageView(this.driver);
	}
	
	public IOS_CommunityBaseView backToSetSearch(){
		System.out.println(btnNavUp);
		this.driver.findElement(btnNavUp).click();
		return new IOS_CommunityBaseView(this.driver);
	}
}
