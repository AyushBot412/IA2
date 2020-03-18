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

        Map<String, Object> user = new HashMap<>();

        EditText name = findViewById(R.id.editText8);
        String actualName = name.getText().toString();
        user.put("Name", actualName);

        EditText Username = findViewById(R.id.editText5);
        String actualUserName = Username.getText().toString();
        user.put("Username", actualUserName);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        EditText Password = findViewById(R.id.editText6);
        String actualPassword = Password.getText().toString();
        user.put("Password", actualPassword);

        EditText circleID = findViewById(R.id.editText7);
        String actualCircleID = circleID.getText().toString();
        user.put("circleID", actualCircleID);

        RadioGroup rg = (findViewById(R.id.radioGroup));
        int selectedID = rg.getCheckedRadioButtonId();
        RadioButton rb = findViewById(selectedID);
        String userType = rb.getText().toString();


        if (userType.equals("Admin")) {
            user.put("isAdmin", true);
            if(validateCircleIDForAdmin() == false) {
                Snackbar.make(view, "Invalid Circle ID. Circle ID must be unique.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        else {
            user.put("isAdmin", false);
            if(validateCircleIDForRegularUser() == false) {
                Snackbar.make(view, "Invalid Circle ID. Provide an admin-given Circle ID.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        database.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("1", "DocumentSnapshot added with ID: " + documentReference.getId());
                        sendToMainActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("2", "Error adding document", e);
                    }
                });

    }
    public void sendToMainActivity () {
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
