package com.perfectomobile.androidCommunityPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;


// TODO: Auto-generated Javadoc
/**
 * The Class ANDROID_ProfilePageView.
 */
public class ANDROID_ProfilePageView extends ANDROID_CommunityBaseView {
	
	/** The nav button. */
	String navButton = "'Navigate up'";
	

	/** The btn nav up. */
	By btnNavUp = By.id("android:id/up");		
	
	/** The txt name. */
	By txtName = By.id("com.bloomfire.android.perfecto:id/name");
	
	/** The txt title. */
	By txtTitle = By.id("com.bloomfire.android.perfecto:id/title");
	
	/** The btn edit profile. */
	By btnEditProfile = By.id("com.bloomfire.android.perfecto:id/item_edit_profile");	// Works
//	By btnOptions = By.xpath("//*[@class='android.widget.ImageButton']");	// Works
//	By txtFollow = By.xpath("//*[@text='Follow']");
	
	/**
 * Instantiates a new ANDROI d_ profile page view.
 *
 * @param driver the driver
 */
public ANDROID_ProfilePageView(RemoteWebDriver driver) {
		super(driver);
	}
	
	/**
	 * Back to dashboard.
	 *
	 * @return the ANDROI d_ community base view
	 */
	public ANDROID_CommunityBaseView backToDashboard(){
		//System.out.println(btnNavUp);
		this.driver.findElement(btnNavUp).click();
		return new ANDROID_CommunityBaseView(this.driver);
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		String author = this.driver.findElement(txtName).getAttribute("text");
		System.out.println("------------------------>>>Profile Name:" + author);
		return author;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle(){
		String author = this.driver.findElement(txtTitle).getAttribute("text");
		System.out.println("------------------------>>>Profile User Title:" + author);
		return author;
	}

	/**
	 * Edits the profile.
	 *
	 * @return the ANDROI d_ post page view
	 */
	public ANDROID_PostPageView editProfile(){
		//System.out.println("--------------------->>> Clicking " + btnRefresh);
		this.driver.findElement(btnEditProfile).click();
		return new ANDROID_PostPageView(this.driver);
	}
	
}
