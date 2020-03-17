package com.example.ia2;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

        //I want to send a notification the first time durationa exceeds the limit
        //regardless of where the user is.
        if (mDurationOutsideSafeArea >= mLimitOnOutsideSafeAreaTimeInSeconds) {
            if (mSentNotification == false ) {
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

    private void sendNotification() {
        //TODO send notification with priority alert


        mSentNotification = true;
    }
}
