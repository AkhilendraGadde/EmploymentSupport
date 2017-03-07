package com.charolia.gadde.ess.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charolia.gadde.ess.R;

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
    private SearchFragment$JobAdapter mJobAdapter;
    private List<SearchFragment$JobData> mDataList;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_srcjob);
        mDataList = new ArrayList<>();
        load_data_from_server(0);

        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJobAdapter = new SearchFragment$JobAdapter(getContext(),mDataList);
        mRecyclerView.setAdapter(mJobAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
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
                        SearchFragment$JobData data = new SearchFragment$JobData(obj.getInt("id"),obj.getString("Description"),obj.getString("Title"));
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



    /*@Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Search Jobs");
    }*/
}
