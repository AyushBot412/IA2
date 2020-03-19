package com.example.ia2;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.TimerTask;

public class SafeAreaCheckTask extends TimerTask {
    //TODO explain why inSafeArea can be simplified

    private long mEndTimeInSeconds;
    private long mStartTimeInSeconds;
    private boolean mPreviouslyOutsideSafeArea = false;
    private long mDurationOutsideSafeArea;
    private long mLimitOnOutsideSafeAreaTimeInSeconds;
    private boolean mSentNotification = false;

    @Override
    public void run() {
      // 1: call checkIfUSerInSafeArea function
        boolean CurrentlyInSafeArea = checkIfUSerInSafeArea();
        // 2: if in safe area and stopwatch is running, record time and stop the stopwatch
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
            //TODO record the time in server
            /*TODO make sure the total time the user was outside the is recorded in Day, Hour, Minute, Second format
             */
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

        //I want to send a notification the first time duration exceeds the limit
        //regardless of where the user is.


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
        return true;
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


        mSentNotification = true;
    }
}
