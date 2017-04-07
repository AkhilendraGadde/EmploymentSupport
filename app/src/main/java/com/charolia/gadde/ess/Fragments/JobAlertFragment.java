package com.charolia.gadde.ess.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobAlertFragment extends Fragment {

    boolean isValidDetails = false;

    private Spinner spinner;
    private ArrayList<String> alertsList = new ArrayList<String>();
    private ArrayList<String> alertTitle = new ArrayList<String>();
    private ArrayList<String> alertDesc = new ArrayList<String>();
    private ArrayList<String> alertComp = new ArrayList<String>();
    private ArrayList<String> alertLoc = new ArrayList<String>();
    private ArrayList<String> alertDesig = new ArrayList<String>();
    private ArrayList<String> alertSkills = new ArrayList<String>();
    private ArrayList<String> alertSalary = new ArrayList<String>();
    private ArrayList<String> alertVacancy = new ArrayList<String>();
    private ArrayList<String> alertDuration = new ArrayList<String>();

    private RequestQueue requestQueue;
    private String user_id,data;
    private FloatingActionMenu menuYellow;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private TextView tvInfo;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    int getArraySize=0;

    private LinearLayout linearLayout;
    EditText etTitle,etDesc,etCompany,etLocation,etDesignation,etrSkills,etSalary,etVacancy,etDuration;
    String title,desc,comp,loc,desig,skills,salary,vacancy,duration;
    Button bSubmit;

    public JobAlertFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_alert, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        spinner = (Spinner) view.findViewById(R.id.alertList);
        requestQueue = Volley.newRequestQueue(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuYellow = (FloatingActionMenu) view.findViewById(R.id.menu_fab);

        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        menuYellow.hideMenuButton(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populate_list();

        menus.add(menuYellow);

        menuYellow.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text;
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
            }
        });

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }
    }

    private void validateDetails(){

        String isValid = "";
        if (etDuration.getText().toString().equals("")) {
            etDuration.setError("Please enter duration");
            etDuration.requestFocus();
            isValid = "no";
        }
        if (etVacancy.getText().toString().equals("")) {
            etVacancy.setError("Please enter vacancy");
            etVacancy.requestFocus();
            isValid = "no";
        }
        if (etSalary.getText().toString().equals("")) {
            etSalary.setError("Please enter salary");
            etSalary.requestFocus();
            isValid = "no";
        }
        if (etrSkills.getText().toString().equals("")) {
            etrSkills.setError("Please enter skills");
            etrSkills.requestFocus();
            isValid = "no";
        }
        if (etDesignation.getText().toString().equals("")) {
            etDesignation.setError("Please enter designation");
            etDesignation.requestFocus();
            isValid = "no";
        }
        if (etLocation.getText().toString().equals("")) {
            etLocation.setError("Please enter location");
            etLocation.requestFocus();
            isValid = "no";
        }
        if (etCompany.getText().toString().equals("")) {
            etCompany.setError("Please enter Company name");
            etCompany.requestFocus();
            isValid = "no";
        }
        if (etDesc.getText().toString().equals("")) {
            etDesc.setError("Please enter description");
            etDesc.requestFocus();
            isValid = "no";
        }
        if (etTitle.getText().toString().equals("")) {
            etTitle.setError("Please enter title");
            etTitle.requestFocus();
            isValid = "no";
        }
        // Check if valid
        if(!isValid.equals("no")){
            isValidDetails = true;
        }

    }

    private void toDatabase(final String str) {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Processing", "Please wait...", false, false);

        title = etTitle.getText().toString();
        desc = etDesc.getText().toString();
        comp = etCompany.getText().toString();
        loc = etLocation.getText().toString();
        desig = etDesignation.getText().toString();
        skills = etrSkills.getText().toString();
        salary = etSalary.getText().toString();
        vacancy = etVacancy.getText().toString();
        duration = etDuration.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JOBLALERT_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        if (response.equalsIgnoreCase("success")) {
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Submit Successfull")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            JobAlertFragment fragment = (JobAlertFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
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
                            builder.setMessage("Operation Failed")
                                    .setCancelable(false)
                                    .setNegativeButton("Retry",null)
                                    .create()
                                    .show();
                            Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
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
                params.put("title", title);
                params.put("desc", desc);
                params.put("comp", comp);
                params.put("loc", loc);
                params.put("desig", desig);
                params.put("skills", skills);
                params.put("salary", salary);
                params.put("vacancy", vacancy);
                params.put("duration", duration);
                params.put("context", str);
                params.put("uid", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void initInputs()   {
        etTitle = (EditText) getActivity().findViewById(R.id.etTitle);
        etDesc = (EditText) getActivity().findViewById(R.id.etDesc);
        etCompany = (EditText) getActivity().findViewById(R.id.etCompany);
        etLocation = (EditText) getActivity().findViewById(R.id.etLocation);
        etDesignation = (EditText) getActivity().findViewById(R.id.etDesignation);
        etrSkills = (EditText) getActivity().findViewById(R.id.etrSkills);
        etSalary = (EditText) getActivity().findViewById(R.id.etSalary);
        etVacancy = (EditText) getActivity().findViewById(R.id.etVacancy);
        etDuration = (EditText) getActivity().findViewById(R.id.etDuration);
        bSubmit = (Button) getActivity().findViewById(R.id.bSubmit);
    }

    private void setEmpty() {
        etTitle.setText("");
        etTitle.setFocusable(true);
        etTitle.setFocusableInTouchMode(true);
        etDesc .setText("");
        etDesc.setFocusable(true);
        etDesc.setFocusableInTouchMode(true);
        etCompany.setText("");
        etCompany.setFocusable(true);
        etCompany.setFocusableInTouchMode(true);
        etLocation.setText("");
        etLocation.setFocusable(true);
        etLocation.setFocusableInTouchMode(true);
        etDesignation.setText("");
        etDesignation.setFocusable(true);
        etDesignation.setFocusableInTouchMode(true);
        etrSkills.setText("");
        etrSkills.setFocusable(true);
        etrSkills.setFocusableInTouchMode(true);
        etSalary.setText("");
        etSalary.setFocusable(true);
        etSalary.setFocusableInTouchMode(true);
        etVacancy.setText("");
        etVacancy.setFocusable(true);
        etVacancy.setFocusableInTouchMode(true);
        etDuration.setText("");
        etDuration.setFocusable(true);
        etDuration.setFocusableInTouchMode(true);
    }

    private void showDetails()  {
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
        linearLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_about_card_show);
        linearLayout.startAnimation(animation);
        tvInfo = (TextView) getActivity().findViewById(R.id.job_desc);
        try {
            if(tvInfo.getVisibility() == View.VISIBLE)
                tvInfo.setVisibility(View.GONE);
        } catch ( Exception e){
            e.printStackTrace();
        }

        initInputs();
        etTitle.setText(title);
        etTitle.setFocusable(false);
        etDesc.setText(desc);
        etDesc.setFocusable(false);
        etCompany.setText(comp);
        etCompany.setFocusable(false);
        etLocation.setText(loc);
        etLocation.setFocusable(false);
        etDesignation.setText(desig);
        etDesignation.setFocusable(false);
        etrSkills.setText(skills);
        etrSkills.setFocusable(false);
        etSalary.setText(salary);
        etSalary.setFocusable(false);
        etVacancy.setText(vacancy);
        etVacancy.setFocusable(false);
        etDuration.setText(duration);
        etDuration.setFocusable(false);
        bSubmit.setVisibility(View.GONE);
    }

    private void showDetailsforUpdate()  {
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
        linearLayout.setVisibility(View.VISIBLE);
        tvInfo = (TextView) getActivity().findViewById(R.id.job_desc);
        try{
            if(tvInfo.getVisibility() == View.VISIBLE)
                tvInfo.setVisibility(View.GONE);
        } catch (Exception e){
            e.printStackTrace();
        }

        initInputs();
        etTitle.setText(title);
        etTitle.setFocusable(true);
        etTitle.setFocusableInTouchMode(true);
        etDesc.setText(desc);
        etDesc.setFocusable(true);
        etDesc.setFocusableInTouchMode(true);
        etCompany.setText(comp);
        etCompany.setFocusable(true);
        etCompany.setFocusableInTouchMode(true);
        etLocation.setText(loc);
        etLocation.setFocusable(true);
        etLocation.setFocusableInTouchMode(true);
        etDesignation.setText(desig);
        etDesignation.setFocusable(true);
        etDesignation.setFocusableInTouchMode(true);
        etrSkills.setText(skills);
        etrSkills.setFocusable(true);
        etrSkills.setFocusableInTouchMode(true);
        etSalary.setText(salary);
        etSalary.setFocusable(true);
        etSalary.setFocusableInTouchMode(true);
        etVacancy.setText(vacancy);
        etVacancy.setFocusable(true);
        etVacancy.setFocusableInTouchMode(true);
        etDuration.setText(duration);
        etDuration.setFocusable(true);
        etDuration.setFocusableInTouchMode(true);

        bSubmit.setVisibility(View.VISIBLE);
    }

    private void onCreateClick()    {

        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
        linearLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_about_card_show);
        linearLayout.startAnimation(animation);
        tvInfo = (TextView) getActivity().findViewById(R.id.job_desc);
        try {
            if(tvInfo.getVisibility() == View.VISIBLE)
                tvInfo.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }

        initInputs();
        setEmpty();
        bSubmit.setVisibility(View.VISIBLE);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
                if(isValidDetails) {
                    toDatabase("create");
                }
            }
        });
    }

    private void onUpdateClick()    {

        showDetailsforUpdate();
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
                if(isValidDetails) {
                    toDatabase("update");
                }
            }
        });
    }

    private void onDeleteClick()    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure, you want to delelte?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDatabase("delete");
                    }
                })
                .setNegativeButton("no",null)
                .create()
                .show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    if(spinner.getSelectedItem().equals("Select your created alert"))  {
                        Snackbar.make(v,"Select your alert first",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    onDeleteClick();
                    menuYellow.close(true);
                    break;
                case R.id.fab2:
                    if(spinner.getSelectedItem().equals("Select your created alert"))  {
                        Snackbar.make(v,"Select your alert first",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    onUpdateClick();
                    menuYellow.close(true);
                    break;
                case R.id.fab3:
                    onCreateClick();
                    menuYellow.close(true);
                    break;
            }
        }
    };

    private void getData() {
        requestQueue.add(getDataFromServer());
    }

    private JsonArrayRequest getDataFromServer() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.JOBLALERT_SPINNER_REQUEST_URL + String.valueOf(user_id),
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
            getArraySize = array.length();
            JSONObject obj;
            try {
                obj = array.getJSONObject(i);
                data = obj.getString("jTitle");
                alertsList.add(data);
                alertTitle.add(obj.getString("jTitle"));
                alertDesc.add(obj.getString("jDesc"));
                alertComp.add(obj.getString("jCompany"));
                alertLoc.add(obj.getString("jLoc"));
                alertDesig .add(obj.getString("jDesig"));
                alertSkills.add(obj.getString("jSkills"));
                alertSalary.add(obj.getString("jSalary"));
                alertVacancy.add(obj.getString("jVacancy"));
                alertDuration.add(obj.getString("jDuration"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(alertsList.size() == 1){
            tvInfo = (TextView) getActivity().findViewById(R.id.job_desc);
            tvInfo.setVisibility(View.VISIBLE);
        }else {
            tvInfo = (TextView) getActivity().findViewById(R.id.job_desc);
            tvInfo.setVisibility(View.GONE);
        }
    }

    void populate_list()
    {
        try {
            alertsList.clear();
            alertsList.add("Select your created alert");
            getData();

            spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, alertsList));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {

                            String temp = (String) spinner.getSelectedItem();

                            if(!temp.equals("Select your created alert"))  {

                                int id = spinner.getSelectedItemPosition()-1;
                                title = alertTitle.get(id);
                                desc = alertDesc.get(id);
                                comp = alertComp.get(id);
                                loc = alertLoc.get(id);
                                desig = alertDesig.get(id);
                                skills = alertSkills.get(id);
                                salary = alertSalary.get(id);
                                vacancy = alertVacancy.get(id);
                                duration = alertDuration.get(id);
                                showDetails();
                            }   else {
                                linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
                                if(linearLayout.getVisibility() == View.VISIBLE)
                                    linearLayout.setVisibility(View.GONE);
                            }
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