package com.springsource.greenhouse.signup;

public interface SignupService {

	void signup(Person person) throws EmailAlreadyOnFileException;

}
