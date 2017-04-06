package com.charolia.gadde.ess;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.charolia.gadde.ess.Activity.SearchJobActivity;
import com.charolia.gadde.ess.Fragments.AboutFragment;
import com.charolia.gadde.ess.Fragments.FeedbackFragment;
import com.charolia.gadde.ess.Fragments.ForumFragment;
import com.charolia.gadde.ess.Fragments.HomeFragment;
import com.charolia.gadde.ess.Fragments.JobAlertFragment;
import com.charolia.gadde.ess.Fragments.LogoutFragment;
import com.charolia.gadde.ess.Fragments.ResumeFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private String ActionBarTitle = "Employment Support",uid;
    private int type_id = 0;

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
        uid = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        if(uid.charAt(uid.length()-1) == '1')  {
            type_id = 1;
        }   else if(uid.charAt(uid.length()-1) == '2')   {
            type_id = 2;
        }

        // Navigation View
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.openDrawer(GravityCompat.START);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up Navigation Texts and actions.
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        View header = mNavigationView.getHeaderView(0);
        tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        tvName = (TextView) header.findViewById(R.id.tvName);

        tvName.setText(name);
        tvEmail.setText(email);
        mNavigationView.setNavigationItemSelectedListener(this);

        Menu mMenu = mNavigationView.getMenu();
        MenuItem resume,jobalerts;
        resume = mMenu.findItem(R.id.nav_resume);
        jobalerts = mMenu.findItem(R.id.nav_jobalert);

        if(type_id == 1)   {
            resume.setEnabled(true);
            jobalerts.setEnabled(false);
        } else if (type_id == 2)   {
            resume.setEnabled(false);
            jobalerts.setEnabled(true);
        } else {
            resume.setEnabled(true);
            jobalerts.setEnabled(true);
        }

        Snackbar.make(getWindow().getDecorView().getRootView(),"Hello, "+name+"! Welcome To Employment Support.",Snackbar.LENGTH_LONG).show();
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof HomeFragment ) {
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
            }else
                mDrawerLayout.openDrawer(GravityCompat.START);
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
            case R.id.logout:

                HideActionBar();
                Fragment fragment = new LogoutFragment();
                if (fragment != null) {

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
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
        if (id != R.id.logout)   {
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
                mDrawerLayout.closeDrawer(GravityCompat.START);
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(Config.CONTACT_EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, Config.CONTACT_SUBJECT);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(UserActivity.this, getString(R.string.main_not_found_email), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_about:
                fragment = new AboutFragment();
                ActionBarTitle = "About";
                break;

            default:
                break;
        }

        if (fragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            getSupportActionBar().setTitle(ActionBarTitle);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
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
