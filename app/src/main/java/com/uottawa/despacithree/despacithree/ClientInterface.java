package com.uottawa.despacithree.despacithree;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientInterface extends AppCompatActivity {

    private HomeownerAccount userAccount;

    private List<Service> servList;
    private DatabaseReference servicesDatabase = FirebaseDatabase.getInstance().getReference("services");
    private ListView serviceView;

    private List<Booking> bookList;
    private DatabaseReference bookingsDatabase = FirebaseDatabase.getInstance().getReference("bookings");
    private ListView bookingView;

    private ListView SPACCOUNTS;
    private List<ServiceProviderAccount> spList;
    private DatabaseReference spDatabase = FirebaseDatabase.getInstance().getReference("accounts");

    private List<Rating> ratingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_interface);

        Intent intent = getIntent();
        userAccount = (HomeownerAccount) intent.getSerializableExtra("USER_ACCOUNT");

        TextView userName = findViewById(R.id.textWelcomeUser);
        userName.setText(userName.getText().toString() + userAccount.getFirstName());

        TextView userRole = findViewById(R.id.textAccountType);
        userRole.setText(userRole.getText().toString() + userAccount.getAccountType());

        servList = new ArrayList<Service>();
        serviceView = (ListView) this.findViewById(R.id.userServiceList);
        bookList = new ArrayList<Booking>();
        bookingView = (ListView) this.findViewById(R.id.bookingList);
        spList = new ArrayList<ServiceProviderAccount>();
        SPACCOUNTS = this.findViewById(R.id.userServicePList);
        ratingList = new ArrayList<Rating>();

        findViewById(R.id.btnAddBooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbtnAddBooking();
            }
        });

        findViewById(R.id.btnCancelSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onbtnCancelService();
            }
        });


    }

    private void onbtnAddBooking(){
        //Display Services
        findViewById(R.id.homeOwnerLayout).setVisibility(View.GONE);
        findViewById(R.id.homeOwnerServices).setVisibility(View.VISIBLE);
    }

    private void onSelectService(final Service service){
        //Display Service Providers by rating/time per Day
        findViewById(R.id.homeOwnerServices).setVisibility(View.GONE);
        findViewById(R.id.homeOwnerSP).setVisibility(View.VISIBLE);

        final StringBuilder selectedDay = new StringBuilder();
        final StringBuilder selectedRating = new StringBuilder();
        final Button cancel = (Button) this.findViewById(R.id.btnCancelSelectP);
        final Spinner day = (Spinner) this.findViewById(R.id.timeServiceSpinner);
        final Spinner rating = (Spinner) this.findViewById(R.id.ratingServiceSpinner);

        selectedDay.append("All");
        selectedRating.append("All");
        fillSortSPList("All", "All", service);

        ArrayAdapter<CharSequence> days = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_list_item_1);
        days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(days);

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay.setLength(0);
                selectedDay.append(parent.getItemAtPosition(position).toString());
                fillSortSPList(selectedDay.toString(), selectedRating.toString(), service);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> ratings = ArrayAdapter.createFromResource(this, R.array.ratings, android.R.layout.simple_list_item_1);
        ratings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating.setAdapter(ratings);
        rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRating.setLength(0);
                selectedRating.append(parent.getItemAtPosition(position).toString());
                fillSortSPList(selectedDay.toString(), selectedRating.toString(), service);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.homeOwnerServices).setVisibility(View.VISIBLE);
                findViewById(R.id.homeOwnerSP).setVisibility(View.GONE);
            }
        });

        //ListView listener
        SPACCOUNTS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogShowSPProfile(spList.get(position), service.getName(), selectedDay.toString());
            }
        });
    }

    private void fillSortSPList(final String currentDay, final String currentRating, Service service){
        servicesDatabase.child(service.getName()).child("serviceProviders").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> serviceProviders = new ArrayList<String>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                   serviceProviders.add(postSnapshot.getValue(String.class));
                }

                spDatabase.orderByChild("averageRating").startAt(currentRating.equals("All") ? 0.0 : Float.parseFloat(currentRating)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        spList.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (serviceProviders.indexOf(postSnapshot.getKey()) != -1 && postSnapshot.child("profile").exists() && postSnapshot.child("availability").exists() && (currentDay.equals("All") || postSnapshot.child("availability").child(currentDay.toLowerCase()).exists())) {
                                spList.add(postSnapshot.getValue(ServiceProviderAccount.class));
                            }
                        }
                        ServiceProviderList listOfSp = new ServiceProviderList(ClientInterface.this, spList, !currentDay.equals("All"), currentDay);
                        SPACCOUNTS.setAdapter(listOfSp);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void dialogShowSPProfile (final ServiceProviderAccount provider, final String servicename, final String curDay){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_select_serviceprovider_user, null);
        dialogBuilder.setView(dialogView);

        final TextView companyName = dialogView.findViewById(R.id.companyName);
        final TextView isLicensed = dialogView.findViewById(R.id.isLicensed);
        final TextView phoneNumber = dialogView.findViewById(R.id.servicePPhone);
        final TextView address = dialogView.findViewById(R.id.servicePAddress);
        final TextView description = dialogView.findViewById(R.id.servicePDescription);
        ServiceProviderProfile userProfile = provider.getProfile();
        Log.i("Test", "profile");
        if (userProfile != null) {
            companyName.setText("Company Name: " + userProfile.getCompanyName() + "\n");
            String license = "";
            license = userProfile.getIsLicensed() ? "Yes" : "No";
            isLicensed.setText("Licensed: " + license + "\n");
            phoneNumber.setText("Phone Number: " + userProfile.getPhoneNumber() + "\n");
            address.setText("Address: " + userProfile.getAddress() + ", " + userProfile.getCity() + ", " + userProfile.getProvince() + ", " + userProfile.getPostalCode() + "\n");
            description.setText("Description:\n" + userProfile.getDescription());
            dialogBuilder.setTitle(provider.getFullName());
        }

        final ListView rating = dialogView.findViewById(R.id.listViewRatingsSP);
        final ScrollView profile = dialogView.findViewById(R.id.scrollViewProfileSP);
        final ToggleButton showRT = dialogView.findViewById(R.id.btnShowRatingSP);
        final Button select = (Button) dialogView.findViewById(R.id.btnSelectSP);
        final Button cancel = (Button) dialogView.findViewById(R.id.btnCancelSelectSP);
        final ArrayList<Rating> rateList = new ArrayList<>();

        //Query to Fill ListView w ratings

        final DatabaseReference ratingsDatabase = FirebaseDatabase.getInstance().getReference("accounts/" + provider.getId() + "/ratings");

        ratingsDatabase.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rateList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    rateList.add(postSnapshot.getValue(Rating.class));
                }

                RatingList listOfRatings= new RatingList(ClientInterface.this, rateList);

                rating.setAdapter(listOfRatings);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final AlertDialog b = dialogBuilder.create();

        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        b.show();


        showRT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rating.setVisibility(View.VISIBLE);
                    profile.setVisibility(View.GONE);
                } else {
                    rating.setVisibility(View.GONE);
                    profile.setVisibility(View.VISIBLE);
                }
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                dialogueCreateBooking(servicename, provider, curDay);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    private void dialogueCreateBooking(final String service, final ServiceProviderAccount provider, final String curDay){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_create_booking, null);
        dialogBuilder.setView(dialogView);

        final StringBuilder selectedDay = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        final Spinner day = (Spinner) dialogView.findViewById(R.id.createBookingDay);
        final TextView spDay = (TextView) dialogView.findViewById(R.id.createBookingSPAvailability);
        final TimePicker startT = (TimePicker) dialogView.findViewById(R.id.timePickerStart);
        final TimePicker endT = (TimePicker) dialogView.findViewById(R.id.timePickerEnd);
        final TextView errorS = (TextView) dialogView.findViewById(R.id.availabilityError);
        final Button book = (Button) dialogView.findViewById(R.id.btnApplyTimePicker);
        final Button cancel = (Button) dialogView.findViewById(R.id.btnCancelTimePicker);
        final AlertDialog b = dialogBuilder.create();

        startT.setIs24HourView(true);
        endT.setIs24HourView(true);

        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        b.show();

        selectedDay.append("Monday");
        spDay.setText("Availability: " + provider.getAvailability().getMonday());

        ArrayAdapter<CharSequence> days = ArrayAdapter.createFromResource(this, R.array.weekday, android.R.layout.simple_list_item_1);
        days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(days);
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay.setLength(0);
                selectedDay.append(parent.getItemAtPosition(position).toString());
                if (provider.getAvailability() != null) {
                    switch (selectedDay.toString()){
                        case "Monday":
                            spDay.setText("Availability: " + (provider.getAvailability().getMonday() == null ? "Not available" : provider.getAvailability().getMonday()));
                            break;
                        case "Tuesday":
                            spDay.setText("Availability: " + (provider.getAvailability().getTuesday() == null ? "Not available" : provider.getAvailability().getTuesday()));
                            break;
                        case "Wednesday":
                            spDay.setText("Availability: " + (provider.getAvailability().getWednesday() == null ? "Not available" : provider.getAvailability().getWednesday()));
                            break;
                        case "Thursday":
                            spDay.setText("Availability: " + (provider.getAvailability().getThursday() == null ? "Not available" : provider.getAvailability().getThursday()));
                            break;
                        case "Friday":
                            spDay.setText("Availability: " + (provider.getAvailability().getFriday() == null ? "Not available" : provider.getAvailability().getFriday()));
                            break;
                        case "Saturday":
                            spDay.setText("Availability: " + (provider.getAvailability().getSaturday() == null ? "Not available" : provider.getAvailability().getSaturday()));
                            break;
                        case "Sunday":
                            spDay.setText("Availability: " + (provider.getAvailability().getSunday() == null ? "Not available" : provider.getAvailability().getSunday()));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setLength(0);
                TimeAvailability start = new TimeAvailability(startT.getHour(), startT.getMinute());
                TimeAvailability end = new TimeAvailability(endT.getHour(), endT.getMinute());
                Booking newBooking = new Booking();
                if (start.compareTo(end) < 0) {
                } else {
                    error.append("Invalid times picked\n");
                }

                newBooking.setDay(selectedDay.toString());
                newBooking.setStartTime(start);
                newBooking.setEndTime(end);
                newBooking.setHomeownerId(userAccount.getId());
                newBooking.setService(service);
                newBooking.setServiceProviderId(provider.getId());
                if (provider.getProfile() != null){
                    newBooking.setServiceProviderName(provider.getFullName());
                }

                DayAvailability servicePDay = new DayAvailability();
                switch (selectedDay.toString()){
                    case "Monday":
                        servicePDay = provider.getAvailability().getMonday();
                        break;
                    case "Tuesday":
                        servicePDay = provider.getAvailability().getTuesday();
                        break;
                    case "Wednesday":
                        servicePDay = provider.getAvailability().getWednesday();
                        break;
                    case "Thursday":
                        servicePDay = provider.getAvailability().getThursday();
                        break;
                    case "Friday":
                        servicePDay = provider.getAvailability().getFriday();
                        break;
                    case "Saturday":
                        servicePDay = provider.getAvailability().getSaturday();
                        break;
                    case "Sunday":
                        servicePDay = provider.getAvailability().getSunday();
                        break;
                }


                error.append(newBooking.validateBookingTime(servicePDay) == true ? "" : "Time is out of bounds for selected day on Service Provider's Availability.\n");

                if (error.toString().equals("")){
                    userAccount.pushBooking(newBooking);
                    b.dismiss();
                } else {
                    errorS.setText(error.toString());
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

    private void onbtnCancelService(){
        findViewById(R.id.homeOwnerServices).setVisibility(View.GONE);
        findViewById(R.id.homeOwnerLayout).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        servicesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    servList.add(postSnapshot.getValue(Service.class));
                }

                ServiceList listOservices = new ServiceList(ClientInterface.this, servList);

                serviceView.setAdapter(listOservices);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        serviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Service myS = servList.get(position);
                    onSelectService(myS);
            }
        });

        bookingsDatabase.orderByChild("homeownerId").equalTo(userAccount.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    bookList.add(postSnapshot.getValue(Booking.class));
                }

                BookingList listOfBookings = new BookingList(ClientInterface.this, bookList);

                bookingView.setAdapter(listOfBookings);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bookingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking myB = bookList.get(position);
                onBookingClick(myB);
            }
        });

    }

    private void onBookingClick(final Booking booking){
        //Dialog: Rate Service Provider
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_rate_serviceprovider, null);
        dialogBuilder.setView(dialogView);

        final Spinner rating = (Spinner) dialogView.findViewById(R.id.rateServiceSpinner);
        final EditText comment = (EditText) dialogView.findViewById(R.id.commentRateSP);
        final Button apply = (Button) dialogView.findViewById(R.id.btnRateSP);
        final Button cancel = (Button) dialogView.findViewById(R.id.btnCancelRateSP);
        final StringBuilder selectedRating = new StringBuilder();


        ArrayAdapter<CharSequence> ratings = ArrayAdapter.createFromResource(this, R.array.rating, android.R.layout.simple_list_item_1);
        ratings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rating.setAdapter(ratings);
        rating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRating.setLength(0);
                selectedRating.append(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogBuilder.setTitle(booking.getServiceProviderName());
        final AlertDialog b = dialogBuilder.create();

        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        b.show();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Apply Rating
                userAccount.pushRating(booking.getServiceProviderId(), comment.getText().toString(), Integer.parseInt(selectedRating.toString()));
                b.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }
}
