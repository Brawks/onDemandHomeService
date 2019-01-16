package com.uottawa.despacithree.despacithree;

/** Object based representation of a boolean.
*/
public class DespacithreeBoolean {
	private boolean value;
	
	/** Constructor
	* Instantiates DespacithreeBoolean with a boolean value.
	*
	* @param value boolean value representing the boolean state of DespacithreeBoolean
	*/
	public DespacithreeBoolean(boolean value) {
		this.value = value;
	}
	
	/** Sets the boolean value to true.
	*/
	protected void makeTrue() {
		value = true;
	}
	
	/** Sets the boolean value to false.
	*/
	protected void makeFalse() {
		value = false;
	}
	
	/** Getter method for the boolean value.
	*
	* @return boolean representing the boolean state of DespacithreeBoolean
	*/
	protected boolean getValue() {
		return value;
	}
}