package com.lenovo.smarttraffic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private int isImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /*InitData();
        if (isImage == 0){
            InitView();
        }else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }*/
       InitView();
    }

    private void InitData() {
        preferences = getSharedPreferences("MyAppCompatActivity", MODE_PRIVATE);
        editor = preferences.edit();
        isImage = preferences.getInt("isImage",0);
    }


    private void InitView() {
        InitApp.getHandler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        },1000);
    }
}
