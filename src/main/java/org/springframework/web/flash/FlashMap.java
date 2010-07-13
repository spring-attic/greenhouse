package org.springframework.web.flash;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class FlashMap {
	
	static final String FLASH_MAP_ATTRIBUTE = FlashMap.class.getName();
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCurrent(HttpServletRequest request) {
		HttpSession session = request.getSession(); 
		Map<String, Object> flash = (Map<String, Object>) session.getAttribute(FLASH_MAP_ATTRIBUTE);
		if (flash == null) {
			flash = new HashMap<String, Object>();
			session.setAttribute(FLASH_MAP_ATTRIBUTE, flash);
		}
		return flash;
	}
	
	private FlashMap() {
	}

	public static void put(String name, Object value) {
		
	}

	public static void setInfoMessage(String message) {
	}

	public static void setWarningMessage(String warning) {
	}

	public static void setErrorMessage(String error) {
	}

	public static void setSuccessMessage(String error) {
	}

}
