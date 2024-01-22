package tests.page;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TakeScreenshot {

	private final ThreadLocal<WebDriver> webDriverTL;


	public TakeScreenshot(ThreadLocal<WebDriver> driverTL) {
		this.webDriverTL = driverTL;
	}


	public void takeScreenshot() throws IOException {
		takeScreenshot((TakesScreenshot)webDriverTL.get());
	}


	public void takeScreenshot(TakesScreenshot source) throws IOException {
		File screenshot = source.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFileToDirectory(screenshot, new File("./target/screenshots/"));
	}
}