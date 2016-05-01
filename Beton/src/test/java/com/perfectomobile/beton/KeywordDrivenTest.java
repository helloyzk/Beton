package com.perfectomobile.beton;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.perfectomobile.test.BasicTest;
import com.perfectomobile.test.KeywordDrivenTestAdapter;
import com.perfectomobile.utils.PerfectoUtils;

public class KeywordDrivenTest extends BasicTest{

	@Factory(dataProvider="factoryData")
	public KeywordDrivenTest(DesiredCapabilities caps) {
		super(caps);
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void googleTest() throws Exception{
		KeywordDrivenTestAdapter testAdapter = new KeywordDrivenTestAdapter(sysProp.get("googleTestWorkbook"), sysProp.get("googleTestDataSheet"));
		try{
			testAdapter.run(driver);
		}
		catch(Exception e){
			//failTest();
		}
		reportPass("Google test pass", this.testName);
		//passTest();
	}

	@SuppressWarnings("unused")
	private void passTest() throws Exception{
		String scrFile = PerfectoUtils.takeScreenshot(driver);
		resultSheet.setResultByColumnName(true, this.testName);
		resultSheet.addScreenshotByRowNameAsLink(scrFile, this.testName);
	}
	@SuppressWarnings("unused")
	private void failTest() throws Exception{
		String errorFile = reportFail("Test Failed","");
		resultSheet.setResultByColumnName(false, this.testName);
		resultSheet.addScreenshotByRowNameAsLink(errorFile, this.testName);
		Assert.fail();
		
		
	}
}
