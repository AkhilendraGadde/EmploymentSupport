package com.charolia.gadde.ess.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private ForumFragmentAdapter mAdapter;
    private List<ForumFragmentData> mDataList;
    private RequestQueue requestQueue;

    private CardView cardView;
    private TextView contentInfo;
    private Button bSubmit;
    private EditText etQuery;
    public String name;
    int level = 0,visible = 0,requestCount = 0;

    public ForumFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        //Fetching values from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");

        initLayout(view);
        initCheck();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_forum);
        mDataList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getActivity());
        //getData();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ForumFragmentAdapter(getContext(),mDataList);
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
        return view;
    }

    private void initLayout(View view) {

        etQuery = (EditText) view.findViewById(R.id.queryQuestion);
        contentInfo = (TextView) view.findViewById(R.id.tvContentInfo);
        cardView = (CardView) view.findViewById(R.id.card_forum);
        cardView.setVisibility(View.GONE);

        bSubmit = (Button) view.findViewById(R.id.querySubmit);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuery();
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab_forum_add);
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
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Processing", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.QUERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                        //You can handle error here if you want
                        loading.dismiss();
                        Toast.makeText(getContext(), "Error while connection to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_QUERY, "");
                params.put(Config.KEY_NAME, "");
                params.put("level", String.valueOf(level));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void submitQuery()  {

        level = 2;
        final String query = etQuery.getText().toString();
        if (TextUtils.isEmpty(query))   {
            etQuery.setError("Query can't be empty");
            etQuery.requestFocus();
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Submitting your query", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.QUERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                            loading.dismiss();
                            etQuery.setText("");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Query Submit successful !")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ForumFragment fragment = (ForumFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                                            getFragmentManager().beginTransaction()
                                                    .detach(fragment)
                                                    .attach(fragment)
                                                    .commit();
                                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Query submit Failed")
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
                        Toast.makeText(getContext(), "Error while connection to network", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_QUERY, query);
                params.put(Config.KEY_NAME, name);
                params.put("level", String.valueOf(level));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void getData() {
        requestQueue.add(getDataFromServer(requestCount));
        requestCount++;
    }

    private JsonArrayRequest getDataFromServer(int requestCount) {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Fetching queries", "Please wait...", false, false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.FORUM_QLIST_URL + String.valueOf(requestCount),
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
                        Toast.makeText(getActivity(), "no internet access!", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject obj;
            try {
                obj = array.getJSONObject(i);
                ForumFragmentData data = new ForumFragmentData(obj.getInt("id"),obj.getString("query"),obj.getString("q_uname"));
                mDataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Community Forum");

        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(fab.getVisibility() == View.VISIBLE){
                        (getActivity()).onBackPressed();
                    }
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
                    return true;
                }
                return false;
            }
        });
    }
}

