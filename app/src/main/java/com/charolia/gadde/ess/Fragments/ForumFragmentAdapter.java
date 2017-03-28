package com.charolia.gadde.ess.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.charolia.gadde.ess.Activity.ForumActivityExpanded;
import com.charolia.gadde.ess.Activity.SearchJobActivityExpanded;
import com.charolia.gadde.ess.R;

import java.util.List;

/**
 * Created by Administrator on 3/25/2017.
 */

public class ForumFragmentAdapter extends RecyclerView.Adapter<ForumFragmentAdapter.ViewHolder> {

    private Context context;
    private List<ForumFragmentData> data;

    public ForumFragmentAdapter(Context context, List<ForumFragmentData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_forum_query,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.fQuery.setText(data.get(position).getForum_query());
        holder.fPost.setText(" - " + data.get(position).getForum_post());
        // onclick
        holder.setfQuery(data.get(position).getForum_query());
        holder.setfPost(data.get(position).getForum_post());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String mQuery,mPost;
        TextView fQuery,fPost;

        public ViewHolder(View itemView) {
            super(itemView);
            Fragment f = ((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof FeedbackFragment)
                itemView.setOnClickListener(null);// onclickDisable
            else
                itemView.setOnClickListener(this);// onclick
            fQuery = (TextView) itemView.findViewById(R.id.tvQuery);
            fPost = (TextView) itemView.findViewById(R.id.tvPost);
        }
        // onclick----------

        public void setfQuery(String item) {
            mQuery = item;
        }

        public void setfPost(String item) {
            mPost = item;
        }
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), ForumActivityExpanded.class);
            myIntent.putExtra("query", mQuery);
            myIntent.putExtra("post_id", mPost);
            context.startActivity(myIntent);
        }
    }
}
