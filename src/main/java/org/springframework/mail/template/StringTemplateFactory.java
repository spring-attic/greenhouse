package org.springframework.mail.template;

import org.springframework.core.io.Resource;

//Copied verbatim from Spring WebFlow - See http://jira.springframework.org/browse/SPR-6987
public interface StringTemplateFactory {
	
	StringTemplate getStringTemplate(Resource resource);
		
}