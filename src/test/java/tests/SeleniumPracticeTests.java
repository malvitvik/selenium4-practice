package tests;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SeleniumPracticeTests extends BaseTest {
	
	private WebDriver webDriver;
	private WebDriverWait wdWait;
	
	@BeforeMethod
	public void init() {
		webDriver = webdriverStarter.getWebDriver();
		wdWait = new WebDriverWait(webDriver, Duration.ofSeconds(30), Duration.ofMillis(500));
	}
	
	@Test
	public void fluentWait1() {
		Wait<WebDriver> wait = new FluentWait<>(webDriver)
				.pollingEvery(Duration.ofSeconds(3))
				.withTimeout(Duration.ofSeconds(30))
				.ignoring(NoSuchElementException.class)
				.withMessage("Wait for element");
		
		webDriver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
		webDriver.findElement(By.cssSelector("#start button")).click();
		
		WebElement finish = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4")));
		Assert.assertTrue(finish.isDisplayed());
		System.out.println(finish.getText());
		
	}



	@Test
	public void fluentWait2() {

		webDriver.get("https://the-internet.herokuapp.com/dynamic_loading/1");


		WebElement finish = webDriver.findElement(By.cssSelector("#finish h4"));
		Wait<WebElement> wait = new FluentWait<>(finish)
				.pollingEvery(Duration.ofSeconds(3))
				.withTimeout(Duration.ofSeconds(30))
				.ignoring(NoSuchElementException.class)
				.withMessage("Wait for element");

//		System.out.println((boolean)wait.until(it -> it.isDisplayed()));
		webDriver.findElement(By.cssSelector("#start button")).click();

		Assert.assertTrue(wait.until(it -> it.isDisplayed()));
		System.out.println(finish.getText());
	}
	
	
	@Test
	public void switchWindow() {
		webDriver.get("https://the-internet.herokuapp.com/windows");
		webDriver.findElement(By.cssSelector("a[href*='window']")).click();

		String parent = webDriver.getWindowHandle();
		Set<String> windowHandles = webDriver.getWindowHandles();
		windowHandles.remove(parent);

		//child window
		webDriver.switchTo().window(windowHandles.iterator().next());
		System.out.println(webDriver.findElement(By.tagName("h3")).getText());
		
		//parent window
		webDriver.switchTo().window(parent);
		System.out.println(webDriver.findElement(By.tagName("h3")).getText());
	}


	@Test
	public void nestedFrame() {
		webDriver.get("https://the-internet.herokuapp.com/nested_frames");

		WebElement topFrame = webDriver.findElement(By.name("frame-top"));
		webDriver.switchTo().frame(topFrame);

		WebElement nestedFrame = webDriver.findElement(By.name("frame-middle"));
		webDriver.switchTo().frame(nestedFrame);

		System.out.println(webDriver.findElement(By.id("content")).getText());
		
		webDriver.switchTo().defaultContent();
	}

	@Test
	public void dragAndDropInFrame() {
		webDriver.get("https://jqueryui.com/droppable/");
		
		webDriver.switchTo().frame(webDriver.findElement(By.cssSelector(".demo-frame")));
		WebElement source = webDriver.findElement(By.id("draggable"));
		WebElement target = webDriver.findElement(By.id("droppable"));

		System.out.println(source.getText());
		String color = target.getCssValue("color");
		System.out.println(target.getText());
		
		Actions actions = new Actions(webDriver);
		actions.dragAndDrop(source, target).build().perform();

		String text = target.getText();
		System.out.println(text);
		Assert.assertEquals(text, "Dropped!");
		Assert.assertNotEquals(color, target.getCssValue("color"));
		
		webDriver.switchTo().defaultContent();
	}
	
	
	
	@Test
	public void orderTest() {
		List<String> items = List.of("Cucumber", "Brocolli", "Beetroot");
		String promoCode = "rahulshettyacademy";
		String countryValue = "United States";

		openPage();
		addToCart(items);
		verifyProductsCountInCart(items);
		proceedToCheckout(promoCode);
		placeOrder(countryValue);
	}


	@Test
	public void countLinks() {
		webDriver.get("https://rahulshettyacademy.com/AutomationPractice/");
		
		int count = webDriver.findElements(By.tagName("a")).size();
		System.out.printf("Total links count %d\n", count);
		Assert.assertEquals(count, 27);

		WebElement footerElement = webDriver.findElement(By.id("gf-BIG"));
		count = footerElement.findElements(By.tagName("a")).size();
		System.out.printf("Footer links count %d\n", count);
		Assert.assertEquals(count, 20);

		List<WebElement> links = footerElement.findElement(By.xpath("//td[1]/ul")).findElements(By.tagName("a"));
		count = links.size();
		System.out.printf("The first column in Footer has links count %d\n", count);
		Assert.assertEquals(count, 5);

		for (WebElement link : links) {
			link.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));
		}

		System.out.println("\nTabs Titles:");
		
		for (String handler : webDriver.getWindowHandles()) {
			webDriver.switchTo().window(handler);
			System.out.println(webDriver.getTitle());
		}
	}
	
	@Test
	public void practice() {
		webDriver.get("https://rahulshettyacademy.com/AutomationPractice/");
		
		webDriver.findElements(By.cssSelector("#checkbox-example [type='checkbox']")).get(1).click();
		String selectedOption =
				webDriver.findElement(By.cssSelector("#checkbox-example [type='checkbox']:checked"))
				         .findElement(By.xpath("./parent::label")).getText();

		System.out.println(selectedOption);
		
		Select dropdown = new Select(webDriver.findElement(By.id("dropdown-class-example")));
		dropdown.selectByVisibleText(selectedOption);
		
		webDriver.findElement(By.id("name")).sendKeys(selectedOption);
		webDriver.findElement(By.id("alertbtn")).click();

		Alert alert = webDriver.switchTo().alert();
		Assert.assertTrue(alert.getText().contains(selectedOption), 
		                  "Alert text contains [%s]".formatted(selectedOption));
		alert.accept();
		webDriver.switchTo().defaultContent();
	}
	
	
	@Test
	public void tableTest() {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
		
		webDriver.get("https://rahulshettyacademy.com/AutomationPractice/");
		
		jsExecutor.executeScript("window.scrollTo(0, 500);");
//		Thread.sleep(500);
		jsExecutor.executeScript("document.querySelector('.tableFixHead').scrollTop = 500");
		
		WebElement total = webDriver.findElement(By.cssSelector(".totalAmount"));
		List<WebElement> values = webDriver.findElements(By.cssSelector("table#product td:nth-child(4)"));
		int sum = values.stream()
		                .map(v -> v.getText())
		                .mapToInt(v -> Integer.parseInt(v))
		                .sum();
		System.out.println(sum);
		int totalAmount = Integer.parseInt(total.getText().split(":")[1].trim());
		
		Assert.assertEquals(sum, totalAmount, "Total Amount Collected");
	}
	
	
	@Test
	public void tableTest2() {
		webDriver.get("https://rahulshettyacademy.com/AutomationPractice/");
		
		WebElement table = webDriver.findElement(By.name("courses"));

		System.out.printf("Count of rows: %d\n", table.findElements(By.tagName("tr")).size());
		System.out.printf("Count of columns: %d\n", table.findElements(By.tagName("th")).size());
		System.out.printf("Text of 2nd column: [%s]\n", table.findElements(By.xpath(".//tr[3]/td"))
		                                                     .stream()
		                                                     .map(e -> e.getText())
		                                                     .collect(Collectors.joining("|")));
	}


	private void openPage() {
		webDriver.get("https://rahulshettyacademy.com/seleniumPractise/");
	}


	private void addToCart(List<String> items) {
		List<WebElement> products = webDriver.findElements(By.cssSelector(".product"));

		for (int i = 0, j = 0; i < products.size() && j < items.size(); i++) {
			WebElement product = products.get(i);
			
			String productName = product.findElement(By.cssSelector(".product-name")).getText().split(" ")[0];
			
			if (items.contains(productName)) {
				product.findElement(By.cssSelector(".product-action button")).click();
				j++;
				System.out.printf("Product [%s] is added to cart\n", productName);
			}
		}
	}


	private void verifyProductsCountInCart(List<String> items) {
		WebElement productsInCart = webDriver.findElement(By.cssSelector(".cart-info tr:nth-child(1) td:nth-child(3)"));
		Assert.assertEquals(String.valueOf(items.size()), productsInCart.getText(), "Unique products in cart");
	}


	private void proceedToCheckout(String promoCode) {
		webDriver.findElement(By.cssSelector("a.cart-icon")).click();
		webDriver.findElement(By.cssSelector(".cart-preview button")).click();

		WebElement inputPromoCode = wdWait.withMessage("Shopping Cart is loaded")
		                                  .until(ExpectedConditions.visibilityOfElementLocated(
												  By.cssSelector(".promoCode")));

		if (promoCode != null && !promoCode.isBlank()) {
			inputPromoCode.clear();
			inputPromoCode.sendKeys(promoCode);
			webDriver.findElement(By.cssSelector(".promoBtn")).click();
			WebElement messageElement =
					wdWait.withMessage("Promo code is applied")
					      .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".promoInfo")));
			Assert.assertEquals(messageElement.getText(), "Code applied ..!", "Promo message");
			Assert.assertEquals(messageElement.getCssValue("color"), "rgba(0, 128, 0, 1)", "Promo message color");
			System.out.printf("Promo code [%s] is added to order\n", promoCode);
		}

		webDriver.findElement(By.cssSelector("br + button")).click();
	}


	private void placeOrder(String countryValue) {
		Select countryDropDown = new Select(webDriver.findElement(By.tagName("select")));
		countryDropDown.selectByValue(countryValue);
		Assert.assertEquals(countryDropDown.getFirstSelectedOption().getAttribute("value"), countryValue, 
		                    "Country is selected");
		System.out.printf("Country [%s] is selected\n", countryValue);

		WebElement checkboxTermsAndConditions = webDriver.findElement(By.cssSelector(".chkAgree"));
		checkboxTermsAndConditions.click();
		Assert.assertTrue(checkboxTermsAndConditions.isSelected(), "'Terms & Conditions' checkbox is checked");
		System.out.println("'Terms & Conditions' checkbox is checked");

		webDriver.findElement(By.cssSelector("br + button")).click();
		String message = webDriver.findElement(By.cssSelector(".products")).getText();
		System.out.printf("Message: [%s]\n", message);
		Assert.assertEquals(message.split("\n")[0], "Thank you, your order has been placed successfully", 
		                    "Order Success message");
	}


	@AfterTest
	public void closeBrowser() {
		try {
			webDriver.quit();
		} catch (Exception ex) {
			System.out.println("Browser is already closed");
		}
	}
}
