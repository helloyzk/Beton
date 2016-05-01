package com.perfectomobile.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Reporter;

import com.google.common.base.Function;
//import com.perfectomobile.selenium.util.EclipseConnector;
import com.perfectomobile.test.Init;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.util.Base64;

// TODO: Auto-generated Javadoc
/**
 * The Class PerfectoUtils.
 */
public class PerfectoUtils {
	
	/** The sys prop. */
	protected static HashMap<String,String> sysProp = Init.getSysProp();

	
	/**
	 * Gets the driver.
	 *
	 * @param cap the DesiredCapabilities
	 * @param retries the integer number retries
	 * @param retryIntervalSeconds the integer number of retry interval seconds
	 * @return the driver
	 */
	public static RemoteWebDriver getDriver(DesiredCapabilities cap,int retries, int retryIntervalSeconds)
	{
		System.out.println("Current capabilities " + cap.toString());

		RemoteWebDriver driver;
		boolean waitForDevice = true;
		int index = retries;
		do {
			try {
				cap.setCapability("user", sysProp.get("perfectoUserName"));
				String password = sysProp.get("perfectoPassword"); 
				if(password.startsWith("~")){
					password = decryptPassword(password.substring(1), "beton");
				}
				cap.setCapability("password", password);
				Object automationNameObj = cap.getCapability("automationName");
				Object platformNameObj = cap.getCapability("platformName");
				if(automationNameObj != null && automationNameObj.toString().toLowerCase().equals("appium")){
					if(platformNameObj != null && platformNameObj.toString().toLowerCase().equals("ios")){
						driver = new IOSDriver<WebElement>(new URL(sysProp.get("perfectoURL")), cap);
					}
					else if(platformNameObj != null && platformNameObj.toString().toLowerCase().equals("android")){
						driver = new AndroidDriver<WebElement>(new URL(sysProp.get("perfectoURL")), cap);
					}
					else{
						throw new Exception("Illegal to use automationName=Appium without platformName");
					}
				}
				else{
					driver = new RemoteWebDriver(new URL(sysProp.get("perfectoURL")), cap);
				}
				System.out.println(sysProp.get("sysDriverMSG"));
				return driver;

			} catch (Exception e) {
				index--;
				System.out.println("device wasn't allocated successfully: " + cap.toString() +"\n Retries left: " + index);
				
				sleep(retryIntervalSeconds * 1000);
				if (e.getMessage().contains("command browser open")) {
					waitForDevice = false;
				}
			}
				//waitForDevice = false;
		} while (waitForDevice && index > 0);
		return null;

	}

	/**
	 * Install app.
	 *
	 * @param appLocation the app location
	 * @param d the driver
	 */
	public static void installApp(String appLocation,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("file", appLocation);
		d.executeScript("mobile:application:install", params);
	}
	
	/**
	 * Start app.
	 *
	 * @param appName the app name
	 * @param d the driver
	 */
	public static void startApp(String appName, RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", appName);
		d.executeScript("mobile:application:open", params);
	}
	
	/**
	 * Uninstall app.
	 *
	 * @param appName the app name
	 * @param d the driver
	 */
	public static void uninstallApp(String appName,RemoteWebDriver d ){
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", appName);
		d.executeScript("mobile:application:uninstall", params);
	}
	
	/**
	 * Swipe.
	 *
	 * @param start the start
	 * @param end the end
	 * @param d the driver
	 */
	public static void swipe(String start,String end,RemoteWebDriver d )
	{
		Map<String,String> params = new HashMap<String,String>();
		params.put("start", start);  //50%,50%
		params.put("end", end);  //50%,50%

		d.executeScript("mobile:touch:swipe", params);

	}
	
	/**
	 * Sleep.
	 *
	 * @param millis the millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}

	}

	/**
	 * Switch to context.
	 *
	 * @param driver the driver
	 * @param context the context
	 */
	public static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	/**
	 * Download report.
	 *
	 * @param driver the driver
	 * @param type the type
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
	
	/**
	 * Download attachment.
	 *
	 * @param driver the driver
	 * @param type the type
	 * @param fileName the file name
	 * @param suffix the suffix
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Gets the current context handle.
	 *
	 * @param driver the driver
	 * @return the current context handle
	 */
	public static String getCurrentContextHandle(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	/**
	 * Gets the context handles.
	 *
	 * @param driver the driver
	 * @return the List of context handles
	 */
	public static List<String> getContextHandles(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		@SuppressWarnings("unchecked")
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
	
	/**
	 * checkVisual
	 * <ul>
	 * 	<li>Performs a visual context evaluation to identify the existence of a text string on a page
	 * <ul>
	 *
	 * @param driver the driver
	 * @param needle the needle
	 * @return true, if successful
	 */
	public static boolean checkVisual(RemoteWebDriver driver, String needle){
		String previousContext = getCurrentContextHandle(driver);
		// Switch to visual driver, to perform text checkpoint
		switchToContext(driver, "VISUAL");
		
		// Perform the checkpoint
		try{
			driver.findElement(By.linkText(needle));
		}
		catch(Exception e){
			switchToContext(driver, previousContext);
			return false;
		}
		
		// Switch back to webview context
		switchToContext(driver, previousContext);
		return true;
		
	}
	
	/**
	 * fluentWait
	 * <ul>
	 * 	<li>Uses Selenium FluentWait method
	 * 	<li>Looks for an element identifier locator defined as a By type for an overall period of time define by the timeout, polling every 250 ms
	 * 	<li>Returns the WebElement once found or null if not found inside timeout limit
	 * <ul>
	 * 
	 * @param locator the By locator
	 * @param driver the RemoteWebDriver driver
	 * @param timeout the long timeout
	 * @return the web element
	 */
	/* Wait until the objects loads until the timeout */
	public static WebElement fluentWait(final By locator, RemoteWebDriver driver, long timeout) {
		 
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
	  
	/**
	 * Gets the date and time.
	 *
	 * @param offset the offset
	 * @return the date and time
	 */
	public static String getDateAndTime(int offset){
		  Calendar c = Calendar.getInstance();
		  c.setTime(new Date());
		  c.add(Calendar.DATE, offset);
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS-z");
		  return dateFormat.format(c.getTime());
	  }
	    
	/**
	 * Gets the date and time according to a pattern and an offset.
	 * 
	 * @param format The format of the requested response. e.g. "yyyy-MM-dd HH-mm-ss"
	 * @param offset The offset in days from today.
	 * @return The date and time with the requested format
	 */
	public static String getDateAndTimeByFormat(String format, int offset){
		  Calendar c = Calendar.getInstance();
		  c.setTime(new Date());
		  c.add(Calendar.DATE, offset);
		  DateFormat dateFormat = new SimpleDateFormat(format);
		  return dateFormat.format(c.getTime());
	  }
	
	/**
	 * Gets the screenshot on error.
	 *
	 * @param driver the driver
	 * @return the screenshot on error
	 */
	public static void getScreenshotOnError(RemoteWebDriver driver) {
		  
		String errorFile = takeScreenshot(driver);
		Reporter.log("Error screenshot saved in file: " + errorFile);
		  
		  
	  }
	  	  
	/**
	 * Gets the screen shot.
	 *
	 * @param driver the driver
	 * @param name the name
	 * @return the screen shot
	 */
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
	  
 	/**
	  * Take screenshot.
	  *
	  * @param driver the driver
	  * @return the string
	  */
	 public static String takeScreenshot(RemoteWebDriver driver) {
	  
 		
	  String filePath = new File("").getAbsolutePath();
	  
	  //filePath += "\\test-output\\screenshots-tests";
	  filePath += sysProp.get("screenshotsFolder");
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
	  //filePath+= "/";		  
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
	  
 	/**
	  * Generate capabilities object.
	  *
	  * @param map the map
	  * @return the desired capabilities
	  */
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
	  
 	/**
	  * Gets the capabilities array.
	  *
	  * @param mapsArray the maps array
	  * @return the capabilities array
	  */
	 public static Object[][] getCapabilitiesArray(ArrayList<HashMap<String,String>> mapsArray){
	    Object[][] returnArray;
	    int arraySize = mapsArray.size();
	    returnArray = new Object[arraySize][1];
	    for(int i = 0; i < arraySize; i++){
		   returnArray[i][0] = generateCapabilitiesObject(mapsArray.get(i));
	    }
	    return returnArray;
	 	}

	/**
	 * Checks if is device.
	 *
	 * @param driver the driver
	 * @return true, if is device
	 */
	public static boolean isDevice(RemoteWebDriver driver){
		//first check if driver is a mobile device:
	    Capabilities capabilities = driver.getCapabilities();
	    String type = (String) capabilities.getCapability("browserName");
	    if (type.toLowerCase().equals("chrome") || type.toLowerCase().equals("firefox")){
	          return false;
	    }
	    return true;
    }

	/**
	 * Gets the device properties list.
	 *
	 * @param driver the driver
	 * @return the device properties list
	 */
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
	
    /**
     * ***************************************************************************
     * gets a specific device property out of the Dictionary.
     * returns the value of the property
     * for example:
     * getDeviceProperty("Model") will return the model of the device(ie iPhone-6)
     * ****************************************************************************
     *
     * @param driver the driver
     * @param Property the property
     * @return the device property
     */
    public static String getDeviceProperty(RemoteWebDriver driver, String Property){
    	HashMap<String, String> deviceProperties = getDevicePropertiesList(driver);
        return (deviceProperties.get(Property));
    }

    /**
     * ******************************************************************
     * 
     * decryptPassword(String message, String key)
     * <ul>
     * 	<li> Uses provided key to decrypt provided message encrypted using the same key
     * <ul>
     * @author Brian Clark
     * 
     * *******************************************************************.
     *
     * @param message the encrypted message to decrypt
     * @param key the key used to encrypt the message
     * @return the unencrypted stringstring
     */
	
	public static String decryptPassword(String message, String key){
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


//	/**
//	 * *****************************************************************************
//	 * 
//	 * Properties available are: 
//	 * <ul>
//	 * 	<li>deviceId; manufacturer; model; distributer; description; firmware; imsi; wifiMacAddress; 
//	 * 		link; operator; phoneNumber; location; lastCradleId; language; status; mode; available; 
//	 * 		reserved; inUse; allocatedTo; operabilityRating; cradleId; position; os; osVersion; 
//	 * 		resolution; additionalParams 
//	 * <ul>
//	 * @author Brian Clark
//	 * ******************************************************************************.
//	 *
//	 * @param driver the driver
//	 * @param property the Properties Enum
//	 * @return the device property
//	 */
//	public static String getDeviceProperty(RemoteWebDriver driver, Properties property){		
//		
//		String host = driver.getCapabilities().asMap().get("host").toString();
//		String user = driver.getCapabilities().asMap().get("user").toString();
//		String pwd = driver.getCapabilities().asMap().get("password").toString();
//		String deviceID = driver.getCapabilities().asMap().get("deviceName").toString();
//		String res = null;
//		try {
//			URL url = new URL("https://"+host+"/services/handsets/"+deviceID+"?operation=info&user="+user+"&password="+pwd);
//		  //make connection
//	        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//	        urlc.setRequestMethod("GET");
//	        urlc.setRequestProperty("Accept", "application/xml");
//	        InputStream xml = urlc.getInputStream();
//	        
//	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//	        DocumentBuilder db = dbf.newDocumentBuilder();
//	        Document doc = db.parse(xml);
//	        doc.getDocumentElement().normalize();
//	        NodeList found =  doc.getElementsByTagName(property.toString());
//	        if(found.getLength() == 1){
//	        	res = found.item(0).getTextContent();
//	        }	        
//		} catch (IOException | ParserConfigurationException | SAXException e) {
//			e.printStackTrace();
//		}
//		return res;		
//	}
	
	/**
	 * Upload local file to media repo.
	 *
	 * @param driver the driver
	 * @param file the file
	 * @param repoPath the repo path
	 * @return the string
	 */
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

	
	
	/**
	 * **********************************************************************************************************
	 * 	>>>>>>>>>>>>>>>>>>>>>>>>>	ENUMS	<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 * ***********************************************************************************************************.
	 */
	
	public static enum LabelPosition {// Above;Below;Left;Right
		
		/** The above. */
		ABOVE("Above"),
		
		/** The below. */
		BELOW("Below"),
		
		/** The left. */
		LEFT("Left"),
		
		/** The right. */
		RIGHT("Right");
	
		/** The label position. */
		private final String labelPosition;
	
		/**
		 * Instantiates a new label position.
		 *
		 * @param position the position
		 */
		LabelPosition(String position) {
	        this.labelPosition = position;
	    }
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
	        return this.labelPosition;
	    }
	}
	
	/**
	 * The Enum MatchMode.
	 */
	public static enum MatchMode {//all caps -- contain,equal,startwith,endwith,first,last,index
		
		/** The contain. */
		CONTAIN("contain"),
		
		/** The equal. */
		EQUAL("equal"),
		
		/** The startwith. */
		STARTWITH("startwith"),
		
		/** The endwith. */
		ENDWITH("endwith"),
		
		/** The first. */
		FIRST("first"),
		
		/** The last. */
		LAST("last"),
		
		/** The index. */
		INDEX("index");
	
		/** The match mode. */
		private final String matchMode;
	
		/**
		 * Instantiates a new match mode.
		 *
		 * @param mode the mode
		 */
		MatchMode(String mode) {
	        this.matchMode = mode;
	    }
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
	        return this.matchMode;
	    }
	}
	
	/**
	 * The Enum ScrollNext.
	 */
	public static enum ScrollNext {//all caps -- SWIPE_UP,SWIPE_DOWN,SWIPE_RIGHT,SWIPE_LEFT,UP,DOWN,RIGHT,LEFT
		
		/** The swipe up. */
		SWIPE_UP("SWIPE_UP"),
		
		/** The swipe down. */
		SWIPE_DOWN("SWIPE_DOWN"),
		
		/** The swipe right. */
		SWIPE_RIGHT("SWIPE_RIGHT"),
		
		/** The up. */
		UP("UP"),
		
		/** The down. */
		DOWN("DOWN"),
		
		/** The right. */
		RIGHT("RIGHT");
	
		/** The scroll next. */
		private final String scrollNext;
	
		/**
		 * Instantiates a new scroll next.
		 *
		 * @param direction the direction
		 */
		ScrollNext(String direction) {
	        this.scrollNext = direction;
	    }	
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
	        return this.scrollNext;
	    }
	}
	
	/**
	 * The Enum Properties.
	 */
	public static enum Properties {//all caps
	
		/** The device id. */
		DEVICE_ID("deviceId"),
		
		/** The manufacturer. */
		MANUFACTURER("manufacturer"),
		
		/** The model. */
		MODEL("model"),
		
		/** The distributer. */
		DISTRIBUTER("distributer"),
		
		/** The description. */
		DESCRIPTION("description"),
		
		/** The firmware. */
		FIRMWARE("firmware"),
		
		/** The imsi. */
		IMSI("imsi"),
		
		/** The wifi mac address. */
		WIFI_MAC_ADDRESS("wifiMacAddress"), 
		
		/** The link. */
		LINK("link"),
		
		/** The operator. */
		OPERATOR("operator"),
		
		/** The phone number. */
		PHONE_NUMBER("phoneNumber"),
		
		/** The location. */
		LOCATION("location"),
		
		/** The last cradel id. */
		LAST_CRADEL_ID("lastCradleId"),
		
		/** The language. */
		LANGUAGE("language"),
		
		/** The status. */
		STATUS("status"),
		
		/** The mode. */
		MODE("mode"),
		
		/** The available. */
		AVAILABLE("available"), 
		
		/** The reserverd. */
		RESERVERD("reserved"),
		
		/** The in use. */
		IN_USE("inUse"),
		
		/** The allocated to. */
		ALLOCATED_TO("allocatedTo"),
		
		/** The operability rating. */
		OPERABILITY_RATING("operabilityRating"),
		
		/** The cradel id. */
		CRADEL_ID("cradleId"),
		
		/** The position. */
		POSITION("position"),
		
		/** The os. */
		OS("os"),
		
		/** The os version. */
		OS_VERSION("osVersion"), 
		
		/** The resolution. */
		RESOLUTION("resolution"),
		
		/** The additional params. */
		ADDITIONAL_PARAMS("additionalParams");
		
		/** The property name. */
		private final String propertyName;
		
		/**
		 * Instantiates a new properties.
		 *
		 * @param name the name
		 */
		Properties(String name) {
		    this.propertyName = name;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
		    return this.propertyName;
		}
	}

}

/** The Constant REPOSITORY. */
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
