package com.perfectomobile.utils;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfectomobile.utils.PerfectoUtils.*;


// TODO: Auto-generated Javadoc
/**
 * The Class VisualDriverControl.
 */
public class VisualDriverControl {
	
	/**
	 * ***************************************************************************</br>
	 *
	 * 		
	 * 	@param driver:			RemoteWebDriver from test
	 *	@param label:			Label to find on screen
	 * 	@param labelPosition:	Direction of click location relative to label. 
	 * 							<ul>
	 * 								<li>Uses Enum LabelPosition: Above;Below;Left;Right
	 * 							</ul>
	 * 	@param offset: 			Value of the offset in pixels or percentage. 
	 * 							<ul>
	 * 								<li>Percentage is noted by the use of a trailing % character
	 * 							</ul>
	 * ****************************************************************************
	 *
	 */
	public static void clickByTextOffset(RemoteWebDriver driver, String label,  LabelPosition labelPosition, String offset){
		//Get current context
		String initialContext = PerfectoUtils.getCurrentContextHandle(driver);
		PerfectoUtils.switchToContext(driver, "VISUAL");
				
		String command = "mobile:text:select";
		Map<String, Object> params = new HashMap<>();
		params.put("content", label);
		params.put("shift", labelPosition.toString());
		params.put("offset", offset);
		
		driver.executeScript(command, params);
		
		PerfectoUtils.switchToContext(driver, initialContext);		
	}
	
	/**
	 * ***************************************************************************</br>
	 * 	
	 * 	@author Brian Clark
	 * 	
	 * 	@param driver:		RemoteWebDriver from test
	 * 	@param label:		Label to find on screen
	 * 	@param scroll:		Scroll if true (default is false)			
	 * 	@param next: 		Direction of scroll (default is SWIPE_UP). Uses Enum ScrollNext:	SWIPE_UP,SWIPE_DOWN,SWIPE_RIGHT,SWIPE_LEFT,UP,DOWN,RIGHT,LEFT 
	 * 						<ul>
	 * 							<li>
	 * 						</ul>								
	 * 	@param maxScroll:	Maximum number of times to scroll (default is 5).
	 * 						<ul>
	 * 							<li>Possible values 0 - 100
	 * 						</ul>
	 * 	@param index:		Number of index if multiples found.
	 * 						<ul>
	 * 							<li>Possible values 1 - 2147483647
	 * 						</ul>
	 * 	@param threshold:	Minimum accuracy. 
	 * 						<ul>
	 * 							<li>Possible Values 1 - 100
	 * 						</ul>
	 * 	@param matchMode:	How to process match ( default is contain).	
	 * 						<ul>
	 * 							<li>Uses Enum MatchMode: contain, equal, startwith, endwith, first, last, index
	 * 						</ul>
	 * 	@param dpi:			Specifies the dots per inch resolution (default is 96). 
	 * 						<ul>
	 * 							<li>Possible values 1 - 2147483647
	 * 							<li>Please try first the value 160, if the direction is correct then find the best value around this value [120-200].If not, then try the lower range: [50-120];
	 * 						</ul>							 
	 * 								 
	 * ****************************************************************************
	 *
	 * 
	 */
	public static void clickVisualText(RemoteWebDriver driver, String label, Boolean scroll, ScrollNext next, int maxScroll, int index, int threshold, MatchMode matchMode, int dpi){
		//Get current context
		String initialContext = PerfectoUtils.getCurrentContextHandle(driver);
		
		PerfectoUtils.switchToContext(driver, "VISUAL");
				
		String command = "mobile:text:select";
		Map<String, Object> params = new HashMap<>();
		params.put("content", label);
		if(scroll){
			params.put("scrolling", "scroll");
		}				
		params.put("next", next.toString());//SWIPE_UP,SWIPE_DOWN,SWIPE_RIGHT,SWIPE_LEFT,UP,DOWN,RIGHT,LEFT
		
		if(!(String.valueOf(maxScroll)=="")){
			if(maxScroll<0){maxScroll=0;}
			if(maxScroll>100){maxScroll=100;}
			params.put("maxscroll", maxScroll);
		}
		if (!(String.valueOf(index)=="")){	//1 - 2147483647
			if(index<1){index=1;}
			if(index>2147483647){index=2147483647;}
			params.put("index", index);
		}
		if (!(String.valueOf(threshold)=="")){	// 20 - 100
			if(threshold<20){threshold=20;}
			if(threshold>100){threshold=100;}
			params.put("threshold", threshold);
		}		
		params.put("match", matchMode.toString());	//contain,equal,startwith,endwith,first,last,index
			
		if (!(String.valueOf(dpi)=="")){
			if(dpi<1){dpi=1;}
			if(dpi>2147483647){dpi=2147483647;}
			params.put("dpi", dpi);
		}
		
		driver.executeScript(command, params);
		
		PerfectoUtils.switchToContext(driver, initialContext);		
	}
	
	/**
	 * ***************************************************************************</br>
	 * 	
	 * 	@author Brian Clark
	 * 
	 * 	@param driver:	RemoteWebDriver from test
	 * 	@param label:	Label to find on screen
	 * 
	 * 	@return 		boolean true if found
	 * ****************************************************************************.
	 */
	public static Boolean findVisualTextElement (RemoteWebDriver driver, String label){
		//Get current context
		String initialContext = PerfectoUtils.getCurrentContextHandle(driver);
		
		//Change driver to VISUAL context
		PerfectoUtils.switchToContext(driver, "VISUAL");
		
		String command = "mobile:text:find";
		Map<String, Object> params = new HashMap<>();
		params.put("content", label);
				
		Boolean found =  (Boolean) driver.executeScript(command, params);
		
		// change driver back to initial context
		PerfectoUtils.switchToContext(driver, initialContext);
		
		return found;
	}
	
	/**
	 * ***************************************************************************</br>
	 * 
	 * 	@author Brian Clark
	 * 	
	 * 	@param driver:			RemoteWebDriver from test
	 * 	@param label:			Label to find on screen
	 * 	@param scroll:			Scroll if true (default is false)			
	 * 	@param next: 			Direction of scroll (default is SWIPE_UP). 	
	 * 							<ul>
	 * 								<li>Uses Enum ScrollNext: SWIPE_UP,SWIPE_DOWN,SWIPE_RIGHT,SWIPE_LEFT,UP,DOWN,RIGHT,LEFT 
	 * 							</ul>							
	 * 	@param maxScroll:		Maximum number of times to scroll (default is 5). 
	 * 							<ul>
	 * 								<li>Possible values 0 - 100
	 * 							</ul>
	 * 	@param index:			Number of index if multiples found. 
	 * 							<ul>
	 * 								<li>Possible values 1 - 2147483647
	 * 							</ul>
	 * 	@param threshold:		Minimum accuracy. 
	 * 							<ul>
	 * 								<li>Possible Values 1 - 100
	 * 							</ul>
	 * 	@param matchMode:		How to process match (default is contain). 
	 * 							<ul>
	 * 								<li>Uses Enum MatchMode: contain,equal,startwith,endwith,first,last,index
	 * 							</ul>
	 * 	@param dpi:			    Specifies the dots per inch resolution (default is 96). 
	 * 							<ul>
	 * 								<li>Possible values 1 - 2147483647
	 * 								<li>Please try first the value 160, if the direction is correct then find the best value around this value [120-200].If not, then try the lower range: [50-120];
	 * 							</ul>
	 * 
	 * 	@return  boolean true if found
	 * ****************************************************************************
	 */
	public static Boolean findVisualTextElement (RemoteWebDriver driver, String label, Boolean scroll, ScrollNext next, int maxScroll, int index, int threshold, MatchMode matchMode, int dpi){
		//Get current context
		String initialContext = PerfectoUtils.getCurrentContextHandle(driver);
		
		//Change driver to VISUAL context
		PerfectoUtils.switchToContext(driver, "VISUAL");
		
		String command = "mobile:text:find";
		Map<String, Object> params = new HashMap<>();
		params.put("content", label);
		if(scroll){ 	//noscroll,scroll
			params.put("scrolling", "scroll");
		}
		
		params.put("next", next.toString());	//SWIPE_UP,SWIPE_DOWN,SWIPE_RIGHT,SWIPE_LEFT,UP,DOWN,RIGHT,LEFT
		
		if(!(String.valueOf(maxScroll)=="")){	// 0 - 100
			if(maxScroll<0){maxScroll=0;}
			if(maxScroll>100){maxScroll=100;}
			params.put("maxscroll", maxScroll);
		}
		if (!(String.valueOf(index)=="")){	//1 - 2147483647
			if(index<1){index=1;}
			if(index>2147483647){index=2147483647;}
			params.put("index", index);
		}
		if (!(String.valueOf(threshold)=="")){	// 20 - 100
			if(threshold<20){threshold=20;}
			if(threshold>100){threshold=100;}
			params.put("threshold", threshold);
		}
		
		params.put("match", matchMode.toString());	//contain,equal,startwith,endwith,first,last,index
		
		if (!(String.valueOf(dpi)=="")){
			if(dpi<1){dpi=1;}
			if(dpi>2147483647){dpi=2147483647;}
			params.put("dpi", dpi);
		}	
		
		Boolean found =  (Boolean) driver.executeScript(command, params);
		
		// change driver back to initial context
		PerfectoUtils.switchToContext(driver, initialContext);
		
		return found;
	}
	
	/**
	 * ***************************************************************************</br>
	 * 
	 * 	@author Brian Clark
	 * 
	 * 	@param driver:		RemoteWebDriver from test
	 * 	@param filePath:	Path to image file :  or PRIVATE:images\\image.png
	 * 						<ul>
	 * 							<li> Uses Perfecto media repository if starts with PRIVATE: or PUBLIC: Example-- PRIVATE:images/image.png
	 * 							<li> Uploads file to media repository images folder if it's a local file path. 
	 * 							<li> Must use double backslash when referencing local path-- ie. C:\\temp\\image.png
	 * 						</ul>
	 * 	@return  boolean	true if found
	 * ****************************************************************************
	 */
	public static Boolean findVisualImageElement(RemoteWebDriver driver, String filePath){
		Boolean found = false;
		//Get current context
		String initialContext = PerfectoUtils.getCurrentContextHandle(driver);
		
		//Change driver to VISUAL context
		PerfectoUtils.switchToContext(driver, "VISUAL");
		
		String command = "mobile:image:find";
		Map<String, Object> params = new HashMap<>();
		
		if(filePath.contains("PRIVATE:") || filePath.contains("PUBLIC:")){
			params.put("content", filePath);
		}else{
			String repoPath = PerfectoUtils.uploadLocalFileToMediaRepo(driver, filePath, "PRIVATE:images");
			params.put("content", repoPath);			
		} 
		
		//executeScript
		found = Boolean.valueOf((String) driver.executeScript(command, params));
				
		// change driver back to initial context
		PerfectoUtils.switchToContext(driver, initialContext);
		
		//return result
		return found;		
	}

	/**
	 * ***************************************************************************</br>
	 * 	
	 * 	@author Brian Clark
	 * 
	 * 	@param driver:		RemoteWebDriver from test
	 * 	@param filePath:	Local or Perfecto Cloud repository path to image file 
	 * 						<ul>
	 * 							<li> Uses Perfecto media repository if starts with PRIVATE: or PUBLIC: Example-- PRIVATE:images/image.png
	 * 							<li> Uploads file to media repository images folder if it's a local file path. 
	 * 							<li> Must use double backslash when referencing local path-- ie. C:\\temp\\image.png
	 * 						</ul>
	 * 	@param bound		The ratio in percentage by which the original needle width may vary. 
	 * 						<ul>
	 * 							<li> The ratio is defined as a ( 100 - specified percentage ) / 100
	 * 						</ul>
	 * 	@param threshold	Specifies the percentage of acceptable match level. Possible values 20 - 100
	 * 						<ul>
	 * 							<li> Default is calculated by the system according to the needle. 
	 * 							<li> Too low values might lead to FALSE POSITIVE results while too high values might lead to FALSE NEGATIVE results. 
	 *						</ul>
	 * 
	 * 	@return  boolean	true if found </br>
	 * ****************************************************************************</br>
	 */
	public static Boolean findVisualImageElement(RemoteWebDriver driver, String filePath, int bound, int threshold){
		Boolean found = false;
		//Get current context
		String initialContext = PerfectoUtils.getCurrentContextHandle(driver);
		
		//Change driver to VISUAL context
		PerfectoUtils.switchToContext(driver, "VISUAL");
		
		String command = "mobile:image:find";
		Map<String, Object> params = new HashMap<>();
		//set params
		params.put("match", "bounded");
		if(bound<0){bound = 0;}
		if(bound>99){bound = 99;}
		params.put("imageBounds.needleBound", bound);
		if(threshold < 20){threshold = 20;}
		if(threshold > 100){threshold = 100;}
		params.put("threshold", threshold);
		
		if(filePath.contains("PRIVATE:") || filePath.contains("PUBLIC:")){
			params.put("content", filePath);
		}else{
			String repoPath = PerfectoUtils.uploadLocalFileToMediaRepo(driver, filePath, "PRIVATE:images");
			params.put("content", repoPath);			
		}
		
		//executeScript
		found = Boolean.valueOf((String) driver.executeScript(command, params));
		
		// change driver back to initial context
		PerfectoUtils.switchToContext(driver, initialContext);
		
		//return result
		
		return found;		
	}
}

	
