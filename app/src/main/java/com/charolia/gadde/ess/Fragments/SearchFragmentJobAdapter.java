package com.charolia.gadde.ess.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.jCompany.setText(job_data.get(position).getJob_company());
        holder.jTitle.setText(job_data.get(position).getJob_title());
        holder.jLocation.setText(job_data.get(position).getJob_locationy());
        // onclick
        holder.setjTitle(job_data.get(position).getJob_title());
        holder.setjCompany(job_data.get(position).getJob_company());
        holder.setjLocation(job_data.get(position).getJob_locationy());
        holder.setjDesc(job_data.get(position).getJob_desc());
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

        private String mTitle,mDesc,mComp,mLoc;

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
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), SearchJobActivityExpanded.class);
            myIntent.putExtra("title", mTitle);
            myIntent.putExtra("description", mDesc);
            myIntent.putExtra("company", mComp);
            myIntent.putExtra("location", mLoc);
            context.startActivity(myIntent);
        }
    }
}
