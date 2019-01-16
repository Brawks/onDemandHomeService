package com.uottawa.despacithree.despacithree;

import com.google.common.hash.Hashing;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.lang.StringBuilder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

/** Class used to sign in an account.
* Given an email and password, it searches the database for the account and validates 
* its authentication if the account exists and the correct password was given. 
*/
public class SignInManager {
    private DatabaseReference accountsDatabase = FirebaseDatabase.getInstance().getReference("accounts");
    private AccountContainer accountContainer = new AccountContainer();
	private StringBuilder userPass = new StringBuilder();
	
	/** Given an email, this method finds the account tied to the email and, if it exists, stores the account information.
	*
	* @param email String representing the email of the account
	*/
    protected synchronized void signInAccount(String email) {
        if (email == null) {
			throw new NullPointerException();
		}
		
		// Resets the AccountContainer and StringBuilder
		accountContainer = new AccountContainer();
		userPass = new StringBuilder();

		// Queries the database for an account containing the given email
		accountsDatabase.orderByChild("email").equalTo(email.toLowerCase()).addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				String accountType = dataSnapshot.child("accountType").getValue(String.class);
				userPass.append(dataSnapshot.child("password").getValue(String.class));
				
				// If an account with the email is found, stores the account information in an account object and puts it in the AccountContainer
				if (accountType.equals("Admin")) {
					accountContainer.setAccount(dataSnapshot.getValue(AdminAccount.class));
				}
				else if (accountType.equals("Service Provider")) {
					accountContainer.setAccount(dataSnapshot.getValue(ServiceProviderAccount.class));
				}
				else if (accountType.equals("Homeowner")) {
						accountContainer.setAccount(dataSnapshot.getValue(HomeownerAccount.class));
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
	
	/** Given an email and a password, this method validates and signs in the account if the given information matches the information stored.
	*
	* @param email String representing the email of the account that wants to sign in
	* @param password String representing the password of the account that wants to sign in
	*/
    protected Account signIn(String email, String password){
        if (email == null || password == null) {
			throw new NullPointerException();
		}
		
		try {
			// If the validation is passed, the account is signed in.
			if (accountContainer.getAccount().getEmail().equals(email.toLowerCase()) && userPass.toString().equals(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())){
				return accountContainer.getAccount();
			} else {
				// If the validation is not passed SignInFailureException is thrown.
				throw new SignInFailureException();
			}
		}
		catch (NoSuchElementException e) {
			throw new SignInFailureException();
		}
    }
}