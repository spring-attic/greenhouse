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
package com.springsource.greenhouse.signup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.hibernate.validator.util.LazyValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.signup.SignupHelper.SignupCallback;
import com.springsource.greenhouse.utils.Message;
import com.springsource.greenhouse.utils.MessageType;

/**
 * UI Controller for signing up new members.
 * @author Keith Donald
 */
@Controller
public class SignupController {

	private final SignupHelper signupHelper;
	
	@Inject
	public SignupController(AccountRepository accountRepository, SignedUpGateway gateway) {
		this.signupHelper = new SignupHelper(accountRepository, gateway);
	}

	/**
	 * Render a signup form to the person as HTML in their web browser.
	 */
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		if (connection != null) {
			request.setAttribute("message", new Message(MessageType.INFO, "Your " + StringUtils.capitalize(connection.getKey().getProviderId()) + " account is not associated with a Greenhouse account. If you're new, please sign up."), WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	/**
	 * Process a signup form submission.
	 * Delegate to a {@link SignupHelper} to actually complete the signin transaction.
	 * Redirects the new member to the application home page on successful sign-in.
	 */
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, final WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		boolean result = signupHelper.signup(form, formBinding, new SignupCallback() {
			public void postSignup(Account account) {
				ProviderSignInUtils.handlePostSignUp(account.getId().toString(), request);
			}
		});
		return result ? "redirect:/" : null;
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<Map<String, Object>> signupFromApi(@RequestBody SignupForm form) {
		
		BindingResult formBinding = validate(form); // Temporary manual validation until SPR-9826 is fixed.
		
		if (formBinding.hasErrors()) {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", "Validation error");
			errorResponse.put("errors", getErrorsMap(formBinding));			
			return new ResponseEntity<Map<String, Object>>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		boolean result = signupHelper.signup(form, formBinding);
		
		if (result) {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", "Account created");
			return new ResponseEntity<Map<String, Object>>(errorResponse, HttpStatus.CREATED);			
		} else {
			HashMap<String, Object> errorResponse = new HashMap<String, Object>();
			errorResponse.put("message", "Account creation error");
			errorResponse.put("errors", getErrorsMap(formBinding));			
			return new ResponseEntity<Map<String, Object>>(errorResponse, HttpStatus.BAD_REQUEST);			
		}
	}

	private BindException validate(SignupForm form) {
		BindException errors;
		errors = new BindException(form, "signupForm");
		LazyValidatorFactory lvf = new LazyValidatorFactory();
		Validator validator = new SpringValidatorAdapter(lvf.getValidator());
		ValidationUtils.invokeValidator(validator, form, errors);
		return errors;
	}

	private List<Map<String, String>> getErrorsMap(BindingResult formBinding) {
		List<FieldError> fieldErrors = formBinding.getFieldErrors();
		List<Map<String, String>> errors = new ArrayList<Map<String,String>>(fieldErrors.size());						
		for (FieldError fieldError : fieldErrors) {
			Map<String, String> fieldErrorMap = new HashMap<String, String>();
			fieldErrorMap.put("field", fieldError.getField());
			fieldErrorMap.put("code", fieldError.getCode());
			fieldErrorMap.put("message", fieldError.getDefaultMessage());
			errors.add(fieldErrorMap);
		}
		return errors;
	}
	
}