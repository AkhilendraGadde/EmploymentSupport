package com.charolia.gadde.ess.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charolia.gadde.ess.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    public CategoryFragment() {
        // Required empty public constructor
    }


    private List<Category> mCat_data;
    private CategoryViewAdapter mCategoryViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        setHasOptionsMenu(true);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_job_cat);
        mRecyclerView.setHasFixedSize(true);
        //final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        //mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);

        initializeData();
        mCategoryViewAdapter = new CategoryViewAdapter(getContext(),mCat_data);
        mRecyclerView.setAdapter(mCategoryViewAdapter);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    private void initializeData(){
        mCat_data = new ArrayList<>();
        mCat_data.add(new Category("Company", "Search jobs by Company", R.drawable.ic_job));
        mCat_data.add(new Category("Designation", "Search jobs by Designation", R.drawable.ic_job));
        mCat_data.add(new Category("Types", "Search jobs by different types", R.drawable.ic_job));
        mCat_data.add(new Category("Skills", "Search jobs by required skills", R.drawable.ic_job));
        mCat_data.add(new Category("Location", "Search jobs by vacancy", R.drawable.ic_job));
        mCat_data.add(new Category("Salary", "Search jobs by vacancy", R.drawable.ic_job));
        mCat_data.add(new Category("Vacancy", "Search jobs by vacancy", R.drawable.ic_job));
    }

}

class Category {
    String name;
    String desc;
    int photoId;

    Category(String name, String desc, int photoId) {
        this.name = name;
        this.desc = desc;
        this.photoId = photoId;
    }
}

class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.ViewHolder>{

    private Context context;
    private List<Category> mCategory;

    public CategoryViewAdapter(Context context, List<Category> mCategory) {
        this.context = context;
        this.mCategory = mCategory;
    }

    @Override
    public CategoryViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_job_cat,parent,false);
        return new CategoryViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewAdapter.ViewHolder holder, int position) {

        holder.cat_name.setText(mCategory.get(position).name);
        //holder.cat_desc.setText(mCategory.get(position).desc);
        holder.img.setImageResource(mCategory.get(position).photoId);

        //holder.setnTitle(mCategory.get(position).name);
        //holder.setDesc(mCategory.get(position).desc);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cView;
        TextView cat_name;
        TextView cat_desc;
        ImageView img;
        String mTitle,mDesc;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);// onclick
            cView = (CardView)itemView.findViewById(R.id.cat_card);
            cat_name = (TextView)itemView.findViewById(R.id.cat_name);
            cat_desc = (TextView)itemView.findViewById(R.id.cat_desc);
            img = (ImageView)itemView.findViewById(R.id.img_job);
        }
        /*public void setnTitle(String item) {
            mTitle = item;
        }*/

        /*public void setDesc(String item) {
            mDesc = item;
        }*/

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(v.getContext(), SearchCategoryActivity.class);
            //myIntent.putExtra("title", mTitle);
            //myIntent.putExtra("description", mDesc);
            context.startActivity(myIntent);
        }
    }

}
