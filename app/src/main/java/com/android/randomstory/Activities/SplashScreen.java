package com.android.randomstory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.R;
import com.google.firebase.FirebaseApp;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        Paper.init(getApplicationContext());

        // to pause the screen for 1 sec
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, WelcomActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
}