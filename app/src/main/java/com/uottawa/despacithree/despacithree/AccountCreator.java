package com.uottawa.despacithree.despacithree;

import com.google.common.hash.Hashing;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.nio.charset.StandardCharsets;

/** Class used to create accounts.
* Validates given information and creates an account if all of it is present an valid. 
*/
public class AccountCreator {
	
	private DatabaseReference accountsDatabase = FirebaseDatabase.getInstance().getReference("accounts");
	private DatabaseReference tokensDatabase = FirebaseDatabase.getInstance().getReference("adminTokens");
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String accountType;
    private String passwordRetry;
	private String adminToken;
	private DespacithreeBoolean adminTokenIsAvailable = new DespacithreeBoolean(false);
	private DespacithreeBoolean emailIsAvailable = new DespacithreeBoolean(true);
	
	/** Method that checks if the token is valid.
	*
	* @return boolean that is true if the token exists and is not tied to an account and false otherwise
	*/
    protected boolean validateAdminToken() {
		if (adminToken == null) {
            return false;
        }
		
		return adminTokenIsAvailable.getValue();
	}
	
	/*** Protected method used to check if the email is valid or not.
	* For an email to be valid, it must be composed of characters, followed by a period, followed by characters, 
	* followed by @, followed characters, followed by a period, followed by characters
     * 
     * @return boolean. True if the email is valid and false otherwise.
     */
	protected boolean validateEmail() {

        if (email == null) {
            return false;
        } else {
            String email_regex = "[a-zA-Z0-9]+(.[a-zA-Z0-9]+)*?@([a-zA-Z0-9]+.)+?[a-zA-Z]+";
            boolean b = email.matches(email_regex);
            return b;
        }
    }
	
	/** Method that checks if the email is already tied to an account.
	*
	* @return boolean that is true if the email is not tied to an account and false if the email is tied to an account
	*/
	protected boolean validateAvailableEmail() {
		return emailIsAvailable.getValue();
	}
	
	/*** Protected method used to check if the first name is valid or not.
	* For a first name to be valid, it must start with an upper case, and be only composed of letters, spaces and hyphens.
     * 
     * @return boolean. True if the first name is valid and false otherwise.
     */
        protected boolean validateFirstName() {
        if (firstName == null) {
            return false;
        } else {
            String first_regex = "([A-Z]{1}?[a-zA-Z]*)+([ -][A-Z]{1}?[a-zA-Z]*)*";
            boolean b = firstName.matches(first_regex);
            return b;
        }
    }
	
	/*** Protected method used to check if the last name is valid or not.
	* For a last name to be valid, it must start with an upper case, and be only composed of letters, spaces and hyphens.
     * 
     * @return boolean. True if the last name is valid and false otherwise.
     */
        protected boolean validateLastName() {
        if (lastName == null) {
            return false;
        } else {
            String last_regex = "([A-Z]{1}?[a-zA-Z]*)+([ -][A-Z]{1}?[a-zA-Z]*)*";
            boolean b = lastName.matches(last_regex);
            return b;
        }
    }
	
	/*** Protected method used to check if the password is valid or not.
	* For a password to be valid, it must be at least eight characters.
     * 
     * @return boolean. True if the password is valid and false otherwise.
     */
    protected boolean validatePassword() {
        if (password == null) {
            return false;
        } else {
            String password_regex = "[a-zA-Z0-9_]{8,}?";
            boolean b = password.matches(password_regex);
            return b;
        }
    }
	
	/*** Protected method used to check if the password entered the second time is identical to when it was entered the first time.
     * 
     * @return boolean. True if they are identical and false otherwise.
     */
    protected boolean validatePasswordRetry() {
        if (passwordRetry == null) {
            return false;
        }

        return password.equals(passwordRetry);
    }
	
	/** Setter method for the admin token.
	* Also queries the database and checks if the token is exists and is not tied to an account.
	*
	* @param String representing the admin token
	*/
	protected synchronized void setAdminToken(String adminToken) {
		if (adminToken == null) {
			throw new NullPointerException();
		}
		
		this.adminToken = adminToken;
		
		adminTokenIsAvailable.makeFalse();
		
		// Queries the database to find if the token exists and is linked to an account 
        tokensDatabase.orderByKey().equalTo(adminToken).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if ( !(dataSnapshot.child("linkedAccount").exists()) ) {
					
                    adminTokenIsAvailable.makeTrue(); // Set to true if the token exists and is not linked to an account
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
	}
	
	/** Setter method for the email.
	* Also queries the database and checks if the email is already linked to an account.
	*
	* @param String representing the email
	*/
    protected synchronized void setEmail(String email) {
        if (email == null) {
			throw new NullPointerException();
		}
		
		this.email = email.toLowerCase();
		
		emailIsAvailable.makeTrue();
		
		// Queries the database to find if the email is already linked to an account
		accountsDatabase.orderByChild("email").equalTo(this.email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                
				emailIsAvailable.makeFalse(); // Set to false if the is already linked to an account
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
	
	/** Setter method for the password.
	*
	* @param String representing the password
	*/
    protected void setPassword(String password) {
		if (password == null) {
			throw new NullPointerException();
		}
		
        this.password = password;
    }
	
	/** Setter method for the password retry.
	*
	* @param String representing the password retry
	*/
    protected void setPasswordRetry(String passwordRetry) {
		if (passwordRetry == null) {
			throw new NullPointerException();
		}
		
        this.passwordRetry = passwordRetry;
    }
	
	/** Setter method for the first name.
	*
	* @param String representing the first name
	*/
    protected void setFirstName(String firstName) {
		if (firstName == null) {
			throw new NullPointerException();
		}
		
        this.firstName = firstName;
    }
	
	/** Setter method for the last name.
	*
	* @param String representing the last name
	*/
    protected void setLastName(String lastName) {
		if (lastName == null) {
			throw new NullPointerException();
		}
		
        this.lastName = lastName;
    }
	
	/** Setter method for the account type.
	*
	* @param String representing the account type
	*/
    protected void setAccountType(String accountType) {
		if (accountType == null) {
			throw new NullPointerException();
		}
		
        this.accountType = accountType;
    }
	
	/** Method that creates an account and adds it to the database, if all the necessary information was given and is valid.
	*
	* @return Account that was created
	*/
    protected Account createAccount() {
        
		// Creates an account and adds it to the database, if all the necessary information was given and is valid
		if (validateEmail() && validatePassword() && validatePasswordRetry() &&
        validateFirstName() && validateFirstName() && validateAvailableEmail() && validateLastName() && ( !(accountType.equals("Admin")) || validateAdminToken())){
            Account newAccount = null;
            String id = accountsDatabase.push().getKey();
			
			// Creates an instance of the proper account class
            if (accountType.equals("Homeowner")) {
                newAccount = new HomeownerAccount(id, email, firstName, lastName);
            } else if (accountType.equals("Service Provider")) {
                newAccount = new ServiceProviderAccount(id, email, firstName, lastName);
            } else if (accountType.equals("Admin")) {
                newAccount = new AdminAccount(id, email, firstName, lastName);
            }
			
			// Adds account to database
            accountsDatabase.child(id).setValue(newAccount);
            accountsDatabase.child(id).child("password").setValue(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
			if (accountType.equals("Admin")) {
				accountsDatabase.child(id).child("adminToken").setValue(adminToken);
				
				tokensDatabase.child(adminToken).child("linkedAccount").setValue(id);
			}

            return newAccount;
        }
		
		// Throws AccountCreationFailureException if account could not be created
        throw new AccountCreationFailureException();
    }
}