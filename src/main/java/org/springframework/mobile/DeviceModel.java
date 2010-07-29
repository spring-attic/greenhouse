package org.springframework.mobile;

import java.util.Map;

import net.sourceforge.wurfl.core.CapabilityNotDefinedException;
import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.MarkUp;
import net.sourceforge.wurfl.core.handlers.AppleHandler;

public class DeviceModel implements Device {
	
	private Device device;
	
	public DeviceModel(Device device) {
		this.device = device;
	}

	public boolean isMobileBrowser() {
		String mobileBrowserCapability = device.getCapability("mobile_browser");
		return mobileBrowserCapability != null && mobileBrowserCapability.length() > 0;
	}
	
	public boolean isApple() {
		return new AppleHandler().canHandle(device.getUserAgent());
	}
	
	// implementing wurfl Device

	public Map getCapabilities() {
		return device.getCapabilities();
	}

	public String getCapability(String name)
			throws CapabilityNotDefinedException {
		return device.getCapability(name);
	}

	public String getId() {
		return device.getId();
	}

	public MarkUp getMarkUp() {
		return device.getMarkUp();
	}

	public String getUserAgent() {
		return device.getUserAgent();
	}

}
