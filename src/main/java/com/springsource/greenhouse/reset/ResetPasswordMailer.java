package com.springsource.greenhouse.reset;

public interface ResetPasswordMailer {

	void send(ResetPasswordRequest request);

}
