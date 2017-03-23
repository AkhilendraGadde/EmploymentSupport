package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.LoginActivity;
import com.charolia.gadde.ess.R;
import com.charolia.gadde.ess.UserActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {


    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_logout, container, false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        final Button bLogout = (Button) view.findViewById(R.id.bLogoutYes);
        final Button bCancel = (Button) view.findViewById(R.id.bLogoutNo);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                ((UserActivity )  getActivity()).ShowActionBar();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((UserActivity )  getActivity()).refreshContent();
                ((UserActivity )  getActivity()).ShowActionBar();
            }
        });

        return view;
    }

    private void logout(){
            //Getting out sharedpreferences
            SharedPreferences preferences = getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            //Getting editor
            SharedPreferences.Editor editor = preferences.edit();

            //Puting the value false for loggedin
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

            //Putting blank value to prefs
            editor.putString(Config.NAME_SHARED_PREF, "");
            editor.putString(Config.PASSWORD_SHARED_PREF, "");
            editor.putString(Config.UID_SHARED_PREF, "");
            editor.putString(Config.USERNAME_SHARED_PREF, "");
            editor.putString(Config.EMAIL_SHARED_PREF, "");
            editor.putString(Config.PHONE_SHARED_PREF, "");
            editor.putString(Config.TYPE_SHARED_PREF, "");

            //Saving the sharedpreferences
            editor.commit();

            //Starting login activity
            Intent intent = new Intent(((UserActivity) getActivity()), LoginActivity.class);
            startActivity(intent);
            ((UserActivity) getActivity()).finish();
    }

}
