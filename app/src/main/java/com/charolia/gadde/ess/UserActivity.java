package com.charolia.gadde.ess;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.charolia.gadde.ess.Activity.SearchJobActivity;
import com.charolia.gadde.ess.Fragments.AboutFragment;
import com.charolia.gadde.ess.Fragments.FeedbackFragment;
import com.charolia.gadde.ess.Fragments.ForumFragment;
import com.charolia.gadde.ess.Fragments.HomeFragment;
import com.charolia.gadde.ess.Fragments.JobAlertFragment;
import com.charolia.gadde.ess.Fragments.LogoutFragment;
import com.charolia.gadde.ess.Fragments.ResumeFragment;
import com.charolia.gadde.ess.Fragments.SearchFragment;
import com.charolia.gadde.ess.Fragments.SupportFragment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private String ActionBarTitle = "Employment Support";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // set initial fragment.
        HomeFragment home = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, home);
        fragmentTransaction.commit();

        final TextView tvEmail, tvName, tvType;

        //Fetching values from shared preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF,"Not Available");
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        String phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF,"Not Available");
        String type = sharedPreferences.getString(Config.TYPE_SHARED_PREF,"Not Available");
        String password = sharedPreferences.getString(Config.PASSWORD_SHARED_PREF,"Not Available");
        String uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");
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

        tvType.setText(trimUppercase(name));
        tvName.setText(type);
        tvEmail.setText(email);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public String trimUppercase(String name){
        Pattern p = Pattern.compile("((^| )[A-Za-z])");
        Matcher m = p.matcher(name);
        String initials="";
        while(m.find()){
            initials+=m.group().trim();
        }
        return initials.toUpperCase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof HomeFragment) {
                // do something with f

            /*if(ActionBarTitle.equals("Employment Support"))
                moveTaskToBack(true);
            else
                super.onBackPressed();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure , you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                            }
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(false)
                        .create()
                        .show();
                //moveTaskToBack(true);
            }else
                super.onBackPressed();

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

                refreshContent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;

        int id = item.getItemId();
        if (id != R.id.nav_logout)   {
            ShowActionBar();
        }   else {
            HideActionBar();
        }

        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                ActionBarTitle = "Employment Support";
                break;

            case R.id.nav_srch:
                Intent i = new Intent(UserActivity.this, SearchJobActivity.class);
                UserActivity.this.startActivity(i);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
                ActionBarTitle = "Search Jobs";
                break;

            case R.id.nav_jobalert:
                fragment = new JobAlertFragment();
                ActionBarTitle = "Job Alerts";
                break;

            case R.id.nav_resume:
                fragment = new ResumeFragment();
                ActionBarTitle = "Manage Resume";
                break;

            case R.id.nav_cf:
                fragment = new ForumFragment();
                ActionBarTitle = "Community Forum";
                break;

            case R.id.nav_feedback:
                fragment = new FeedbackFragment();
                ActionBarTitle = "Feedback";
                break;

            case R.id.nav_sup:
                fragment = new SupportFragment();
                ActionBarTitle = "Contact Support";
                break;

            case R.id.nav_about:
                fragment = new AboutFragment();
                ActionBarTitle = "About";
                break;

            case R.id.nav_logout:
                fragment = new LogoutFragment();
                ActionBarTitle = "Logout";
                break;

            default:
                break;
        }

        // update selected fragment and title
        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            getSupportActionBar().setTitle(ActionBarTitle);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void HideActionBar(){

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void ShowActionBar(){

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getSupportActionBar().show();
    }

    public void refreshContent(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
