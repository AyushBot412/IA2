package com.example.ia2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.TimerTask;

public class timerNotifications  implements Runnable {

    NotificationManager mNotificationManager;

    public timerNotifications(NotificationManager manager){
        mNotificationManager = manager;
    }

    public void showAdminNotifications() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("LimitBreaks", "LimitBreaks", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(null, "LimitBreaks")
                .setContentTitle("Notification Title")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentText("This is my notification.");
        mNotificationManager.notify(999, builder.build());
    }

    @Override
    public void run() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        final CollectionReference notificationCollection = database.collection("Notifications");
        Query query = notificationCollection.whereEqualTo("circleID", LoginScreen.mLoggedInUser.getCircleId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        NotificationsRecord notificationsRecord = document.toObject(NotificationsRecord.class);
                        showAdminNotifications();
                    }
                    ;
                }
            }
        });
    }
}
