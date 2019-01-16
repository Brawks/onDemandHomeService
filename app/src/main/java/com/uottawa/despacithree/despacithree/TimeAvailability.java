package com.uottawa.despacithree.despacithree;

import java.io.Serializable;

public class TimeAvailability implements Serializable {
	private int hours = 0;
	private int minutes = 0;
	
	public TimeAvailability() {
	}
	
	public TimeAvailability(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public void setHours(int hours) {
		this.hours = hours;
	}
	
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	public int compareTo(TimeAvailability time) {
		if (hours != time.getHours()) {
			return hours - time.getHours();
		}
		
		return minutes - time.getMinutes();
	}
	
	public String toString() {
		String time = "";
		
		if (hours < 10) {
			time = time + "0" + hours;
		}
		else {
			time = time + hours;
		}
		
		if (minutes < 10) {
			time = time + ":0" + minutes;
		}
		else {
			time = time + ":" + minutes;
		}
		
		return time;
	}
}