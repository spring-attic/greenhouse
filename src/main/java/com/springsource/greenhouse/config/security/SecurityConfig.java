package com.springsource.greenhouse.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({
	"com/springsource/greenhouse/config/security/security.xml",
	"com/springsource/greenhouse/config/security/security-oauth-provider.xml"
})
public class SecurityConfig {

}
