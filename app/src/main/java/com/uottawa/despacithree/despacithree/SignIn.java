package com.uottawa.despacithree.despacithree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {

    SignInManager signInManger = new SignInManager();
    Account userAccount;
    EditText emailT, passT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInManger = new SignInManager();
        emailT = findViewById(R.id.editEmail01);
        passT = findViewById(R.id.editPassword00);

        emailT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signInManger.signInAccount(emailT.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        signInManger.signInAccount(emailT.getText().toString());
    }

    //ALTERNATE METHOD::
	/*
	Have a setAccount() Method in this class that is protected
	SignInManager has a constructor that takes "this" object has parameter (passed onCreate)
	Every time the editText updates on the listener, signInManger.signInAccount is called (the method calls another function to changed execute setAccount() on "this" object
	When button is pressed, only check if null
	 */

    public void onLoginbtn(View view) {
        String email, password = "";

        email = emailT.getText().toString();

        password = passT.getText().toString();

        //Call Login from Account with email and password Strings
        if (email.equals("") || password.equals("")) {
            Toast errorSign = Toast.makeText(getApplicationContext(), "Invalid Email/Password entered", Toast.LENGTH_LONG);
            errorSign.show();
        } else {
            try {
                userAccount = signInManger.signIn(email, password);
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
                }
                intent.putExtra("USER_ACCOUNT", userAccount);
                startActivity(intent);
                userAccount = null;
            } catch (SignInFailureException e) {
                Toast errorSign = Toast.makeText(getApplicationContext(), "Invalid Email/Password entered", Toast.LENGTH_LONG);
                errorSign.show();
            }
        }
    }

    public void onSignUpbtn(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}
