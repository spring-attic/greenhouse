package org.springframework.templating;

import org.springframework.core.io.Resource;

public interface StringTemplateLoader {
	
	StringTemplate getStringTemplate(Resource resource);
		
}