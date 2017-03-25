package com.charolia.gadde.ess.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import static java.security.AccessController.getContext;

/**
 * Created by Administrator on 3/25/2017.
 */

public class ForumActivityExpanded extends SwipeBackActivity {
    private Toolbar mToolbar;
    private String ActionBarTitle = "Selected Query";

    private FloatingActionButton fab;
    private CardView cardView;
    private Button bSubmit;
    private EditText etReply;
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_expanded);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        // Navigation View
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ActionBarTitle);

        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");

        initLayout();

        TextView tvQuery = (TextView) findViewById(R.id.tvQuery);
        TextView tvPost = (TextView) findViewById(R.id.tvPost);

        String query = getIntent().getStringExtra("query");
        String post_id = getIntent().getStringExtra("post_id");

        tvQuery.setText(query);
        tvPost.setText(post_id);

        Snackbar.make(getWindow().getDecorView().getRootView(), "Tap on FAB button to add a reply", Snackbar.LENGTH_SHORT).show();
    }

    private void initLayout() {

        etReply = (EditText) findViewById(R.id.queryReply);
        cardView = (CardView) findViewById(R.id.card_reply);
        cardView.setVisibility(View.GONE);

        bSubmit = (Button) findViewById(R.id.queryReplySubmit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Submit Click Detected",Snackbar.LENGTH_LONG).show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab_forum_add_reply);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mRecyclerView.setVisibility(View.GONE);
                //contentInfo.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        fab.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
        //super.onBackPressed();
    }
}
