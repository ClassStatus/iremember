package com.socialmedianetworking.iremember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.socialmedianetworking.iremember.OnboardingActivity;
import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.room.AppDatabase;
import com.socialmedianetworking.iremember.room.DAO;
import com.socialmedianetworking.iremember.util.UserSession;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds
    private UserSession session;
    private HashMap<String, String> user;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        session = new UserSession(getApplicationContext());
        session.isLoggedIn();
        user = session.getUserDetails();
        dao = AppDatabase.getDb(this).getDAO();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isFirstTime = session.isFirstTimeLaunch();

                if (isFirstTime) {
                    session.setFirstTimeLaunch(false);
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, Login.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}