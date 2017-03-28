package com.charolia.gadde.ess.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.charolia.gadde.ess.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 3/25/2017.
 */

public class ForumActivityExpanded extends AppCompatActivity {

    private Toolbar mToolbar;
    private String ActionBarTitle = "Selected Query";
    private TextView contentInfo;

    private RecyclerView mRecyclerView;
    private ForumActivityExpandedAdapter mAdapter;
    private List<ForumActivityExpandedData> mDataList;
    private RequestQueue requestQueue;

    private FloatingActionButton fab;
    private CardView cardView;
    private Button bSubmit;
    private EditText etReply;
    public String name,query;

    private int level = 0,visible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_expanded);

        // Navigation View
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(ActionBarTitle);

        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");

        initLayout();
        initCheck();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_qReply);
        mDataList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
        //getData();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ForumActivityExpandedAdapter(getBaseContext(),mDataList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    private void initLayout() {

        TextView tvQuery = (TextView) findViewById(R.id.tvQuery);
        TextView tvPost = (TextView) findViewById(R.id.tvPostR);

        query = getIntent().getStringExtra("query");
        String post_id = getIntent().getStringExtra("post_id");

        tvQuery.setText(query);
        tvPost.setText(post_id);

        Snackbar.make(getWindow().getDecorView().getRootView(), "Tap on FAB button to add a reply", Snackbar.LENGTH_SHORT).show();

        contentInfo = (TextView) findViewById(R.id.tvContentInfoQR);
        etReply = (EditText) findViewById(R.id.queryReply);
        cardView = (CardView) findViewById(R.id.card_reply);
        cardView.setVisibility(View.GONE);

        bSubmit = (Button) findViewById(R.id.queryReplySubmit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReply();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab_forum_add_reply);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setVisibility(View.GONE);
                contentInfo.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initCheck() {
        level = 1;
        final ProgressDialog loading = ProgressDialog.show(this, "Processing", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REPLY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        if (response.equalsIgnoreCase("Found")) {
                            loading.dismiss();
                            mRecyclerView.setVisibility(View.VISIBLE);
                            contentInfo.setVisibility(View.GONE);
                            visible = 1; // setRecyclerView
                            getData();
                        } else {
                            loading.dismiss();
                            mRecyclerView.setVisibility(View.GONE);
                            contentInfo.setVisibility(View.VISIBLE);
                            visible = 2; // setTextView
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Error while connection to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_QUERY, query);
                params.put(Config.KEY_REPLY, "");
                params.put(Config.KEY_NAME, "");
                params.put("level", String.valueOf(level));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void submitReply()  {
        level = 2;
        final String reply = etReply.getText().toString();
        if (TextUtils.isEmpty(reply))   {
            etReply.setError("Reply can't be empty");
            etReply.requestFocus();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this, "Submitting your reply", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REPLY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("res",response);
                        if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                            loading.dismiss();
                            etReply.setText("");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForumActivityExpanded.this);
                            builder.setMessage("Reply Submit successful !")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForumActivityExpanded.this);
                            builder.setMessage("Reply submit Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Error while connection to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_QUERY, query);
                params.put(Config.KEY_REPLY, reply);
                params.put(Config.KEY_NAME, name);
                params.put("level", String.valueOf(level));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getData() {
        requestQueue.add(getDataFromServer());
    }

    private JsonArrayRequest getDataFromServer() {
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching feedbacks", "Please wait...", false, false);
        JsonArrayRequest jsonArrayRequest = null;
        try {
            jsonArrayRequest = new JsonArrayRequest(Config.FORUM_RLIST_URL + URLEncoder.encode(query, "UTF-8"),
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
                            loading.dismiss();
                            Toast.makeText(ForumActivityExpanded.this, "no internet access!", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject obj;
            try {
                obj = array.getJSONObject(i);


                if (obj.has("reply_"+(i+1))) {
                    if(array.getJSONObject(0).getString("reply_1").equals("")) {
                        contentInfo.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        break;
                    } else if(!obj.getString("reply_"+(i+1)).equals("")){
                        String reply = obj.getString("reply_"+(i+1));
                        String uname = obj.getString("uname_"+(i+1));
                        Log.d("reply :",reply);
                        ForumActivityExpandedData data = new ForumActivityExpandedData(obj.getInt("id"),reply,uname);
                        mDataList.add(data);
                    } else {
                        break;
                    }
                }else {
                    Log.i("Column ","No Such Tag found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item){

        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        if (fab.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }else   {
            if(visible == 1)    {
                mRecyclerView.setVisibility(View.VISIBLE);
                contentInfo.setVisibility(View.GONE);
            }
            else if (visible == 2)  {
                contentInfo.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            fab.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
        }
    }
}
