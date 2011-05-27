/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.springsource.greenhouse.config;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.flash.FlashMapFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Code-based alternative to web.xml for use within Servlet 3.0+ environments. See
 * {@link WebApplicationInitializer} Javadoc for complete details.
 *
 * @author Chris Beams
 */
public class GreenhouseWebAppInitializer implements WebApplicationInitializer {

	/**
	 * Register and configure all Servlet container components necessary to power the
	 * Greenhouse web application.
	 */
	@Override
	public void onStartup(ServletContext sc) throws ServletException {
		System.out.println("GreenhouseWebAppInitializer.onStartup()");

		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext root = new AnnotationConfigWebApplicationContext();
		root.scan("com.springsource.greenhouse.config");
		root.getEnvironment().setDefaultProfiles("embedded");

		// Manages the lifecycle of the root application context
		sc.addListener(new ContextLoaderListener(root));

		// Allows attributes to be accessed on the next request
		sc.addFilter("flashMapFilter", FlashMapFilter.class)
			.addMappingForUrlPatterns(null, false, "/*");

		// Enables support for DELETE and PUT request methods with web browser clients
		sc.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class)
			.addMappingForUrlPatterns(null, false, "/*");

		// Secures the application
		sc.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
			.addMappingForUrlPatterns(null, false, "/*");

		// Handles requests into the application
		ServletRegistration.Dynamic appServlet =
			sc.addServlet("appServlet", new DispatcherServlet(new GenericWebApplicationContext()));
		appServlet.setLoadOnStartup(1);
		Set<String> mappingConflicts = appServlet.addMapping("/");
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException("'appServlet' could not be mapped to '/' due " +
				"to an existing mapping. This is a known issue under Tomcat versions " +
				"<= 7.0.14; see https://issues.apache.org/bugzilla/show_bug.cgi?id=51278");
		}

		// H2 Database Console for managing the app's database
		ServletRegistration.Dynamic h2Servlet =
			sc.addServlet("H2Console", org.h2.server.web.WebServlet.class);
		h2Servlet.setInitParameter("webAllowOthers", "true");
		h2Servlet.setLoadOnStartup(2);
		h2Servlet.addMapping("/admin/h2/*");
	}

}
