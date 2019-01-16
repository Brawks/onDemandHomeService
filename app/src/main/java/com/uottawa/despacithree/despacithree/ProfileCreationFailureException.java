package com.uottawa.despacithree.despacithree;

import java.lang.RuntimeException;

/** Exception to be thrown when a profile fails to be created. Is a child of RuntimeException.
*/
public class ProfileCreationFailureException extends RuntimeException {
	public ProfileCreationFailureException() {
		super();
	}

	public ProfileCreationFailureException(String message) {
		super(message);
	}
}