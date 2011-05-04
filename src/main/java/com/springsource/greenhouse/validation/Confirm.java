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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Class-level constraint denoting that a field value should be confirmed by matching the value of another field.
 * @author Keith Donald
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy=ConfirmValidator.class)
@Documented
public @interface Confirm {
	
	String message() default "{com.springsource.greenhouse.validation.constraints.Confirm.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * The name of the field to be confirmed. e.g. password
	 */
	String field();

	/**
	 * The name of the field whose value must match the value of {@link #field()} to satisfy this constraint.
	 * Optional. If not specified, defaults to "confirm${field}" e.g. confirmPassword.
	 */
	String matches() default "";

	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {

		/**
		 * Used to specify multiple confirm fields per class.
		 */
		Confirm[] value();
		
	}
}
