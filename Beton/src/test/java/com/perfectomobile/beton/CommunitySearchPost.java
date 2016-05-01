package com.perfectomobile.beton;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Factory;

import com.perfectomobile.test.BasicTest;

public class CommunitySearchPost extends BasicTest{
	
	@Factory (dataProvider="factoryData")
	public CommunitySearchPost(DesiredCapabilities caps) {
		super(caps);
		// TODO Auto-generated constructor stub
	}
}
