package com.example.ia2;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Object GeoLocater;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    public void locationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //the if statement checks whether or not the perms have been granted

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        }
    }


    @Override
    public void onRequestPermissionsResult(int request, String[] permissions, int[] grantResults) {
        switch (request) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(button, "You granted access to view this device's location.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                } else {
                    //this is in reference to the deny button and the snackbar will only
                    //show up when you click deny.



                    //TODO make an intent to send them back to the login/register page


                   Snackbar.make(button, "You denied permission.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                }

                }
                return;
            }


    }
    //TODO fix the current place reciever
/*
    List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
    public FindCurrentPlaceRequest () {

    }

    // Use the builder to create a FindCurrentPlaceRequest.
    public static FindCurrentPlaceRequest.Builder builder (List<Place.Field> placeFields){

    }
    public static FindCurrentPlaceRequest newInstance (List<Place.Field> placeFields)
    FindCurrentPlaceRequest request =
            FindCurrentPlaceRequest.newInstance(placeFields);



*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationPermission();

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                openSecondActivity();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Fly, you fool.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    public void openSecondActivity() {
        Intent intent = new Intent(this, GeoLocater.class);
        startActivity(intent);
    }


    public void Locate(View v) {
        String ur = "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBFWN6L8d-rvFKj15Jg1IqdxkITBLdB0nI";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ur,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());

                        TextView textView = (TextView) findViewById(R.id.abc);
                        textView.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());

                    }
                }


        );

        requestQueue.add(jsonObjectRequest);
    }



}