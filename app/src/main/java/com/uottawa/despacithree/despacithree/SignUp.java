package com.uottawa.despacithree.despacithree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String selectedRole;
    private Account userAccount;
    String errorS;
    AccountCreator accountCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        selectedRole = "";
        errorS = "";
        accountCreator = new AccountCreator();

        Spinner userRole = findViewById(R.id.spinnerRole);
        ArrayAdapter<CharSequence> userRoleAdapter = ArrayAdapter.createFromResource(this, R.array.client_role, android.R.layout.simple_list_item_1);
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRole.setAdapter(userRoleAdapter);
        userRole.setOnItemSelectedListener(this);

        final EditText emailEntry = findViewById(R.id.editEmail02);
        final EditText tokenEntry = findViewById(R.id.editTokenEntry);

        emailEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                accountCreator.setEmail(emailEntry.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tokenEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                accountCreator.setAdminToken(tokenEntry.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String userT = parent.getItemAtPosition(position).toString();
        selectedRole = userT;

        RelativeLayout clientRole = (RelativeLayout)findViewById(R.id.layoutClientRole);
        clientRole.setVisibility(View.VISIBLE);

        LinearLayout activeLayout = null;
        switch (userT){
            case "Homeowner":
                activeLayout = findViewById(R.id.layoutHomeOwner);
                activeLayout.setVisibility(View.VISIBLE);
                activeLayout = findViewById(R.id.layoutServiceProvider);
                activeLayout.setVisibility(View.GONE);
                activeLayout = findViewById(R.id.layoutAdministrator);
                activeLayout.setVisibility(View.GONE);
                break;
            case "Service Provider":
                activeLayout = findViewById(R.id.layoutServiceProvider);
                activeLayout.setVisibility(View.VISIBLE);
                activeLayout = findViewById(R.id.layoutHomeOwner);
                activeLayout.setVisibility(View.GONE);
                activeLayout = findViewById(R.id.layoutAdministrator);
                activeLayout.setVisibility(View.GONE);
                break;
            case "Admin":
                activeLayout = findViewById(R.id.layoutAdministrator);
                activeLayout.setVisibility(View.VISIBLE);
                activeLayout = findViewById(R.id.layoutServiceProvider);
                activeLayout.setVisibility(View.GONE);
                activeLayout = findViewById(R.id.layoutHomeOwner);
                activeLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        RelativeLayout clientRole = (RelativeLayout)findViewById(R.id.layoutClientRole);
        clientRole.setVisibility(View.GONE);
    }

    public void onSignUpClicked(View view){
        String userEmail = ((EditText)findViewById(R.id.editEmail02)).getText().toString();
        String userPass1 = ((EditText)findViewById(R.id.editSignUpPassword00)).getText().toString();
        String userPass2 = ((EditText)findViewById(R.id.editSignUpPassword01)).getText().toString();
        String firstName = ((EditText)findViewById(R.id.editUserName00)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.editUserName01)).getText().toString();
        String tokenEntry = ((EditText)findViewById(R.id.editTokenEntry)).getText().toString();

        TextView errorMessage = (TextView)findViewById(R.id.textErrorSignUp);

        accountCreator.setFirstName(firstName);
        accountCreator.setLastName(lastName);
        accountCreator.setPassword(userPass1);
        accountCreator.setPasswordRetry(userPass2);

        errorS = "";

        errorS = accountCreator.validateEmail() ? errorS : errorS + "Invalid user email address\n";
        errorS = accountCreator.validateAvailableEmail() ? errorS : errorS + "User email address already in use\n";
        errorS = accountCreator.validateFirstName() ? errorS : errorS + "Invalid First Name\n";
        errorS = accountCreator.validateLastName() ? errorS : errorS + "Invalid Last Name\n";
        errorS = accountCreator.validatePassword() ? errorS : errorS + "Invalid password, must be at least 8 characters\n";
        errorS = accountCreator.validatePasswordRetry() ? errorS : errorS + "Password does not match\n";

        accountCreator.setAccountType(selectedRole);

        if (selectedRole.equals("Admin"))
            errorS = accountCreator.validateAdminToken() ? errorS : errorS + "Invalid token";

        userAccount = null;

        try {
            userAccount = accountCreator.createAccount();
            Intent intent;
            switch (userAccount.getAccountType()) {
                case "Admin":
                    intent = new Intent(this, NavigationDrawInterface.class);
                    break;
                case "Service Provider":
                    intent = new Intent(this, ServiceProviderInterface.class);
                    break;
                default:
                    intent = new Intent(this, ClientInterface.class);
            }intent.putExtra("USER_ACCOUNT", userAccount);
            startActivity(intent);
        } catch (AccountCreationFailureException e) {
            errorMessage.setText(errorS);
            errorMessage.setVisibility(View.VISIBLE);
        }

    }
}
