package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final EditText text_phone        = (EditText) view.findViewById(R.id.etPhone_home);
        final EditText text_email       = (EditText) view.findViewById(R.id.etEmail_home);

        //Fetching values from shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        final String phone = sharedPreferences.getString(Config.PHONE_SHARED_PREF,"Not Available");
        final String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        text_phone.setText(phone);
        text_email.setText(email);


        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
    }*/

}
