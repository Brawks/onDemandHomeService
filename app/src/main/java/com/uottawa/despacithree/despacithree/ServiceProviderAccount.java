package com.uottawa.despacithree.despacithree;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/** Class representing a service provider account. Is a child of abstract Account.
*/
public class ServiceProviderAccount extends Account{
	private String accountType = "Service Provider";
	private ServiceProviderProfile profile;
	private WeekAvailability availability = new WeekAvailability();
	private float averageRating = 0;
    
	/** Default empty constructor.
	*/
	public ServiceProviderAccount() {
		super();
	}
	
	/** ServiceProviderAccount constructor used used to instantiate a homeowner account and all its attributes.
	*
	* @param id String representing the id where the account is stored in the database
	* @param email String representing the email stored in the account
	* @param firstName String representing the last name of the account's user
	* @param lastName String representing the last name of the account's user
	*/
	public ServiceProviderAccount(String id, String email, String firstName, String lastName){
		super(id, email, firstName, lastName);
    }

    public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public float getAverageRating() {
		return averageRating;
	}

	/** Getter for the account's type.
	*
	* @return String representing the type of the account
	*/
	public String getAccountType(){
		return accountType;
    }
	
	/** Getter for the account's profile.
	*
	* @return ServiceProviderProfile representing the profile of the account
	*/
	public ServiceProviderProfile getProfile(){
		return profile;
    }
	
	public WeekAvailability getAvailability() {
		return availability;
	}
	
	/** Setter for the account's profile.
	*
	* @param ServiceProviderProfile representing the profile of the account
	*/
	public void setProfile(ServiceProviderProfile profile){
		if (profile == null) {
			throw new NullPointerException();
		}
		
		this.profile = profile;
    }
	
	public void setAvailability(WeekAvailability availability) {
		if (availability == null) {
			throw new NullPointerException();
		}
		
		this.availability = availability;
	}
	
	protected void pushAvailability() {
		DatabaseReference availabilityData = FirebaseDatabase.getInstance().getReference("accounts/" + getId() + "/availability");
		
		availabilityData.setValue(availability);
	}
	
	/** Add a service to the service provider.
	*
	* @param serviceName String representing name of the service
	*/
    protected synchronized void addService(final String serviceName) {
		if (serviceName == null) {
			throw new NullPointerException();
		}
		
		final DatabaseReference servicesDatabase = FirebaseDatabase.getInstance().getReference("services");
		
		// Queries the database for the service and, if the service exists, adds the service provider to its list of service providers
		servicesDatabase.orderByKey().equalTo(serviceName).addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				servicesDatabase.child(serviceName).child("serviceProviders").child(getId()).setValue(getId());
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
	
	/** Remove a service from the service provider.
	*
	* @param serviceName String representing name of the service
	*/
    protected void removeService(String serviceName) {
		if (serviceName == null) {
			throw new NullPointerException();
		}
		
		DatabaseReference serviceData = FirebaseDatabase.getInstance().getReference("services/" + serviceName + "/serviceProviders/" + getId());
		
		serviceData.removeValue();
    }
}
