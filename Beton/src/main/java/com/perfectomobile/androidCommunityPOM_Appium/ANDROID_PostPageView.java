package com.perfectomobile.androidCommunityPOM_Appium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.perfectomobile.utils.PerfectoUtils;

import io.appium.java_client.android.AndroidDriver;


// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_PostPageView.
 */
public class ANDROID_PostPageView extends ANDROID_CommunityBaseView {
	
	/** The nav button. */
	String navButton = "'Navigate up'";
	

	/** The btn nav up. */
	By btnNavUp = By.id("android:id/up");		
	
	/** The txt author. */
	By txtAuthor = By.id("com.bloomfire.android.perfecto:id/authors");
	
	/** The txt description. */
	By txtDescription = By.id("com.bloomfire.android.perfecto:id/description");
	
	/** The btn refresh. */
	By btnRefresh = By.id("com.bloomfire.android.perfecto:id/item_refresh");	// Works
	
	/** The btn options. */
	By btnOptions = By.xpath("//*[@class='android.widget.ImageButton']");	// Works
	
	/** The txt follow. */
	By txtFollow = By.xpath("//*[@text='Follow']");
	
	/**
	 * Instantiates a new ANDROI d_ post page view.
	 *
	 * @param driver the driver
	 */
	public ANDROID_PostPageView(AndroidDriver<WebElement> driver) {
		super(driver);
		PerfectoUtils.sleep(3000);
	}
	
	/**
	 * Back to search results.
	 *
	 * @return the ANDROI d_ search results view
	 */
	public ANDROID_SearchResultsView backToSearchResults(){
		//System.out.println(btnNavUp);
		this.driver.findElement(btnNavUp).click();
		return new ANDROID_SearchResultsView(this.driver);
	}
	
	/**
	 * Refresh post.
	 *
	 * @return the ANDROI d_ post page view
	 */
	public ANDROID_PostPageView refreshPost(){
		//System.out.println("--------------------->>> Clicking " + btnRefresh);
		this.driver.findElement(btnRefresh).click();
		return new ANDROID_PostPageView(this.driver);
	}
	
	/**
	 * Follow post.
	 *
	 * @return the ANDROI d_ post page view
	 */
	public ANDROID_PostPageView followPost(){
		//System.out.println("--------------------->>> Clicking " + btnOptions);
		this.driver.findElement(btnOptions).click();		
		this.driver.findElement(txtFollow).click();
		return new ANDROID_PostPageView(this.driver);
	}
	
	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor(){
		String author = this.driver.findElement(txtAuthor).getAttribute("text");
		System.out.println("------------------------>>>Post Author:" + author);
		return author;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription(){
		String author = this.driver.findElement(txtDescription).getAttribute("text");
		System.out.println("------------------------>>>Post Description:" + author);
		return author;
	}

}
