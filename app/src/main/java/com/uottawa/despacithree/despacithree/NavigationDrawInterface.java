package com.uottawa.despacithree.despacithree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class NavigationDrawInterface extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AdminAccount userAccount;
    private List<Service> servList;
    private List<Account> userList;
    private ListView serviceView, userView;
	private DatabaseReference servicesDatabase = FirebaseDatabase.getInstance().getReference("services");
    private TextView programTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_draw_interface);

        servList = new ArrayList<Service>();
        userList = new ArrayList<Account>();

        serviceView = (ListView) findViewById(R.id.servicesList);
        userView = (ListView) findViewById(R.id.usersList);
        programTitle = (TextView) findViewById(R.id.textServUser);

        Intent intent = getIntent();
        userAccount = (AdminAccount) intent.getSerializableExtra("USER_ACCOUNT");

        TextView userName = findViewById(R.id.textWelcomeUser1);
        userName.setText(userName.getText().toString() + userAccount.getFirstName());

        TextView userRole = findViewById(R.id.textAccountType1);
        userRole.setText(userRole.getText().toString() + userAccount.getAccountType());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                showCreateUpdateDialog();
            }
        });

        serviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service myS = servList.get(position);
                showEditUpdateDialog(myS.getName());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void showCreateUpdateDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AppBarOverlay));
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_service_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView create_error = (TextView) dialogView.findViewById(R.id.create_ServiceError);
        final EditText create_SName = (EditText) dialogView.findViewById(R.id.create_serviceNameE);
        final EditText create_SWage = (EditText) dialogView.findViewById(R.id.create_servicePriceE);

        final Button create_button = (Button) dialogView.findViewById(R.id.btncreate_service);
        final Button cancel_button = (Button) dialogView.findViewById(R.id.btnCancel_service);
        final StringBuilder errorS = new StringBuilder();

        dialogBuilder.setTitle("Service Creation");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorS.setLength(0);
                if (create_SName.getText().toString().equals("") || !Service.validateServiceName(create_SName.getText().toString())){
                    errorS.append("Invalid service name. (Words must start with an upper case, and be only composed of letters, spaces and hyphens.)\n");
                }

                if (create_SWage.getText().toString().equals("") || !Service.validateServiceWage(Double.parseDouble(create_SWage.getText().toString()))){
                    errorS.append("Invalid wage. Must be over 0 $/hour.\n");
                }

                for (int i = 0; i < servList.size(); i++){
                    if(servList.get(i).getName().equals(create_SName.getText().toString())) {
                        errorS.append("Service name already exists.\n");
                        break;
                    }
                }

                if (!errorS.toString().equals("")){
                    create_error.setText(errorS.toString());
                } else {
                    userAccount.createService(create_SName.getText().toString(), Double.parseDouble(create_SWage.getText().toString()));
                    b.dismiss();
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

    private void showEditUpdateDialog(final String servName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_DayNight_DarkActionBar));
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_service_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView create_error = (TextView) dialogView.findViewById(R.id.create_ServiceError);
        final EditText create_SWage = (EditText) dialogView.findViewById(R.id.create_servicePriceE);

        final Button apply_button = (Button) dialogView.findViewById(R.id.btnApply_service);
        final Button cancel_button = (Button) dialogView.findViewById(R.id.btnCancel_service);
        final Button delete_button = (Button) dialogView.findViewById(R.id.btnDelete_service);
        final StringBuilder errorS = new StringBuilder();

        dialogBuilder.setTitle(servName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorS.setLength(0);
                if (create_SWage.getText().toString().equals("") || !Service.validateServiceWage(Double.parseDouble(create_SWage.getText().toString()))){
                    errorS.append("Invalid wage. Must be over 0 $/hour.\n");
                }

                if (!errorS.toString().equals("")){
                    create_error.setText(errorS.toString());
                } else {
                    userAccount.editServiceWage(servName, Double.parseDouble(create_SWage.getText().toString()));
                    b.dismiss();
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccount.removeService(servName);
                b.dismiss();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
		
		servicesDatabase.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				servList.clear();
				
				for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
					servList.add(postSnapshot.getValue(Service.class));
				}

				ServiceList listOservices = new ServiceList(NavigationDrawInterface.this, servList);

				serviceView.setAdapter(listOservices);
				
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {
				
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
        getMenuInflater().inflate(R.menu.navigation_draw_interface, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_services) {
            // Handle the services action
            programTitle.setText("Services: ");
            serviceView.setVisibility(View.VISIBLE);
            userView.setVisibility(View.GONE);
        } else if (id == R.id.nav_users) {
            programTitle.setText("Users: ");
            serviceView.setVisibility(View.GONE);
            userView.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
