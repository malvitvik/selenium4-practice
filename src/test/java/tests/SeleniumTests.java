package tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SeleniumTests extends BaseTest {


	@Test
	public  void testSelenium() {
		WebDriver webDriver = webdriverStarter.getWebDriver();
		WebDriverWait wdWait = new WebDriverWait(webDriver, Duration.ofSeconds(120));

		webDriver.get("https://www.google.com/");

		wdWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("textarea[name='q']")))
		      .sendKeys("Selenium 4" + Keys.ENTER);

		wdWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#search")));

		List<WebElement> searchResults = webDriver.findElements(By.cssSelector("#search .g"));

		assertTrue(searchResults.size() > 0, "Search results");

		webDriver.quit();
	}
	
	
	@Test(invocationCount = 50, threadPoolSize = 5)
	public void fillForm() {
		WebDriver webDriver = webdriverStarter.getWebDriver();
		
		webDriver.get("https://rahulshettyacademy.com/angularpractice/");
		
		webDriver.findElement(By.name("name")).sendKeys("Jane");
		webDriver.findElement(By.name("email")).sendKeys("j.jonson@ave.com");
		webDriver.findElement(By.id("exampleInputPassword1")).sendKeys("password");
		webDriver.findElement(By.id("exampleCheck1")).click();
		
		Select genderDropdown = new Select(webDriver.findElement(By.id("exampleFormControlSelect1")));
		genderDropdown.selectByVisibleText("Female");
		
		webDriver.findElement(By.id("inlineRadio1")).click();
		String selectedEmploymentStatus =
				webDriver.findElement(By.cssSelector("input[type='radio']:checked + label")).getText();
		System.out.println(selectedEmploymentStatus);
		Assert.assertEquals(selectedEmploymentStatus, "Student");
		
		webDriver.findElement(By.name("bday")).sendKeys("01/07/1990");
		webDriver.findElement(By.xpath("//input[@value='Submit']")).click();
		String message = webDriver.findElement(By.cssSelector(".alert-success")).getText();
		System.out.println(message);
		Assert.assertTrue(message.contains("Success"));
		
		webDriver.quit();
	}

}
