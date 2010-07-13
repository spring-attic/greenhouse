package com.springsource.greenhouse.signup;

import com.springsource.greenhouse.account.Account;

public interface SignupService {

	Account signup(Person person) throws EmailAlreadyOnFileException;

}
