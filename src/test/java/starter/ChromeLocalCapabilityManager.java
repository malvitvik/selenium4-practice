package starter;

import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeLocalCapabilityManager implements CapabilityManager<ChromeOptions>{

	@Override 
	public ChromeOptions getCapabilities() {
		ChromeOptions options = new ChromeOptions();
//		options.setBrowserVersion("stable");
		options.setAcceptInsecureCerts(true);
		options.addArguments("--start-maximized");
		return options;
	}
}
