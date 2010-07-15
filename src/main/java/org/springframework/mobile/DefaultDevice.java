package org.springframework.mobile;

import java.util.Map;

import net.sourceforge.wurfl.core.CapabilityNotDefinedException;
import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.MarkUp;
import net.sourceforge.wurfl.core.handlers.AppleHandler;

public class DefaultDevice implements Device {
	
	private Device wurflDevice;
	
	private boolean isMobileBrowser;
	
	private boolean isAppleDevice;
	
	public DefaultDevice(Device device) {
		this.wurflDevice = device;
		this.isMobileBrowser = !wurflDevice.getCapability("mobile_browser").isEmpty();
		this.isAppleDevice = new AppleHandler().canHandle(device.getUserAgent());
	}

	public Map getCapabilities() {
		return wurflDevice.getCapabilities();
	}

	public String getCapability(String name)
			throws CapabilityNotDefinedException {
		return wurflDevice.getCapability(name);
	}

	public String getId() {
		return wurflDevice.getId();
	}

	public MarkUp getMarkUp() {
		return wurflDevice.getMarkUp();
	}

	public String getUserAgent() {
		return wurflDevice.getUserAgent();
	}

	public boolean getIsMobileBrowser() {
		return this.isMobileBrowser;
	}
	
	public boolean getIsAppleDevice() {
		return this.isAppleDevice;
	}
}
