package com.example.ia2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Timer;
import java.util.*;


public class LoginScreen extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


      /*  try {
            mHandler.removeCallbacks(mUpdateTimerTask);
            mHandler.postDelayed(mUpdateTimerTask, 5000);//this delay is only for initial startup

        } catch (Exception e) {
        }*/
    }

 /*   public Runnable mUpdateTimerTask = new Runnable() {
        @Override
        public void run() {
            ((EditText) findViewById(R.id.editText)).setText(Calendar.getInstance().getTime().toString());
            mHandler.postDelayed(this, 3000);
        }
    };*/


    public void onClick (View v){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }
    }




