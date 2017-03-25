package com.charolia.gadde.ess.Fragments;

/**
 * Created by Administrator on 3/25/2017.
 */

public class ForumFragmentData {
    private int id;
    private String forum_query,forum_post;

    public ForumFragmentData(int id, String forum_query,String forum_post) {
        this.id = id;
        this.forum_query = forum_query;
        this.forum_post = forum_post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForum_query() {
        return forum_query;
    }

    public void setForum_query(String job_desc) {
        this.forum_query = forum_query;
    }

    public String getForum_post() {
        return forum_post;
    }

    public void setForum_post(String job_title) {
        this.forum_post = forum_post;
    }

}
