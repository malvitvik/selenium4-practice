package starter;

import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeRemoteCapabilityManager implements CapabilityManager<ChromeOptions> {

	@Override
	public ChromeOptions getCapabilities() {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setCapability("browserName", "chrome");
		chromeOptions.setBrowserVersion("109");
		// Showing a test name instead of the session id in the Grid UI
//		chromeOptions.setCapability("se:name", getClass().getName().replaceAll("(.)([A-Z])", "$1 $2"));
		// Other type of metadata can be seen in the Grid UI by clicking on the
		chromeOptions.addArguments("--remote-allow-origins=*");
		chromeOptions.setAcceptInsecureCerts(true);
		chromeOptions.addArguments("--start-maximized");
		// session info or via GraphQL
//		chromeOptions.setCapability("se:sampleMetadata", "Meta data");
		return chromeOptions;
	}
}
