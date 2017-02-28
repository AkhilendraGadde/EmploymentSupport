package com.charolia.gadde.ess;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    String username,password;
    EditText etUsername,etPassword;
    Button bLogin;
    TextView tvRegisterHere;
    boolean isValid;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_collapsetoolbar);

       /* CollapsingToolbarLayout collapsingToolbarLayout;

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);

            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.user_name));
            //dynamicToolbarColor();
            //toolbarTextAppernce();

        */


        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkFields();
                if(isValid)
                    logUserIn();
            }
        });
    }

    private void checkFields() {

        String valid = "";
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.equals("")) {
            etUsername.setError("Username field cannot be empty.");
            etUsername.requestFocus();
            valid = "no";
        }

        if (password.equals("")) {
            etPassword.setError("Password field cannot be empty.");
            etPassword.requestFocus();
            valid = "no";
        }

        if(!valid.equals("no")){
            isValid = true;
        }
    }

    private void logUserIn()    {
        final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Logging in", "Please wait...", false, false);
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean(Config.LOGIN_SUCCESS);

                    if (success) {
                        // Fetching user details from server......
                        String name = jsonResponse.getString("name");
                        String username = jsonResponse.getString("username");
                        String email = jsonResponse.getString("email");
                        //String password = jsonResponse.getString("password");
                        String phone = jsonResponse.getString("phone");
                        String type = jsonResponse.getString("type");

                        //Creating a shared preference
                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //Adding values to editor
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                        editor.putString(Config.NAME_SHARED_PREF, name);
                        editor.putString(Config.USERNAME_SHARED_PREF, username);
                        //editor.putString(Config.PASSWORD_SHARED_PREF, password);
                        editor.putString(Config.EMAIL_SHARED_PREF, email);
                        editor.putString(Config.PHONE_SHARED_PREF, phone);
                        editor.putString(Config.TYPE_SHARED_PREF, type);

                        //Saving values to editor
                        editor.commit();

                        loading.dismiss();
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(intent);
                    } else {
                        loading.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the User Activity
            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
