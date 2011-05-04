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
package com.springsource.greenhouse.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Validates @Confirm fields.
 * @author Keith Donald
 */
public final class ConfirmValidator implements ConstraintValidator<Confirm, Object> {

    private String field;

    private String matches;

    private String message;
    
	public void initialize(Confirm constraintAnnotation) {
		field = constraintAnnotation.field();
		matches = constraintAnnotation.matches();
		if (matches == null || matches.length() == 0) {
			matches = "confirm" + StringUtils.capitalize(field);
		}
		message = constraintAnnotation.message();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		Object fieldValue = beanWrapper.getPropertyValue(field);
		Object matchesValue = beanWrapper.getPropertyValue(matches);
		boolean matched = ObjectUtils.nullSafeEquals(fieldValue, matchesValue);
		if (matched) {
			return true;
		} else {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addNode(field).addConstraintViolation();
			return false;
		}
	}

}
