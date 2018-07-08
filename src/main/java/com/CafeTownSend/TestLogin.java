package com.CafeTownSend;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterClass;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**  
* TestLogin.java - To validate login scenarios including casing, incorrect entries and one correct entry.  
* @author  Kumar Aarjab
* @version 1.0
*/ 
public class TestLogin {
	private WebDriver driver;
	Properties prop = new Properties();
	InputStream input = null;
	/**  
	    * Test invalid password with the application and validate the response.  
	    * @throws InterruptedException for usage of Thread.  
	*/ 
  @Test
  public void testInvalidPassword() throws InterruptedException {
	  driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	  driver.get(prop.getProperty("url"));
	  String s = String.valueOf(prop.getProperty("InvalidPassword"));
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys(prop.getProperty("username"));
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(s);
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathInvalidPassword")),prop.getProperty("InvalidPasswordText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathInvalidPassword"))).getText(), prop.getProperty("InvalidPasswordText"));
  }
  /**  
   * Test invalid username with special characters in the application and validate the response.  
   * @throws InterruptedException for usage of Thread.  
   */ 
  @Test
  public void testInvalidUsername() throws InterruptedException {
	  driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	  driver.get(prop.getProperty("url"));
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys("&*"+prop.getProperty("username"));
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(String.valueOf(prop.getProperty("Invalidpassword")));
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathInvalidPassword")),prop.getProperty("InvalidPasswordText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathInvalidPassword"))).getText(), prop.getProperty("InvalidPasswordText"));
  }
  /**  
   * Test username with Upper characters in the application and validate the response.  
   * @throws InterruptedException for usage of Thread.  
   */ 
  @Test
  public void testCasingInvalidityUsername() throws InterruptedException {
	  driver.get(prop.getProperty("url"));
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys(prop.getProperty("username").toUpperCase());
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(prop.getProperty("ValidPassword"));
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathInvalidPassword")),prop.getProperty("InvalidPasswordText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathInvalidPassword"))).getText(), prop.getProperty("InvalidPasswordText"));
  }
  /**  
   * Test password with Upper characters in the application and validate the response.  
   * @throws InterruptedException for usage of Thread.  
   */ 
  @Test
  public void testCasingInvalidityPassword() throws InterruptedException {
	  driver.get(prop.getProperty("url"));
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys(prop.getProperty("username"));
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(prop.getProperty("ValidPassword").toUpperCase());
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathInvalidPassword")),prop.getProperty("InvalidPasswordText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathInvalidPassword"))).getText(), prop.getProperty("InvalidPasswordText"));
  }
  /**  
   * Test valid login with correct case username and password in the application and validate the response.  
   * @throws InterruptedException for usage of Thread.  
   */ 
  @Test
  public void testValidLogin() throws InterruptedException {
	  driver.get(prop.getProperty("url"));
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys(prop.getProperty("username"));
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(prop.getProperty("ValidPassword"));
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathLogout")),prop.getProperty("xpathLogoutText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathLogout"))).getText(), prop.getProperty("xpathLogoutText"));
  }
  /**  
   * Test scrolling works in the list of names and validate if we are able to see the element after scrolling.  
   * @throws InterruptedException for usage of Thread.  
   */ 
  @Test
  public void testScrollToElement() throws InterruptedException {
	  driver.get(prop.getProperty("url"));
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys(prop.getProperty("username"));
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(prop.getProperty("ValidPassword"));
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      WebElement e = driver.findElement(By.xpath(prop.getProperty("xpathUserElementScrollCheck")));
      Coordinates cor=((Locatable)e).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      boolean b = driver.findElement(By.xpath(prop.getProperty("xpathUserElementScrollCheck"))).isDisplayed();
      Assert.assertEquals(b, true);
  }
  /**  
   * Load the respective driver and element reference file and starts the browser for test.  
   * @throws FileNotFoundException and IOException  
   */
  @Parameters("browser")
  @BeforeClass
  public void beforeClass(String browser) throws FileNotFoundException,IOException {
	  if(browser.equalsIgnoreCase("firefox")) {
		  System.setProperty("webdriver.gecko.driver","webdriver/geckodriver.exe");  
		  driver = new FirefoxDriver();
	  }else if (browser.equalsIgnoreCase("chrome")) { 
	 	  System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
		  driver = new ChromeDriver();
	 
	  } 
	  input = new FileInputStream(getClass().getClassLoader().getResource("xpath-values.properties").getPath());
	  prop.load(input);
	  driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
  }
  /**  
   * Shuts down the browser after all tests  
   *   
   */ 
  @AfterClass
  public void afterClass() {
	  driver.quit();
  }

}
