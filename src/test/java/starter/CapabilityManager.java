package starter;

import org.openqa.selenium.Capabilities;

public interface CapabilityManager<T extends Capabilities> {
	
	T getCapabilities();
}
