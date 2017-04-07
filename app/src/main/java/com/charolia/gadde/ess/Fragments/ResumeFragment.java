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
import android.text.method.KeyListener;
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
import com.charolia.gadde.ess.RegisterActivity;
import com.charolia.gadde.ess.UserActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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
public class ResumeFragment extends Fragment {

    boolean isValidDetails = false;

    private Spinner spinner;
    private ArrayList<String> resumeList = new ArrayList<String>();
    private RequestQueue requestQueue;
    private String user_id,data;
    private FloatingActionMenu menuYellow;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private TextView tvInfo;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    private LinearLayout linearLayout;
    EditText etAadhaar,etQual,etSkills,etProj,etAch,etHobb,etExp;
    String aadhaar,qual,skills,proj,ach,hobb,exp;
    Button bSubmit;

    public ResumeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(Config.UID_SHARED_PREF,"Not Available");

        spinner = (Spinner) view.findViewById(R.id.resumeList);
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
                //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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

    private void initInputs()   {

        etAadhaar = (EditText) getActivity().findViewById(R.id.etAadhar);
        etQual = (EditText) getActivity().findViewById(R.id.etQual);
        etSkills = (EditText) getActivity().findViewById(R.id.etSkills);
        etProj = (EditText) getActivity().findViewById(R.id.etProjects);
        etAch = (EditText) getActivity().findViewById(R.id.etAchievements);
        etHobb = (EditText) getActivity().findViewById(R.id.etHobbies);
        etExp = (EditText) getActivity().findViewById(R.id.etWExp);
        bSubmit = (Button) getActivity().findViewById(R.id.bSubmit);

    }

    private void validateDetails(){

        String isValid = "";

        boolean isComplete = isComplete(etAadhaar.getText().toString());

        // check Exp
        if (etExp.getText().toString().equals("")) {
            etExp.setError("Please enter your Work Experience (type none, if you have no work experience)");
            etExp.requestFocus();
            isValid = "no";
        }
        // check Hobb
        if (etHobb.getText().toString().equals("")) {
            etHobb.setError("Please enter your Hobbies.");
            etHobb.requestFocus();
            isValid = "no";
        }
        // check Ach
        if (etAch.getText().toString().equals("")) {
            etAch.setError("Please enter your Achievements.");
            etAch.requestFocus();
            isValid = "no";
        }
        // check Proj
        if (etProj.getText().toString().equals("")) {
            etProj.setError("Please enter your Projects.");
            etProj.requestFocus();
            isValid = "no";
        }
        // check Skills
        if (etSkills.getText().toString().equals("")) {
            etSkills.setError("Please enter your Skills.");
            etSkills.requestFocus();
            isValid = "no";
        }
        // check Qual
        if (etQual.getText().toString().equals("")) {
            etQual.setError("Please enter your Qualifications");
            etQual.requestFocus();
            isValid = "no";
        }
        // Check valid aadhaar info
        if (etAadhaar.getText().toString().equals("")) {
            etAadhaar.setError("Please enter your aadhaar number.");
            etAadhaar.requestFocus();
            isValid = "no";
        }else if(!isComplete){
            etAadhaar.setError("Enter valid number ( 12 digit aadhaar number)");
            etAadhaar.requestFocus();
            isValid = "no";
        }
        // Check if valid
        if(!isValid.equals("no")){
            isValidDetails = true;
        }

    }
    boolean isComplete(String str) {
        if(str.length() != 12)
            return false;
        else
            return true;
    }

    private void toDatabase(final String str) {
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Processing", "Please wait...", false, false);

        aadhaar = etAadhaar.getText().toString();
        qual = etQual.getText().toString();
        skills = etSkills.getText().toString();
        proj = etProj.getText().toString();
        ach = etAch.getText().toString();
        hobb = etHobb.getText().toString();
        exp = etExp.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.RESUME_REQUEST_URL,
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
                                            ResumeFragment fragment = (ResumeFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
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

                params.put("aadhaar", aadhaar);
                params.put("qual", qual);
                params.put("skills", skills);
                params.put("proj", proj);
                params.put("ach", ach);
                params.put("hobb", hobb);
                params.put("exp", exp);
                params.put("context", str);
                params.put("uid", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void showDetails()  {
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
        linearLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_about_card_show);
        linearLayout.startAnimation(animation);
        tvInfo = (TextView) getActivity().findViewById(R.id.res_desc);
        try {
            if(tvInfo.getVisibility() == View.VISIBLE)
                tvInfo.setVisibility(View.GONE);
        } catch ( Exception e){
            e.printStackTrace();
        }

        initInputs();
        etAadhaar.setText(aadhaar);
        etAadhaar.setFocusable(false);
        etQual.setText(qual);
        etQual.setFocusable(false);
        etProj.setText(proj);
        etProj.setFocusable(false);
        etSkills.setText(skills);
        etSkills.setFocusable(false);
        etExp.setText(exp);
        etExp.setFocusable(false);
        etAch.setText(ach);
        etAch.setFocusable(false);
        etHobb.setText(hobb);
        etHobb.setFocusable(false);
        bSubmit.setVisibility(View.GONE);
    }

    private void showDetailsforUpdate()  {
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
        linearLayout.setVisibility(View.VISIBLE);
        tvInfo = (TextView) getActivity().findViewById(R.id.res_desc);
        try{
            if(tvInfo.getVisibility() == View.VISIBLE)
                tvInfo.setVisibility(View.GONE);
        } catch (Exception e){
            e.printStackTrace();
        }

        initInputs();
        etAadhaar.setText(aadhaar);
        etAadhaar.setFocusable(true);
        etAadhaar.setFocusableInTouchMode(true);
        etQual.setText(qual);
        etQual.setFocusable(true);
        etQual.setFocusableInTouchMode(true);
        etProj.setText(proj);
        etProj.setFocusable(true);
        etProj.setFocusableInTouchMode(true);
        etSkills.setText(skills);
        etSkills.setFocusable(true);
        etSkills.setFocusableInTouchMode(true);
        etExp.setText(exp);
        etExp.setFocusable(true);
        etExp.setFocusableInTouchMode(true);
        etAch.setText(ach);
        etAch.setFocusable(true);
        etAch.setFocusableInTouchMode(true);
        etHobb.setText(hobb);
        etHobb.setFocusable(true);
        etHobb.setFocusableInTouchMode(true);
        bSubmit.setVisibility(View.VISIBLE);
    }

    private void onCreateClick()    {

        linearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_inputs);
        linearLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_about_card_show);
        linearLayout.startAnimation(animation);
        tvInfo = (TextView) getActivity().findViewById(R.id.res_desc);
        try {
            if(tvInfo.getVisibility() == View.VISIBLE)
                tvInfo.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }

        initInputs();
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
                    if(spinner.getSelectedItem().equals("Select your created resume"))  {
                        Snackbar.make(v,"Select your resume first",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    onDeleteClick();
                    menuYellow.close(true);
                    break;
                case R.id.fab2:
                    if(spinner.getSelectedItem().equals("Select your created resume"))  {
                        Snackbar.make(v,"Select your resume first",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    onUpdateClick();
                    menuYellow.close(true);
                    break;
                case R.id.fab3:
                    if (resumeList.size() > 1)  {
                        Snackbar.make(v,"Resume already exists",Snackbar.LENGTH_LONG).show();
                        return;
                    }
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.RESUME_SPINNER_REQUEST_URL + String.valueOf(user_id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Click to create resume", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {
        Log.d("",String.valueOf(array));
        Log.d("",String.valueOf(array.length()));
        for (int i = 0; i < array.length(); i++) {

            JSONObject obj;
            try {
                obj = array.getJSONObject(i);
                aadhaar = obj.getString("aadhaar");
                qual = obj.getString("qual");
                skills = obj.getString("skills");
                proj = obj.getString("proj");
                ach = obj.getString("ach");
                hobb = obj.getString("hobb");
                exp = obj.getString("exp");

                data = obj.getString("aadhaar");
                resumeList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(resumeList.size() == 1){
            tvInfo = (TextView) getActivity().findViewById(R.id.res_desc);
            tvInfo.setVisibility(View.VISIBLE);
        }else {
            tvInfo = (TextView) getActivity().findViewById(R.id.res_desc);
            tvInfo.setVisibility(View.GONE);
        }
    }

    void populate_list()    {
        try {
            resumeList.clear();
            resumeList.add("Select your created resume");
            getData();
            Log.d("size " ,String.valueOf(resumeList.size()));

            spinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, resumeList));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {

                    String temp = (String) spinner.getSelectedItem();
                    if(!temp.equals("Select your created resume"))  {
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
        actionBar.setTitle("Manage Resume");
    }

}
