package com.uottawa.despacithree.despacithree;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/** Class used to create ServiceProviderProfiles.
* Validates given information and creates a Service Provider Profile if all of it is present an valid. 
*/
public class ServiceProviderProfileCreator {
	private DatabaseReference accountProfileData;
	private String address;
	private String province;
	private String city;
	private String postalCode;
	private String phoneNumber;
	private String companyName;
	private String description = "No Description";
	private boolean isLicensed = false;
	
	public ServiceProviderProfileCreator(String accountId) {
		accountProfileData = FirebaseDatabase.getInstance().getReference("accounts/" + accountId + "/profile");
	}
	
	/*** Protected method used to check if the address is valid or not.
     * 
     * @return boolean. True if the address is valid and false otherwise.
     */
    protected boolean validateAddress(){
        if(address == null){
            return false;
        }else{
			String address_regex= "[0-9]+ ([a-zA-Z0-9]*[.-]*[a-zA-Z0-9]*)+[ a-zA-Z]+";
			boolean b = address.matches(address_regex);
			return b;
		}
    }  
	
	/*** Protected method used to check if the province is valid or not.
     * 
     * @return boolean. True if the province is valid and false otherwise.
     */
	protected boolean validateProvince() {
        if (province == null) {
            return false;
		} else {
			String province_regex = "([A-Z]{1}?[a-zA-Z]*)+([ -][A-Z]{1}?[a-zA-Z]*)*";
			boolean b = province.matches(province_regex);
			return b;
        }
    }
	
	/*** Protected method used to check if the city is valid or not.
     * 
     * @return boolean. True if the city is valid and false otherwise.
     */
	protected boolean validateCity() {
        if (city == null) {
            return false;
		}else {
			String city_regex = "([A-Z]{1}?[a-zA-Z]*)+([ -][A-Z]{1}?[a-zA-Z]*)*";
			boolean b = city.matches(city_regex);
			return b;
        }
    }
	
	/*** Protected method used to check if the postal code is valid or not.
     * 
     * @return boolean. True if the postal code is valid and false otherwise.
     */
	protected boolean validatePostalCode() {
        if (postalCode == null) {
            return false;
        }else{
			String postalCode_regex= "([a-zA-Z][0-9]){3}+";
			boolean b = postalCode.matches(postalCode_regex);
			return b;
		}
    }
	
	/*** Protected method used to check if the phone number is valid or not.
     * 
     * @return boolean. True if the phone number is valid and false otherwise.
     */
	protected boolean validatePhoneNumber() {
        if (phoneNumber == null) {
            return false;
        }else{
			String phoneNumber_regex= "[0-9]{10,}+";
			boolean b = phoneNumber.matches(phoneNumber_regex);
			return b;
		}
    }
	
	/*** Protected method used to check if the company name is valid or not.
     * 
     * @return boolean. True if the company name is valid and false otherwise.
     */
    protected boolean validateCompanyName(){
        if(companyName == null){
            return false;
        }else{
			String company_regex= "\\S+[ \\S]*";
			boolean b = companyName.matches(company_regex);
			return b;
		}
    }
	
	/** Setter for the profile's address.
	*
	* @param String representing the address of the profile
	*/
	protected void setAddress(String address){
		if (address == null) {
			throw new NullPointerException();
		}
		
		this.address = address;
    }
	
	/** Setter for the profile's province.
	*
	* @param String representing the province of the profile
	*/
	protected void setProvince(String province){
		if (province == null) {
			throw new NullPointerException();
		}
		
		this.province = province;
    }
	
	/** Setter for the profile's city.
	*
	* @param String representing the city of the profile
	*/
	protected void setCity(String city){
		if (city == null) {
			throw new NullPointerException();
		}
		
		this.city = city;
    }
	
	/** Setter for the profile's postal code.
	*
	* @param String representing the postal code of the profile
	*/
	protected void setPostalCode(String postalCode){
		if (postalCode == null) {
			throw new NullPointerException();
		}
		
		this.postalCode = postalCode;
    }
	
	/** Setter for the profile's phone number.
	*
	* @param String representing the phone number of the profile
	*/
	protected void setPhoneNumber(String phoneNumber){
		if (phoneNumber == null) {
			throw new NullPointerException();
		}
		
		this.phoneNumber = phoneNumber;
    }
	
	/** Setter for the profile's company name.
	*
	* @param String representing the company name of the profile
	*/
	protected void setCompanyName(String companyName){
		if (companyName == null) {
			throw new NullPointerException();
		}
		
		this.companyName = companyName;
    }
	
	/** Setter for the profile's description.
	*
	* @param String representing the description of the profile
	*/
	protected void setDescription(String description){
		if (description == null) {
			throw new NullPointerException();
		}

		String description_regex= "\\s+";
		boolean b = description.matches(description_regex);

		if(b || description.equals("")) {
			this.description = "No Description";
		}
		else {
			this.description = description;
		}
    }
	
	/** Setter for the profile's licensed state.
	*
	* @param boolean representing the account of the profile is licensed
	*/
	protected void setIsLicensed(boolean isLicensed){
		this.isLicensed = isLicensed;
    }
	
	/** Method that creates a profile and adds it to the database, if all the necessary information was given and is valid.
	*
	* @return ServiceProviderProfile that was created
	*/
    protected ServiceProviderProfile createProfile() {
        
		// Creates a profile and adds it to the database, if all the necessary information was given and is valid
		if (validateAddress() && validateProvince() && validateCity() && validatePostalCode() && validatePhoneNumber() && validateCompanyName()){
            ServiceProviderProfile profile = new ServiceProviderProfile(address, province, city, postalCode, phoneNumber, companyName, description, isLicensed);
			
			// Adds profile to database
            accountProfileData.setValue(profile);

            return profile;
        }
		
		
        throw new ProfileCreationFailureException();
    }
}