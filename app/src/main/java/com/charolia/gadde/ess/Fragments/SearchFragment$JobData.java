package com.charolia.gadde.ess.Fragments;

/**
 * Created by Administrator on 3/5/2017.
 */

class SearchFragment$JobData {

    private int id;
    private String job_desc,job_title;

    public SearchFragment$JobData(int id, String job_desc, String job_title) {
        this.id = id;
        this.job_desc = job_desc;
        this.job_title = job_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }
}
