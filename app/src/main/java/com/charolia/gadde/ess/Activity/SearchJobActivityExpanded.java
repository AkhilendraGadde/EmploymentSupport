package com.charolia.gadde.ess.Activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.charolia.gadde.ess.Fragments.LogoutFragment;
import com.charolia.gadde.ess.R;

public class SearchJobActivityExpanded extends AppCompatActivity {

    private Toolbar mToolbar;
    private String ActionBarTitle,company,title,description,location,designation,
            duration,vacancy,salary,skills,post_id;
    private TextView title_tv,desc_tv,desig_tv,comp_tv,skills_tv,loc_tv,sal_tv,dur_tv,vac_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job_expanded);

        ActionBarTitle = getIntent().getStringExtra("company");
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ActionBarTitle);

        title_tv = (TextView) findViewById(R.id.jTitle_ex);
        desc_tv = (TextView) findViewById(R.id.jDesc_ex);
        desig_tv = (TextView) findViewById(R.id.jDesig_ex);
        comp_tv = (TextView) findViewById(R.id.jComp_ex);
        loc_tv = (TextView) findViewById(R.id.jLoc_ex);
        skills_tv = (TextView) findViewById(R.id.jSkills_ex);
        sal_tv = (TextView) findViewById(R.id.jSal_ex);
        dur_tv = (TextView) findViewById(R.id.jDur_ex);
        vac_tv = (TextView) findViewById(R.id.jVac_ex);

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        company = getIntent().getStringExtra("company");
        location = getIntent().getStringExtra("location");
        designation = getIntent().getStringExtra("designation");
        duration = getIntent().getStringExtra("duration");
        vacancy = getIntent().getStringExtra("vacancy");
        salary = getIntent().getStringExtra("salary");
        skills = getIntent().getStringExtra("skills");
        post_id = getIntent().getStringExtra("pid");

        title_tv.setText(title);
        desc_tv.setText(description);
        desig_tv.setText(designation);
        comp_tv.setText(company);
        loc_tv.setText(location);
        skills_tv.setText(skills);
        if(salary.contains("by"))
            sal_tv.setText(salary);
        else
            sal_tv.setText(salary+"/- pm");
        dur_tv.setText(duration);
        vac_tv.setText(vacancy+" Vacancies");

        Snackbar.make(getWindow().getDecorView().getRootView(), "Tap on menu icon to apply for this job", Snackbar.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.jApply:

                Snackbar
                        .make(getWindow().getDecorView().getRootView(),"Apply for "+designation+" in this company?",Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent apply = new Intent(SearchJobActivityExpanded.this,ApplyForJobsActivity.class);
                                apply.putExtra("post_id",post_id);
                                apply.putExtra("title",title);
                                apply.putExtra("designation",description);
                                apply.putExtra("company",company);
                                SearchJobActivityExpanded.this.startActivity(apply);
                            }
                        })
                        .show();
                return true;

            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
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
