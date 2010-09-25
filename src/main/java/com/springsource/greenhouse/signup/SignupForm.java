package com.springsource.greenhouse.signup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.LocalDate;

import com.springsource.greenhouse.account.Gender;
import com.springsource.greenhouse.account.Person;

public class SignupForm {
	
	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	@Size(max=320)
	private String email;

	private Gender gender;

	@NotNull
	private Integer month;
	
	@NotNull
	private Integer day;
	
	@NotNull
	private Integer year;
	
	@NotNull
	@Size(min=6)
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
		
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Person createPerson() {
		return new Person(firstName, lastName, email, password, gender, new LocalDate(year, month, day));
	}
}