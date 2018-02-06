package com.charolia.gadde.ess;

/**
 * Created by Administrator on 2/9/2017.
 */

public class Config {
    public static final String OTP_URL = "https://akhilendragadde17.000webhostapp.com/sendsms.php";
    public static final String REGISTER_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/nnRegister.php";
    public static final String LOGIN_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/nnLogin.php";
    public static final String JOBLALERT_SPINNER_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/test/jobalerts_spinner.php?jPost_id=";
    public static final String JOBLALERT_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/test/jobalerts.php";
    public static final String RESUME_SPINNER_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/test/resume_spinner.php?user_id=";
    public static final String RESUME_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/test/resume.php";
    public static final String JOBS_LIST_URL = "https://akhilendragadde17.000webhostapp.com/jobs.php?id=";
    public static final String FORUM_QLIST_URL = "https://akhilendragadde17.000webhostapp.com/test/forum_qlist.php?id=";
    public static final String QUERY_URL = "https://akhilendragadde17.000webhostapp.com/test/forum_query.php";
    public static final String FORUM_RLIST_URL = "https://akhilendragadde17.000webhostapp.com/test/forum_rlist.php?query=";
    public static final String REPLY_URL = "https://akhilendragadde17.000webhostapp.com/test/forum_reply.php";
    public static final String FEEDBACK_LIST_URL = "https://akhilendragadde17.000webhostapp.com/test/feedback_qlist.php?id=";
    public static final String FEEDBACK_URL = "https://akhilendragadde17.000webhostapp.com/test/feedback_query.php";
    public static final String RECRUITER_REQUEST_URL = "https://akhilendragadde17.000webhostapp.com/test/recruiter_data.php?user_id=";
    public static final String CHK_RESUME_URL = "https://akhilendragadde17.000webhostapp.com/test/checkResume.php";
    public static final String PROFILE_UPDATE_URL = "https://akhilendragadde17.000webhostapp.com/test/profile.php";

    // Support Mail
    public static final String CONTACT_EMAIL = "mailto:empsupess1725@gmail.com";
    public static final String CONTACT_SUBJECT = "Support:Employment Support App";
    public static final String GIT_HUB = "https://github.com/AkhilendraGadde/EmploymentSupport";

    //Keys as defined in our $_POST['key'] in login.php & register.php
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TYPE = "type";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_UID = "user_id";
    public static final String KEY_DOB = "dob";
    public static final String KEY_LOC = "location";
    public static final String KEY_QUERY = "query";
    public static final String KEY_REPLY = "reply";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "UserLoginActivity";

    //This would be used to store current logged in user
    public static final String EMAIL_SHARED_PREF = "email";
    public static final String NAME_SHARED_PREF = "name";
    public static final String USERNAME_SHARED_PREF = "username";
    public static final String TYPE_SHARED_PREF = "type";
    public static final String PASSWORD_SHARED_PREF = "password";
    public static final String UID_SHARED_PREF = "user_id";
    public static final String PHONE_SHARED_PREF = "phone";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}