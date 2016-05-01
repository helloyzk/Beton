package com.perfectomobile.beton;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.perfectomobile.androidCommunityPOM_Appium.ANDROID_CommunityBaseView;
import com.perfectomobile.androidCommunityPOM_Appium.ANDROID_ProfilePageView;
import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;
//import com.perfectomobile.test.BasicTest;
import com.perfectomobile.test.BasicTest_Android;

// TODO: Auto-generated Javadoc
/**
 * The Class CommunitySignIn_Android.
 */
public class CommunitySignIn_Android extends BasicTest_Android {


	/**
	 * Test sign in.
	 *
	 * @param username the username
	 * @param password the password
	 * @param message the message
	 * @throws Exception the exception
	 */
	@Test (dataProvider="logInData")
	public void testSignIn(String username, String password, String message) throws Exception{
		boolean testFail = false;
		ANDROID_CommunityBaseView mobileView = null;
		if(this.driver == null){
			Assert.fail("Device not available: " + caps);
		}
	 	  
	 	try{
	 		mobileView = new ANDROID_CommunityBaseView(driver);	

			mobileView.login(username, password);
			//check profile name
			ANDROID_ProfilePageView profile = mobileView.openMenuDrawer().gotoProfile();
			String profileName = profile.getName();
			
			if (profileName.contains(message)){
//	        	resultSheet.setResultByColumnName(true, this.testName, username, password, message);
	        }
			else{
//				resultSheet.setResultByColumnName(false, this.testName, username, password, message);
//				String errorFile = reportFail(message, profileName);
//				resultSheet.addScreenshotByRowNameAsLink(errorFile, this.testName, username, password, message);
				testFail = true;
			}
			//return to dashboard
			profile.backToDashboard();
	 	}
	 	catch(Exception e){
//	 		resultSheet.setResultByColumnName(false, this.testName, username, password, message);
			testFail = true;
//        	String errorFile = PerfectoAppiumUtils.takeScreenshot(driver);
//        	resultSheet.addScreenshotByRowNameAsLink(errorFile, this.testName, username, password, message);
//    		Reporter.log(e.toString());
//    		Reporter.log("Error screenshot saved in file: " + errorFile);
//    		Reporter.log("<br> <img src=" + errorFile + ".png style=\"max-width:50%;max-height:50%\" /> <br>");
	 	}
		
        if(testFail){
        	Assert.fail();
        }

	}

	/**
	 * Search items data.
	 *
	 * @return the object[][]
	 * @throws Exception the exception
	 */
	@DataProvider (name = "logInData", parallel = false)
	public Object[][] searchItemsData() throws Exception{
		  ExcelDriver ed = new ExcelDriver(sysProp.get("inputDataSheet"), sysProp.get("signIn_android"), false);
		  Object[][] s = ed.getData(3);

		  return s;
	}
	
	/**
	 * Instantiates a new community sign in_ android.
	 *
	 * @param caps the caps
	 */
	@Factory(dataProvider="factoryData")
	public CommunitySignIn_Android(DesiredCapabilities caps) {
		super(caps);
	}
}
