package com.example.womenssafety;

import static com.example.womenssafety.showNotification.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class parentsNotifications extends AppCompatActivity {

    private TextView message;

    ImageView back;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        message = findViewById(R.id.message);
        handleNotificationMessage();
        // Get location data from intent or wherever it's stored
        String location = getIntent().getStringExtra("location");

        // Pass location data to the fragment
        LocationFragment locationFragment = (LocationFragment) getSupportFragmentManager().findFragmentById(R.id.locationTextView);
        if (locationFragment != null) {
            locationFragment.updateLocation(location);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(parentsNotifications.this, police.class);
            startActivity(intent);
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }


    @SuppressLint("MissingFirebaseInstanceTokenRefresh")
    public class MyFirebaseMessagingService extends FirebaseMessagingService {


        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            remoteMessage.getData();
            if (remoteMessage.getData().containsKey("type")) {
                String type = remoteMessage.getData().get("type");
                if ("emergency".equals(type)) {
                    // Handle emergency message
                    Intent intent = new Intent(this, notifications.class);
                    intent.putExtra("message", (CharSequence) message);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    CharSequence notificationTitle = "";
                    CharSequence notificationMessage = "";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.notification)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationMessage)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                            // Notifications are disabled
                            // Prompt user to enable notifications
                        }else {
                            showNotification.showEnableNotificationsDialog(context);
                        }
                        return;
                    }
                    int notificationId = 0;
                    notificationManager.notify(notificationId, builder.build());

                    String message = remoteMessage.getData().get("message");
                    // Display notification
                    showNotification.showNotification(getApplicationContext(), "Emergency Alert", message);
                }
            }
        }
    }
    private void handleNotificationMessage() {
        // Check if activity was opened from a notification
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("message")) {
            String notificationMessage = extras.getString("message");
            message.setText(notificationMessage);
        }
    }
}