package com.charolia.gadde.ess.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.charolia.gadde.ess.LoginActivity;
import com.charolia.gadde.ess.RequestOtp;
import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;
import com.charolia.gadde.ess.UserActivity;
import com.charolia.gadde.ess.util.Encrypt;
import com.charolia.gadde.ess.util.OnSwipeTouchListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

    private Context context;
    private String name, email, type,phone,pass,uid;
    private TextView tvName,tvType,tvEmail,tvUpdatePhone,tvUpdateEmail;
    private ExpandableLayout expandableLayout;
    private ImageView expandButton,image_phone,image_email;
    private EditText text_phone,text_email,text_oldpass,text_newpass,text_newpass_r;
    private Button bLogout;
    private CardView cardView;
    boolean isValidEntry = false;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        type = sharedPreferences.getString(Config.TYPE_SHARED_PREF,"Not Available");
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF,"Not Available");
        pass = sharedPreferences.getString(Config.PASSWORD_SHARED_PREF,"Not Available");
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        text_phone        = (EditText) view.findViewById(R.id.etPhone_home);
        text_email       = (EditText) view.findViewById(R.id.etEmail_home);

        text_phone.setText(phone);
        text_email.setText(email);
        text_phone.setFocusable(false);
        text_email.setFocusable(false);


        expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
        expandButton = (ImageView) view.findViewById(R.id.editProf);
        expandableLayout.setOnExpansionUpdateListener(this);
        expandButton.setOnClickListener(this);
        cardView = (CardView) view.findViewById(R.id.card_profile);
        cardView.setOnTouchListener( new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeDown()   {
                expandableLayout.expand();
            }

            @Override
            public void onSwipeUp() {
                expandableLayout.collapse();
            }
        });

        tvName = (TextView) view.findViewById(R.id.tName);
        tvType = (TextView) view.findViewById(R.id.tType);
        tvEmail = (TextView) view.findViewById(R.id.tEmail);

        tvName.setText(name);
        tvType.setText(type);
        tvEmail.setText(email);

        tvUpdatePhone = (TextView) view.findViewById(R.id.tv_upUpdatePhone);
        image_phone = (ImageView) view.findViewById(R.id.confirm_phone);
        tvUpdateEmail = (TextView) view.findViewById(R.id.tv_upUpdateEmail);
        image_email = (ImageView) view.findViewById(R.id.confirm_email);

        tvUpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_phone.setFocusable(true);
                text_phone.setFocusableInTouchMode(true);

                image_phone.setVisibility(View.VISIBLE);
            }
        });

        image_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateDetails("phone");
                if (isValidEntry) {
                    UpdatePhone();
                }
            }
        });

        tvUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_email.setFocusable(true);
                text_email.setFocusableInTouchMode(true);

                image_email.setVisibility(View.VISIBLE);
            }
        });

        image_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateDetails("email");
                if (isValidEntry) {
                    UpdateEmail();
                }
            }
        });


        text_oldpass = (EditText) view.findViewById(R.id.etcPass);
        text_newpass = (EditText) view.findViewById(R.id.etnPass);
        text_newpass_r = (EditText) view.findViewById(R.id.etnrPass);

        bLogout = (Button) view.findViewById(R.id.bLogoutYes);
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"You will need to login again after this action.",Snackbar.LENGTH_LONG).show();
                ValidateDetails("password");
                if (isValidEntry) {
                    UpdatePass();
                }
            }
        });

        return view;
    }

    private void ValidateDetails(String str)   {

        String isValid = "";
        boolean email = isValidEmail(text_email.getText().toString());
        boolean pass = checkPass(text_newpass_r.getText().toString());
        boolean passLen = passLength(text_newpass_r.getText().toString());
        isValidEntry = false;

        switch (str)    {
            case "phone":
                if (text_phone.getText().toString().equals("")) {
                    text_phone.setError("Please enter your phone number.");
                    text_phone.requestFocus();
                    isValid = "no";
                }else if(!isPhone(text_phone.getText().toString())){
                    text_phone.setError("Phone number must be of 10 digits.");
                    text_phone.requestFocus();
                    isValid = "no";
                }
                break;

            case "email":
                if (text_email.getText().toString().equals("")) {
                    text_email.setError("Please enter your email-id.");
                    text_email.requestFocus();
                    isValid = "no";
                }else if(!email){
                    text_email.setError("Invalid email-id.");
                    text_email.requestFocus();
                    isValid = "no";
                }
                break;

            case "password":
                if (text_newpass_r.getText().toString().equals("")) {
                    text_newpass_r.setError("Please re-enter your password.");
                    text_newpass_r.requestFocus();
                    isValid = "no";
                }else if(!pass){
                    text_newpass_r.setError("Passwords don't match.");
                    text_newpass_r.requestFocus();
                    isValid = "no";
                }
                if (text_newpass.getText().toString().equals("")) {
                    text_newpass.setError("Please enter a password.");
                    text_newpass.requestFocus();
                    isValid = "no";
                }else if(!passLen){
                    text_newpass.setError("Password length must be greater than 6 characters.");
                    text_newpass.requestFocus();
                    isValid = "no";
                }
                if(text_oldpass.getText().toString().equals(""))    {
                    text_oldpass.setError("Please enter your current password.");
                    text_oldpass.requestFocus();
                    isValid = "no";
                }
                break;

            default:
                break;

        }

        // Check if valid
        if(!isValid.equals("no")){
            isValidEntry = true;
        }
    }

    private void toDatabase(final String str) {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Processing", "Please wait...", false, false);

        final String phone = text_phone.getText().toString();
        final String email = text_email.getText().toString();
        final String password = text_newpass.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.PROFILE_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        if (response.equalsIgnoreCase("success")) {
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Submit Successfull")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();

                                            if(str.equals("password"))  {
                                                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                                                editor.putString(Config.NAME_SHARED_PREF, "");
                                                editor.putString(Config.PASSWORD_SHARED_PREF, "");
                                                editor.putString(Config.UID_SHARED_PREF, "");
                                                editor.putString(Config.USERNAME_SHARED_PREF, "");
                                                editor.putString(Config.EMAIL_SHARED_PREF, "");
                                                editor.putString(Config.PHONE_SHARED_PREF, "");
                                                editor.putString(Config.TYPE_SHARED_PREF, "");
                                                editor.commit();

                                                Intent intent = new Intent(((UserActivity) getActivity()), LoginActivity.class);
                                                startActivity(intent);
                                                ((UserActivity) getActivity()).finish();
                                            }   else if(str.equals("phone"))    {
                                                editor.putString(Config.PHONE_SHARED_PREF, phone);
                                                editor.commit();
                                            }   else if(str.equals("email"))    {
                                                editor.putString(Config.EMAIL_SHARED_PREF, email);
                                                editor.commit();
                                            }
                                            try {
                                                image_phone = (ImageView) getActivity().findViewById(R.id.confirm_phone);
                                                image_email = (ImageView) getActivity().findViewById(R.id.confirm_email);
                                                if(image_phone.getVisibility() == View.VISIBLE ||
                                                        image_email.getVisibility() == View.VISIBLE)    {
                                                    image_phone.setVisibility(View.GONE);
                                                    image_email.setVisibility(View.GONE);
                                                }
                                            } catch (Exception e)   {
                                                    e.printStackTrace();
                                            }
                                            text_newpass.setText("");
                                            text_oldpass.setText("");
                                            text_newpass_r.setText("");
                                            ProfileFragment fragment = (ProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                                            getFragmentManager().beginTransaction()
                                                    .detach(fragment)
                                                    .remove(fragment)
                                                    .attach(fragment)
                                                    .replace(R.id.fragment_container, fragment)
                                                    .addToBackStack(null)
                                                    .commit();
                                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Operation Failed")
                                    .setCancelable(false)
                                    .setNegativeButton("Retry",null)
                                    .create()
                                    .show();
                            Toast.makeText(getContext(),"Entry already exists ",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getContext(), "Error while connection to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                params.put("context", str);
                params.put("uid", uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getOtp()   {
        Random r = new Random(System.currentTimeMillis());
        final String otp = String.valueOf((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
        RequestOtp req = new RequestOtp(phone, otp, getActivity().getApplicationContext());
        req.execute((Void) null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.otp_dialog, (ViewGroup) getActivity().findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        alertDialog.setView(viewInflated);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String OTP = input.getText().toString();
                if (OTP.equals(otp)) {
                    Toast.makeText(getContext(), "Phone number verification successfull.", Toast.LENGTH_LONG).show();
                    toDatabase("password");
                } else {
                    Toast.makeText(getContext(), "Incorrect OTP", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Invalid Otp")
                            .setCancelable(false)
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        });
        alertDialog.show();
    }

    private void UpdatePhone()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.otp_dialog, (ViewGroup) getActivity().findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        input.setHint("Enter current password");
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        final TextView textView = (TextView) viewInflated.findViewById(R.id.OTPLabel);
        textView.setText("Verification");
        alertDialog.setView(viewInflated);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String oldPass = Encrypt.data(input.getText().toString());
                if (oldPass.equals(pass)) {
                    Toast.makeText(getContext(), "Password verification successfull", Toast.LENGTH_LONG).show();
                    toDatabase("phone");
                } else {
                    Toast.makeText(getContext(), "Password verification failure", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Invalid Password")
                            .setCancelable(false)
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        });
        alertDialog.show();

    }

    private void UpdateEmail()  {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.otp_dialog, (ViewGroup) getActivity().findViewById(android.R.id.content), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        input.setHint("Enter current password");
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        final TextView textView = (TextView) viewInflated.findViewById(R.id.OTPLabel);
        textView.setText("Verification");
        alertDialog.setView(viewInflated);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String oldPass = Encrypt.data(input.getText().toString());
                if (oldPass.equals(pass)) {
                    Toast.makeText(getContext(), "Password verification successfull", Toast.LENGTH_LONG).show();
                    toDatabase("email");
                } else {
                    Toast.makeText(getContext(), "Password verification failure", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Invalid Password")
                            .setCancelable(false)
                            .setNegativeButton("Retry",null)
                            .create()
                            .show();
                }
            }
        });
        alertDialog.show();
    }

    private void UpdatePass()   {

        ExpandableLayout expandableLayout = (ExpandableLayout) getActivity().findViewById(R.id.expandable_layout);

        //if(!pass.equals(Encrypt.data(text_newpass.getText().toString())) &&
        if(pass.equals(Encrypt.data(text_oldpass.getText().toString())))   {
            if(!text_oldpass.getText().toString().equals(text_newpass.getText().toString()))
                getOtp();
            else
                Snackbar.make(expandableLayout,"New password cannot be same as current one.",Snackbar.LENGTH_LONG).show();
        }   else
            Snackbar.make(expandableLayout,"Current password invalid",Snackbar.LENGTH_LONG).show();
    }

    boolean checkPass(String rpass)  {
        if(rpass.equals(text_newpass.getText().toString())) {
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

    @Override
    public void onExpansionUpdate(float expansionFraction) {
        expandButton.setRotation(expansionFraction * 180);
    }

    @Override
    public void onClick(View view) {
        expandableLayout.toggle();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Profile");
    }

}
