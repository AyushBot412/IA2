package com.example.ia2;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
/*
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.  libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
*/
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {
    private Button button;
    private Button yourLocationButton;
    private Object GeoLocater;
    private Button notificationButton;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;


    public void  locationPermission() {
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


                   Snackbar.make(button, "You denied permission.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        onClickToMainPage();


                }

                }
                return;
            }


    }

    public void onClickToMainPage () {
        Intent intent = new Intent (this, LoginScreen.class);
        startActivity(intent);
    }



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
        yourLocationButton = (Button) findViewById(R.id.button5);
        yourLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                openYourLocationActivity();
            }
        });
        notificationButton = findViewById(R.id.button6);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if(LoginScreen.mLoggedInUser.isAdmin()) {
                    checkAdminNotifications(v);
                }
                else {
                    checkRegularUserIsInSafeArea(v);

                }
                //notificationSend();
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

    public void checkAdminNotifications(final View v){
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                final CollectionReference notificationCollection = database.collection("Notifications");
                Date date = new Date(System.currentTimeMillis() - 1);

                Query query = notificationCollection.whereEqualTo("circleID", LoginScreen.mLoggedInUser.getCircleId())
                       .whereGreaterThan("notificationDateTime", date);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NotificationsRecord notificationsRecord = document.toObject(NotificationsRecord.class);

                               //TODO convert this to a notification

                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel("LimitBreaks", "LimitBreaks", NotificationManager.IMPORTANCE_DEFAULT);
                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(channel);
                                }

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "LimitBreaks")
                                        .setContentTitle("Safe Area Limit Breached")
                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                        .setAutoCancel(true)
                                        .setContentText(notificationsRecord.getNotificationMessage());
                                NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
                                manager.notify(999, builder.build());
                            }
                            ;
                        }
                    }
                });

                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(r, 1000);
    }
    public void checkRegularUserIsInSafeArea(final View view) {
        final Handler handler = new Handler();
        final SafeAreaCheckTask safeAreaCheckTask = new SafeAreaCheckTask();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                safeAreaCheckTask.check();
                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    public void openSecondActivity() {
        Intent intent = new Intent(this, GeoLocater.class);
        startActivity(intent);
    }
   
    public void openNotificationActivity () {
        Intent intent = new Intent (this, FirebaseActivity.class);
        startActivity(intent);
    }
    public void notificationSend() {
       Intent intent = new Intent(this, CreateNotification.class);
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


        SafeAreaCheckTask safeAreaCheckTask = new SafeAreaCheckTask();
        safeAreaCheckTask.recordDurationInDatabase();
    }




}