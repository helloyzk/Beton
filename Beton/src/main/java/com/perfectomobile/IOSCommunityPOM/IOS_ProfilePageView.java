package com.perfectomobile.IOSCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;


public class IOS_ProfilePageView extends IOS_CommunityBaseView {
	
	String navButton = "'Navigate up'";
	By btnNavUp = By.id("android:id/up");		
	By txtName = By.id("com.bloomfire.android.perfecto:id/name");
	By txtTitle = By.id("com.bloomfire.android.perfecto:id/title");
	By btnEditProfile = By.id("com.bloomfire.android.perfecto:id/item_edit_profile");	// Works
//	By btnOptions = By.xpath("//*[@class='android.widget.ImageButton']");	// Works
//	By txtFollow = By.xpath("//*[@text='Follow']");
	
	public IOS_ProfilePageView(RemoteWebDriver driver) {
		super(driver);
	}
	
	public IOS_CommunityBaseView backToDashboard(){
		//System.out.println(btnNavUp);
		this.driver.findElement(btnNavUp).click();
		return new IOS_CommunityBaseView(this.driver);
	}
	
	public String getName(){
		String author = this.driver.findElement(txtName).getAttribute("text");
		System.out.println("------------------------>>>Profile Name:" + author);
		return author;
	}
	public String getTitle(){
		String author = this.driver.findElement(txtTitle).getAttribute("text");
		System.out.println("------------------------>>>Profile User Title:" + author);
		return author;
	}

	public IOS_PostPageView editProfile(){
		this.driver.findElement(btnEditProfile).click();
		return new IOS_PostPageView(this.driver);
	}
	
}
