package com.uottawa.despacithree.despacithree;

import java.io.Serializable;

public class WeekAvailability implements Serializable {
	private DayAvailability monday;
	private DayAvailability tuesday;
	private DayAvailability wednesday;
	private DayAvailability thursday;
	private DayAvailability friday;
	private DayAvailability saturday;
	private DayAvailability sunday;
	
	public WeekAvailability() {
	}
	
	public DayAvailability getMonday() {
		return monday;
	}
	
	public DayAvailability getTuesday() {
		return tuesday;
	}
	
	public DayAvailability getWednesday() {
		return wednesday;
	}
	
	public DayAvailability getThursday() {
		return thursday;
	}
	
	public DayAvailability getFriday() {
		return friday;
	}
	
	public DayAvailability getSaturday() {
		return saturday;
	}
	
	public DayAvailability getSunday() {
		return sunday;
	}
	
	public void setMonday(DayAvailability availability) {
		monday = availability;
	}
	
	public void setTuesday(DayAvailability availability) {
		tuesday = availability;
	}
	
	public void setWednesday(DayAvailability availability) {
		wednesday = availability;
	}
	
	public void setThursday(DayAvailability availability) {
		thursday = availability;
	}
	
	public void setFriday(DayAvailability availability) {
		friday = availability;
	}
	
	public void setSaturday(DayAvailability availability) {
		saturday = availability;
	}
	
	public void setSunday(DayAvailability availability) {
		sunday = availability;
	}
}