package com.example.ia2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import org.json.JSONObject;


public class GeoLocater extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap personalMap;
    private LatLng ltlng;
    private LatLng newCoords;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_locater);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);

        /*
        I had an error in which I copied and pasted the code from
        MainActivity which was a basic activity, not a empty activity
        like geolocater is.
        It was failing because the code was trying to access toolbar
        and fab which doesn't in an empty activity.
        */

    }

    public void initLatLongToCurrentlocation ()
    {
        String u = "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBFWN6L8d-rvFKj15Jg1IqdxkITBLdB0nI";

        try {

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    u,
                    null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //the log was deleted because it just recorded what
                                //the object did

                                JSONObject j = response.getJSONObject("location");
                                double lat = j.getDouble("lat");
                                double lng = j.getDouble("lng");
                                ltlng = new LatLng(lat, lng);
                            } catch (Exception e) {
                            }
                            double lat1 = 47.600350;
                            double lng1 = -122.031670;
                            newCoords = new LatLng(lat1, lng1);
                            personalMap.addMarker(new MarkerOptions().position(ltlng).title("Current"));
                            personalMap.addMarker(new MarkerOptions().position(newCoords).title("New"));
                            personalMap.moveCamera(CameraUpdateFactory.newLatLng(ltlng));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response", error.toString());

                        }
                    });

            requestQueue.add(jsonObjectRequest);

        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        personalMap = googleMap;
        initLatLongToCurrentlocation();

    }
}
