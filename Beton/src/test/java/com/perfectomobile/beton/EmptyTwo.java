package com.perfectomobile.beton;

import java.io.IOException;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;
import com.perfectomobile.test.BasicTest;

public class EmptyTwo extends BasicTest {

	@Factory(dataProvider="factoryData")
	public EmptyTwo(DesiredCapabilities caps) {
		super(caps);
		// TODO Auto-generated constructor stub
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
	
	@Test (dataProvider="logInData")
	public void testEmpty(String username, String password, String message) throws Exception{
		boolean testFail = false;
		//WebCommunityBaseView mobileView = null;
		if(this.driver == null){
			Assert.fail("Device not available: " + caps);
		}
	 	try{
	 		System.out.println("test");
	 		reportPass("test pass", "param1", "param2");
	 	}
	 	catch(Exception e){
	 		e.printStackTrace();
	 	}
		
        if(testFail){
        	Assert.fail();
        }

	}
}
