package com.charolia.gadde.ess.Activity;

/**
 * Created by Administrator on 3/28/2017.
 */

public class ForumActivityExpandedData {
    private int id;
    private String forum_reply,forum_post;

    public ForumActivityExpandedData(int id, String forum_reply,String forum_post) {
        this.id = id;
        this.forum_reply = forum_reply;
        this.forum_post = forum_post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForum_reply() {
        return forum_reply;
    }

    public void setForum_reply(String job_desc) {
        this.forum_reply = forum_reply;
    }

    public String getForum_post() {
        return forum_post;
    }

    public void setForum_post(String job_title) {
        this.forum_post = forum_post;
    }
}
