package com.charolia.gadde.ess;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {

    String name,username,password,email,phone,dob,location,gender,otp;
    String type;
    EditText etName,etUsername,etPassword,etrPassword,etEmail,etPhone,etDOB,etLocation;
    ImageView ivDate;
    Button bRegister;
    boolean isValidUser = false;
    Spinner spGender;
    RadioGroup rg;
    RadioButton rb;
    int rButtonId = 0;
    Calendar myCalendar = Calendar.getInstance();
    private final int seconds = 15;
    private Timer timer;
    private ProgressDialog loading;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rg = (RadioGroup) findViewById(R.id.rGrp);
        spGender = (Spinner) findViewById(R.id.spGender);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etrPassword = (EditText) findViewById(R.id.etrPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etDOB = (EditText) findViewById(R.id.etDOB);
        ivDate  = (ImageView) findViewById(R.id.ivDate);
        bRegister = (Button) findViewById(R.id.bRegister);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isConnectedToInternet();
                if(isConnectedToInternet()) {
                    validateUser();
                    if (isValidUser) {
                        confirmOtp();
                    }
                }   else {
                    Toast.makeText(RegisterActivity.this,"No Internet Connection found",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null)
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }

        }
        return false;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDOB.setText(sdf.format(myCalendar.getTime()));
    }

    public void rbClick(View view){
        rButtonId = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(rButtonId);

        Snackbar.make(view, "Registering as : "+rb.getText().toString(), Snackbar.LENGTH_SHORT).show();

        if(rb.getText().toString().equals("Job Seeker")){
            type = rb.getText().toString();
        }else if(rb.getText().toString().equals("Recruiter")){
            type = rb.getText().toString();
        }
    }

    private void validateUser(){

        String isValid = "";

        boolean name = checkName(etName.getText().toString());
        boolean pass = checkPass(etrPassword.getText().toString());
        boolean passLen = passLength(etrPassword.getText().toString());
        boolean nameLen = nameLength(etName.getText().toString());
        boolean unameLen = nameLength(etUsername.getText().toString());
        boolean loc = checkLoc(etLocation.getText().toString());
        boolean phone = isNumeric(etPhone.getText().toString());
        boolean isTenDigits = isPhone(etPhone.getText().toString());
        boolean email = isValidEmail(etEmail.getText().toString());

        // Check Type
        if(rButtonId == 0){
            View parentLayout = findViewById(R.id.activity_register);
            Snackbar.make(parentLayout, "Please select registration type", Snackbar.LENGTH_LONG).show();
            isValid = "no";
        }
        // Check gender
        if(spGender.getSelectedItem().toString().equals("Gender")){
            View view = spGender.getSelectedView();
            setError("Select appropriate Gender", view);
            isValid = "no";
        }
        // Check DOB
        if(etDOB.getText().toString().equals("")){
            etDOB.setError("Please select your date of birth.");
            etDOB.requestFocus();
            isValid = "no";
        }
        // Check valid phone info
        if (etPhone.getText().toString().equals("")) {
            etPhone.setError("Please enter your phone number.");
            etPhone.requestFocus();
            isValid = "no";
        }else if(!phone){
            etPhone.setError("Only Numbers (0-9) are allowed");
            etPhone.requestFocus();
            isValid = "no";
        }else if(!isTenDigits){
            etPhone.setError("Phone number must be of 10 digits.");
            etPhone.requestFocus();
            isValid = "no";
        }
        // check location
        if (etLocation.getText().toString().equals("")) {
            etLocation.setError("Please enter your Location.");
            etLocation.requestFocus();
            isValid = "no";
        }else if(!loc){
            etLocation.setError("Enter proper location.");
            etLocation.requestFocus();
            isValid = "no";
        }
        // check email
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError("Please enter your email-id.");
            etEmail.requestFocus();
            isValid = "no";
        }else if(!email){
            etEmail.setError("Invalid email-id.");
            etEmail.requestFocus();
            isValid = "no";
        }
        // Check passwords
        if (etrPassword.getText().toString().equals("")) {
            etrPassword.setError("Please re-enter your password.");
            etrPassword.requestFocus();
            isValid = "no";
        }else if(!pass){
            etrPassword.setError("Passwords don't match.");
            etrPassword.requestFocus();
            isValid = "no";
        }
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError("Please enter a password.");
            etPassword.requestFocus();
            isValid = "no";
        }else if(!passLen){
            etPassword.setError("Password length must be greater than 6 characters.");
            etPassword.requestFocus();
            isValid = "no";
        }
        // Check username
        if (etUsername.getText().toString().equals("")) {
            etUsername.setError("Please enter a username.");
            etUsername.requestFocus();
            isValid = "no";
        }else if(!unameLen){
            etName.setError("Minimum length should be 3 character");
            etName.requestFocus();
            isValid = "no";
        }
        // Check name
        if (etName.getText().toString().equals("")) {
            etName.setError("Please enter your name.");
            etName.requestFocus();
            isValid = "no";
        }else if(!name) {
            etName.setError("Only Alphabets (A-Z or a-z) are allowed");
            etName.requestFocus();
            isValid = "no";
        }else if(!nameLen){
            etName.setError("Minimum length should be 3 character");
            etName.requestFocus();
            isValid = "no";
        }

        // Check if valid
        if(!isValid.equals("no")){
            isValidUser = true;
        }

    }
    public void setError(String errorMessage, View view)
    {
        TextView tvListItem = (TextView)view;
        if(errorMessage != null)
        {
            tvListItem.setError(errorMessage);
            tvListItem.requestFocus();
        }
        else
            tvListItem.setError(null);
    }

    boolean checkName(String name) {
        for(char c : name.toCharArray())
        {
            if(Character.isDigit(c))
                return false;
        }
        return true;
    }

    boolean checkLoc(String name) {
        for(char c : name.toCharArray())
        {
            if(Character.isDigit(c))
                return false;
        }
        return true;
    }

    boolean checkPass(String rpass)  {
        if(rpass.equals(etPassword.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }

    boolean passLength(String len) {
        if(len.length() < 6)
            return false;
        else
            return true;
    }

    boolean nameLength(String len) {
        if(len.length() < 3)
            return false;
        else
            return true;
    }

    boolean isNumeric(String str) {
        for(char c : str.toCharArray())
        {
            if(Character.isDigit(c)){}
            else
                return false;
        }
        return true;
    }

    boolean isPhone(String str) {
        if(str.length() != 10)
            return false;
        else
            return true;
    }

    boolean isValidEmail(String mail) {
        try {
                String emailregex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                Boolean b = mail.matches(emailregex);
                if (!b)
                    return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void registerUser(String otp){
        loading = ProgressDialog.show(RegisterActivity.this, "Registering", "Please wait...", false, false);

        timer = new Timer();
        timer.schedule(new Notify(),seconds*1000);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                loading.dismiss();
                Toast.makeText(RegisterActivity.this, "Connection Timed Out!", Toast.LENGTH_LONG).show();
            }
        };

        name = etName.getText().toString();
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        dob = etDOB.getText().toString();
        location = etLocation.getText().toString();
        gender = spGender.getSelectedItem().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Register Response",response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        timer.cancel();
                        loading.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        RegisterActivity.this.finish();
                    }else{
                        timer.cancel();
                        loading.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Register Failed")
                                .setCancelable(false)
                                .setNegativeButton("Retry",null)
                                .create()
                                .show();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Username or email or phone no already exists", Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest regReq = new RegisterRequest(name, username, password, email, gender, phone, dob, location, type, otp, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(regReq);

    }

    private class Notify extends TimerTask  {

        @Override
        public void run() {
            Message message = mHandler.obtainMessage(seconds);
            message.sendToTarget();
            timer.cancel();
        }
    }

    public void confirmOtp() {
        final Context mContext = this;
        phone = etPhone.getText().toString();
        Random r = new Random(System.currentTimeMillis());
        otp = String.valueOf((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

        int val = Integer.parseInt(otp);
        if(type.equals("Job Seeker"))
            val = val * 10 + 1;
        else
            val = val * 10 + 2;
        otp = Integer.toString(val);

        Log.d("OTP",otp);
        Toast.makeText(getApplicationContext(),"Your otp is :" + otp,Toast.LENGTH_LONG).show();
        RequestOtp req;
        req = new RequestOtp(phone, otp, getApplicationContext());
        req.execute((Void) null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.otp_dialog, (ViewGroup) findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        alertDialog.setView(viewInflated);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String OTP = input.getText().toString();
                if (OTP.equals(otp)) {
                    Toast.makeText(getApplicationContext(), "Phone number verification successfull.", Toast.LENGTH_LONG).show();
                    registerUser(OTP);
                } else {

                    Toast.makeText(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Resend new OTP to you mobile number?")
                           .setCancelable(false)
                           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    confirmOtp();
                                }
                           })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   // updatePhone();
                                    dialog.cancel();
                                }
                           })
                            .create()
                            .show();
                }
            }
        });
        alertDialog.show();
    }


    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }
}
