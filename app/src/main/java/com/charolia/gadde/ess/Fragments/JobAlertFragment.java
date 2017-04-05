package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;
import com.charolia.gadde.ess.UserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobAlertFragment extends Fragment {

    private int type = 0;
    private Spinner list;
    private ArrayList<String> alerts = new ArrayList<String>();
    private RequestQueue requestQueue;
    private String user_id,data;

    public JobAlertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_alert, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //String name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        user_id = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        Log.d("ID",user_id);
        Log.d("last :",String.valueOf(user_id.charAt(user_id.length()-1)));

        if(user_id.charAt(user_id.length()-1) == '1')  {
            type = 1;
        }   else    {
            type = 2;
        }

        if(type == 1)   {

        } else if (type == 2)   {
            /*
                Your created alerts

                Create/update/del job alert
                display his/here alerts


             */
        }

        list = (Spinner) view.findViewById(R.id.alertList);
        requestQueue = Volley.newRequestQueue(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populate_list();
    }

    private void getData() {
        requestQueue.add(getDataFromServer());
    }

    private JsonArrayRequest getDataFromServer() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_REQUEST_URL + String.valueOf(user_id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                //JobAlerts ja = new JobAlerts();
                //ja.setTitle(obj.getString("jTitle"));
                data = obj.getString("jTitle");
                /*SearchFragmentJobData data = new SearchFragmentJobData(obj.getInt("ID"),
                        obj.getString("jTitle"),obj.getString("jDesc"),obj.getString("jCompany"),
                        obj.getString("jLoc"),obj.getString("jDesig"),obj.getString("jSkills"),
                        obj.getString("jSalary"),obj.getString("jVacancy"),obj.getString("jDuration"),
                        obj.getString("jPost_id"));*/
                alerts.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void populate_list()
    {
        try {
            alerts.add("Select your created alert");
            getData();


            list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, alerts));
            list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {

                            TextView example = (TextView) getActivity().findViewById(R.id.etAlert_1);
                            example.setText(""+ list.getSelectedItem());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Job Alerts");
    }
}
