package com.uottawa.despacithree.despacithree;

import java.io.Serializable;

/** Abstract class representing an account. Stores basic account info.
*/
public abstract class Account implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
	private String id;
   
	/** Default empty constructor.
	*/
    public Account() {
	}
	
	/** Account constructor used used to instantiate an account and all its attributes.
	*
	* @param id String representing the id where the account is stored in the database
	* @param email String representing the email stored in the account
	* @param firstName String representing the last name of the account's user
	* @param lastName String representing the last name of the account's user
	*/
	public Account(String id, String email, String firstName, String lastName){
        this.id =id;
		this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
    }
    
	/** Getter for the account's id.
	*
	* @return String representing the id where the account is stored in the database
	*/
	public String getId(){
        return id;
    }
	
	/** Getter for the account's email.
	*
	* @return String representing the email stored in the account
	*/
    public String getEmail(){
        return this.email;
    }
    
	/** Getter for the account's first name.
	*
	* @return String representing the first name of the account's user
	*/
	public String getFirstName(){
        return this.firstName;
    }
    
	/** Getter for the account's last name.
	*
	* @return String representing the last name of the account's user
	*/
	public String getLastName(){
        return this.lastName;
    }
	
	/** Getter for the account's full name.
	*
	* @return String representing the full name of the account's user
	*/
	protected String getFullName(){
        return (firstName + " " + lastName);
    }
    
	/** Abstract getter for the account's type. To be implemented by children of Account
	*
	* @return String representing the type of the account
	*/
	public abstract String getAccountType();
	
	/** Setter for the account's id.
	*
	* @param String representing the id where the account is stored in the database
	*/
	public void SetId(String id){
		if(id == null) {
			throw new NullPointerException();
		}
		
        this.id = id;
    }
	
	/** Setter for the account's email.
	*
	* @param String representing the email stored in the account
	*/
    public void SetEmail(String email){
		if(email == null) {
			throw new NullPointerException();
		}
		
        this.email = email;
    }
    
	/** Setter for the account's first name.
	*
	* @param String representing the first name of the account's user
	*/
	public void SetFirstName(String firstName){
		if(firstName == null) {
			throw new NullPointerException();
		}
		
        this.firstName = firstName;
    }
    
	/** Setter for the account's last name.
	*
	* @param String representing the last name of the account's user
	*/
	public void SetLastName(String lastName){
		if(lastName == null) {
			throw new NullPointerException();
		}
		
        this.lastName = lastName;
    }
}
