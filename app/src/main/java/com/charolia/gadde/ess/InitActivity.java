package com.charolia.gadde.ess;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class InitActivity extends AppCompatActivity {
    private int SPLASH_TIMER = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to remove the action bar (title bar)
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                final Intent mainIntent = new Intent(InitActivity.this, LoginActivity.class);
                InitActivity.this.startActivity(mainIntent);
                InitActivity.this.finish();
            }
        }, SPLASH_TIMER);
    }
}
