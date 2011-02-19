package com.springsource.greenhouse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({
	"com/springsource/greenhouse/activity/integration-activity.xml",
	"com/springsource/greenhouse/signup/integration-signup.xml" }
)
public class IntegrationConfig {

}
