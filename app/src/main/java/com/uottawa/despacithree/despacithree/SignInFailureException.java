package com.uottawa.despacithree.despacithree;

import java.lang.RuntimeException;

/** Exception to be thrown when an account fails to sign in. Is a child of RuntimeException.
*/
public class SignInFailureException extends RuntimeException {
	public SignInFailureException() {
		super();
	}

	public SignInFailureException(String message) {
		super(message);
	}
}