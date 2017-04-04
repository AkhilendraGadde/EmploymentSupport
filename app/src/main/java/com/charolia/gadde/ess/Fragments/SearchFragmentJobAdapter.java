package com.charolia.gadde.ess.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charolia.gadde.ess.Activity.SearchCategoryActivity;
import com.charolia.gadde.ess.Activity.SearchJobActivityExpanded;
import com.charolia.gadde.ess.R;

import java.util.List;

/**
 * Created by Administrator on 3/5/2017.
 */

public class SearchFragmentJobAdapter extends RecyclerView.Adapter<SearchFragmentJobAdapter.ViewHolder> {

    private Context context;
    private List<SearchFragmentJobData> job_data;

    public SearchFragmentJobAdapter(Context context, List<SearchFragmentJobData> job_data) {
        this.context = context;
        this.job_data = job_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_job,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.imageView.setImageResource(R.drawable.ic_job_loc_16);
        holder.jTitle.setText(job_data.get(position).getJob_title());
        holder.jLocation.setText(job_data.get(position).getJob_location());
        holder.jCompany.setText(job_data.get(position).getJob_company());

        if(context instanceof SearchCategoryActivity) {
            String title = ((SearchCategoryActivity) context).getIntent().getStringExtra("Name");
            if(title.equals("Title")){
                holder.jCompany.setText(job_data.get(position).getJob_title());
                holder.jTitle.setText(job_data.get(position).getJob_company());
            } else if(title.equals("Designation"))   {
                holder.jCompany.setText(job_data.get(position).getJob_desig());
                holder.jTitle.setText(job_data.get(position).getJob_title());
            } else if (title.equals("Company")){
                holder.jCompany.setText(job_data.get(position).getJob_company());
            } else if (title.equals("Skills")){
                holder.jCompany.setText(job_data.get(position).getJob_skills());
            } else if (title.equals("Salary")){
                holder.jCompany.setText(job_data.get(position).getJob_salary()+"/- pm");
            } else if (title.equals("Vacancy")){
                holder.jCompany.setText("Required Employees : "+job_data.get(position).getJob_vacancy());
                holder.jTitle.setText(job_data.get(position).getJob_company());
            } else if (title.equals("Duration")){
                holder.jCompany.setText(job_data.get(position).getJob_duration());
            }
        }
        // onclick
        holder.setjTitle(job_data.get(position).getJob_title());
        holder.setjCompany(job_data.get(position).getJob_company());
        holder.setjLocation(job_data.get(position).getJob_location());
        holder.setjDesc(job_data.get(position).getJob_desc());
        holder.setjDesig(job_data.get(position).getJob_desig());
        holder.setjSkills(job_data.get(position).getJob_skills());
        holder.setjSalary(job_data.get(position).getJob_salary());
        holder.setjVacancy(job_data.get(position).getJob_vacancy());
        holder.setjDuration(job_data.get(position).getJob_duration());
        holder.setjPid(job_data.get(position).getJob_pid());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return job_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String mTitle,mDesc,mComp,mLoc,mDesig,mSkills,mSalary,mVac,mDur,mPid;

        ImageView imageView;
        TextView jTitle,jCompany,jLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);// onclick
            imageView = (ImageView) itemView.findViewById(R.id.img_job);
            jTitle = (TextView) itemView.findViewById(R.id.tvJobTitle);
            jCompany = (TextView) itemView.findViewById(R.id.tvJobC_Name);
            jLocation = (TextView) itemView.findViewById(R.id.tvJobLoc);
        }
        // onclick----------
        public void setjCompany(String item) {
            mComp = item;
        }
        public void setjTitle(String item) {
            mTitle = item;
        }
        public void setjDesc(String item) {
            mDesc = item;
        }
        public void setjLocation(String item) {
            mLoc = item;
        }
        public void setjDesig(String item) {
            mDesig = item;
        }
        public void setjSkills(String item) {
            mSkills = item;
        }
        public void setjSalary(String item) {
            mSalary = item;
        }
        public void setjVacancy(String item) {
            mVac = item;
        }
        public void setjDuration(String item) {
            mDur = item;
        }
        public void setjPid(String item) {
            mPid = item;
        }
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), SearchJobActivityExpanded.class);
            myIntent.putExtra("title", mTitle);
            myIntent.putExtra("description", mDesc);
            myIntent.putExtra("company", mComp);
            myIntent.putExtra("location", mLoc);
            myIntent.putExtra("designation", mDesig);
            myIntent.putExtra("skills", mSkills);
            myIntent.putExtra("salary", mSalary);
            myIntent.putExtra("vacancy", mVac);
            myIntent.putExtra("duration", mDur);
            myIntent.putExtra("pid", mPid);
            context.startActivity(myIntent);
        }
    }
}
