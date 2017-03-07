package com.charolia.gadde.ess.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charolia.gadde.ess.R;

import java.util.List;

/**
 * Created by Administrator on 3/5/2017.
 */

class SearchFragment$JobAdapter extends RecyclerView.Adapter<SearchFragment$JobAdapter.ViewHolder> {

    private Context context;
    private List<SearchFragment$JobData> job_data;

    public SearchFragment$JobAdapter(Context context, List<SearchFragment$JobData> job_data) {
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

        holder.description.setText(job_data.get(position).getJob_desc());
        holder.title.setText(job_data.get(position).getJob_title());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return job_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView description;
        public TextView title;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvJobTitle);
            description = (TextView) itemView.findViewById(R.id.tvJobDesc);
            imageView = (ImageView) itemView.findViewById(R.id.img_job);
        }

    }
}
