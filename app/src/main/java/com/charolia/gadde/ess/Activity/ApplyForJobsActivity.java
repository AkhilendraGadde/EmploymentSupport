package com.charolia.gadde.ess.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.Fragments.JobAlertFragment;
import com.charolia.gadde.ess.Fragments.ResumeFragment;
import com.charolia.gadde.ess.Fragments.SearchFragmentJobData;
import com.charolia.gadde.ess.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ApplyForJobsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String title,post_id,desig,comp;
    private final String ActionBarTitle = "Job Application";
    private TextView tName,tEmail,tPhone,tComp,tTitle,tDesig;
    private RequestQueue requestQueue;
    public String email,name,phone;
    private FloatingActionButton fab;
    private CardView cvP,cvJ;
    private String uid,Uname,jName,jContact,jEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_jobs);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(ActionBarTitle);
        } catch (Exception e)   {
            e.printStackTrace();
        }

        title = getIntent().getStringExtra("title");
        desig = getIntent().getStringExtra("designation");
        comp = getIntent().getStringExtra("company");
        post_id = getIntent().getStringExtra("post_id");

        requestQueue = Volley.newRequestQueue(this);
        getData();

        tName = (TextView) findViewById(R.id.tName);
        tEmail = (TextView) findViewById(R.id.tEmail);
        tPhone = (TextView) findViewById(R.id.tNumber);
        tComp = (TextView) findViewById(R.id.tName_job);
        tTitle = (TextView) findViewById(R.id.tEmail_job);
        tDesig = (TextView) findViewById(R.id.tNumber_job);
        tComp.setText(comp);
        tTitle.setText(title);
        tDesig.setText(desig);

        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        jName = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        jEmail = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        jContact = sharedPreferences.getString(Config.PHONE_SHARED_PREF,"Not Available");
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");
        Uname = sharedPreferences.getString(Config.USERNAME_SHARED_PREF,"Not Available");
        fab = (FloatingActionButton) findViewById(R.id.fab_apply);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if resume exists
                toDatabase();
            }
        });
    }

    private void toDatabase() {
        final ProgressDialog loading = ProgressDialog.show(ApplyForJobsActivity.this, "Processing", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CHK_RESUME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        if (response.equalsIgnoreCase("resume_found")) {
                            loading.dismiss();
                            sendEmail();
                        } else {
                            loading.dismiss();
                            Snackbar
                                    .make(getWindow().getDecorView().getRootView(),"Resume not found, Please create one.",Snackbar.LENGTH_LONG)
                                    .setAction("Create Now", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            fab = (FloatingActionButton) findViewById(R.id.fab_apply);
                                            cvJ = (CardView) findViewById(R.id.card_profile_job);
                                            cvP = (CardView) findViewById(R.id.card_profile);
                                            try {
                                                fab.setVisibility(View.GONE);
                                                cvJ.setVisibility(View.GONE);
                                                cvP.setVisibility(View.GONE);
                                            }   catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Fragment fragment = new ResumeFragment();
                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                        }
                                    })
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ApplyForJobsActivity.this, "Error while connection to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ApplyForJobsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void sendEmail()    {
        String body = "Dear Mr/Mrs. "+name+",\n" +
                "\n" +
                "I am writing to apply for the "+desig+" position advertised in the Employment Support App." +
                " Here ,I am enclosing a completed job application, with my resume.\n\n" +
                "Please see my resume for additional information on my experience.\n" +
                "\n" +
                "I can be reached anytime via email at "+jEmail+" or my cell phone, "+jContact+"."+
                "Thank you for your time and consideration. I look forward to speaking with you about this employment opportunity.\n\nSincerely,\n" +
                "\n" +jName;
        String subject = "EmploymentSupport: Job Application for post of "+desig+" in your company";

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "EmploymentSupport/Resume_"+Uname+".txt";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            Snackbar
                    .make(getWindow().getDecorView().getRootView(),"Please select your resume from Resume section.",Snackbar.LENGTH_LONG)
                    .setAction("Take me there", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fab = (FloatingActionButton) findViewById(R.id.fab_apply);
                            cvJ = (CardView) findViewById(R.id.card_profile_job);
                            cvP = (CardView) findViewById(R.id.card_profile);
                            try {
                                fab.setVisibility(View.GONE);
                                cvJ.setVisibility(View.GONE);
                                cvP.setVisibility(View.GONE);
                            }   catch (Exception e) {
                                e.printStackTrace();
                            }
                            Fragment fragment = new ResumeFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        }
                    })
                    .show();
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    private void getData() {
        requestQueue.add(getDataFromServer());
    }

    private JsonArrayRequest getDataFromServer() {

        final ProgressDialog loading = ProgressDialog.show(this, "Fetching data", "Please wait...", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.RECRUITER_REQUEST_URL + String.valueOf(post_id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loading.dismiss();
                        parseData(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            loading.dismiss();
                            Toast.makeText(ApplyForJobsActivity.this, "no internet access!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e)   {
                            e.printStackTrace();
                        }
                    }
                });
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {

        tName = (TextView) findViewById(R.id.tName);
        tEmail = (TextView) findViewById(R.id.tEmail);
        tPhone = (TextView) findViewById(R.id.tNumber);
        for (int i = 0; i < array.length(); i++) {

            JSONObject obj;
            try {
                obj = array.getJSONObject(i);
                name = obj.getString("name");
                email = obj.getString("email");
                phone = obj.getString("phone");
                tName.setText(obj.getString("name"));
                tEmail.setText(obj.getString("email"));
                tPhone.setText(obj.getString("phone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }
    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab = (FloatingActionButton) findViewById(R.id.fab_apply);
        cvJ = (CardView) findViewById(R.id.card_profile_job);
        cvP = (CardView) findViewById(R.id.card_profile);
        try {
            if(fab.getVisibility() == View.GONE)    {
                fab.setVisibility(View.VISIBLE);
                cvJ.setVisibility(View.VISIBLE);
                cvP.setVisibility(View.VISIBLE);
                ResumeFragment fragment = (ResumeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                getSupportFragmentManager().beginTransaction()
                        .detach(fragment)
                        .remove(fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }   catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setTitle(ActionBarTitle);
    }
}
