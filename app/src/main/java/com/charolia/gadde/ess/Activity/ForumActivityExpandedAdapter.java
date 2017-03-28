package com.charolia.gadde.ess.Activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charolia.gadde.ess.R;

import java.util.List;

/**
 * Created by Administrator on 3/28/2017.
 */

public class ForumActivityExpandedAdapter extends RecyclerView.Adapter<ForumActivityExpandedAdapter.ViewHolder> {

    private Context context;
    private List<ForumActivityExpandedData> data;

    public ForumActivityExpandedAdapter(Context context, List<ForumActivityExpandedData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_forum_reply,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.fReply.setText(data.get(position).getForum_reply());
        holder.fPost.setText(" - " + data.get(position).getForum_post());
        // onclick
        holder.setfReply(data.get(position).getForum_reply());
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

        private String mReply,mPost;

        TextView fReply,fPost;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);// onclick
            fReply = (TextView) itemView.findViewById(R.id.tvReply);
            fPost = (TextView) itemView.findViewById(R.id.tvPostRr);
        }
        // onclick----------

        public void setfReply(String item) {
            mReply = item;
        }

        public void setfPost(String item) {
            mPost = item;
        }
        @Override
        public void onClick(View view) {
            Snackbar.make(view,"Click Detected",Snackbar.LENGTH_LONG);
        }
    }
}

