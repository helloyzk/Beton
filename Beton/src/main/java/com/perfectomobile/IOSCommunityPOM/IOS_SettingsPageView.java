package com.perfectomobile.IOSCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class IOS_SettingsPageView extends IOS_CommunityBaseView{
	
	By btnLogout = By.xpath("//*[@text='Logout']");
	
	public IOS_SettingsPageView(RemoteWebDriver driver) {
		super(driver);
	}
	
	public IOS_CommunityBaseView logout(){
		this.driver.findElement(btnLogout).click();
		return new IOS_CommunityBaseView(this.driver);
	}

}
