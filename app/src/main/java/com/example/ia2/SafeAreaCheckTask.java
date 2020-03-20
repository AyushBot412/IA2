package com.example.ia2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.location.Location;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SafeAreaCheckTask {
    //TODO explain why inSafeArea can be simplified

    private long mEndTimeInSeconds;
    private long mStartTimeInSeconds;
    private boolean mPreviouslyOutsideSafeArea = false;
    private long mDurationOutsideSafeArea;
    private long mLimitOnOutsideSafeAreaTimeInSeconds;
    public static boolean mSentNotification = false;
    private SafeArea [] safeAreas = new SafeArea[10];

    public SafeAreaCheckTask () {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference safeAreaCollection = database.collection("Safe Areas");

        Query query = safeAreaCollection.whereEqualTo("circleID", LoginScreen.mLoggedInUser.getCircleId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        safeAreas[i++] = document.toObject(SafeArea.class);
                    }
                    ;
                }
            }
        });
    }

    public void check() {
        boolean CurrentlyInSafeArea = checkIfUSerInSafeArea();
        if (CurrentlyInSafeArea == true) {
            if(mPreviouslyOutsideSafeArea == true) {
                //mEndTimeInSeconds is the current time in seconds
                //when the user came back into the safe area
                //from outside.
                mEndTimeInSeconds = System.currentTimeMillis()/1000;
                mPreviouslyOutsideSafeArea = false;
                mDurationOutsideSafeArea = mEndTimeInSeconds - mStartTimeInSeconds;
           }
            else {
                //nothing to do here
            }

        }
        else {
            if (mPreviouslyOutsideSafeArea == false) {
                //this is the first time the user is leaving the
                //safe area, so record the current time.
                mStartTimeInSeconds = System.currentTimeMillis()/1000;
                mPreviouslyOutsideSafeArea = true;
            }
            else {
                mDurationOutsideSafeArea = (System.currentTimeMillis()/1000) - mStartTimeInSeconds;
            }
        }
        //TODO change limit to hours
        if (mDurationOutsideSafeArea >= mLimitOnOutsideSafeAreaTimeInSeconds) {
            if (mSentNotification == false ) {
                recordDurationInDatabase();
                sendNotification();
            }
            else {
                //do nothing
            }
        }
        else {
            //do nothing
        }
    }
    private boolean checkIfUSerInSafeArea () {
        //TODO finish this method
        Location location = com.example.ia2.GeoLocater.getLastKnownLocation();
        boolean inSafeArea = false;
        for (int i = 0;  i < safeAreas.length; i++) {
            inSafeArea = compareSafeAreas(location, safeAreas[i]);
            if (inSafeArea == true) {
                return true;
            }
        }
        return inSafeArea;
    }

    public boolean compareSafeAreas (Location location, SafeArea safeArea)
    {
        double distance = distance(location.getLatitude(), location.getLongitude(), safeArea.getLatitude(), safeArea.getLongitude());
        if (distance > safeArea.getRadius()) {
            return false;
        }
        else {
            return true;
        }
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return (dist);
        }
    }

    public void recordDurationInDatabase() {
        //add person, limit, duration, date, in notification
        String notificationMessage = LoginScreen.mLoggedInUser.getUsername() + " has been outside the safe " +
                "area for " + mDurationOutsideSafeArea + " seconds and the limit was " + mLimitOnOutsideSafeAreaTimeInSeconds;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference notificationCollection =  database.collection("Notifications");

        NotificationsRecord notificationsRecord = new NotificationsRecord();

        notificationsRecord.setUsername(LoginScreen.mLoggedInUser.getUsername());
        notificationsRecord.setNotificationDateTime(new Date(System.currentTimeMillis()));
        notificationsRecord.setCircleID(LoginScreen.mLoggedInUser.getCircleId());
       //add date and person and time
        notificationsRecord.setNotificationMessage(notificationMessage);
        notificationCollection.add(notificationsRecord);

    }

    private void sendNotification() {
        //TODO send notification with priority alert and create a Timer task to keep
        //checking for new notifications


    }
}
