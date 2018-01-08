package caseManagement.SamsTesting;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import cucumber.api.java.en.Then;
import gherkin.formatter.model.Comment;

public class stepDefinition {

    public WebDriver obj;
    public WebElement table;
    List<WebElement> cells;
    
    @Before
    public void beforeScenario(){
 	   System.setProperty("webdriver.ie.driver", "C:\\Users\\piercewi\\Drivers\\IEDriverServer.exe");
 	   obj = new InternetExplorerDriver();
 	   obj.manage().window().maximize();

 	   obj.get("http://localhost:8080/sams/");
    }	

   @Given("^url opened$")
   public void url_opened() {

	   //firefox options
      //System.setProperty("webdriver.gecko.driver", "C:\\Users\\piercewi\\Drivers\\geckodriver.exe");

      //obj = new FirefoxDriver();
      
      //Chrome options
	  /*System.setProperty("webdriver.chrome.driver", "C:\\Users\\piercewi\\Documents\\chromedriver.exe");
	  ChromeOptions options = new ChromeOptions();
	  options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");*/
	  
      //obj = new ChromeDriver();
      
      //IE options (32-bit version on 64-bit machine)
	  System.setProperty("webdriver.ie.driver", "C:\\Users\\piercewi\\Drivers\\IEDriverServer.exe");
      obj = new InternetExplorerDriver();
      obj.manage().window().maximize();

      obj.get("http://localhost:8080/sams/");

   }



   @Then("^enter user id and click next$")
   public void enter_user_id_and_click_next() throws InterruptedException {

      obj.findElement(By.id("hudid")).sendKeys("C63254");

   }



   @Then("^enter password$")
   public void enter_password() {

      obj.findElement(By.id("password")).sendKeys("password");

   }



   @Then("^click login$")
   public void click_login() throws InterruptedException {

      obj.findElement(By.id("singlebutton")).click();
      System.out.println("Login Successful");
      //Thread.sleep(6000);
      By selection = By.className("navbar");
      (new WebDriverWait(obj, 30)).until(
      ExpectedConditions.visibilityOfElementLocated(selection));
	  String msg = obj.findElement(By.className("heading")).getText();
	  if(!msg.isEmpty())
		msg = msg.split("\n")[0].trim();
	  Assert.assertEquals("Dashboard", msg);
     // obj.quit();
//      //click drop down
//      obj.findElement(By.xpath("//*[@id=\"navbar\"]/ul[1]/li[2]/a")).click();
//      //click closing disclosure search
//      obj.findElement(By.xpath("//*[@id=\"navbar\"]/ul[1]/li[2]/ul/li[3]/a")).click();

   }
   
   
   //search xpath
   //*[@id="navbar"]/ul[1]/li[2]/a 
   
   //row xpath
   //*[@id="case_search_results"]/tbody/tr
//   @Then("^click search$")
//   public void click_search() throws Throwable {
//	   //search button(possibly enter params first?)
//	   obj.findElement(By.xpath("//*[@id=\"caseSearchForm\"]/div[5]/div/input[2]")).click();
//	   Thread.sleep(3000);
//   }
//
//   @Then("^click row$")
//   public void click_row() throws Throwable {
//	   //click the first row
//	   obj.findElement(By.xpath("//*[@id=\"case_search_results\"]/tbody/tr")).click();
//   }
   
   @Given("^I have logged in$")
   public void logged_in() throws Throwable {

	   obj.findElement(By.id("hudid")).sendKeys("C63254");
	   obj.findElement(By.id("password")).sendKeys("password");
	   obj.findElement(By.id("singlebutton")).click();
	   System.out.println("Login Successful");
	   //Thread.sleep(6000);
	   
	   By selection = By.className("navbar");
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
		String msg = obj.findElement(By.className("heading")).getText();
		if(!msg.isEmpty())
			msg = msg.split("\n")[0].trim();
		Assert.assertEquals("Dashboard", msg);
		//Thread.sleep(3000);
		
		//obj.findElement(By.id("caseManagementDropDownBtn")).click();

//	    selection = By.id("stepZeroPropertyListingBtn");
//        (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
//	    //Step 0 Property Listing Link
//	    obj.findElement(By.id("stepZeroPropertyListingBtn")).click();
//	    selection = By.id("zeroTable");
//        (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
   }
   
   @Given("^navigated to \"([^\"]*)\"$")
   public void navigated_to(String arg1) throws Throwable {
	   obj.findElement(By.id("caseManagementDropDownBtn")).click();

	   By selection = By.linkText(arg1);
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
	   //specific case management link
	   obj.findElement(selection).click();
	   //wait for page to load
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("legend")));
   }
   
/*   @Then("^click step (\\d+)$")
   public void click_step(int arg1) throws Throwable {
	   //Case Management Drop Down
	   obj.findElement(By.id("caseManagementDropDownBtn")).click();

	   By selection = By.id("stepZeroPropertyListingBtn");
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
	   //Step 0 Property Listing Link
	   obj.findElement(By.id("stepZeroPropertyListingBtn")).click();
	   selection = By.id("zeroTable");
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
       
   }*/
   
   //@Then("^click options$")
   public void click_options() throws Throwable {
	   By selection = By.className("btn");
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
       table = obj.findElement(By.id("zeroTable"));
       cells = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).get(0).findElements(By.tagName("td"));
       
       cells.get(9).findElement(By.className("btn")).click();
   }
   
//   @Then("^fail to update comments$")
//   public void fail_to_update_comments() throws Throwable {
//
//	   //"update comment" Options dropdown selection
//	   cells.get(9).findElements(By.tagName("li")).get(1).click();
//
//	   obj.switchTo().alert().dismiss();
//	 
//	   WebElement comment = cells.get(7).findElement(By.tagName("input"));
//	   //String txt = comment.getText();
//	   comment.sendKeys("A");
//   }
   
   @Then("^I update comments$")
   public void update_comments() throws Throwable {
	   click_options();
	   //fail to update a comment by not changing its text
	   //wait until li appears
	   By selection = By.tagName("li");
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
	   //"update comment" Options dropdown selection
	   cells.get(9).findElements(By.tagName("li")).get(1).click();
	   //pop up should appear
	   obj.switchTo().alert().dismiss();
	 
	   WebElement comment = cells.get(7).findElement(By.tagName("input"));
	   //String txt = comment.getText();
	   comment.sendKeys("A");
	   click_options();
	   (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
	 //"update comment" Options dropdown selection
	   cells.get(9).findElements(By.tagName("li")).get(1).click();

   }

   @Then("^I click \"([^\"]*)\"$")
   public void click_move_to_step(String arg1) throws Throwable {
	   click_options();
	   //click passed in argument from Options drop down
	   //cells.get(9).findElements(By.tagName("li")).get(0).click();
	   cells.get(9).findElement(By.linkText(arg1)).click();
   }
   
   @Then("^element having class \"([^\"]*)\" should have text as \"([^\"]*)\"$")
   public void element_having_class(String arg1, String arg2) throws Throwable {
	   By selection = By.className(arg1);
       (new WebDriverWait(obj, 30)).until(ExpectedConditions.visibilityOfElementLocated(selection));
		String msg = obj.findElement(By.className(arg1)).getText();
		if(!msg.isEmpty())
			msg = msg.split("\n")[0].trim();
		String regex = "(.*?)" + arg2 + "(.*?)";
		try {
			Assert.assertTrue(msg.matches(regex));
		} catch (Exception e) {
			obj.quit();
		}
		System.out.println(msg);
		Thread.sleep(3000);
		//obj.quit();
   }
   
   @After
   public void afterScenario() 
   {
	   System.out.println("After Scenario Hook");
        obj.quit();
   }

}