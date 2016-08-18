package com.example.luos.cst_project.View;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.luos.cst_project.R;

public class WelcomActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcom);
        getSupportActionBar().hide();
        initView();
    }

    public void initView() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            public void run() {
                // 进入登录页面
                goToLoginActivity();
            }
        }, 1000);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(WelcomActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
