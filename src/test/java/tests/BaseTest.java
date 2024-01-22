package tests;

import java.net.MalformedURLException;

import org.testng.annotations.BeforeSuite;
import starter.RemoteStarter;
import starter.WebdriverStarter;

public class BaseTest {

	protected WebdriverStarter webdriverStarter;

	@BeforeSuite
	public void setWebdriverStarter() throws MalformedURLException {

		webdriverStarter = new RemoteStarter();
//		webdriverStarter.setUrl("http://127.0.0.1:4444/wd/hub");
//		webdriverStarter.setCapabilityManager(new ChromeRemoteCapabilityManager());
	}

}
