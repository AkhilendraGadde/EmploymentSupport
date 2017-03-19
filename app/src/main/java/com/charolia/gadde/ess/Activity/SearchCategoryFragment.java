package com.charolia.gadde.ess.Activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
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
import com.charolia.gadde.ess.Fragments.SearchFragmentJobAdapter;
import com.charolia.gadde.ess.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCategoryFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private SearchCategoryJobAdapter mJobAdapter;
    private List<SearchCategoryJobData> mDataList;
    public Context context;

    private RequestQueue requestQueue;
    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 0;

    public SearchCategoryFragment() {
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
        requestQueue = Volley.newRequestQueue(getActivity());
        getData();
        //load_data_from_server(0);

        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJobAdapter = new SearchCategoryJobAdapter(getContext(),mDataList);
        mRecyclerView.setAdapter(mJobAdapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);


            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //if (isLastItemDisplaying(recyclerView)) {
                //Calling the method getdata again
                // getData(0);
                //}
            }
        });

        return view;
    }


    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                Toast.makeText(getActivity(), "Reached end of view", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }

    private JsonArrayRequest getDataFromServer(int requestCount) {

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_REQUEST_URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(getActivity(), "no internet access!", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object

            //Getter_Setter cars = new Getter_Setter();
            JSONObject obj = null;

            try {
                //Getting json
                obj = array.getJSONObject(i);

                //Adding data to the superhero object
                SearchCategoryJobData data = new SearchCategoryJobData(obj.getInt("id"),obj.getString("Description"),obj.getString("Title"));
                mDataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
        }

        //Notifying the adapter that data has been added or changed
        mJobAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

