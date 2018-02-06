<<<<<<< HEAD
package com.charolia.gadde.ess;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2/1/2017.
 */

public class LoginRequest extends StringRequest {

    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, Config.LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(Config.KEY_USERNAME, username);
        params.put(Config.KEY_PASSWORD, password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
=======
package com.charolia.gadde.ess;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2/1/2017.
 */

public class LoginRequest extends StringRequest {

    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, Config.LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put(Config.KEY_USERNAME, username);
        params.put(Config.KEY_PASSWORD, password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
>>>>>>> f706976561161babd0bad7879af2a8e13e4cdf25
