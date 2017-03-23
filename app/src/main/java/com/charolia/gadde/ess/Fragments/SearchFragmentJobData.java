package com.charolia.gadde.ess.Fragments;

/**
 * Created by Administrator on 3/5/2017.
 */

public class SearchFragmentJobData {

    private int id;
    private String job_desc,job_title,job_company,job_location;

    public SearchFragmentJobData(int id, String job_title,String job_desc, String job_company, String job_location) {
        this.id = id;
        this.job_desc = job_desc;
        this.job_title = job_title;
        this.job_company = job_company;
        this.job_location = job_location;
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

    public String getJob_company() {
        return job_company;
    }

    public void setJob_company(String job_company) {
        this.job_company = job_company;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }
}
