package com.charolia.gadde.ess;

/**
 * Created by Administrator on 2/9/2017.
 */

public class Config {
    public static final String OTP_URL = "https://akhilendragadde17.000webhostapp.com/sendsms.php";
    public static final String REGISTER_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/nRegister.php";
    public static final String LOGIN_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/nnLogin.php";

    //Keys as defined in our $_POST['key'] in login.php & register.php
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TYPE = "type";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TYPE_ID = "type_id";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_OTP = "otp";
    public static final String KEY_OTP_VERIFY = "otp_verified";
    public static final String KEY_DOB = "dob";
    public static final String KEY_LOC = "location";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "UserLoginActivity";

    //This would be used to store current logged in user
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String NAME_SHARED_PREF = "name";
    public static final String USERNAME_SHARED_PREF = "username";
    public static final String TYPE_SHARED_PREF = "type";
    public static final String PASSWORD_SHARED_PREF = "password";
    //public static final String TYPEID_SHARED_PREF = "type_id";
    public static final String PHONE_SHARED_PREF = "phone";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
    /*
            @snack bar styles !
            Snackbar snackbar = Snackbar
                .make(view, "Replace with your own action" , Snackbar.LENGTH_SHORT);
            snackbar.show();

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).show();

            Snackbar.make(getWindow().getDecorView().getRootView(), "Replace with your own action", Snackbar.LENGTH_SHORT).show();

            View parentLayout = findViewById(R.id.activity_register);
            Snackbar.make(parentLayout, "Replace with your own action", Snackbar.LENGTH_SHORT).show();
    */

/*
    private void login(){

        final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Logging in", "Please wait...", false, false);
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.USERNAME_SHARED_PREF, username);

                            //Saving values to editor
                            editor.commit();

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(intent);
                        }else{
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Login Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                            View parentLayout = findViewById(R.id.activity_login);
                            Snackbar.make(parentLayout, "Invalid username or password", Snackbar.LENGTH_LONG).show();
                            //Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_USERNAME, username);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/

/*
        Fetch from svr  : DO in LOGIN
        String name = jsonResponse.getString("name");
        intent.putExtra("name", name);

        Put in userAct
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        tv.setText(name);
 */