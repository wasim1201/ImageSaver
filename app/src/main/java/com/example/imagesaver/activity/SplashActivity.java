package com.example.imagesaver.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.imagesaver.R;
import com.example.imagesaver.Util.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent homePage = new Intent(SplashActivity.this, HomePageActivity.class);
                SplashActivity.this.startActivity(homePage);
                SplashActivity.this.finish();
            }
        }, Constants.SPLASH_DISPLAY_LENGTH);
    }
}
