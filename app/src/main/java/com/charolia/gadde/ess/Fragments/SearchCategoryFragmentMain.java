package com.charolia.gadde.ess.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charolia.gadde.ess.Activity.SearchCategoryActivity;
import com.charolia.gadde.ess.Activity.SearchJobActivity;
import com.charolia.gadde.ess.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCategoryFragmentMain extends Fragment {


    public SearchCategoryFragmentMain() {
        // Required empty public constructor
    }


    private List<Category> mCat_data;
    private CategoryViewAdapter mCategoryViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        setHasOptionsMenu(true);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_job_cat);
        mRecyclerView.setHasFixedSize(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        ((SearchJobActivity)getActivity()).onBackPressed();
        return true;
    }

    private void initializeData(){
        mCat_data = new ArrayList<>();
        mCat_data.add(new Category("Title", R.drawable.ic_job));
        mCat_data.add(new Category("Company", R.drawable.ic_job));
        mCat_data.add(new Category("Location", R.drawable.ic_job));
        mCat_data.add(new Category("Designation", R.drawable.ic_job));
        mCat_data.add(new Category("Skills", R.drawable.ic_job));
        mCat_data.add(new Category("Salary", R.drawable.ic_job));
        mCat_data.add(new Category("Vacancy", R.drawable.ic_job));
        mCat_data.add(new Category("Duration", R.drawable.ic_job));
    }

    class Category {
        String name;
        int photoId;

        Category(String name, int photoId) {
            this.name = name;
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
            holder.img.setImageResource(mCategory.get(position).photoId);

            holder.getName(mCategory.get(position).name);
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
            String mName;

            ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                cView = (CardView)itemView.findViewById(R.id.cat_card);
                cat_name = (TextView)itemView.findViewById(R.id.cat_name);
                cat_desc = (TextView)itemView.findViewById(R.id.cat_desc);
                img = (ImageView)itemView.findViewById(R.id.img_job);
            }
            public void getName(String item) {
                mName = item;
            }

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SearchCategoryActivity.class);
                myIntent.putExtra("Name", mName);
                context.startActivity(myIntent);
            }
        }
    }
}




