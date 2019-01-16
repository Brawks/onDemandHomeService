package com.uottawa.despacithree.despacithree;

import java.io.Serializable;

public class DayAvailability implements Serializable {
	private TimeAvailability startTime;
	private TimeAvailability endTime;
	
	public DayAvailability() {
	}
	
	public DayAvailability(TimeAvailability startTime, TimeAvailability endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public TimeAvailability getStartTime() {
		return startTime;
	}
	
	public TimeAvailability getEndTime() {
		return endTime;
	}
	
	public void setStartTime(TimeAvailability startTime) {
		if (startTime == null) {
			throw new NullPointerException();
		}
		
		this.startTime = startTime;
	}
	
	public void setEndTime(TimeAvailability endTime) {
		if (endTime == null) {
			throw new NullPointerException();
		}
		
		this.endTime = endTime;
	}
	
	public String toString() {
		return startTime.toString() + " to " + endTime.toString();
	}
}