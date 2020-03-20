package com.example.ia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONObject;
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


public class GeoLocater extends AppCompatActivity implements OnMapReadyCallback {
    private static GoogleMap personalMap;

    private static final String TAG = GeoLocater.class.getSimpleName();
    private CameraPosition mCameraPosition;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    public static Location getLastKnownLocation() {
        return mLastKnownLocation;
    }

    public static Location mLastKnownLocation;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            personalMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        personalMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        personalMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public static void showSafeAreas (SafeArea[] safeAreas) {
     for (int i = 0; i < safeAreas.length; i++) {
         if (safeAreas[i] != null) {
             LatLng latLng = new LatLng(safeAreas[i].getLatitude(), safeAreas[i].getLongitude());
             personalMap.addMarker(new MarkerOptions().position(latLng).title(safeAreas[i].getName()));
             personalMap.addCircle(new CircleOptions().center(latLng).radius(5).fillColor(-16776961));
         }
     }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_locater);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);
    }

    public void checkRegularUserIsInSafeArea() {
        final Handler handler = new Handler();
        final SafeAreaCheckTask safeAreaCheckTask = new SafeAreaCheckTask();

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                safeAreaCheckTask.check();
                handler.postDelayed(this, 5000);
                showSafeAreas(safeAreaCheckTask.safeAreas);
            }
        };

        handler.postDelayed(r, 1000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        personalMap = googleMap;
        getDeviceLocation();

        personalMap.setMyLocationEnabled(true);
        personalMap.getUiSettings().setMyLocationButtonEnabled(true);
        checkRegularUserIsInSafeArea();
    }
}