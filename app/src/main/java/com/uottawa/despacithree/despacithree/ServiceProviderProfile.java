package com.uottawa.despacithree.despacithree;

import java.io.Serializable;

/** Class representing a service provider profile.
*/
public class ServiceProviderProfile implements Serializable {
	private String address;
	private String province;
	private String city;
	private String postalCode;
	private String phoneNumber;
	private String companyName;
	private String description;
	private boolean isLicensed;
    
	/** Default empty constructor.
	*/
	public ServiceProviderProfile() {
	}
	
	/** ServiceProviderProfile constructor used used to instantiate a service provider profile and all its attributes.
	*
	* @param address String representing the address of the account that this profile is attached to
	* @param province String representing the province of the account that this profile is attached to
	* @param city String representing the city of the account that this profile is attached to
	* @param postalCode String representing the postal code of the account that this profile is attached to
	* @param phoneNumber String representing the phone number of the account that this profile is attached to
	* @param companyName String representing the company name of the account that this profile is attached to
	* @param description String representing the description of the account that this profile is attached to
	* @param isLicensed boolean that is true if the account that this profile is attached to is licensed and false otherwise
	*/
	public ServiceProviderProfile(String address, String province, String city, String postalCode, String phoneNumber, String companyName, String description, boolean isLicensed) {
		this.address = address;
		this.province = province;
		this.city = city;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
		this.companyName = companyName;
		this.description = description;
		this.isLicensed = isLicensed;
    }
    
	/** Getter for the profile's address.
	*
	* @return String representing the address of the profile
	*/
	public String getAddress(){
		return address;
    }
	
	/** Getter for the profile's province.
	*
	* @return String representing the province of the profile
	*/
	public String getProvince(){
		return province;
    }
	
	/** Getter for the profile's city.
	*
	* @return String representing the city of the profile
	*/
	public String getCity(){
		return city;
    }
	
	/** Getter for the profile's postal code.
	*
	* @return String representing the postal code of the profile
	*/
	public String getPostalCode(){
		return postalCode;
    }
	
	/** Getter for the profile's phone number.
	*
	* @return String representing the phone number of the profile
	*/
	public String getPhoneNumber(){
		return phoneNumber;
    }
	
	/** Getter for the profile's company name.
	*
	* @return String representing the company name of the profile
	*/
	public String getCompanyName(){
		return companyName;
    }
	
	/** Getter for the profile's description.
	*
	* @return String representing the description of the profile
	*/
	public String getDescription(){
		return description;
    }
	
	/** Getter for the profile's licensed state.
	*
	* @return boolean representing the account of the profile is licensed
	*/
	public boolean getIsLicensed(){
		return isLicensed;
    }
	
	/** Setter for the profile's address.
	*
	* @param String representing the address of the profile
	*/
	public void setAddress(String address){
		if (address == null) {
			throw new NullPointerException();
		}
		
		this.address = address;
    }
	
	/** Setter for the profile's province.
	*
	* @param String representing the province of the profile
	*/
	public void setProvince(String province){
		if (province == null) {
			throw new NullPointerException();
		}
		
		this.province = province;
    }
	
	/** Setter for the profile's city.
	*
	* @param String representing the city of the profile
	*/
	public void setCity(String city){
		if (city == null) {
			throw new NullPointerException();
		}
		
		this.city = city;
    }
	
	/** Setter for the profile's postal code.
	*
	* @param String representing the postal code of the profile
	*/
	public void setPostalCode(String postalCode){
		if (postalCode == null) {
			throw new NullPointerException();
		}
		
		this.postalCode = postalCode;
    }
	
	/** Setter for the profile's phone number.
	*
	* @param String representing the phone number of the profile
	*/
	public void setPhoneNumber(String phoneNumber){
		if (phoneNumber == null) {
			throw new NullPointerException();
		}
		
		this.phoneNumber = phoneNumber;
    }
	
	/** Setter for the profile's company name.
	*
	* @param String representing the company name of the profile
	*/
	public void setCompanyName(String companyName){
		if (companyName == null) {
			throw new NullPointerException();
		}
		
		this.companyName = companyName;
    }
	
	/** Setter for the profile's description.
	*
	* @param String representing the description of the profile
	*/
	public void setDescription(String description){
		if (description == null) {
			throw new NullPointerException();
		}
		
		this.description = description;
    }
	
	/** Setter for the profile's licensed state.
	*
	* @param boolean representing the account of the profile is licensed
	*/
	public void setIsLicensed(boolean isLicensed){
		this.isLicensed = isLicensed;
    }
}
