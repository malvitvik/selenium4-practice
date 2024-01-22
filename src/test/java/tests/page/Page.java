package tests.page;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Page {

	private final ThreadLocal<WebDriver> webDriverTL;


	public Page(ThreadLocal<WebDriver> driver) {
		webDriverTL = driver;
	}


	public Optional<WebElement> findItem(String itemName) {
		return webDriverTL.get().findElements(By.cssSelector("table tr td:first-child"))
		                  .stream()
		                  .filter(it -> itemName.equals(it.getText()))
		                  .findFirst();
	}


	public String getItemPrice(List<? extends WebElement> items, String itemName) {
		String name = Objects.toString(itemName, "");

		String itemPrice = items.stream()
		                        .filter(it -> name.equals(it.getText()))
		                        .findFirst()
		                        .map(it -> getPrice(it))
		                        .orElse("");

		Assert.assertFalse("".equals(itemPrice), "Item price is extracted for [%s]".formatted(itemName));
		System.out.printf("[%s] price: %s\n", itemName, itemPrice);
		return itemPrice;
	}


	public String getPrice(WebElement item) {
		return item.findElement(By.xpath("following-sibling::td[1]")).getText();
	}


	public void verifyChangeInputState() {
		WebDriver webDriver = webDriverTL.get();
		WebElement input = webDriver.findElement(By.id("displayed-text"));
		String logMessage = "Input field is %s\n";
		System.out.printf(logMessage, input.isDisplayed() ? "shown" : "hidden");
		Assert.assertTrue(input.isDisplayed());
		webDriver.findElement(By.id("hide-textbox")).click();
		System.out.printf(logMessage, input.isDisplayed() ? "shown" : "hidden");
		Assert.assertFalse(input.isDisplayed());
		webDriver.findElement(By.id("show-textbox")).click();
		System.out.printf(logMessage, input.isDisplayed() ? "shown" : "hidden");
		Assert.assertTrue(input.isDisplayed());
	}


	public void verifyCheckbox() {
		WebDriver webDriver = webDriverTL.get();
		WebElement checkbox = webDriver.findElement(By.id("checkBoxOption2"));
		Assert.assertFalse(checkbox.isSelected(), "Checkbox is unselected");
		checkbox.click();
		Assert.assertTrue(checkbox.isSelected(), "Checkbox is selected");
		System.out.println("Checkbox is " + (checkbox.isSelected() ? "selected" : "unselected"));
	}


	public void verifyDropdown() {
		WebDriver webDriver = webDriverTL.get();
		Select dropdown = new Select(webDriver.findElement(By.id("dropdown-class-example")));
		dropdown.selectByVisibleText("Option1");
		System.out.printf("Selected option text: [%s]\n", dropdown.getFirstSelectedOption().getText());
		Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option1");
		dropdown.selectByIndex(2);
		System.out.printf("Selected option text: [%s]\n", dropdown.getFirstSelectedOption().getText());
		Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option2");
		dropdown.selectByValue("option3");
		System.out.printf("Selected option text: [%s]\n", dropdown.getFirstSelectedOption().getText());
		Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option3");
	}


	public boolean selectFromAutocomplete(String country) {
		WebDriver webDriver = webDriverTL.get();
		webDriver.findElement(By.id("autocomplete")).sendKeys(country.substring(0, 3));

		WebDriverWait wdWait = new WebDriverWait(webDriver, Duration.ofSeconds(2));
		List<WebElement> suggestions = wdWait.withMessage("Wait for autocomplete suggestions")
		                                     .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				                                     By.cssSelector(".ui-menu-item")));
		for (WebElement suggestion : suggestions) {
			if (suggestion.getText().equalsIgnoreCase(country)) {
				System.out.printf("Selecting country [%s]...\n", suggestion.getText());
				suggestion.click();
				return true;
			}
		}

		System.out.printf("No country [%s] in autocomplete suggestions\n", country);
		return false;
	}


	public boolean selectByValue(List<? extends WebElement> webElements, String value) {
		for (WebElement button : webElements) {
			if (button.getAttribute("value").equals(value)) {
				button.click();
				System.out.printf("Select radiobutton with [value='%s']\n", value);
				return true;
			}
		}

		System.out.printf("No radiobutton with [value='%s']\n", value);
		return false;
	}


	public void acceptAlert(String name) {
		WebDriver webDriver = webDriverTL.get();
		WebElement input = webDriver.findElement(By.name("enter-name"));
		input.clear();
		input.sendKeys(name);
		webDriver.findElement(By.id("alertbtn")).click();
		Alert alert = webDriver.switchTo().alert();
		String message = alert.getText();
		alert.accept();
		System.out.printf("Alert message: [%s]\n", message);
		Assert.assertTrue(message.contains(name), "Alert message contains: " + name);
	}


	public void acceptConfirm(String name) {
		WebDriver webDriver = webDriverTL.get();
		WebElement input = webDriver.findElement(By.name("enter-name"));
		input.clear();
		input.sendKeys(name);
		webDriver.findElement(By.id("confirmbtn")).click();
		Alert alert = webDriver.switchTo().alert();
		String message = alert.getText();
		alert.accept();
		System.out.printf("Alert message: [%s]\n", message);
		Assert.assertTrue(message.contains(name), "Alert message contains: " + name);
	}
}