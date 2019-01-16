package com.uottawa.despacithree.despacithree;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/** Class representing a homeowner account. Is a child of abstract Account.
*/
public class HomeownerAccount extends Account{

	private static class IntContainer implements Serializable {
		private int value = 0;

		private IntContainer() {}

		private void add(int addInt) {
			value += addInt;
		}

		private int getValue() {
			return value;
		}
	}

	private String accountType = "Homeowner";
    
	/** Default empty constructor.
	*/
	public HomeownerAccount() {
		super();
	}
	
	/** HomeownerAccount constructor used used to instantiate a homeowner account and all its attributes.
	*
	* @param id String representing the id where the account is stored in the database
	* @param email String representing the email stored in the account
	* @param firstName String representing the last name of the account's user
	* @param lastName String representing the last name of the account's user
	*/
	public HomeownerAccount(String id, String email, String firstName, String lastName){
		super(id, email, firstName, lastName);
    }
	
    /** Getter for the account's type.
	*
	* @return String representing the type of the account
	*/
	public String getAccountType(){
		return accountType;
    }

    public synchronized void pushRating(String serviceProviderId, String comment, int rating) {
		if (serviceProviderId == null || comment == null) {
			throw new NullPointerException();
		}

		final DatabaseReference serviceProviderData = FirebaseDatabase.getInstance().getReference("accounts/" + serviceProviderId);
		serviceProviderData.child("ratings").child(serviceProviderData.push().getKey()).setValue(new Rating(getId(), getFullName(), comment, rating));

		final IntContainer sumOfRatings = new IntContainer();
		final IntContainer numOfRatings = new IntContainer();

		serviceProviderData.child("ratings").orderByKey().addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				numOfRatings.add(1);
				sumOfRatings.add(dataSnapshot.child("rating").getValue(Integer.class).intValue());

				serviceProviderData.child("averageRating").setValue((float) sumOfRatings.getValue()/numOfRatings.getValue());
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

	public void pushBooking(Booking booking) {
		if (booking == null) {
			throw new NullPointerException();
		}

		DatabaseReference bookingsDatabase = FirebaseDatabase.getInstance().getReference("bookings");
		bookingsDatabase.child(bookingsDatabase.push().getKey()).setValue(booking);
	}
}
