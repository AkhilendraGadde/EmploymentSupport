package com.charolia.gadde.ess.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.charolia.gadde.ess.R;

public class ApplyForJobsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String title,post_id,desig,comp;
    private final String ActionBarTitle = "Job Application";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_jobs);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ActionBarTitle);

        title = getIntent().getStringExtra("title");
        desig = getIntent().getStringExtra("designation");
        comp = getIntent().getStringExtra("company");
        post_id = getIntent().getStringExtra("post_id");

        Toast.makeText(this,title+" : "+desig+" : "+comp+" : "+post_id,Toast.LENGTH_LONG).show();
    }

    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
