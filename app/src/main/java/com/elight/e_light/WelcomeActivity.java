package com.elight.e_light;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Fabric.with(this, new Crashlytics());

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 Intent createAccount = new Intent(WelcomeActivity.this,HomeActivity.class);

                 startActivity(createAccount);
                 finish();
             }
         },3000);
    }
}


