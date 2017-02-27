package com.charolia.gadde.ess;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        final TextView tv = (TextView) findViewById(R.id.tv_nav);
        final TextView tvEmail, tvName, tvType;

        //Fetching values from shared preferences

        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF,"Not Available");
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        String phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF,"Not Available");
        String type = sharedPreferences.getString(Config.TYPE_SHARED_PREF,"Not Available");

        //Showing the current logged in user details to textview
        /*String message = "\n Name : "+name + "\n Username : " + username + "\n Email : " + email + "\n Phone : " + phone + "\n Type : " + type;
        tv.setText(message);*/


        // Navigation View

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up Navigation Texts and actions.
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header = mNavigationView.getHeaderView(0);
        tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        tvName = (TextView) header.findViewById(R.id.tvName);
        tvType = (TextView) header.findViewById(R.id.tvType);

        tvType.setText(type);
        tvName.setText(name);
        tvEmail.setText(email);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_srch:

                        Toast.makeText(UserActivity.this, "Search Selected", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_jobalert:

                        Toast.makeText(UserActivity.this, "JobAlert Selected", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_resume:

                        Toast.makeText(UserActivity.this, "Resume Selected", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_cf:

                        Toast.makeText(UserActivity.this, "Community Forum Selected", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_feedback:

                        Intent fb = new Intent(UserActivity.this, FeedbackActivity.class);
                        UserActivity.this.startActivity(fb);
                        Toast.makeText(UserActivity.this, "Feedback Selected", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_sup:
                        Toast.makeText(UserActivity.this, "Support Selected", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_logout:

                        logout();
                        mDrawerLayout.closeDrawers();
                        return true;

                    default:
                        mDrawerLayout.closeDrawers();
                        return true;
                }
            }
        });

    }

    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to prefs
                        editor.putString(Config.NAME_SHARED_PREF, "");
                        editor.putString(Config.USERNAME_SHARED_PREF, "");
                        editor.putString(Config.EMAIL_SHARED_PREF, "");
                        editor.putString(Config.PHONE_SHARED_PREF, "");
                        editor.putString(Config.TYPE_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                        startActivity(intent);
                        UserActivity.this.finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_additions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()) {
            case R.id.refresh:

                Intent intent = getIntent();
                finish();
                startActivity(intent);
                View parentLayout = findViewById(R.id.drawerLayout);
                Snackbar.make(parentLayout, "Employment Support App v1.1.0", Snackbar.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }


}
