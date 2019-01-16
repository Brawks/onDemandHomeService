package com.uottawa.despacithree.despacithree;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocalUnitTest {

    @Test
    public void checkDespacithreeBooleanGetValue() {
        DespacithreeBoolean bool = new DespacithreeBoolean(true);
        assertEquals("DespacithreeBoolean getValue", true, bool.getValue());
    }

    @Test
    public void checkDespacithreeBooleanMakeFalse() {
        DespacithreeBoolean bool = new DespacithreeBoolean(true);
		bool.makeFalse();
        assertEquals("DespacithreeBoolean getValue", false, bool.getValue());
    }

	@Test
    public void checkDespacithreeBooleanMakeTrue() {
        DespacithreeBoolean bool = new DespacithreeBoolean(false);
		bool.makeTrue();
        assertEquals("DespacithreeBoolean getValue", true, bool.getValue());
    }

    @Test
    public void checkServiceName() {
        double wage = 420.73;
		Service service = new Service("Test", wage);
        assertEquals("Check service name", "Test", service.getName());
    }
	
	@Test
    public void checkServiceWage() {
        double wage = 420.73;
        double delta = 0.001;
		Service service = new Service("Test", wage);
        assertEquals("Check service wage", wage, service.getWage(), delta);
    }

    @Test
    public void checkValidServiceName() {
        assertEquals("Check valid service name", true, Service.validateServiceName("Plumber"));
    }
	
	@Test
    public void checkInvalidServiceName() {
        assertEquals("Check invalid service name", false, Service.validateServiceName("pl5u%mber"));
    }

    @Test
    public void checkValidServiceWage() {
        double wage = 420.73;
		assertEquals("Check valid service wage", true, Service.validateServiceWage(wage));
    }
	
	@Test
    public void checkInvalidServiceWage() {
        double wage = 420.7389;
		assertEquals("Check invalid service wage", false, Service.validateServiceWage(wage));
    }
	
	@Test
    public void checkServiceProviderProfileCompanyName() {
        ServiceProviderProfile profile = new ServiceProviderProfile();
		profile.setCompanyName("Test Inc");
		assertEquals("Check ServiceProviderProfile companyName", "Test Inc", profile.getCompanyName());
    }
	
	@Test
    public void checkServiceProviderProfileAddress() {
        ServiceProviderProfile profile = new ServiceProviderProfile();
		profile.setAddress("190 Test");
		assertEquals("Check ServiceProviderProfile address", "190 Test", profile.getAddress());
    }
	
	@Test
    public void checkServiceProviderProfileIsLicensed() {
        ServiceProviderProfile profile = new ServiceProviderProfile();
		profile.setIsLicensed(true);
		assertEquals("Check ServiceProviderProfile IsLicensed", true, profile.getIsLicensed());
    }
	
	@Test
    public void checkBookingDay1() {
        Booking booking = new Booking();
		booking.setDay("Monday");
		assertEquals("Check Booking day", "Monday", booking.getDay());
    }
	
	@Test
    public void checkBookingDay2() {
        Booking booking = new Booking();
		booking.setDay("Sunday");
		assertEquals("Check Booking day", "Sunday", booking.getDay());
    }
	
	@Test
    public void checkBookingHomeownerId() {
        Booking booking = new Booking();
		booking.setHomeownerId("test");
		assertEquals("Check Booking homeownerId", "test", booking.getHomeownerId());
    }
	
	@Test
    public void checkBookingServiceProviderId() {
        Booking booking = new Booking();
		booking.setServiceProviderId("test");
		assertEquals("Check Booking serviceProviderId", "test", booking.getServiceProviderId());
    }
	
	@Test
    public void checkBookingServiceProviderName() {
        Booking booking = new Booking();
		booking.setServiceProviderName("test");
		assertEquals("Check Booking serviceProviderName", "test", booking.getServiceProviderName());
    }
	
	@Test
    public void checkRatingRating1() {
        Rating rating = new Rating();
		rating.setRating(3);
		assertEquals("Check Rating rating", 3, rating.getRating());
    }
	
	@Test
    public void checkRatingRating2() {
        Rating rating = new Rating();
		rating.setRating(1);
		assertEquals("Check Rating rating", 1, rating.getRating());
    }
	
	@Test
    public void checkRatingHomeownerId() {
        Rating rating = new Rating();
		rating.setHomeownerId("test");
		assertEquals("Check Rating homeownerId", "test", rating.getHomeownerId());
    }
	
	@Test
    public void checkRatingHomeownerName() {
        Rating rating = new Rating();
		rating.setHomeownerName("test");
		assertEquals("Check Rating homeownerName", "test", rating.getHomeownerName());
    }
	
	@Test
    public void checkRatingComment() {
        Rating rating = new Rating();
		rating.setComment("test");
		assertEquals("Check Rating comment", "test", rating.getComment());
    }
}
