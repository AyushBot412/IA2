package com.example.ia2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onCreateUser(View view) {

        //TODO Error Handling
        //Username, Name and Password should have their own prerequisites.

        User user = new User();


        EditText name = findViewById(R.id.editText8);
        String actualName = name.getText().toString();
        user.setName(actualName);

        EditText Username = findViewById(R.id.editText5);
        String actualUserName = Username.getText().toString();
        user.setUsername(actualUserName);


        EditText Password = findViewById(R.id.editText6);
        String actualPassword = Password.getText().toString();
        user.setPassword(actualPassword);

        EditText circleID = findViewById(R.id.editText7);
        String actualCircleID = circleID.getText().toString();
        user.setCircleId(actualCircleID);

        RadioGroup rg = (findViewById(R.id.radioGroup));
        int selectedID = rg.getCheckedRadioButtonId();
        RadioButton rb = findViewById(selectedID);
        String userType = rb.getText().toString();


        if (userType.equals("Admin")) {
            user.setAdmin(true);
            if(validateCircleIDForAdmin() == false) {
                Snackbar.make(view, "Invalid Circle ID. Circle ID must be unique.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        else {
            user.setAdmin(false);
            if(validateCircleIDForRegularUser() == false) {
                Snackbar.make(view, "Invalid Circle ID. Provide an admin-given Circle ID.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Users").document(user.getUsername()).set(user);
        //this collects the user's info and creates a user object with a key according to the provided username

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //TODO make sure to validate all admin info
    public boolean validateCircleIDForAdmin () {
        //check for empty string, ensure circle id is unique
        return true;

    }
    //TODO make sure to validate regular user
    public boolean validateCircleIDForRegularUser () {
        //check for empty string, ensure circle id is existing admin ID

        return true;
    }

}
