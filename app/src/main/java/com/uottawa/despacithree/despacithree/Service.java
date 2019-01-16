package com.uottawa.despacithree.despacithree;

/** Class representing a service. Stores the name and hourly wage of the service.
*/
public class Service {
	private String name;
	private double wage;
	
	/** Default empty constructor.
	*/
	public Service() {
	}
	
	/** ServiceProviderAccount constructor used used to instantiate a homeowner account and all its attributes.
	*
	* @param name String representing the name of the service
	* @param wage double representing the hourly wage of the service
	*/
	public Service(String name, double wage) {
		this.name = name;
		this.wage = wage;
	}
	
	/** Getter for the service's name.
	*
	* @return String representing the name of the service
	*/
	public String getName() {
		return name;
	}
	
	/** Getter for the service's hourly wage.
	*
	* @return double representing the hourly wage of the service
	*/
	public double getWage() {
		return wage;
	}
	
	/** Setter for the service's name.
	*
	* @param String representing the name of the service
	*/
	public void setName(String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		
		this.name = name;
	}
	
	/** Setter for the service's hourly wage.
	*
	* @param double representing the hourly wage of the service
	*/
	public void setWage(double wage) {
		this.wage = wage;
	}
	
	/** Static method used to check if a String can be a valid service name.
	* For a name to be valid, it must start with an upper case, and be only composed of letters, spaces and hyphens.
	*
	* @return String representing the name to validate
	*/
	protected static boolean validateServiceName(String name) {
		if (name == null) {
			return false;
		}
		
		String first_regex = "([A-Z]{1}?[a-zA-Z]+)+([ -][A-Z]{1}?[a-zA-Z]+)*";
		boolean b = name.matches(first_regex);
		return b;
	}
	
	/** Static method used to check if a double can be a valid service wage.
	* For a wage to be valid, it must be more than 0 and must not have more than two decimal places.
	*
	* @return double representing the wage to validate
	*/
	protected static boolean validateServiceWage(double wage) {
		return wage > 0 && ((wage*1000)%10) == 0;
	}
}