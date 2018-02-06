package com.charolia.gadde.ess;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2/1/2017.
 */

public class RegisterRequest extends StringRequest {

    private Map<String, String> params;

    public RegisterRequest(String name, String username, String password, String email,String gender, String phone, String dob, String location, String type, String otp,  Response.Listener<String> listener) {
        super(Method.POST, Config.REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(Config.KEY_NAME, name);
        params.put(Config.KEY_USERNAME, username);
        params.put(Config.KEY_PASSWORD, password);
        params.put(Config.KEY_EMAIL, email);
        params.put(Config.KEY_GENDER, gender);
        params.put(Config.KEY_PHONE, phone);
        params.put(Config.KEY_DOB, dob);
        params.put(Config.KEY_LOC, location);
        params.put(Config.KEY_TYPE, type);
        params.put(Config.KEY_UID, otp);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
