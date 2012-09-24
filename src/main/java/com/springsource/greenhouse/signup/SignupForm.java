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

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.springframework.social.connect.UserProfile;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.Gender;
import com.springsource.greenhouse.account.Person;
import com.springsource.greenhouse.validation.Confirm;

/**
 * The model for the new member signup form.
 * @author Keith Donald
 */
@Confirm(field="email", message="does not match confirmation email")
@JsonIgnoreProperties(ignoreUnknown=true)
public class SignupForm {
	
	@NotEmpty
	@JsonProperty("first-name")
	private String firstName;

	@NotEmpty
	@JsonProperty("last-name")
	private String lastName;

	@NotEmpty
	@Email
	@JsonProperty("email")
	private String email;

	@JsonProperty("confirm-email")
	private String confirmEmail;
	
	@JsonProperty("gender")
	@JsonDeserialize(using=SignupFormDeserializers.GenderDeserializer.class)
	private Gender gender;

	@NotNull
	private Integer month;
	
	@NotNull
	private Integer day;
	
	@NotNull
	private Integer year;
	
	@Size(min=6, message="must be at least 6 characters")
	@JsonProperty("password")
	private String password;

	/**
	 * The person's first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * The person's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * The person's email, must be unique.
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * A confirmation of the person's email. Must match.
	 */
	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	/**
	 * The member's password.
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * The member's gender.
	 */
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * The month them member as born.
	 */
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	/**
	 * The day them member was born.
	 */
	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	/**
	 * The year the member was born.
	 */
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	
	@JsonProperty("birthdate")
	@JsonDeserialize(using=SignupFormDeserializers.BirthdateDeserializer.class)
	protected void setBirthdate(List<Integer> birthdateFields) {
		this.month = birthdateFields.get(0);
		this.day = birthdateFields.get(1);
		this.year = birthdateFields.get(2);
	}
	
	/**
	 * Creates a Person record from this SignupForm.
	 * A Person is used as input to {@link AccountRepository} to create a new member Account.
	 */
	public Person createPerson() {
		return new Person(firstName, lastName, email, password, gender, new LocalDate(year, month, day));
	}
	
	/**
	 * Factory method that creates a pre-populated SignupForm from a ServiceProviderUser model obtained from a provider user sign-in attempt.
	 * @param providerUser the providerUser model
	 */
	public static SignupForm fromProviderUser(UserProfile providerUser) {
		SignupForm form = new SignupForm();
		form.setFirstName(providerUser.getFirstName());
		form.setLastName(providerUser.getLastName());
		form.setEmail(providerUser.getEmail());
		form.setConfirmEmail(providerUser.getEmail());
		return form;
	}
}