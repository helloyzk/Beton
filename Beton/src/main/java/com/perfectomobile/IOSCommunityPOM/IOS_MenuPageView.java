package com.perfectomobile.IOSCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfectomobile.utils.PerfectoUtils;

public class IOS_MenuPageView extends IOS_CommunityBaseView{
	
	// Page elements
	By btnCloseMenu = By.xpath("//*[@text='Perfecto Mobile']");
	By btnDashboard = By.xpath("//*[@text='Dashboard']");
	By btnStash = By.xpath("//*[@text='Stash']");
	By btnTags = By.xpath("//*[@text='Tags']");		
	By btnCategories = By.xpath("//*[@text='Categories']");
	By btnPosts = By.xpath("//*[@text='Posts']");
	By btnSeries = By.xpath("//*[@text='Series']");
	By btnMembers = By.xpath("//*[@text='Members']");
	By btnProfile = By.xpath("//*[@text='Profile']");
	By btnCommunities = By.xpath("//*[@text='Your Communities']");
	By btnSettings = By.xpath("//*[@text='Settings']");
	
	
	public IOS_MenuPageView(RemoteWebDriver driver) {
		super(driver);
		PerfectoUtils.sleep(3000);
		
	}
	public IOS_CommunityBaseView closeMenu(){
		this.driver.findElement(btnCloseMenu).click();
		return new IOS_CommunityBaseView(this.driver);
	}
	
	public IOS_CommunityBaseView gotoDashboard(){
		this.driver.findElement(btnDashboard).click();
		return new IOS_CommunityBaseView(this.driver);
	}
	public IOS_ProfilePageView gotoProfile(){
		this.driver.findElement(btnDashboard).click();
		return new IOS_ProfilePageView(this.driver);
	}
	
	public IOS_SettingsPageView gotoSettings(){
		this.driver.findElement(btnSettings).click();
		return new IOS_SettingsPageView(this.driver);
	}
}
