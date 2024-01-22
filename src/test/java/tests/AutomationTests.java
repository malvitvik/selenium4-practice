package tests;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.page.Page;
import tests.page.TakeScreenshot;

import static org.openqa.selenium.support.locators.RelativeLocator.with;


public class AutomationTests extends  BaseTest {

	private TakeScreenshot takeScreenshot;
	private Page page;
	ThreadLocal<WebDriver> webDriverTL = new ThreadLocal<>();
	
	@BeforeClass
	public void init() {
		takeScreenshot = new TakeScreenshot(webDriverTL);
		page = new Page(webDriverTL);
	}
	
	@BeforeMethod
	public void startUp() {
		WebDriver webDriver = webdriverStarter.getWebDriver();
		webDriverTL.set(webDriver);

	}


	@AfterMethod
	public void tearDown() {
		if (webDriverTL.get() != null) {
			webDriverTL.get().quit();
			webDriverTL.remove();
		}
	}

	@Test
	public void automationPractice() {
		WebDriver webDriver = webDriverTL.get();
		webDriver.get("https://rahulshettyacademy.com/AutomationPractice/");

		//radio buttons
		List<WebElement> radioButtons = webDriver.findElements(By.name("radioButton"));
		Assert.assertTrue(page.selectByValue(radioButtons, "radio2"), "Radiobutton selected by value");

		//checkbox
		page.verifyCheckbox();

		//dropdown
		page.verifyDropdown();

		//autocomplete
		Assert.assertTrue(page.selectFromAutocomplete("india"), "Select country from autocomplete");

		page.acceptAlert("John");
		page.acceptConfirm("Alfred");

		page.verifyChangeInputState();
	}


	@Test
	public void takePageScreenshot() throws IOException {
		WebDriver webDriver = webDriverTL.get();
		webDriver.manage().window().maximize();

		webDriver.get("https://www.google.com/");
		webDriver.manage().addCookie(new Cookie("SOCS", "CAISHAgBEhJnd3NfMjAyMzEyMTEtMF9SQzYaAnBsIAEaBgiA0q2sBg"));
		webDriver.navigate().refresh();

		takeScreenshot.takeScreenshot();
	}


	@Test
	public void brokenLinks() {
		SoftAssert v = new SoftAssert();
		WebDriver webDriver = webDriverTL.get();
		webDriver.get("https://rahulshettyacademy.com/AutomationPractice/");
		List<WebElement> links = webDriver.findElements(By.cssSelector(".gf-li a"));
		
		for (WebElement link : links) {
			String path = link.getAttribute("href");
			String text = link.getText();
			int responseCode;

			try {
				URL url = new URL(path);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("HEAD");
				connection.connect();
				responseCode = connection.getResponseCode();
				System.out.printf("Response code for [%s] link is [%s]\n", text, responseCode);
			} catch (IOException e) {
				v.assertTrue(false, "[%s] link has wrong url: [%s]".formatted(text, path));
				e.printStackTrace();
				continue;
			}

			v.assertTrue(200 <= responseCode && responseCode < 400,
			             "Response code for [%s] link is [%s]".formatted(text, responseCode));
		}

		v.assertAll("Broken links");
	}


	@Test
	public void verifyTableSorted() {
		WebDriver webDriver = webDriverTL.get();

		
		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");
		WebElement nameColumnHeader = webDriver.findElement(By.cssSelector("table tr th:first-child"));
		nameColumnHeader.click();
		List<WebElement> elements = webDriver.findElements(By.cssSelector("table tr td:first-child"));
		List<String> names = elements.stream()
		                             .map(it -> it.getText())
		                             .toList();

		List<String> sortedNames = names.stream()
		                                .sorted()
		                                .toList();
//		List<String> sortedNames = new ArrayList<>(names);
//		sortedNames.sort(Comparator.naturalOrder());

		System.out.printf("Vegetable/fruit names are %s\n", names.equals(sortedNames) ? "sorted" : "not sorted");
		Assert.assertEquals(names, sortedNames, "Vegetable/fruit names");
	}

	@DataProvider(parallel = true)
	public Object[] itemNames() {
		return new Object[] {"Beans", "Rice", "Cheese", "NoItem"};
	}

	@Test(dataProvider = "itemNames")
	public void findItemByName(String itemName) {
		SoftAssert v = new SoftAssert();
		WebDriver webDriver = webDriverTL.get();
		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(Duration.ZERO);

		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");
		WebElement firstPage = webDriver.findElement(By.cssSelector("[aria-label='First']"));
		WebElement nextPage = webDriver.findElement(By.cssSelector("[aria-label='Next']"));
		
		if ("false".equals(firstPage.getAttribute("aria-disabled"))) 
			firstPage.click();

		Optional<WebElement> itemOpt = page.findItem(itemName);

		while (itemOpt.isEmpty() && "false".equals(nextPage.getAttribute("aria-disabled"))) {
			nextPage.click();
			itemOpt = page.findItem(itemName);
		}

		String itemPrice = itemOpt.map(page::getPrice).orElse("");
		v.assertFalse("".equals(itemPrice), "Item price is extracted for [%s]".formatted(itemName));
		System.out.printf("[%s] price: %s\n", itemName, itemPrice);

		v.assertAll("Find by name");
	}


	@Test(dataProvider = "itemNames")
	public void relativeLocators(String itemName) {
		SoftAssert v = new SoftAssert();
		WebDriver webDriver = webDriverTL.get();
		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(Duration.ZERO);

		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");
		WebElement firstPage = webDriver.findElement(By.cssSelector("[aria-label='First']"));
		WebElement nextPage = webDriver.findElement(By.cssSelector("[aria-label='Next']"));

		if ("false".equals(firstPage.getAttribute("aria-disabled")))
			firstPage.click();

		Optional<WebElement> itemOpt = page.findItem(itemName);

		while (itemOpt.isEmpty() && "false".equals(nextPage.getAttribute("aria-disabled"))) {
			nextPage.click();
			itemOpt = page.findItem(itemName);
		}

		String itemPrice = itemOpt.map(it -> 
				                               webDriver.findElement(with(By.tagName("td")).toRightOf(it)).getText())
		                          .orElse("");
		v.assertFalse("".equals(itemPrice), "Item price is extracted for [%s]".formatted(itemName));
		System.out.printf("[%s] price: %s\n", itemName, itemPrice);

		v.assertAll("Find by name");
	}
	
	
	@Test
	public void invokeMultipleWindows() {
		WebDriver webDriver = webDriverTL.get();
		webDriver.manage().window().maximize();

		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");
		
		webDriver.switchTo().newWindow(WindowType.TAB);
		webDriver.get("https://www.google.com/");
	}
	
	
	@Test
	public void takeElementScreenshot() throws IOException {
		WebDriver webDriver = webDriverTL.get();

		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/#/offers");
		WebElement searchField = webDriver.findElement(By.cssSelector("#search-field"));
		searchField.sendKeys("Wheat");
		takeScreenshot.takeScreenshot(searchField);
		Dimension dimension = searchField.getRect().getDimension();
		System.out.printf("height: %s, width: %s\n", dimension.getHeight(), dimension.getWidth());

		takeScreenshot.takeScreenshot(webDriver.findElement(By.cssSelector(".pagination")));
	}


	private void takeScreenshot() throws IOException {
		takeScreenshot.takeScreenshot();
	}

	private void takeScreenshot(TakesScreenshot source) throws IOException {
		takeScreenshot.takeScreenshot(source);
	}
}