package com.example.ia2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        remoteMessage.getNotification().getBody();
        remoteMessage.getNotification().getTitle();

    }
    public void showNotification (String title, String message) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "LimitBreaks")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentText(message);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999, builder.build());

        SafeAreaCheckTask.mSentNotification = true;
    }
}
