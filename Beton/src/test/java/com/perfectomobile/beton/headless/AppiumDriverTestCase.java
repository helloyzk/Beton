


package com.perfectomobile.beton.headless;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class AppiumDriverTestCase {
	
	public static void main(String[] args) throws MalformedURLException, IOException {

		System.out.println("Run started");
		
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
		String host = "demo.perfectomobile.com";		
		capabilities.setCapability("user", "rajp@perfectomobile.com");
		capabilities.setCapability("password", "Perfecto12!");
		capabilities.setCapability("deviceName", "30E9D3E3");
		
		//capabilities.setCapability("", "settings");
		capabilities.setCapability("appPackage", "com.android.settings");
		
		
		// Use the automationName capability to defined the required framework - Appium (this is the default) or PerfectoMobile.
		// capabilities.setCapability("automationName", "PerfectoMobile");
		// capabilities.setCapability("automationName", "Appium");

        AppiumDriver<WebElement> driver = new AndroidDriver<WebElement>(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		
		try {
			
			driver.findElementByXPath("").click();
			
			
         
         
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
		
		System.out.println("Run ended");
	}
	
	@SuppressWarnings("unused")
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Download the report. 
	 * type - pdf, html, csv, xml
	 * Example: downloadReport(driver, "pdf", "C:\\test\\report");
	 * 
	 */
	@SuppressWarnings("unused")
	private static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
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
	 * Download all the report attachments with a certain type.
	 * type - video, image, vital, network
	 * Examples:
	 * downloadAttachment("video", "C:\\test\\video", "flv");
	 * downloadAttachment("image", "C:\\test\\Image", "jpg");
	 */
	@SuppressWarnings("unused")
	private static void downloadAttachment(RemoteWebDriver driver, String type, String fileName, String suffix) throws IOException {
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


	@SuppressWarnings("unused")
	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	@SuppressWarnings("unused")
	private static String getCurrentContextHandle(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	@SuppressWarnings("unused")
	private static List<String> getContextHandles(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		@SuppressWarnings("unchecked")
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
	
	/**
	 * Upload files (applications, images etc.) to the media repository.
	 * path - path to a local file. E.g. C:\\test\\ApiDemos.apk
	 * repositoryKey - key in the repository. E.g. PRIVATE:apps\\ApiDemos.apk
	 * 
	 */
	@SuppressWarnings("unused")
	private static void uploadMedia(String host, String user, String password, String path, String repositoryKey) throws IOException {
		MediaUploader uploader = new MediaUploader();
		uploader.upload(host, user, password, path, repositoryKey);
	}
	
	// Recommended:
	// Remove the comment and use this method if you want the script to share the devices with the recording plugin.
	/*
 	private static void setExecutionIdCapability(DesiredCapabilities capabilities) throws IOException {
		EclipseConnector connector = new EclipseConnector();
		String executionId = connector.getExecutionId();
		capabilities.setCapability(EclipseConnector.ECLIPSE_EXECUTION_ID, executionId);
	}
	 */
	
	public static class MediaUploader {

		private static final String UTF_8 = "UTF-8";

		/**
		 * This class can be used for uploading files (applications, images etc.) to the media repository when working with the RemoteWebDriver.
		 * Usage example:
		 * MediaUploader uploader = new MediaUploader();
		 * uploader.upload("demo.perfectomobile.com", "john@perfectomobile.com", "123456", "C:\\test\\ApiDemos.apk", "PRIVATE:apps\\ApiDemos.apk");
		 */
		public void upload(String host, String user, String password, String path, String repositoryKey) throws IOException {
			File file = new File(path);
			byte[] content = readFile(file);

			if (content != null) {
				String encodedUser = URLEncoder.encode(user, UTF_8);
				String encodedPassword = URLEncoder.encode(password, UTF_8);
				String urlStr = "https://" + host + "/services/repositories/media/" + repositoryKey + "?operation=upload&overwrite=true&user=" + encodedUser + "&password=" + encodedPassword;
				URL url = new URL(urlStr);

				sendRequest(content, url);
			}
		}

		private void sendRequest(byte[] content, URL url) throws IOException {
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/octet-stream");
			connection.connect();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			outStream.write(content);
			outStream.writeTo(connection.getOutputStream());
			outStream.close();
			int code = connection.getResponseCode();
			if (code > HttpURLConnection.HTTP_OK) {
				handleError(connection);
			}
		}

		private void handleError(HttpURLConnection connection) throws IOException {
			InputStream errorStream = connection.getErrorStream();
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream, UTF_8);
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			try {
				StringBuilder builder = new StringBuilder();
				String outputString;
				while ((outputString = bufferReader.readLine()) != null) {
					if (builder.length() != 0) {
						builder.append("\n");
					}
					builder.append(outputString);
				}
				String response = builder.toString();
				throw new RuntimeException("Failed to upload media. Reponse: " + response);
			}
			finally {
				bufferReader.close();
			}
		}

		private byte[] readFile(File path) throws FileNotFoundException, IOException {
			int length = (int)path.length();
			byte[] content = new byte[length];
			InputStream inStream = new FileInputStream(path);
			try {
				inStream.read(content);
			}
			finally {
				inStream.close();
			}
			return content;
		}	
	}

}
