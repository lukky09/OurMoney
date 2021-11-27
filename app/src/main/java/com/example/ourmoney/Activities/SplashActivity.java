package com.example.ourmoney.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.ourmoney.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPref = this.getSharedPreferences("setting", this.MODE_PRIVATE);
        Intent i;
        String key = "guntingmengguntingtuit";
        if (sharedPref.getString("name", key).equals(key)) {
            i = new Intent(SplashActivity.this, NameRequestActivity.class);
        }else{
            i = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(i);
        finish();
    }
}