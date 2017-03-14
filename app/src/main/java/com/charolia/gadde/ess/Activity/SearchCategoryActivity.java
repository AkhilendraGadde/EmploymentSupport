package com.charolia.gadde.ess.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.charolia.gadde.ess.R;

/**
 * Created by Administrator on 3/14/2017.
 */

public class SearchCategoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String ActionBarTitle = "Jobs by Category ";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);

        //setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        // Navigation View
        mToolbar = (Toolbar) findViewById(R.id.nav_action_category);
        setSupportActionBar(mToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(ActionBarTitle);

        initFragment();
    }

    public void initFragment(){


        SearchCategoryFragment catFrag = new SearchCategoryFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, catFrag);
        fragmentTransaction.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }
}
