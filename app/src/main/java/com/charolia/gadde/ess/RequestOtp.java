package com.charolia.gadde.ess;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2/9/2017.
 */

public class RequestOtp extends AsyncTask<Void, Void, String> {

    private final String mobile;
    private final String otp;
    private final Context Ctx;

    public RequestOtp(String Mobile, String OTP, Context ctx)
    {
        mobile = Mobile;
        otp = OTP;
        Ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        String sendsms = Config.OTP_URL;
        try {
            String response;
            String data = URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" + URLEncoder.encode("msg", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8");
            URL url = new URL(sendsms);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                sb.append(response);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(final String success)
    {
        try {
            if(success.equals("SENT OTP."))
            {
                Toast.makeText(Ctx, "SUCCESS in sending OTP", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(Ctx,success, Toast.LENGTH_LONG).show();
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
}
