package com.springsource.greenhouse.signup;

import com.springsource.greenhouse.account.Account;

// TODO consider pushing down to account package... highly dependent upon it
public interface SignupService {

	Account signup(Person person) throws EmailAlreadyOnFileException;

}
