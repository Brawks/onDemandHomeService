package com.uottawa.despacithree.despacithree;

import java.util.NoSuchElementException;

/** Class that contains an instance of an Account object.
*/
public class AccountContainer {
	private Account account;
	
	/** Default empty constructor.
	*/
	public AccountContainer() {
	}
	
	/** Constructor
	* Instantiates AccountContainer with an account.
	*
	* @param account Account object that will be contained
	*/
	public AccountContainer(Account account) {
		this.account = account;
	}
	
	/** Getter for the Account object that is contained.
	* Throws NoSuchElementException if account is not instantiated.
	*
	* @return Account object that is contained
	*/
	protected Account getAccount() {
		if (account == null) {
			throw new NoSuchElementException();
		}
		
		return account;
	}
	
	/** Setter for the Account object that will be contained.
	*
	* @param account Account object that will be contained
	*/
	protected void setAccount(Account account) {
		if(account == null) {
			throw new NullPointerException();
		}
		
		this.account = account;
	}
}