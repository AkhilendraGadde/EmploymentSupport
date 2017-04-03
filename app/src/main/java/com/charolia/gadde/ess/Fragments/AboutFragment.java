package com.charolia.gadde.ess.Fragments;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.charolia.gadde.ess.Config;
import com.charolia.gadde.ess.R;
import com.charolia.gadde.ess.UserActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener  {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        CardView card_about = (CardView) view.findViewById(R.id.card_ab);
        CardView card_about_1 = (CardView) view.findViewById(R.id.card_about);
        CardView card_about_2 = (CardView) view.findViewById(R.id.card_about_new);
        LinearLayout ll_card_about_2_email = (LinearLayout) view.findViewById(R.id.ll_card_about_2_email);
        LinearLayout ll_card_about_2_git_hub = (LinearLayout) view.findViewById(R.id.ll_card_about_2_git_hub);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_about_card_show);
        card_about.startAnimation(animation);
        card_about_1.startAnimation(animation);
        card_about_2.startAnimation(animation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setStartOffset(1000);

        ll_card_about_2_email.setOnClickListener(this);
        ll_card_about_2_git_hub.setOnClickListener(this);

        TextView tv_about_version = (TextView) view.findViewById(R.id.tv_about_version);
        tv_about_version.setText(getVersionName());
        tv_about_version.startAnimation(alphaAnimation);

        return  view;
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();

        switch (view.getId()) {

            case R.id.ll_card_about_2_email:
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(Config.CONTACT_EMAIL));
                intent.putExtra(Intent.EXTRA_SUBJECT, Config.CONTACT_SUBJECT);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this.getActivity(), "Error finding email", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_card_about_2_git_hub:
                intent.setData(Uri.parse(Config.GIT_HUB));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
                break;
        }
    }

    public String getVersionName() {
        try {
            PackageManager manager = this.getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getActivity().getPackageName(), 0);
            String version = info.versionName;
            return "Version : " + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
