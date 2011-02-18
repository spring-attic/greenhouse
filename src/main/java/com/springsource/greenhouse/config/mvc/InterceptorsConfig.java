package com.springsource.greenhouse.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("com/springsource/greenhouse/config/mvc/mvc-interceptors.xml")
public class InterceptorsConfig {

}
