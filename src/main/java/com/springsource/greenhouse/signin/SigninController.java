/*
 * Copyright 2010 the original author or authors.
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
package com.springsource.greenhouse.signin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * UI Controller that renders the signin-form.
 * @author Keith Donald
 */
@Controller
public class SigninController {
	
	/**
	 * Render the signin form to the person as HTML in their web browser.
	 * Returns void and relies in request-to-view-name translation to kick-in to resolve the view template to render.
	 */
	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public void signin() {
	}
}
