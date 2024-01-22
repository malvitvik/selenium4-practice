package starter;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class RemoteStarter implements WebdriverStarter {
	
	private Optional<URL> url = Optional.empty();
	
	private CapabilityManager<? extends Capabilities> capabilityManager;
	
	private Map<Class, Function<Capabilities, RemoteWebDriver>> localDrivers = new HashMap<>() {{
		put(ChromeOptions.class, (cap) -> new ChromeDriver((ChromeOptions)cap));
		put(FirefoxOptions.class, (cap) -> new FirefoxDriver((FirefoxOptions)cap));
		put(EdgeOptions.class, (cap) -> new EdgeDriver((EdgeOptions)cap));
		put(SafariOptions.class, (cap) -> new SafariDriver((SafariOptions)cap));
		
	}};
	
	public RemoteStarter() {
		
		setCapabilityManager(new ChromeLocalCapabilityManager());
	}
	
	@Override
	public void setUrl(String url) throws MalformedURLException {
		if (url != null && !url.isBlank()) {
			this.url = Optional.of(new URL(url));
		}
	}


	@Override
	public void setCapabilityManager(CapabilityManager<? extends Capabilities> capabilityManager) {
		this.capabilityManager = Objects.requireNonNull(capabilityManager);
	}


	@Override 
	public WebDriver getWebDriver() {
		RemoteWebDriver webDriver = url.map(value -> remoteWebDriver(value, capabilityManager))
		                               .orElseGet(() -> localWebDriver(capabilityManager));
		System.out.println(webDriver.getCapabilities().asMap());
		webDriver.manage().timeouts().implicitlyWait(Duration.ZERO);

		return webDriver;
	}


	protected RemoteWebDriver remoteWebDriver(URL value, CapabilityManager<? extends Capabilities> capManager) {
		return new RemoteWebDriver(value, capManager.getCapabilities());
	}

	protected RemoteWebDriver localWebDriver(CapabilityManager<? extends Capabilities> capManager) {
		Capabilities capabilities = capManager.getCapabilities();

		return Objects.requireNonNull(localDrivers.get(capabilities.getClass()))
		              .apply(capabilities);
	}
}
