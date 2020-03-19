package com.example.ia2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.*;


public class LoginScreen extends AppCompatActivity {
    public static User mLoggedInUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onLogin (final View v){
        EditText userName = findViewById(R.id.editText);
        String actualUserName = userName.getText().toString();

        EditText password = findViewById(R.id.editText4);
        final String actualPassword = password.getText().toString();

        if(actualUserName.isEmpty()) {
            Snackbar.make(v, "Username is incorrect or fields are empty; please enter in a valid username.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
            //this will just stop the app, but won't crash it. It will remain on the login page.
        }
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference userCollection = database.collection("Users");
        userCollection.document(actualUserName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot userDetailsDocument) {
                if(userDetailsDocument.exists()) {
                    mLoggedInUser = userDetailsDocument.toObject(User.class);
                    assert mLoggedInUser != null;
                    if (mLoggedInUser.getPassword().equals(actualPassword)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        if(actualPassword.isEmpty()) {
                            Snackbar.make(v, "Empty Password, please enter in details.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        else {
                            Snackbar.make(v, "Incorrect Password, please enter in details correctly.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
                else {
                    Snackbar.make(v, "Username is incorrect; please enter in a valid username.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            //TODO set up checknotifications for admin method
            //create a timer task to go to the database and check if there are any
            //new notifications in admin's personal family circle id the last five seconds which is current time minus 5 seconds
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void onNewUserRegistration(View view) {
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);

    }
}




