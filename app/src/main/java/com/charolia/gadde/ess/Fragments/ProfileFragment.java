package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String name, email, type;
    private TextView tvName,tvType,tvEmail;
    private CardView cardView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        name = sharedPreferences.getString(Config.NAME_SHARED_PREF,"Not Available");
        type = sharedPreferences.getString(Config.TYPE_SHARED_PREF,"Not Available");
        email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        cardView = (CardView) view.findViewById(R.id.card_profile);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeMeHome();
            }
        });

        tvName = (TextView) view.findViewById(R.id.tName);
        tvType = (TextView) view.findViewById(R.id.tType);
        tvEmail = (TextView) view.findViewById(R.id.tEmail);

        tvName.setText(name);
        tvType.setText(type);
        tvEmail.setText(email);

        return view;
    }

    public void takeMeHome()   {

        HomeFragment home = new HomeFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, home);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Profile");

        if(getView() == null){
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    (getActivity()).onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

}
