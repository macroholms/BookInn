package com.example.bookinn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity{

    private final String MY_SETTINGS = "prefs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Crypt crypt = new Crypt();

        SharedPreferences prefs = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= null;
                if (prefs.getAll().isEmpty()){
                    intent = new Intent(SplashScreen.this,
                            OnboardingActivity.class);
                }
                else if(prefs.getAll().get("firstStart").equals("Login")) {
                    intent = new Intent(SplashScreen.this,
                            Login.class);
                }
                else if (prefs.getAll().get("firstStart").equals("Authorized")) {
                    intent = new Intent(SplashScreen.this,
                            MainActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
