package com.charolia.gadde.ess.Fragments;

/**
 * Created by Administrator on 3/5/2017.
 */

public class SearchFragmentJobData {

    private int id;
    private String job_desc,job_title,job_company,job_location,job_desig,job_skills,job_salary,job_vacancy,job_duration,job_pid;

    public SearchFragmentJobData(int id, String job_title,String job_desc, String job_company, String job_location,
                                String job_desig, String job_skills, String job_salary, String job_vacancy, String job_duration,
                                 String job_pid) {
        this.id = id;
        this.job_desc = job_desc;
        this.job_title = job_title;
        this.job_company = job_company;
        this.job_location = job_location;
        this.job_desig = job_desig;
        this.job_skills = job_skills;
        this.job_salary = job_salary;
        this.job_vacancy = job_vacancy;
        this.job_duration = job_duration;
        this.job_pid = job_pid;
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

    public String getJob_desig() {
        return job_desig;
    }
    public String getJob_skills() {
        return job_skills;
    }
    public String getJob_salary() {
        return job_salary;
    }
    public String getJob_vacancy() {
        return job_vacancy;
    }
    public String getJob_duration() {
        return job_duration;
    }
    public String getJob_pid() {
        return job_pid;
    }

}
