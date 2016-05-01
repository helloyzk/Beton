package com.perfectomobile.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Init {

	protected static Properties prop;
	protected static HashMap<String,String> sysProp;
	
	private Init(){}
	
	public static HashMap<String, String> prop() {
		prop = new Properties();
		sysProp = new HashMap<String, String>();
		
		try {
			prop.load(new FileInputStream("src/main/resources/beton.properties"));
			 for (String key : prop.stringPropertyNames()) {
			      String value = prop.getProperty(key);
			      sysProp.put(key, value);
			 }
			 /* Set Hash Map */
			setSysProp(sysProp);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		/* The sysProp is only used for the dataprovided initially as TestNG runs the data provided before the constructor */
		return sysProp;
	}
	
	public static HashMap<String, String> getSysProp() {
		return sysProp;
	}
	public static void setSysProp(HashMap<String, String> sysProp) {
		Init.sysProp = sysProp;
	}

}
