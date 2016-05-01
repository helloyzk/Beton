package com.perfectomobile.androidCommunityPOM_Appium;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Function;
//import com.perfectomobile.selenium.util.EclipseConnector;
import com.perfectomobile.test.Init;

import io.appium.java_client.android.AndroidDriver;
import java.util.Base64;


public class PerfectoAppiumUtils {
	protected static HashMap<String,String> sysProp = Init.getSysProp();

	
	public static AndroidDriver<WebElement> getDriver(DesiredCapabilities cap,int retries, int retryIntervalSeconds)
	{
		System.out.println("Current capabilities " + cap.toString());

		AndroidDriver<WebElement> driver;
		boolean waitForDevice = true;
		int index = retries;
		
		do { 
			try { 
	//			cap.setCapability("user", sysProp.get("userName"));
	//			cap.setCapability("password", sysProp.get("password"));			
				driver = new AndroidDriver<WebElement>(new URL("http://" + sysProp.get("URL") + "/wd/hub"), cap);
				System.out.println(sysProp.get("sysDriverMSG"));
				return driver;

			} catch (Exception e) {
				index--;
				System.out.println("device not found: " + cap.toString() +"\n Retries left: " + index);
				
				sleep(retryIntervalSeconds * 1000);
				if (e.getMessage().contains("command browser open")) {
					waitForDevice = false;
				}
			}
				//waitForDevice = false;
		} while (waitForDevice && index > 0);
		return null;

	}

//	public static void installApp(String appLocation,RemoteWebDriver d )
//	{
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("file", appLocation);
//		d.executeScript("mobile:application:install", params);
//	}
//	
//	public static void startApp(String appName,RemoteWebDriver d )
//	{
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("name", appName);
//		d.executeScript("mobile:application:open", params);
//	}
//	
//	public static void uninstallApp(String appName,RemoteWebDriver d ){
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("name", appName);
//		d.executeScript("mobile:application:uninstall", params);
//	}
//	
//	public static void swipe(String start,String end,RemoteWebDriver d )
//	{
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("start", start);  //50%,50%
//		params.put("end", end);  //50%,50%
//
//		d.executeScript("mobile:touch:swipe", params);
//
//	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}

	}

//	public static void switchToContext(RemoteWebDriver driver, String context) {
//		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("name", context);
//		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
//	}

	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try { 
			String command = "mobile:report:download"; 
			Map<String, Object> params = new HashMap<>(); 
			params.put("type", type); 
			String report = (String)driver.executeScript(command, params); 
			File reportFile = new File(fileName + "." + type); 
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
			output.write(reportBytes); output.close(); 
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); }
	}
	
	public static void downloadAttachment(RemoteWebDriver driver, String type, String fileName, String suffix) throws IOException {
		try {
			String command = "mobile:report:attachment";
			boolean done = false;
			int index = 0;

			while (!done) {
				Map<String, Object> params = new HashMap<>();	

				params.put("type", type);
				params.put("index", Integer.toString(index));

				String attachment = (String)driver.executeScript(command, params);
				
				if (attachment == null) { 
					done = true; 
				}
				else { 
					File file = new File(fileName + index + "." + suffix); 
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file)); 
					byte[] bytes = OutputType.BYTES.convertFromBase64Png(attachment);	
					output.write(bytes); 
					output.close(); 
					index++; }
			}
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); 
		}
	}

//	public static String getCurrentContextHandle(RemoteWebDriver driver) {		  
//		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
//		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
//		return context;
//	}
//
//	public static List<String> getContextHandles(RemoteWebDriver driver) {		  
//		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
//		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
//		return contexts;
//	}
	
//	public static boolean checkVisual(RemoteWebDriver driver, String needle){
//		String previousContext = getCurrentContextHandle(driver);
//		// Switch to visual driver, to perform text checkpoint
//		switchToContext(driver, "VISUAL");
//		
//		// Perform the checkpoint
//		try{
//			driver.findElement(By.linkText(needle));
//		}
//		catch(Exception e){
//			switchToContext(driver, previousContext);
//			return false;
//		}
//		
//		// Switch back to webview context
//		switchToContext(driver, previousContext);
//		return true;
//		
//	}
	
	/* Wait until the objects loads until the timeout */
	public static WebElement fluentWait(final By locator, AndroidDriver<WebElement> driver, long timeout) {
		 
		try {
			 FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				        .withTimeout(timeout, TimeUnit.SECONDS)
				        .pollingEvery(250, TimeUnit.MILLISECONDS)
				        .ignoring(Exception.class);
				        //.ignoring(NoSuchElementException.class);
				       
					  WebElement webelement = wait.until(new Function<WebDriver, WebElement>() {
						  public WebElement apply(WebDriver driver) {
				            return driver.findElement(locator);
						  }
					  });
					    return  webelement;
		} catch (Exception e) {
			return null;
		}
		 
		
	  }
	  
	public static String getDateAndTime(int offset){
		  Calendar c = Calendar.getInstance();
		  c.setTime(new Date());
		  c.add(Calendar.DATE, offset);
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-z");
		  return dateFormat.format(c.getTime());
	  }
	    
	public static void getScreenshotOnError(RemoteWebDriver driver) {
		  
		String errorFile = takeScreenshot(driver);
		Reporter.log("Error screenshot saved in file: " + errorFile);
		  
		  
	  }
	  	  
	public static void getScreenShot(RemoteWebDriver driver,String name ){
			driver   = (RemoteWebDriver) new Augmenter().augment( driver );
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

			try {
				FileUtils.copyFile(scrFile, new File("c:\\test\\"+name+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	  
 	public static String takeScreenshot(RemoteWebDriver driver) {
	  
 		
	  String filePath = new File("").getAbsolutePath();
	  filePath += "\\test-output\\screenshots-tests";
	  File theDir = new File(filePath);

	  // if the directory does not exist, create it
	  if (!theDir.exists()) {
		  //System.out.println("creating directory: " + directoryName);

		  try{
			  theDir.mkdir();
		  } 
		  catch(SecurityException se) {
			  
			  return null;
		  }        
	  }
	  filePath+= "\\";		  
	  File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	  String filename = filePath + getDateAndTime(0) + ".png";
	  //System.out.println(filename);
		try {
			FileUtils.copyFile(scrFile, new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	  return filename;
 	}
	  
 	public static DesiredCapabilities generateCapabilitiesObject(HashMap<String,String> map){
	  
	  String browserName = map.get("browserName"); 
	  if(browserName == null){
		  browserName = "";
	  }
	  DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
	  for(Entry<String,String> entry : map.entrySet()){
		  capabilities.setCapability(entry.getKey(),entry.getValue());
	  }
	  return capabilities;
 	}
	  
 	public static Object[][] getCapabilitiesArray(ArrayList<HashMap<String,String>> mapsArray){
	    Object[][] returnArray;
	    int arraySize = mapsArray.size();
	    returnArray = new Object[arraySize][1];
	    for(int i = 0; i < arraySize; i++){
		   returnArray[i][0] = generateCapabilitiesObject(mapsArray.get(i));
	    }
	    return returnArray;
	 	}

	public static boolean isDevice(RemoteWebDriver driver){
		//first check if driver is a mobile device:
	    Capabilities capabilities = driver.getCapabilities();
	    String type = (String) capabilities.getCapability("browserName");
	    if (type.toLowerCase().equals("chrome") || type.toLowerCase().equals("firefox")){
	          return false;
	    }
	    return true;
    }

	public static HashMap<String, String> getDevicePropertiesList(RemoteWebDriver driver) {
	    //hashmap to contain device properties
	    HashMap<String, String> deviceProperties = new HashMap<String, String>();
        deviceProperties = new HashMap<String, String>();
        
        if (!isDevice(driver))
    	    return null;
    
        Map<String, Object> params = new HashMap<>();
        params.put("property", "ALL");
        String properties = (String) driver.executeScript("mobile:handset:info", params);

        List<String> items = Arrays.asList(properties.split(","));
        String key,value;
       //build hashmap for all device properties:
        for (int i = 0; i < items.size(); i=i+2) {
    	    key=items.get(i);
    	    if (key.startsWith("[")||key.startsWith(" ")){
    		    key=key.substring(1);
    	    }
	        value=items.get(i+1);
	        if(value.startsWith(" ")){
	    	    value=value.substring(1);
	        }
	        if (value.startsWith("[")){
	    	    for (int j = i+2; j < items.size(); j++) {
	    		    value=value+","+items.get(j);
	                if (value.endsWith("]")){
	            	    value=value.substring(1,value.length()-1);
	                    i=j-1;
	                    break;
	                }
	            }
	        }
	        if (value.endsWith("]")){
	        	  value=value.substring(0,value.length()-1);
	        }
	        deviceProperties.put(key, value);
        }
        return deviceProperties;
	 }
	
    /*****************************************************************************
    * gets a specific device property out of the Dictionary.
    * returns the value of the property
    * for example:
    * getDeviceProperty("Model") will return the model of the device(ie iPhone-6)
    ******************************************************************************/
    public static String getDeviceProperty(RemoteWebDriver driver, String Property){
    	HashMap<String, String> deviceProperties = getDevicePropertiesList(driver);
        return (deviceProperties.get(Property));
    }

    /********************************************************************
	 * 
	 * decryptPassword(String message, String key)
	 * 
	 * 		Uses provided key to decrypt provided message encrypted using the same key
	 * 
	 *********************************************************************/
	
	public String decryptPassword(String message, String key){
		try {
	      if (message==null || key==null ) return null;
	      Base64.Decoder decoder = Base64.getDecoder();
	      char[] keys=key.toCharArray();
	      char[] mesg=new String(decoder.decode(message)).toCharArray();

	      int ml=mesg.length;
	      int kl=keys.length;
	      char[] newmsg=new char[ml];

	      for (int i=0; i<ml; i++){
	        newmsg[i]=(char)(mesg[i]^keys[i%kl]);
	      }
	      mesg=null; keys=null;
	      return new String(newmsg);
	    }
	    catch ( Exception e ) {
	      return null;
    } 
  }


	/*******************************************************************************
	 * 
	 * Properties available are: 
	 * 
	 * deviceId; manufacturer; model; distributer; description; firmware; imsi; wifiMacAddress; 
	 * link; operator; phoneNumber; location; lastCradleId; language; status; mode; available; 
	 * reserved; inUse; allocatedTo; operabilityRating; cradleId; position; os; osVersion; 
	 * resolution; additionalParams 
	 *
	 ********************************************************************************/
	public static String getDeviceProperty(RemoteWebDriver driver, Properties property){		
		
		String host = driver.getCapabilities().asMap().get("host").toString();
		String user = driver.getCapabilities().asMap().get("user").toString();
		String pwd = driver.getCapabilities().asMap().get("password").toString();
		String deviceID = driver.getCapabilities().asMap().get("deviceName").toString();
		String res = null;
		try {
			URL url = new URL("https://"+host+"/services/handsets/"+deviceID+"?operation=info&user="+user+"&password="+pwd);
		  //make connection
	        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	        urlc.setRequestMethod("GET");
	        urlc.setRequestProperty("Accept", "application/xml");
	        InputStream xml = urlc.getInputStream();
	        
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(xml);
	        doc.getDocumentElement().normalize();
	        NodeList found =  doc.getElementsByTagName(property.toString());
	        if(found.getLength() == 1){
	        	res = found.item(0).getTextContent();
	        }	        
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
		return res;		
	}
	
	public static String uploadLocalFileToMediaRepo(RemoteWebDriver driver, String file, String repoPath){

		int index = file.lastIndexOf("\\");
		String fileName = file.substring(index+1);
		String[] fileSplit = fileName.split("\\.");
		String newFile = fileSplit[0] + new Date().getTime() + "." + fileSplit[1];		
		String repoFile = repoPath + "/" + newFile;
		
		String host = driver.getCapabilities().asMap().get("host").toString();
		String user = driver.getCapabilities().asMap().get("user").toString();
		String pwd = driver.getCapabilities().asMap().get("password").toString();		
		
		try {			
			//Create HTTP Connection
			URL url = new URL("https://"+host+"/services/repositories/media/" + repoFile + "?operation=upload&user="+user+"&password="+pwd);
			HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
			
			//Create BufferedInputStream from local file
			BufferedInputStream content = new BufferedInputStream(new FileInputStream(file));
			
			// Set Connection parameters, must be a PUT... default is GET 
		    con.setRequestMethod("POST"); 
		    con.setDoOutput(true);
		    con.setDoInput(true);
		    con.setAllowUserInteraction(false);
		    
		    //Create BufferedOutputStream from the HTTP connection
		    BufferedOutputStream fileStream = new BufferedOutputStream(con.getOutputStream());    
		    
		    //POST
		    System.out.println("\n----------------------------->>>Uploading media file to Perfecto cloud repository : "); 
		    int i;
		    while((i = content.read()) != -1){
		    	fileStream.write(i);
		    }
		    content.close();
		    fileStream.close();
		    
		    //Get Response code and Response
		    int responseCode = con.getResponseCode();
		    System.out.println("\n----------------------------->>>Response Code : " + responseCode); 
		    
		    if (con.getErrorStream() != null){
		        BufferedReader error = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		        String errorLine; 
		        StringBuffer errorResponse = new StringBuffer();  
				 
			    while ((errorLine = error.readLine()) != null) { 
			    	errorResponse.append(errorLine); 
			    	errorResponse.append("\n");	 
			    }	    
			    error.close();
			    System.out.println(errorResponse.toString());
		    }	    
			 
		    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
		    String inputLine; 
		    StringBuffer response = new StringBuffer();  
			 
		    while ((inputLine = in.readLine()) != null) { 
		          response.append(inputLine); 
		          response.append("\n");	 
		    }	    
		    in.close();
		    
		    //return result 
		    System.out.println(response.toString());
		    return repoFile;		      
		        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}

	
	
	/************************************************************************************************************
	*	>>>>>>>>>>>>>>>>>>>>>>>>>	ENUMS	<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	*************************************************************************************************************/
	
	public static enum LabelPosition {// Above;Below;Left;Right
		
		ABOVE("Above"),
		BELOW("Below"),
		LEFT("Left"),
		RIGHT("Right");
	
		private final String labelPosition;
	
		LabelPosition(String position) {
	        this.labelPosition = position;
	    }
		
		@Override
		public String toString() {
	        return this.labelPosition;
	    }
	}
	
	public static enum MatchMode {//all caps -- contain,equal,startwith,endwith,first,last,index
		
		CONTAIN("contain"),
		EQUAL("equal"),
		STARTWITH("startwith"),
		ENDWITH("endwith"),
		FIRST("first"),
		LAST("last"),
		INDEX("index");
	
		private final String matchMode;
	
		MatchMode(String mode) {
	        this.matchMode = mode;
	    }
		
		@Override
		public String toString() {
	        return this.matchMode;
	    }
	}
	
	public static enum ScrollNext {//all caps -- SWIPE_UP,SWIPE_DOWN,SWIPE_RIGHT,SWIPE_LEFT,UP,DOWN,RIGHT,LEFT
		
		SWIPE_UP("SWIPE_UP"),
		SWIPE_DOWN("SWIPE_DOWN"),
		SWIPE_RIGHT("SWIPE_RIGHT"),
		UP("UP"),
		DOWN("DOWN"),
		RIGHT("RIGHT");
	
		private final String scrollNext;
	
		ScrollNext(String direction) {
	        this.scrollNext = direction;
	    }	
		
		@Override
		public String toString() {
	        return this.scrollNext;
	    }
	}
	
	public static enum Properties {//all caps
	
		DEVICE_ID("deviceId"),
		MANUFACTURER("manufacturer"),
		MODEL("model"),
		DISTRIBUTER("distributer"),
		DESCRIPTION("description"),
		FIRMWARE("firmware"),
		IMSI("imsi"),
		WIFI_MAC_ADDRESS("wifiMacAddress"), 
		LINK("link"),
		OPERATOR("operator"),
		PHONE_NUMBER("phoneNumber"),
		LOCATION("location"),
		LAST_CRADEL_ID("lastCradleId"),
		LANGUAGE("language"),
		STATUS("status"),
		MODE("mode"),
		AVAILABLE("available"), 
		RESERVERD("reserved"),
		IN_USE("inUse"),
		ALLOCATED_TO("allocatedTo"),
		OPERABILITY_RATING("operabilityRating"),
		CRADEL_ID("cradleId"),
		POSITION("position"),
		OS("os"),
		OS_VERSION("osVersion"), 
		RESOLUTION("resolution"),
		ADDITIONAL_PARAMS("additionalParams");
		
		private final String propertyName;
		
		Properties(String name) {
		    this.propertyName = name;
		}
		
		@Override
		public String toString() {
		    return this.propertyName;
		}
	}

}

//private static final String REPOSITORY = "PUBLIC:";

//private void uploadMedia(String resource, String repositoryKey) throws URISyntaxException, IOException {
		//repositoryKey = REPOSITORY;
		//String FILENAME;
		//File file = new File(FILENAME);
		//
		//d.uploadMedia(repositoryKey, file);
		//File file = loadResource(resource);
		//_driver.uploadMedia(repositoryKey, file);
//}	

	//	  public static DesiredCapabilities getCapabilites(String deviceName, String platformName, String platformVersion, String manufacturer,
//			  String deviceModel, String deviceResolution, String deviceNetwork, String deviceLocation, String deviceDescription, String browserName, String automationName) throws Exception{
//		  
//		  DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
//		  		  
//		  if(!deviceName.equals("")){
//			  capabilities.setCapability("deviceName", deviceName);
//		  }
//		  if(!platformName.equals("")){
//			  capabilities.setCapability("platformName", platformName);
//		  }
//		  if(!platformVersion.equals("")){
//			  capabilities.setCapability("platformVersion", platformVersion);
//		  }
//		  if(!manufacturer.equals("")){
//			  capabilities.setCapability("manufacturer", manufacturer);
//		  }
//		  if(!deviceModel.equals("")){
//			  capabilities.setCapability("model", deviceModel);
//		  }
//		  if(!deviceResolution.equals("")){
//			  capabilities.setCapability("resolution", deviceResolution);
//		  }
//		  if(!deviceNetwork.equals("")){
//			  capabilities.setCapability("network", deviceNetwork);
//		  }
//		  if(!deviceLocation.equals("")){
//			  capabilities.setCapability("location", deviceLocation);
//		  }
//		  if(!deviceDescription.equals("")){
//			  capabilities.setCapability("description", deviceDescription);
//		  }
//		  if(!automationName.equals("")){
//			  capabilities.setCapability("automationName", automationName);
//		  }
//		  		 
//		  return capabilities;
//	  }
