package com.uottawa.despacithree.despacithree;

public class Booking extends DayAvailability {
	private String day;
	private String homeownerId;
	private String serviceProviderId;
	private String serviceProviderName;
	private String service;
	
	public Booking() {
		super();
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		if (service == null) {
			throw new NullPointerException();
		}

		this.service = service;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		if (day == null) {
			throw new NullPointerException();
		}

		this.day = day;
	}

	public String getHomeownerId() {
		return homeownerId;
	}

	public void setHomeownerId(String homeownerId) {
		if (homeownerId == null) {
			throw new NullPointerException();
		}

		this.homeownerId = homeownerId;
	}

	public String getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(String serviceProviderId) {
		if (serviceProviderId == null) {
			throw new NullPointerException();
		}

		this.serviceProviderId = serviceProviderId;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		if (serviceProviderName == null) {
			throw new NullPointerException();
		}

		this.serviceProviderName = serviceProviderName;
	}

	protected boolean validateBookingTime(DayAvailability availability) {
		if (availability == null) {
			return false;
		}

		if (getStartTime() == null || getEndTime() == null) {
			return false;
		}

		return availability.getStartTime().compareTo(getStartTime()) <= 0 && availability.getEndTime().compareTo(getEndTime()) >= 0;
	}

	public String toString() {
		return day + ": " + getStartTime().toString() + " to " + getEndTime().toString();
	}
}