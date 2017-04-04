package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SearchFragmentJobAdapter mJobAdapter;
    private List<SearchFragmentJobData> mDataList;
    public Context context;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RequestQueue requestQueue;
    private int requestCount = 0;

    public SearchCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_srcjob);
        mDataList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJobAdapter = new SearchFragmentJobAdapter(getContext(),mDataList);
        mRecyclerView.setAdapter(mJobAdapter);

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
        // <----  Refresh Layout ----> //
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(Color.argb(200,128,0,128));
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                mDataList.clear();
                getData();
            }
        });

        initRefreshLayout();
        return view;
    }

    private void initRefreshLayout(){

        mWaveSwipeRefreshLayout.setRefreshing(true);
        mDataList.clear();
        getData();
    }

    private void getData() {
        requestQueue.add(getDataFromServer(requestCount));
        requestCount++;
    }

    private JsonArrayRequest getDataFromServer(int requestCount) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.JOBS_LIST_URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                        parseData(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
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
                SearchFragmentJobData data = new SearchFragmentJobData(obj.getInt("ID"),
                        obj.getString("jTitle"),obj.getString("jDesc"),obj.getString("jCompany"),
                        obj.getString("jLoc"),obj.getString("jDesig"),obj.getString("jSkills"),
                        obj.getString("jSalary"),obj.getString("jVacancy"),obj.getString("jDuration"),
                        obj.getString("jPost_id"));
                mDataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mJobAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

