package com.perfectomobile.beton;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.perfectomobile.IOSCommunityPOM.IOS_CommunityBaseView;
import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;
import com.perfectomobile.test.BasicTest;
import com.perfectomobile.utils.PerfectoUtils;

public class CommunitySignIn_IOS extends BasicTest {
	
	@Test (dataProvider="logInData")
	public void testSignIn(String username, String password, String message) throws Exception{
		boolean testFail = false;
		IOS_CommunityBaseView mobileView = null;
		
		if(this.driver == null){
			Assert.fail("Device not available: " + caps);
		}
	 	  
	 	try{
	 		mobileView = new IOS_CommunityBaseView(driver);

			mobileView.login(username, password);
			String profileName = mobileView.openMenuDrawer().gotoProfile().getName();
			
			if (profileName.contains(message)){
	        	resultSheet.setResultByColumnName(true, this.testName, username, password, message);
	        }
			else{
				resultSheet.setResultByColumnName(false, this.testName, username, password, message);
				String errorFile = reportFail(message, profileName);
				resultSheet.addScreenshotByRowNameAsLink(errorFile, this.testName, username, password, message);
				testFail = true;
			}
	 	}
	 	catch(Exception e){
	 		resultSheet.setResultByColumnName(false, this.testName, username, password, message);
			testFail = true;
        	String errorFile = PerfectoUtils.takeScreenshot(driver);
        	resultSheet.addScreenshotByRowNameAsLink(errorFile, this.testName, username, password, message);
    		Reporter.log(e.toString());
    		Reporter.log("Error screenshot saved in file: " + errorFile);
    		Reporter.log("<br> <img src=" + errorFile + ".png style=\"max-width:50%;max-height:50%\" /> <br>");
	 	}
		
        if(testFail){
        	Assert.fail();
        }

	}

	@DataProvider (name = "logInData", parallel = false)
	public Object[][] searchItemsData() throws Exception{
		  ExcelDriver ed = new ExcelDriver(sysProp.get("inputDataSheet"), "signIn", false);
		  Object[][] s = ed.getData(3);

		  return s;
	}
	

	@Factory(dataProvider="factoryData")
	public CommunitySignIn_IOS(DesiredCapabilities caps) {
		super(caps);
	}
}
