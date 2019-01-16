package com.uottawa.despacithree.despacithree;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ServiceProviderInterface extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static ServiceProviderAccount userAccount;
    private List<Service> servList;
    private ListView serviceView;
    private DatabaseReference servicesDatabase = FirebaseDatabase.getInstance().getReference("services");

    private ConstraintLayout serviceLayout;
    private LinearLayout availableLayout;
    private ScrollView profileLayout;

    private DayAvailability currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_interface);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        servList = new ArrayList<Service>();
        serviceView = (ListView) this.findViewById(R.id.servicesPList);
        profileLayout = this.findViewById(R.id.viewProfileContent);
        serviceLayout = this.findViewById(R.id.viewMyServices);
        availableLayout = this.findViewById(R.id.viewAvailabilities);

        Intent intent = getIntent();
        userAccount = (ServiceProviderAccount) intent.getSerializableExtra("USER_ACCOUNT");

        TextView userName = this.findViewById(R.id.textWelcomeUser);
        userName.setText(userName.getText().toString() + userAccount.getFirstName());

        TextView userRole = this.findViewById(R.id.textAccountType);
        userRole.setText(userRole.getText().toString() + userAccount.getAccountType());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddServiceDialog();
            }
        });
        fab.setVisibility(View.GONE);

        serviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service myS = servList.get(position);
                showRemoveServiceDialog(myS.getName());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupServiceP();
    }

    private void setupServiceP() {
        //FOR NOW CREATE DIALOG

        ServiceProviderProfile userProfile;
        userProfile = userAccount.getProfile();

        if (userProfile != null) {
            displayInfoService(userProfile);
        }
        else {
            showProfileDialog(false);
        }
    }

    private void showProfileDialog(boolean isCancel) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.service_provider_profiledialog, null);
        dialogBuilder.setView(dialogView);

        final ServiceProviderProfileCreator createProfile = new ServiceProviderProfileCreator(userAccount.getId());

        final TextView create_error = (TextView) dialogView.findViewById(R.id.create_ProfileError);
        final EditText create_CName = (EditText) dialogView.findViewById(R.id.create_CompanyNameE);
        final EditText create_CAddLine = (EditText) dialogView.findViewById(R.id.create_CompanyAddressLineE);
        final EditText create_CCity = (EditText) dialogView.findViewById(R.id.create_CompanyAddressCityE);
        final EditText create_CPostal = (EditText) dialogView.findViewById(R.id.create_CompanyAddressE);
        final EditText create_CPhone = (EditText) dialogView.findViewById(R.id.create_CompanyPhoneE);
        final EditText create_CDescription = (EditText) dialogView.findViewById(R.id.create_CompanyDescription);
        final ToggleButton create_CLicense = (ToggleButton) dialogView.findViewById(R.id.create_toggleLicense);
        final StringBuilder selectedProvince = new StringBuilder();


        final Spinner userProvinces = dialogView.findViewById(R.id.create_CompanyAddressProvinceE);
        ArrayAdapter<CharSequence> userProvinceAdapter = ArrayAdapter.createFromResource(this, R.array.provinces, android.R.layout.simple_list_item_1);
        userProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userProvinces.setAdapter(userProvinceAdapter);
        userProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince.setLength(0);
                selectedProvince.append(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Button create_button = (Button) dialogView.findViewById(R.id.btnCreate_profile);
        final Button cancel_button = (Button) dialogView.findViewById(R.id.btnCancel_profile);

        dialogBuilder.setTitle("Service Provider Profile");
        final AlertDialog b = dialogBuilder.create();

        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);

        if (isCancel) {
            cancel_button.setVisibility(View.VISIBLE);
            b.setCancelable(true);
            b.setCanceledOnTouchOutside(true);
        }

        b.show();

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset error message

                //Set all tags
                createProfile.setCompanyName(create_CName.getText().toString());
                createProfile.setAddress(create_CAddLine.getText().toString());
                createProfile.setProvince(selectedProvince.toString());
                createProfile.setCity(create_CCity.getText().toString());
                createProfile.setPostalCode(create_CPostal.getText().toString());
                createProfile.setPhoneNumber(create_CPhone.getText().toString());
                createProfile.setDescription(create_CDescription.getText().toString());
                createProfile.setIsLicensed(create_CLicense.isChecked());

                //Validate all fields
                String errorMsg = "";

                errorMsg = createProfile.validateCompanyName() ? errorMsg : errorMsg + "Invalid Company Name\n";
                errorMsg = createProfile.validateAddress() ? errorMsg : errorMsg + "Invalid Address Line\n";
                errorMsg = createProfile.validateCity() ? errorMsg : errorMsg + "Invalid City\n";
                errorMsg = createProfile.validatePostalCode() ? errorMsg : errorMsg + "Invalid Postal Code\n";
                errorMsg = createProfile.validatePhoneNumber() ? errorMsg : errorMsg + "Invalid Phone Number (Numbers only)";

                try {
                    ServiceProviderProfile providerProfile;
                    providerProfile = createProfile.createProfile();
                    userAccount.setProfile(providerProfile);
                    b.dismiss();
                    displayInfoService(providerProfile);
                } catch (ProfileCreationFailureException e) {
                    create_error.setText(errorMsg);
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    private void displayInfoService(ServiceProviderProfile userProfile) {

        TextView companyName = findViewById(R.id.companyName);
        TextView isLicensed = findViewById(R.id.isLicensed);
        TextView phoneNumber = findViewById(R.id.servicePPhone);
        TextView address = findViewById(R.id.servicePAddress);
        TextView description = findViewById(R.id.servicePDescription);

        companyName.setText("Company Name: " + userProfile.getCompanyName() + "\n");
        String license = "";
        license = userProfile.getIsLicensed() ? "Yes" : "No";
        isLicensed.setText("Licensed: " + license + "\n");
        phoneNumber.setText("Phone Number: " + userProfile.getPhoneNumber() + "\n");
        address.setText("Address: " + userProfile.getAddress() + ", " + userProfile.getCity() + ", " + userProfile.getProvince() + ", " + userProfile.getPostalCode() + "\n");
        description.setText("Description:\n" + userProfile.getDescription());
    }

    private void showRemoveServiceDialog(final String servName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.serviceprovider_removedialog, null);
        dialogBuilder.setView(dialogView);

        final Button cancelT = dialogView.findViewById(R.id.btnCancel_Remove);
        final Button removeT = dialogView.findViewById(R.id.btnRemove_ServToP);

        final AlertDialog b = dialogBuilder.create();
        b.setTitle(servName);
        b.show();

        cancelT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        removeT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccount.removeService(servName);
                b.dismiss();
            }
        });
    }

    private void showAddServiceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.service_list_provider, null);
        dialogBuilder.setView(dialogView);

        final ArrayList<Service> servList = new ArrayList<>();
        final ListView serviceView = dialogView.findViewById(R.id.listViewServices);

        final Button cancel_add = dialogView.findViewById(R.id.btnCancelServiceToP);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        servicesDatabase.orderByChild("serviceProviders/" + userAccount.getId()).equalTo(null).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    servList.add(postSnapshot.getValue(Service.class));
                }

                ServiceList listOservices = new ServiceList(ServiceProviderInterface.this, servList);

                serviceView.setAdapter(listOservices);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        serviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service curS = servList.get(position);
                userAccount.addService(curS.getName());
                b.dismiss();
            }
        });

        cancel_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_provider_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void invokeTimePick(final TextView currentTextView, final String flagDay) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.availability_timepicker, null);
        dialogBuilder.setView(dialogView);

        final TimePicker startT = dialogView.findViewById(R.id.timePickerStart);
        final TimePicker endT = dialogView.findViewById(R.id.timePickerEnd);

        startT.setIs24HourView(true);
        endT.setIs24HourView(true);

        final Button apply = dialogView.findViewById(R.id.btnApplyTimePicker);
        final Button none = dialogView.findViewById(R.id.btnUnavailableTimePicker);
        final Button cancel = dialogView.findViewById(R.id.btnCancelTimePicker);

        final TextView error = dialogView.findViewById(R.id.availabilityError);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeAvailability start = new TimeAvailability(startT.getHour(), startT.getMinute());
                TimeAvailability end = new TimeAvailability(endT.getHour(), endT.getMinute());

                if (start.compareTo(end) < 0) {
                    currentDay = new DayAvailability(start,end);
                    currentTextView.setText(flagDay + ": " + currentDay.toString());
                    setFlagOnDay(flagDay);
                    b.dismiss();
                } else {
                    error.setText("Invalid times picked");
                }
            }
        });

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = null;
                currentTextView.setText(flagDay + ": " + "No Availability");
                setFlagOnDay(flagDay);
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

    private void setFlagOnDay(String flag){
        switch (flag){
            case "Monday":
                userAccount.getAvailability().setMonday(currentDay);
                break;
            case "Tuesday":
                userAccount.getAvailability().setTuesday(currentDay);
                break;
            case "Wednesday":
                userAccount.getAvailability().setWednesday(currentDay);
                break;
            case "Thursday":
                userAccount.getAvailability().setThursday(currentDay);
                break;
            case "Friday":
                userAccount.getAvailability().setFriday(currentDay);
                break;
            case "Saturday":
                userAccount.getAvailability().setSaturday(currentDay);
                break;
            case "Sunday":
                userAccount.getAvailability().setSunday(currentDay);
                break;
        }
        userAccount.pushAvailability();
    }

    private void setListenersforWeekdays(){
        final TextView monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        final Button submit;

        monday = findViewById(R.id.mondayAvailability);
        tuesday = findViewById(R.id.tuesdayAvailability);
        wednesday = findViewById(R.id.wednesdayAvailability);
        thursday = findViewById(R.id.thursdayAvailability);
        friday = findViewById(R.id.fridayAvailability);
        saturday = findViewById(R.id.saturdayAvailability);
        sunday = findViewById(R.id.sundayAvailability);

        monday.setText("Monday: " + (userAccount.getAvailability().getMonday() == null ? "No Availability" : userAccount.getAvailability().getMonday().toString()));
        tuesday.setText("Tuesday: " + (userAccount.getAvailability().getTuesday() == null ? "No Availability" : userAccount.getAvailability().getTuesday().toString()));
        wednesday.setText("Wednesday: " + (userAccount.getAvailability().getWednesday() == null ? "No Availability" : userAccount.getAvailability().getWednesday().toString()));
        thursday.setText("Thursday: " + (userAccount.getAvailability().getThursday() == null ? "No Availability" : userAccount.getAvailability().getThursday().toString()));
        friday.setText("Friday: " + (userAccount.getAvailability().getFriday() == null ? "No Availability" : userAccount.getAvailability().getFriday().toString()));
        saturday.setText("Saturday: " + (userAccount.getAvailability().getSaturday() == null ? "No Availability" : userAccount.getAvailability().getSaturday().toString()));
        sunday.setText("Sunday: " + (userAccount.getAvailability().getSunday() == null ? "No Availability" : userAccount.getAvailability().getSunday().toString()));

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getMonday();
                invokeTimePick(monday, "Monday");
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getTuesday();
                invokeTimePick(tuesday, "Tuesday");
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getWednesday();
               invokeTimePick(wednesday, "Wednesday");
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getThursday();
                invokeTimePick(thursday, "Thursday");
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getFriday();
                invokeTimePick(friday, "Friday");
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getSaturday();
                invokeTimePick(saturday, "Saturday");
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = userAccount.getAvailability().getSunday();
                invokeTimePick(sunday, "Sunday");
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_services) {
            // Handle the services action
            serviceLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.GONE);
            availableLayout.setVisibility(View.GONE);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_available) {
            availableLayout.setVisibility(View.VISIBLE);
            serviceLayout.setVisibility(View.GONE);
            profileLayout.setVisibility(View.GONE);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.GONE);
            setListenersforWeekdays();

        } else if (id == R.id.nav_profile) {
            profileLayout.setVisibility(View.VISIBLE);
            serviceLayout.setVisibility(View.GONE);
            availableLayout.setVisibility(View.GONE);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.GONE);

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();

        servicesDatabase.orderByChild("serviceProviders/" + userAccount.getId()).equalTo(userAccount.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    servList.add(postSnapshot.getValue(Service.class));
                }

                ServiceList listOservices = new ServiceList(ServiceProviderInterface.this, servList);

                serviceView.setAdapter(listOservices);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
