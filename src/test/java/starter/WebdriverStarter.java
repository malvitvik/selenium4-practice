package starter;

import java.net.MalformedURLException;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public interface WebdriverStarter {
	
	void setUrl(String url) throws MalformedURLException;
	void setCapabilityManager(CapabilityManager<? extends Capabilities> capabilityManager);

	WebDriver getWebDriver();
}
