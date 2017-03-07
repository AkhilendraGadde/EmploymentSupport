package com.charolia.gadde.ess.Fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
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

class SearchFragmentJobAdapter extends RecyclerView.Adapter<SearchFragmentJobAdapter.ViewHolder> {

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

        holder.description.setText(job_data.get(position).getJob_desc());
        holder.title.setText(job_data.get(position).getJob_title());
        // onclick
        holder.setItem(job_data.get(position).getJob_title());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return job_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String mItem;// onclick
        public TextView description;
        public TextView title;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);// onclick
            title = (TextView) itemView.findViewById(R.id.tvJobTitle);
            description = (TextView) itemView.findViewById(R.id.tvJobDesc);
            imageView = (ImageView) itemView.findViewById(R.id.img_job);
        }
        // onclick----------
        public void setItem(String item) {
            mItem = item;
        }
        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Selected Job : "+getAdapterPosition() + " " +mItem, Snackbar.LENGTH_LONG).show();
        }
        // -----------onclick
    }
}
