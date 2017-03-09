package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charolia.gadde.ess.R;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private SearchFragmentJobAdapter mJobAdapter;
    private List<SearchFragmentJobData> mDataList;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Context context;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_srcjob);
        mDataList = new ArrayList<>();
        load_data_from_server(0);


        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJobAdapter = new SearchFragmentJobAdapter(getContext(),mDataList);
        mRecyclerView.setAdapter(mJobAdapter);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 10300);

            }
        });

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final SearchFragmentJobData jData = mDataList.get(position);
                                    mDataList.remove(position);
                                    mJobAdapter.notifyItemRemoved(position);
                                    Snackbar.make(recyclerView, "Delete Successful on swipe left", Snackbar.LENGTH_LONG)
                                            .setAction("Undo", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDataList.add(position,jData);
                                                    mJobAdapter.notifyDataSetChanged();
                                                }
                                            }).show();
                                }
                                mJobAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final SearchFragmentJobData jData = mDataList.get(position);
                                    mDataList.remove(position);
                                    mJobAdapter.notifyItemRemoved(position);
                                    Snackbar.make(recyclerView, "Delete Successful on swipe right", Snackbar.LENGTH_LONG)
                                            .setAction("Undo", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDataList.add(position,jData);
                                                    mJobAdapter.notifyDataSetChanged();
                                                }
                                            }).show();
                                }
                                mJobAdapter.notifyDataSetChanged();
                            }
                        });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    private void refresh(){
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 10300);
    }

    private void load_data_from_server(final int id) {

        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://akhilendragadde17.000webhostapp.com/job_desc.php?id="+id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());

                    for (int i=0; i<array.length(); i++){

                        JSONObject obj = array.getJSONObject(i);
                        // get column names from db
                        SearchFragmentJobData data = new SearchFragmentJobData(obj.getInt("id"),obj.getString("Description"),obj.getString("Title"));
                        mDataList.add(data);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mJobAdapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
    }

    @Override
    public void onResume() {

        refresh();
        super.onResume();
    }
}
