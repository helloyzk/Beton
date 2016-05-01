package com.perfectomobile.webCommunityPOM;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import com.perfectomobile.utils.PerfectoUtils;

/**
 * The Class WebSearchResultsPageView.
 */
public class WebSearchResultsPageView extends WebCommunityBaseView {
	
	/** The search page. */
	//private By searchPage = By.xpath("//*[@class='search-results']|//*[contains(text(),'No results to display')]");
	private By searchPage = By.xpath("//*[@class='contribution-card in']|//*[contains(text(),'No results to display')]");
	
	/** The search results. */
	private String searchResults = "//li[starts-with(@class,'contribution-card')]";
	
	/** The posts results. */
	private String postsResults = "(//li[starts-with(@class,'contribution-card')]/a[starts-with(@href,'/posts/')])";
	
	/** The series results. */
	private String seriesResults = "(//li[starts-with(@class,'contribution-card')]/a[starts-with(@href,'/series/')])";
	
	
	/**
	 * 	Constructor.
	 *
	 * @param driver the driver
	 * @throws IOException 
	 */
	public WebSearchResultsPageView(RemoteWebDriver driver) throws IOException{
        super(driver);
        
        //validate page loaded successfully before proceeding
        try{
        	PerfectoUtils.fluentWait(searchPage, driver, 30);
        }
        catch(Exception e){
        	String errorFile = PerfectoUtils.takeScreenshot(driver);
			Reporter.log("Error screenshot saved in file: " + errorFile);
        	throw new IllegalStateException();
        }
    }
	
	/**
	 * Gets the complete results count after a search action.
	 *
	 * @return the complete results count
	 */
	public int getResultsCount(){
		try {
			return this.driver.findElementsByXPath(searchResults).size();
			
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Gets the posts count found after a search.
	 *
	 * @return the posts count 
	 */
	public int getPostsCount(){
		
		try {
			return this.driver.findElementsByXPath(postsResults).size();
						
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Gets the series count after a search action.
	 *
	 * @return the series count
	 */
	public int getSeriesCount(){
		
		try {
			return this.driver.findElementsByXPath(seriesResults).size();

		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * Click post by index.
	 *
	 * @param index the index of the post to click on
	 * @return the web post page view of the post clicked or null if post# index was not found
	 */
	public WebPostPageView clickPost(int index){
		By xpath = By.xpath(postsResults + "[" + index + "]") ;
		try {
			//this.driver.findElement(xpath).click();
			WebElement element = PerfectoUtils.fluentWait(xpath, this.driver, 20);
			//WebDriverWait wait = new WebDriverWait(driver, 10);
			//wait.until(ExpectedConditions.visibilityOf(element));
			//PerfectoUtils.sleep(4000);
			element.click();
			
			return new WebPostPageView(driver);
		} catch (Exception e) {
			System.out.println("didnt find post number " + index);
			return null;
		}
	}
	
	/**
	 * Click series by index.
	 *
	 * @param index the index of the series to click on
	 */
	public void clickSeries(int index){
		By xpath = By.xpath(seriesResults + "[" + index + "]") ;
		try {
			this.driver.findElement(xpath).click();
		} catch (Exception e) {
			System.out.println("didnt find series number " + index);
			
		}
	}
	
	

}
