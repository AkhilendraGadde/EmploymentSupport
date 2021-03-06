package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.charolia.gadde.ess.Activity.SearchCategoryActivity;
import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCategoryFragment extends Fragment {

    private Toolbar searchtoolbar;
    private Menu search_menu;
    private MenuItem item_search;
    private TextView resultView;
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

    public void onSearch(String query){
        Log.d("Query From Activity",query);
        List<SearchFragmentJobData> filteredList = new ArrayList<>();
        resultView = (TextView) this.getActivity().findViewById(R.id.result_view);
        for (int i = 0; i < mDataList.size(); i++) {

            String jTitle,jCompany,jLoc;
            String title = ((SearchCategoryActivity) context).getIntent().getStringExtra("Name");
            jTitle = mDataList.get(i).getJob_title().toLowerCase();
            jCompany = mDataList.get(i).getJob_company().toLowerCase();
            jLoc = mDataList.get(i).getJob_location().toLowerCase();

            if(title.equals("Title")){
                jCompany = mDataList.get(i).getJob_title();
                jTitle = mDataList.get(i).getJob_company();
            } else if(title.equals("Designation"))   {
                jCompany = mDataList.get(i).getJob_desig().toLowerCase();
                jTitle = mDataList.get(i).getJob_title().toLowerCase();
            } else if (title.equals("Company")){
                jCompany = mDataList.get(i).getJob_company().toLowerCase();
            } else if (title.equals("Skills")){
                jCompany = mDataList.get(i).getJob_skills().toLowerCase();
            } else if (title.equals("Salary")){
                jCompany = mDataList.get(i).getJob_salary()+"/- pm".toLowerCase();
            } else if (title.equals("Vacancy")){
                jCompany = "Required Employees : "+mDataList.get(i).getJob_vacancy().toLowerCase();
                jTitle = mDataList.get(i).getJob_company().toLowerCase();
            } else if (title.equals("Duration")){
                jCompany = mDataList.get(i).getJob_duration().toLowerCase();
            }
            Log.d("TITLE",title);
            Log.d("jTITLE",jTitle);
            Log.d("jCOMPANY",jCompany);
            Log.d("jLOC",jLoc);
            if (jTitle.contains(query) || jCompany.contains(query) || jLoc.contains(query) ) {
                resultView.setVisibility(View.GONE);
                filteredList.add(mDataList.get(i));
            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJobAdapter = new SearchFragmentJobAdapter(getContext(),filteredList);
        mRecyclerView.setAdapter(mJobAdapter);
        mJobAdapter.notifyDataSetChanged();
        if(filteredList.isEmpty())  {
            mRecyclerView.setVisibility(View.GONE);
            resultView.setText("No results found for : " + query);
            resultView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            resultView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ((SearchCategoryActivity)  getActivity()).circleReveal(R.id.searchtoolbar,1,true,true);
                else
                    searchtoolbar.setVisibility(View.VISIBLE);

                item_search.expandActionView();
                return true;

            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
    }
    public void setSearchtoolbar()
    {
        searchtoolbar = (Toolbar) this.getActivity().findViewById(R.id.searchtoolbar);
        if (searchtoolbar != null) {
            searchtoolbar.inflateMenu(R.menu.menu_search_filter);
            search_menu=searchtoolbar.getMenu();

            searchtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        ((SearchCategoryActivity )  getActivity()).circleReveal(R.id.searchtoolbar,1,true,false);
                    else
                        searchtoolbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ((SearchCategoryActivity )  getActivity()).circleReveal(R.id.searchtoolbar,1,true,false);
                    }
                    else
                        searchtoolbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });
            initSearchView();
        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    public void initSearchView()
    {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        searchView.setSubmitButtonEnabled(false);
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);

        // set hint and the text colors
        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));

        // set the cursor
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                query = query.toLowerCase();
                Log.i("query", "" + query);
                onSearch(query);
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setSearchtoolbar();
        setHasOptionsMenu(true);
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

