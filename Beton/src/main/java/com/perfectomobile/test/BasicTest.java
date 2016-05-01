package com.perfectomobile.test;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import com.perfectomobile.dataDrivers.excelDriver.ExcelDriver;
import com.perfectomobile.utils.PerfectoUtils;



/**
 * BasicTest is the abstract base class for all tests which are
 * implemented with the Beton framework.
 * The BasicTest class is responsible for:
 * <ul>
 * 	<li> Reading the capabilities file and instantiate drivers per capabilities sets
 *  <li> Dispatching the tests
 * </ul>
 * <p>
 * BasicTest provides methods for the following:
 * <ul>
 * 	<li> Storing/Reading system properties
 * 	<li> Reading device properties
 * 	<li> Writing test results to DB (currently Excel only)
 *  <li> Writing test results to HTML report
 * </ul>
 * 
 * @author Avner Gershtansky
 *
 */
public abstract class BasicTest {
	
	/**
	 * The {@link RemoteWebDriver} driver is used for communication with the device under test
	 */
	protected RemoteWebDriver driver;
	/**
	 * The {@link ExcelDriver} ed is used for reading test data, mostly via dataProviders
	 */
	protected ExcelDriver ed;
	/**
	 * The name of the test function
	 */
	protected String testName;
	/**
	 * The name/number of test cycle or release version
	 */
	protected String testCycle;
	/**
	 * The device description as it's stored on the Perfecto Mobile cloud
	 */
	protected String deviceDesc;
	private boolean isFirstRun = true;
	/**
	 * The {@link ExcelDriver} resultSheet is used for writing test results to Excel DB
	 */
	protected ExcelDriver resultSheet;
	
	/**
	 * The {@link ExcelDriver} sheet is used for writing the full detail test results to Excel DB
	 */
	protected ExcelDriver detailedResultSheet;
	
	/**
	 * The {@link HashMap} deviceProperties stores all device properties from the Perfecto Mobile Cloud
	 */
	private HashMap<String,String> deviceProperties;
	
	/**
	 * The {@link HashMap} testProperties stores all test properties, include device properties
	 */
	private HashMap<String, String> testProperties;
	/**
	 * The {@link DesiredCapabilities} caps stores the capabilities for every device under test
	 */
	protected DesiredCapabilities caps;
	/**
	 * The {@link HashMap} sysProp stores system properties for the test
	 */
	public static HashMap<String,String> sysProp;
	
	/**
	 * @param caps
	 * @description constructor
	 */
	public BasicTest(DesiredCapabilities caps){
		this.caps = caps;
		sysProp = Init.getSysProp();
	}
	
	/**
	 * Reads the capabilities from the Excel sheet, to be passed to the test factory
	 * The files path and sheet name are taken from the properties file entries:
	 * <ul>
	 * 	<li> inputDataSheet - File path
	 *  <li> deviceSheet - Sheet name, inside the Excel file
	 * </ul>
	 * @return 2-Dim object with lists of capabilities
	 * @throws Exception
	 */
	@DataProvider(name="factoryData", parallel=true)
	public static Object[][] factoryData() throws Exception { 		
		 ArrayList<HashMap<String,String>> listMap = new ArrayList<HashMap<String,String>>();
		 listMap = getCapabilitiesListMapFromExcel(Init.prop().get("inputWorkbook"), Init.prop().get("deviceSheet"));
		 Object[][] s = PerfectoUtils.getCapabilitiesArray(listMap);
		 return s;
	}
	
 
	protected static ArrayList<HashMap<String,String>> getCapabilitiesListMapFromExcel(String filepath, String sheetName) throws Exception{
		
		// Open workbook
		ExcelDriver ed = new ExcelDriver(filepath, sheetName, false);
		
//		String absolutePath = file.getAbsolutePath();
//		ed.setWorkbook(absolutePath);
//		
//		// Open sheet
//		ed.setSheet(sheetName, false);
//					// Read the sheet into 2 dim (String)Object array.
//		// "3" is the number of columns to read.
		return ed.getDataWithHeadersAsHashMap();
	}

	
	
	/**@ChangeLog: Changed from variable to context.
	 * @param testCycle
	 * @param context
	 */
	@BeforeClass
	public void beforeClass(ITestContext context){
		//this.testCycle = context.getCurrentXmlTest().getParameter("testCycle");
		this.testCycle = sysProp.get("testCycle");
	}
	
	
	@BeforeMethod
	public void beforeMethod(Method method) throws Exception{
		this.testName = method.getName();
		
		if(!isFirstRun){
			return;
		}
		this.driver = null;
		isFirstRun = false;
		System.out.println("Run started");
		
		if(this.caps.getCapability("deviceName") != null) {
			String deviceName = this.caps.getCapability("deviceName").toString();
			switch (deviceName.toLowerCase()){
			case "chrome":
				try{
					DesiredCapabilities cDc = DesiredCapabilities.chrome();
					this.driver = new RemoteWebDriver(new URL(sysProp.get("remoteDriverURL")),cDc);
					this.deviceDesc = "Chrome";
				}
				catch(Exception e){
					e.printStackTrace();
				}
				break;
			case "iexplorer":
				try{
					DesiredCapabilities ieCapabilities=DesiredCapabilities.internetExplorer();
				    ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
				    ieCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION,true);
				    driver=new InternetExplorerDriver(ieCapabilities);
					this.deviceDesc = "iExplorer";
				}
				catch(Exception e){
					e.printStackTrace();
				}
				break;
			case "firefox":
				try{
					driver = new RemoteWebDriver(
							new URL(sysProp.get("remoteDriverURL")), 
	                        DesiredCapabilities.firefox());
					this.deviceDesc = "Firefox";
				}
				catch(Exception e){
					e.printStackTrace();
				}
				break;
				default: break;
					
			}
			resultSheet = new ExcelDriver(sysProp.get("outputWorkbook"), this.deviceDesc, true);
		 	resultSheet.setResultColumn(this.testCycle, true);
		 	
		 	detailedResultSheet = new ExcelDriver(sysProp.get("detailedResultWorkbook"), "fullDetails", true);
		 	addColumnsToDetailedSheet();
		 	return;
		}
		
		this.driver = PerfectoUtils.getDriver(caps, Integer.parseInt(sysProp.get("driverRetries")), Integer.parseInt(sysProp.get("retryIntervalSeconds")));
		
		if(this.driver != null){
			deviceProperties = PerfectoUtils.getDevicePropertiesList(driver);
			deviceDesc = getDeviceProperty("model");
			deviceDesc += " ";
			String deviceDescription = getDeviceProperty("description");
			if(deviceDescription != null){
				deviceDesc += deviceDescription;
			}
			
//			resultSheet = new ExcelDriver(sysProp.get("outputResultSheet"), this.deviceDesc, true);
//		 	resultSheet.setResultColumn(this.testCycle, true);
		}
		resultSheet = new ExcelDriver(sysProp.get("outputWorkbook"), this.deviceDesc, true);
	 	resultSheet.setResultColumn(this.testCycle, true);
	 	detailedResultSheet = new ExcelDriver(sysProp.get("detailedResultWorkbook"), "fullDetails", true);
	 	addColumnsToDetailedSheet();
	}
	

	@AfterClass
	public void afterClass() {
		try {
			if(this.driver == null){
				return;
			}
	    	resultSheet.setAutoSize();
	        // Close the browser
	        driver.close();
	         
	        /*
	        // Download a pdf version of the execution report
	        PerfectoUtils.downloadReport(driver, "pdf", "C:\\temp\\report.pdf");
	        */
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
		driver.quit();
	}
	/**
	 * Gets a device property from Perfecto Mobile cloud
	 * @param key Property name
	 * @return The {@link String} value of the given property
	 */
	public String getDeviceProperty(String key){
		return deviceProperties.get(key);
	}
	/**
	 * Gets the list of all device properties from Perfecto Mobile cloud
	 * @return The {@link HashMap} instance of all the properties keys and values
	 */
	public HashMap<String, String> getDeviceProperties(){
		return deviceProperties;
	}

	/**
	 * Reports a failure of a test, to the HTML report and Excel DB.
	 * A screenshot of the failure is saved and embedded in the HTML report.
	 * A link to the screenshot is saved in the Excel resultSheet.
	 * @param expectedResult The {@link String} value of the expected result, which would have passed the test
	 * @param actualResult The {@link String} value of the actual result, which is not equal to the expected result
	 * @param params A {@link String} array (String[]), with the test name and parameters.
	 * @return A {@link String} path to the stored screenshot.
	 */
	protected String reportFail(String expectedResult, String actualResult, String... params){
    	Reporter.log("Value is: " + actualResult + ", Should be: " + expectedResult + ".");
    	String screenshot = PerfectoUtils.takeScreenshot(driver);
    	Reporter.log("Resource name: " + this.deviceDesc +".");
		Reporter.log("Error screenshot saved in file: " + screenshot);
		Reporter.log("<br> <img src=" + screenshot + " style=\"max-width:50%;max-height:50%\" /> <br>");
		try {
			addRowToDetailedSheet(false, screenshot, params);
//			resultSheet.setResultByColumnName(false, params);
//			resultSheet.addScreenshotByRowNameAsLink(screenshot, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
		Assert.fail();
		return screenshot;
	}
	
	/**
	 * Reports a failure of a test, to the HTML report and Excel DB.
	 * A screenshot of the failure is saved and embedded in the HTML report.
	 * A link to the screenshot is saved in the Excel resultSheet.
	 * @param messgage A {@link String} message with description of the failure.
	 * @param params A {@link String} array (String[]), with the test name and parameters.
	 */
	protected void reportFailWithMessage(String message, String... params){
    	Reporter.log("Test failed: " + message + ".");
    	Reporter.log("Resource name: " + this.deviceDesc + ".");
    	String screenshot = PerfectoUtils.takeScreenshot(driver);
		Reporter.log("Error screenshot saved in file: " + screenshot);
		Reporter.log("<br> <img src=" + screenshot + " style=\"max-width:50%;max-height:50%\" /> <br>");
		try {
			addRowToDetailedSheet(false, screenshot, params);
//			resultSheet.setResultByColumnName(false, params);
//			resultSheet.addScreenshotByRowNameAsLink(screenshot, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}
		Assert.fail();
	}
	
	/**
	 * Reports a success of a test, to the HTML report and Excel DB.
	 * A screenshot of the checkpoint is saved and embedded in the HTML report.
	 * A link to the screenshot is saved in the Excel resultSheet.
	 * @param messgage A {@link String} message with description of the test.
	 * @param params A {@link String} array (String[]), with the test name and parameters.
	 */
	protected void reportPass(String message, String... params){
    	Reporter.log("Test passed: " + message + ".");
    	Reporter.log("Resource name: " + this.deviceDesc + ".");
    	String screenshot = PerfectoUtils.takeScreenshot(driver);
		Reporter.log("Screenshot saved in file: " + screenshot);
		Reporter.log("<br> <img src=" + screenshot + " style=\"max-width:50%;max-height:50%\" /> <br>");
		try {
			addRowToDetailedSheet(true, screenshot, params);
			//resultSheet.setResultByColumnName(true, params);
			//resultSheet.addScreenshotByRowNameAsLink(screenshot, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Reports a transaction of a test, to the HTML report.
	 * A screenshot of the transaction is saved and embedded in the HTML report.
	 * @param messgage A {@link String} message with description of the transaction.
	 * @param params A {@link String} array (String[]), with the test name and parameters.
	 */
	protected void reportMessage(String message, String... params){
    	Reporter.log(message + ".");
    	Reporter.log("Resource name: " + this.deviceDesc + ".");
    	String screenshot = PerfectoUtils.takeScreenshot(driver);
		Reporter.log("Screenshot saved in file: " + screenshot);
		Reporter.log("<br> <img src=" + screenshot + " style=\"max-width:50%;max-height:50%\" /> <br>");
	}
	
	/**
	 * Adds a the test result to the Excel db sheet.
	 * Row includes:
	 * <ul>
	 * 	<li> Date and time
	 *  <li> Server URL
	 *  <li> User
	 *  <li> Resource type (Desktop/mobile)
	 *  <li> Manufacturer (Mobile only)
	 *  <li> model (Mobile only)
	 *  <li> OS type
	 *  <li> OS version (Mobile only)
	 *  <li> Carrier (Only for mobile)
	 *  <li> Browser version (Desktop only) 
	 *  <li> Test name
	 *  <li> Test params
	 *  <li> Test cycle
	 *  <li> Test result
	 * </ul>
	 * <p>
	 *  
	 */
	protected void addColumnsToDetailedSheet(){
		if(driver == null){
			return;
		}
		Map<String, ?> map = driver.getCapabilities().asMap();
		testProperties = new HashMap<String, String>();

		
		if(map.get("deviceName") == null){ //desktop browser
			testProperties.put("host", sysProp.get("remoteDriverURL"));
			testProperties.put("resourceType", "desktop");
			for(Map.Entry<String, ?> entry : map.entrySet()){
				testProperties.put(entry.getKey(), entry.getValue().toString());
			}
		}
		else{
			testProperties = PerfectoUtils.getDevicePropertiesList(driver);
			testProperties.put("resourceType", "mobile");
		}
		testProperties.put("time", "");
		testProperties.put("testCycle", "");
		testProperties.put("testResult", "");
		testProperties.put("testName", "");
		testProperties.put("testParameters", "");
				
		try {
			detailedResultSheet.addColumnsFromMap(testProperties);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addRowToDetailedSheet(boolean isPassed, String screenshot, String... testParams) throws Exception{
		String params = "";
		for(String s : testParams){
			params += s + ", ";
		}
		params = params.substring(0, params.lastIndexOf(","));
		String time = PerfectoUtils.getDateAndTimeByFormat("yyyy-MM-dd HH:mm:ss", 0);
		String testResult = isPassed? "pass" : "fail";
		testProperties.put("testParameters", params);
		testProperties.put("testName", this.testName);
		testProperties.put("time", time);
		testProperties.put("testCycle", this.testCycle);
		testProperties.put("testResult", testResult);
		testProperties.put("screenshot", screenshot);
		detailedResultSheet.addResultsToDetailedSheet(testProperties);
		
	}
	@SuppressWarnings("unused")
	public void writeResultsToExcel(String testName, String[] params, boolean result){
		String url, user, resourceType, manufacturer, model, OSType,
		OSVersion, carrier, browserName, browserVersion, platform;
		String time = PerfectoUtils.getDateAndTimeByFormat("yyyy-MM-dd HH-mm-ss", 0);
		browserName = driver.getCapabilities().getBrowserName();
		Map<String, ?> map = driver.getCapabilities().asMap();
		String deviceName = driver.getCapabilities().getCapability("deviceName").toString();
		
		if(map.get("deviceName") != null){ // If mobile device
			url = sysProp.get("URL");
			user = sysProp.get("userName");
			resourceType = "mobile";
			manufacturer = getDeviceProperty("manufacturer");
			model = getDeviceProperty("model");
			OSType = getDeviceProperty("os");
			OSVersion = getDeviceProperty("os_version");
			carrier = getDeviceProperty("carrier");
		}
		else{ // else - desktop browser
			url = sysProp.get("remoteDriverURL");
			resourceType = "desktop";
			browserVersion = driver.getCapabilities().getVersion();
			platform = driver.getCapabilities().getPlatform().toString();
		}
		
		// XXX We never write anything here????
	}
	
	/**
	 * Switch the {@link RemoteWebDriver} driver context.
	 * A list with possible contexts can be generated by the {@link #getCurrentContextHandle(RemoteWebDriver)} method.
	 * To check the current context, use the {@link #getCurrentContextHandle(RemoteWebDriver)} method.
	 * @param driver The context of the passed driver will be changed
	 * @param context The context to change to
	 */
	public void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}
	
	/**
	 * Gets the {@link String} value of the current context of the driver.
	 * In order to change the current context, use the {@link #switchToContext(RemoteWebDriver, String)} method.
	 * @param driver The driver to get the context from.
	 * @return {@link String} value of the current context.
	 */
	public String getCurrentContextHandle(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	/**
	 * Get a list of possible contexts for the driver sent.
	 * In order to check the current context, use the {@link #getCurrentContextHandle(RemoteWebDriver)} method.
	 * In order to change the current context, use the {@link #switchToContext(RemoteWebDriver, String)} method.
	 * @param driver The driver to get the list for.
	 * @return {@link List} of possible contexts for the sent driver.
	 */
	public List<String> getContextHandles(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		@SuppressWarnings("unchecked")
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
}
