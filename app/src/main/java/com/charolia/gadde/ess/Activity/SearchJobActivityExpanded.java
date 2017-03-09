package com.charolia.gadde.ess.Activity;

import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.charolia.gadde.ess.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

public class SearchJobActivityExpanded extends SwipeBackActivity {

    private Toolbar mToolbar;
    private String ActionBarTitle = "Selected Job";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job_expanded);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        // Navigation View
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ActionBarTitle);

        TextView title_tv = (TextView) findViewById(R.id.tvJobTitle_exp);
        TextView desc_tv = (TextView) findViewById(R.id.tvJobDesc_exp);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        title_tv.setText(title);
        desc_tv.setText(description);

        Snackbar.make(getWindow().getDecorView().getRootView(), "Tap on FAB button to Apply for this job.", Snackbar.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }
}
