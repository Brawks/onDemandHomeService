package com.uottawa.despacithree.despacithree;

import java.lang.RuntimeException;

/** Exception to be thrown when an account fails to be created. Is a child of RuntimeException.
*/
public class AccountCreationFailureException extends RuntimeException {
	public AccountCreationFailureException() {
		super();
	}

	public AccountCreationFailureException(String message) {
		super(message);
	}
}