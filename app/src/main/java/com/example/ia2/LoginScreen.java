package com.example.ia2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.*;


public class LoginScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




    }





    public void onClick (View v){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    public void onLogin(View view) {
        EditText userName = findViewById(R.id.editText);
        String actualUserName = userName.getText().toString();

        EditText password = findViewById(R.id.editText4);
        String actualPassword = password.getText().toString();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String id = database.collection("users").getId();

    }
}




