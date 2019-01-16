package com.uottawa.despacithree.despacithree;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** Class representing an admin account. Is a child of abstract Account.
* Admins can view, add, edit and remove services.
*/
public class AdminAccount extends Account {
    private String accountType = "Admin";

	/** Default empty constructor.
	*/
    public AdminAccount() {
        super();
    }
	
	/** AdminAccount constructor used used to instantiate an admin account and all its attributes.
	*
	* @param id String representing the id where the account is stored in the database
	* @param email String representing the email stored in the account
	* @param firstName String representing the last name of the account's user
	* @param lastName String representing the last name of the account's user
	*/
    public AdminAccount(String id, String email, String firstName, String lastName) {
        super(id, email, firstName, lastName);
    }
	
	/** Getter for the account's type.
	*
	* @return String representing the type of the account
	*/
    public String getAccountType() {
        return accountType;
    }
	
	/** Edits the wage of a service in the database.
	*
	* @param serviceName String representing name of the service to be edited
	* @param double representing the new wage of the service
	*/
    protected void editServiceWage(String serviceName, double serviceWage) {
		if (serviceName == null) {
			throw new NullPointerException();
		}
		
        DatabaseReference serviceWageData = FirebaseDatabase.getInstance().getReference("services/" + serviceName + "/wage");
		
		serviceWageData.setValue(serviceWage);
    }
	
	/** Creates a service and adds it to the database.
	*
	* @param serviceName String representing name of the service to be created
	* @param double representing the wage of the service to be created
	*/
    protected void createService(String serviceName, double serviceWage) {
		if (serviceName == null) {
			throw new NullPointerException();
		}
		
        DatabaseReference serviceData = FirebaseDatabase.getInstance().getReference("services/" + serviceName);
		Service newService = new Service(serviceName, serviceWage);
		
		serviceData.setValue(newService);
    }
	
	/** Removes a service from the database.
	*
	* @param serviceName String representing name of the service to be removed
	*/
    protected void removeService(String serviceName) {
		if (serviceName == null) {
			throw new NullPointerException();
		}
		
		DatabaseReference serviceData = FirebaseDatabase.getInstance().getReference("services/" + serviceName);
		
		serviceData.removeValue();
    }
}
