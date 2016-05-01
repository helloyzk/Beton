package com.perfectomobile.beton;

import java.io.IOException;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;
import com.perfectomobile.test.BasicTest;
import com.perfectomobile.webCommunityPOM.WebCommunityBaseView;

public class CommunitySignIn extends BasicTest {


	@Test (dataProvider="logInData")
	public void signIn(String username, String password, String greetingMsg) throws Exception{
		boolean testFail = false;
		WebCommunityBaseView mobileView = null;
		if(this.driver == null){
			Assert.fail("Device not available: " + caps);
		}
	 	try{
	 		mobileView = new WebCommunityBaseView(driver);
	        mobileView = mobileView.init();

			mobileView = mobileView.login(username, password);
			String welcomeMessage = mobileView.getWelcomeMessage();
			
			if (welcomeMessage.equals(greetingMsg)){
				reportPass("passed", this.testName, username, password, greetingMsg);
				//addRowToDetailedSheet(true, username, password, message);
	        	//resultSheet.setResultByColumnName(true, );
	        }
			else{
				reportFail(greetingMsg, welcomeMessage,this.testName, username, password, greetingMsg);

			}
	 	}
	 	catch(Exception e){

	 	}
		
        if(testFail){
        	Assert.fail();
        }
	}

	@DataProvider (name = "logInData", parallel = false)
	public Object[][] searchItemsData(){
		 Object[][] s = null;
		try {
		  ExcelDriver ed = new ExcelDriver(sysProp.get("inputWorkbook"), sysProp.get("signInSheet"), false);
		  s = ed.getData(3);
		} catch(IOException e) {
			System.out.println("Not able to search data from excel: " + sysProp.get("inputWorkbook"));
			System.err.println("IndexOutOfBoundsException: " + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	@Factory(dataProvider="factoryData")
	public CommunitySignIn(DesiredCapabilities caps) {
		super(caps);
	}
}
