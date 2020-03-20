package com.example.ia2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class CreateSafeAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_safe_area);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onMakeNewSafeArea(View view) {
        SafeArea safeArea = new SafeArea();

        EditText latitude = findViewById(R.id.editText12);
        double actualLatitude = Double.parseDouble(latitude.getText().toString());
        safeArea.setLat(actualLatitude);

        EditText longitude = findViewById(R.id.editText13);
        double actualLongitude = Double.parseDouble(longitude.getText().toString());
        safeArea.setLongitude(actualLongitude);

        EditText radius = findViewById(R.id.editText14);
        double actualRadius = Double.parseDouble(radius.getText().toString());
        safeArea.setRadius(actualRadius);

        EditText circleId = findViewById(R.id.editText10);
        String actualCircleID = circleId.getText().toString();
        safeArea.setCircleID(actualCircleID);

        EditText name = findViewById(R.id.editText2);
        String actualName = name.getText().toString();
        safeArea.setName(actualName);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference safeAreaCollection = database.collection("SafeAreas");

        safeAreaCollection.add(safeArea);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
