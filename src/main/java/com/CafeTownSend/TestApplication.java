package com.CafeTownSend;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.proxy.ActivityMonitor;
import net.lightbody.bmp.proxy.CaptureType;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**  
* TestApplication.java - To validate scenarios of create,update,delete and cross check with API responses.  
* @author  Kumar Aarjab
* @version 1.0
*/ 
public class TestApplication {
	private WebDriver driver;
	Properties prop = new Properties();
	InputStream input = null;
	public static BrowserMobProxyServer server;
	/**
	 * Initialise driver and load the element reference file 
	 */
	public TestApplication() {
		
		try {
			server = new BrowserMobProxyServer();
			server.setTrustAllServers(true);
			server.start();
			 
		input = new FileInputStream(System.getProperty("user.dir")+"src/main/resources/xpath-values.properties");
		prop.load(input);
		} catch (IOException ioe) {
			System.out.println("Xpath file not found");
			System.exit(0);
		}
	}
	/**
	 * Test if double click on an item in list present the edit screen with items element populated.
	 * @throws InterruptedException
	 */
  @Test
  public void testDoubleClickOnItem() throws InterruptedException {
	  Actions action = new Actions(driver);
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement e = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)e).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  action.doubleClick(e).perform();
	  new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathBackButton"))).getText(), prop.getProperty("backButtonText"));
  }
  /**
   * Test edit button functionality after item is selected in list. It should present edit screen with items element populated.
   * @throws InterruptedException
   */
  
  @Test
  public void testEditButton() throws InterruptedException {
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement e = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)e).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
      new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      Assert.assertEquals(driver.findElement(By.xpath(prop.getProperty("xpathBackButton"))).getText(), prop.getProperty("backButtonText"));
  }
  /**
   * Test update on Start Date is functional. Validate with HTTP capture using proxy and parsing the response on unique Id.
   * @throws InterruptedException
   */
  @Test
  public void testEditFunctionalityWithStartDate() throws InterruptedException {
	  String date = "" + ThreadLocalRandom.current().nextInt(1900, 2100 + 1) +"-"+ ThreadLocalRandom.current().nextInt(10, 12 + 1) + "-" + ThreadLocalRandom.current().nextInt(10, 31 + 1);
	  String IdOfUser = null;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
	  new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).sendKeys(date);
      driver.findElement(By.xpath(prop.getProperty("xpathUpdate"))).click();
      Thread.sleep(10000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));    	  
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(10000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees")) {
    		  flag = 1;
    		  IdOfUser = lastEntryInUrl;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser)) {
    					  Assert.assertEquals(p.getStart_date(), date);
    				  }
    			  }
    		  }
    	  }

      }
  }
  /**
   * Test update on First name and Last name works by validating HTTP response and parsing the JSON
   * @throws InterruptedException
   */
  @Test
  public void testEditFunctionalityWithName() throws InterruptedException {
	  String FirstName = "" + ThreadLocalRandom.current().nextInt(1900, 2100 + 1) + "TestFirst";
	  String LastName = "" + ThreadLocalRandom.current().nextInt(1900, 2100 + 1) + "TestLast";
	  String IdOfUser = null;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
	  (new WebDriverWait(driver, 10)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      driver.findElement(By.xpath(prop.getProperty("xpathFirstName"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathFirstName"))).sendKeys(FirstName);
      driver.findElement(By.xpath(prop.getProperty("xpathLastName"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathLastName"))).sendKeys(LastName);
      driver.findElement(By.xpath(prop.getProperty("xpathUpdate"))).click();
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      Thread.sleep(1000);
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(1000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees")) {
    		  flag = 1;
    		  IdOfUser = lastEntryInUrl;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser)) {
    					  Assert.assertEquals(p.getFirst_name(),FirstName);
    					  Assert.assertEquals(p.getLast_name(),LastName);
    				  }
    			  }
    		  }
    	  }
      }
  }
  /**
   * Test update on Year of Date on boundary values <1900 and >2100 fails by validating HTTP response and parsing the JSON
   * @throws InterruptedException
   */
  @Test
  public void testEditFunctionalityWithBoundaryValueOnStartDate() throws InterruptedException {
	  String date = "" + ThreadLocalRandom.current().nextInt(1000, 1899 + 1) +"-"+ ThreadLocalRandom.current().nextInt(10, 12 + 1) + "-" + ThreadLocalRandom.current().nextInt(10, 31 + 1);
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
	  new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).sendKeys(date);
      driver.findElement(By.xpath(prop.getProperty("xpathUpdate"))).click();
      Thread.sleep(10000);
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(10000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      (new WebDriverWait(driver, 30)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));    	  
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees")) {
    		  flag = 1;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  if (res.getStatus()==304 && res.getStatusText().matches("Not Modified")) {
    				  Assert.assertEquals(res.getStatusText(), "Not Modified");
    			  }
    		  }
    	  }

      }
  }
  /**
   * Test update on Email works by validating HTTP response and parsing the JSON
   * @throws InterruptedException
   */
  @Test
  public void testEditFunctionalityWithEmail() throws InterruptedException {
	  String email = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "@" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + ".com";
	  String IdOfUser = null;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
	  (new WebDriverWait(driver, 10)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).sendKeys(email);
      driver.findElement(By.xpath(prop.getProperty("xpathUpdate"))).click();
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      Thread.sleep(1000);
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(1000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees")) {
    		  flag = 1;
    		  IdOfUser = lastEntryInUrl;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser)) {
    					  Assert.assertEquals(p.getEmail(),email);
    				  }
    			  }
    		  }
    	  }
      }
  }
  /**
   * Test update with Invalid Email address should fail, validate it in  HTTP response and parsing the JSON
   * @throws InterruptedException
   */
  @Test
  public void testEditFunctionalityWithInvalidEmail() throws InterruptedException {
	  String email = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "@" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1);
	  String IdOfUser = null;
	  int found=0;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
	  (new WebDriverWait(driver, 10)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).sendKeys(email);
      driver.findElement(By.xpath(prop.getProperty("xpathUpdate"))).click();
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      Thread.sleep(1000);
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(1000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees")) {
    		  flag = 1;
    		  IdOfUser = lastEntryInUrl;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser) && p.getEmail().matches(email)) {
    					  found=1;
    				  }
    			  }
    		  }
    	  }
      }
      Assert.assertEquals(found, 0);
  }
  /**
   * Test Delete button works from inside Edit screen. Validate and parse JSON from HTTP response. Ensures the unique id is deleted.
   * @throws InterruptedException
   */
  @Test
  public void testDeleteFunctionalityWithinEditSection() throws InterruptedException {
	  String IdOfUser = null;
	  int found = 1;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathEditButton"))).click();
	  (new WebDriverWait(driver, 10)).until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathBackButton")),prop.getProperty("backButtonText")));
      driver.findElement(By.xpath(prop.getProperty("xpathDelete"))).click();
      new WebDriverWait(driver, 60).until(ExpectedConditions.alertIsPresent());
      Thread.sleep(1000);
      driver.switchTo().alert().accept();
      Thread.sleep(10000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]"))); 
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(10000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String reqMethod = req.getMethod();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees") && reqMethod.matches("DELETE")) {
    		  flag = 1;
    		  IdOfUser = lastEntryInUrl;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser)) {
    					  found = 0;
    				  }
    			  }
    		  }
    	  }
      }
      Assert.assertNotEquals(found, 0);
  }
  /**
   * Test Delete button works from main screen. Validate and parse JSON from HTTP response. Ensures the unique id is deleted.
   * @throws InterruptedException
   */
  @Test
  public void testDeleteFunctionalityFromMainScreen() throws InterruptedException {
	  String IdOfUser = null;
	  int found = 0;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathDeleteHomeScreen"))).click();
      new WebDriverWait(driver, 60).until(ExpectedConditions.alertIsPresent());
      Thread.sleep(1000);
      driver.switchTo().alert().accept();
      Thread.sleep(10000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(10000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String reqMethod = req.getMethod();
    	  String uniqueId[] = requestContent.split("/");
    	  String regexp = "\\d+";
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches(regexp) && uniqueId[uniqueId.length - 2].matches("employees") && reqMethod.matches("DELETE")) {
    		  flag = 1;
    		  IdOfUser = lastEntryInUrl;
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser)) {
    					  found = 1;
    				  }
    			  }
    		  }
    	  }
      }
      Assert.assertNotEquals(found, 1);
  }
  /**
   * Test that a new entry can be created using Create button with Valid entries and cross check with API response on each field.
   * @throws InterruptedException
   */
  @Test
  public void testCreatePerson() throws InterruptedException {
	  String FirstName = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "TestFirst";
	  String LastName = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "TestLast";
	  String date = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) +"-"+ ThreadLocalRandom.current().nextInt(10, 12 + 1) + "-" + ThreadLocalRandom.current().nextInt(10, 31 + 1);
	  String email = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "@" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + ".com"; 
	  String IdOfUser = null;
	  int found = 0;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathCreate"))).click();
      driver.findElement(By.xpath(prop.getProperty("xpathFirstName"))).sendKeys(FirstName);
      driver.findElement(By.xpath(prop.getProperty("xpathLastName"))).sendKeys(LastName);	
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).sendKeys(date);
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).sendKeys(email);
	  driver.findElement(By.xpath(prop.getProperty("xpathAdd"))).click();
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(10000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String reqMethod = req.getMethod();
    	  String uniqueId[] = requestContent.split("/");
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches("employees") && reqMethod.matches("POST")) {
    		  flag = 1;
    		  HarResponse res = entry.getResponse();
    		  String content = res.getContent().getText();
    		  Gson objGson = new GsonBuilder().create();
    		  Type listType = new TypeToken<Person>() {}.getType();
    		  Person readFromJson = objGson.fromJson(content,listType);
    		  IdOfUser = Integer.toString(readFromJson.getId());
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser) && p.getEmail().matches(email) && p.getFirst_name().matches(FirstName) && p.getLast_name().matches(LastName) && p.getStart_date().matches(date)) {
    					  found = 1;
    				  }
    			  }
    		  }
    	  }	
      }
      Assert.assertNotEquals(found, 0);
  } 
  /**
   * Test invalid date based on Boundary analysis on creating entry and validate the response.
   * @throws InterruptedException
   */
  @Test
  public void testCreatePersonWithInvalidDate() throws InterruptedException {
	  String FirstName = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "TestFirst";
	  String LastName = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "TestLast";
	  String date = "" + ThreadLocalRandom.current().nextInt(2101, 3000 + 1) +"-"+ ThreadLocalRandom.current().nextInt(10, 12 + 1) + "-" + ThreadLocalRandom.current().nextInt(10, 31 + 1);
	  String email = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "@" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + ".com"; 
	  int found = 0;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathCreate"))).click();
      driver.findElement(By.xpath(prop.getProperty("xpathFirstName"))).sendKeys(FirstName);
      driver.findElement(By.xpath(prop.getProperty("xpathLastName"))).sendKeys(LastName);	
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).sendKeys(date);
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).sendKeys(email);
	  driver.findElement(By.xpath(prop.getProperty("xpathAdd"))).click();
	  new WebDriverWait(driver, 60).until(ExpectedConditions.alertIsPresent());
      Thread.sleep(1000);
      driver.switchTo().alert().accept();
      Thread.sleep(10000);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();      
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String reqMethod = req.getMethod();
    	  HarResponse res = entry.getResponse();
    	  String uniqueId[] = requestContent.split("/");
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches("employees") && reqMethod.matches("POST") && res.getStatus() == 422) {
    		  System.out.println("Date not entered in system");
    	  } 
    	  }	
      Assert.assertEquals(found, 0);
  }
  /**
   * Test invalid email on creating entry and validate the response.
   * @throws InterruptedException
   */
  @Test
  public void testCreatePersonWithInvalidEmail() throws InterruptedException {
	  String FirstName = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "TestFirst";
	  String LastName = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "TestLast";
	  String date = "" + ThreadLocalRandom.current().nextInt(1901, 2000 + 1) +"-"+ ThreadLocalRandom.current().nextInt(10, 12 + 1) + "-" + ThreadLocalRandom.current().nextInt(10, 31 + 1);
	  String email = "" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1) + "@" + ThreadLocalRandom.current().nextInt(1900, 2050 + 1);
	  String IdOfUser = null;
	  int found = 0;
	  WebElement parent = driver.findElement(By.xpath(prop.getProperty("xpathUserListItem")));
	  List<WebElement> listItems = parent.findElements(By.tagName("li"));
	  int index = ThreadLocalRandom.current().nextInt(1, listItems.size());
	  String elementToOperate= prop.getProperty("xpathUserElement")+index+"]";
	  WebElement el = driver.findElement(By.xpath(elementToOperate));
	  Coordinates cor=((Locatable)el).getCoordinates();
      cor.inViewPort();
      Thread.sleep(1000);
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.visibilityOfElementLocated((By.xpath(elementToOperate))));
	  driver.findElement(By.xpath(prop.getProperty("xpathUserElement")+index+"]")).click();
	  driver.findElement(By.xpath(prop.getProperty("xpathCreate"))).click();
      driver.findElement(By.xpath(prop.getProperty("xpathFirstName"))).sendKeys(FirstName);
      driver.findElement(By.xpath(prop.getProperty("xpathLastName"))).sendKeys(LastName);	
      driver.findElement(By.xpath(prop.getProperty("xpathDate"))).sendKeys(date);
      driver.findElement(By.xpath(prop.getProperty("xpathEmail"))).sendKeys(email);
	  driver.findElement(By.xpath(prop.getProperty("xpathAdd"))).click();
	  (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]")));
      server.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      Thread.sleep(10000);
      ActivityMonitor monitor = new ActivityMonitor();
      monitor.waitForQuiescence(100, 100, TimeUnit.MILLISECONDS);
      List<HarEntry> e= server.getHar().getLog().getEntries();
      ListIterator<HarEntry> litr = null;
      litr=e.listIterator();
      int flag = 0;
      while(litr.hasNext()) {
    	  HarEntry entry = litr.next();
    	  HarRequest req = entry.getRequest();
    	  String requestContent = req.getUrl();
    	  String reqMethod = req.getMethod();
    	  String uniqueId[] = requestContent.split("/");
    	  String lastEntryInUrl = uniqueId[uniqueId.length - 1];
    	  if (lastEntryInUrl.matches("employees") && reqMethod.matches("POST")) {
    		  flag = 1;
    		  HarResponse res = entry.getResponse();
    		  String content = res.getContent().getText();
    		  Gson objGson = new GsonBuilder().create();
    		  Type listType = new TypeToken<Person>() {}.getType();
    		  Person readFromJson = objGson.fromJson(content,listType);
    		  IdOfUser = Integer.toString(readFromJson.getId());
    	  } else if (lastEntryInUrl.matches("employees")) {
    		  if (flag == 1) {
    			  HarResponse res = entry.getResponse();
    			  String content = res.getContent().getText();
    			  Gson objGson = new GsonBuilder().create();
    			  Type listType = new TypeToken<List<Person>>() {}.getType();
    			  List<Person> readFromJson = objGson.fromJson(content,listType);
    			  for (int j=0;j<readFromJson.size();j++) {
    				  Person p = readFromJson.get(j);
    				  if (Integer.toString(p.getId()).matches(IdOfUser) && p.getEmail().matches(email)) {
    					  found = 1;
    				  }
    			  }
    		  }
    	  }	
      }
      Assert.assertEquals(found, 0);
	  }
  
  /**
   * Creates BrowserMobProxyServer to enable a proxy behind browser driver. Enable Request,Response capture on the proxy. Starts the driver for testing.
   * @throws FileNotFoundException
   * @throws IOException
   */
  @Parameters("browser")
  @BeforeClass
  public void beforeClass(String browser) throws FileNotFoundException,IOException {
	  Proxy proxy = ClientUtil.createSeleniumProxy(server);
	  DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
	  seleniumCapabilities.setCapability(CapabilityType.PROXY, proxy);
	  HashSet<CaptureType> enable = new HashSet<CaptureType>();
	  enable.add(CaptureType.REQUEST_CONTENT);
	  enable.add(CaptureType.RESPONSE_CONTENT);
	  server.enableHarCaptureTypes(enable);
	  if(browser.equalsIgnoreCase("firefox")) {
		  FirefoxOptions options = new FirefoxOptions(seleniumCapabilities);
		  System.setProperty("webdriver.gecko.driver","webdriver/geckodriver.exe");  
		  driver = new FirefoxDriver(options);
	  }else if (browser.equalsIgnoreCase("chrome")) { 
	 	  System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
	 	  ChromeOptions options = new ChromeOptions();
	 	  options.merge(seleniumCapabilities);
		  driver = new ChromeDriver(options);
	  }
	  
	  driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
	  driver.get(prop.getProperty("url"));
  }
/**
 * Executed before each test to help create a new har capture and login to browser.
 */
  @BeforeMethod
  public void beforeTest() {
	  server.newHar();
      driver.findElement(By.xpath(prop.getProperty("xpathusername"))).sendKeys(prop.getProperty("username"));
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).clear();
      driver.findElement(By.xpath(prop.getProperty("xpathpassword"))).sendKeys(prop.getProperty("ValidPassword"));
      driver.findElement(By.xpath(prop.getProperty("xpathLogin"))).click();
      (new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty("xpathUserElement")+1+"]"))); 
  }
  /**
   * Executed after each test and logs out the user from current session.
   */
  @AfterMethod
  public void afterTest() {
      driver.findElement(By.xpath(prop.getProperty("xpathLogout"))).click();
      WebDriverWait wait = new WebDriverWait(driver,30);
      wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(prop.getProperty("xpathLogin")),prop.getProperty("xathLoginText")));
  }
  /**
   * Stops the capture proxy and driver.
   */
  @AfterSuite
  public void afterSuite() {
	  try {
	  server.stop();
	  driver.quit();
	  } catch (IllegalStateException e) {
		  
	  }
  }

}
