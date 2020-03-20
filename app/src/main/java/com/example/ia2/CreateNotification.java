package com.example.ia2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.AppComponentFactory;
import android.app.NotificationManager;

public class CreateNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification2);



        SafeAreaCheckTask.mSentNotification = true;
    }
}



