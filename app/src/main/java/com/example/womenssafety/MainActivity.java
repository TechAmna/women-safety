package com.example.womenssafety;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private CardView logout, policeAddress, safety, contact, location;
    private TextView textView;
    private ImageButton emergencyButton;

    private  Location currentLocationSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        initializeViews();
        setupListeners();
        updateUI(auth.getCurrentUser());
    }

    private void initializeViews() {
        logout = findViewById(R.id.logout);
        policeAddress = findViewById(R.id.police_add);
        safety = findViewById(R.id.safetyTips);
        contact = findViewById(R.id.contacts);
        location = findViewById(R.id.w_location);
        textView = findViewById(R.id.user);
        emergencyButton = findViewById(R.id.emergency);
    }

    private void setupListeners() {
        logout.setOnClickListener(v -> signOut());
        policeAddress.setOnClickListener(v -> openWebPage("https://pcsw.punjab.gov.pk/important_helplines"));
        safety.setOnClickListener(v -> navigateTo(safetyTips.class));
        contact.setOnClickListener(v -> navigateTo(RecyclerContact.class));
        location.setOnClickListener(v -> navigateTo(police_location.class));
        emergencyButton.setOnClickListener(v -> handleEmergency());
    }

    private void signOut() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, login.class));
        finish();
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    private void handleEmergency() {
        // Assume sendLocationToPolice and sendLocationToParents are defined
        sendLocationToPolice();
        sendLocationToParents();
        notification();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            textView.setText(user.getEmail()); // Display user email
        } else {
            startActivity(new Intent(this, login.class));
            finish();
        }
    }

    private void sendLocationToPolice() {

        if (currentLocationSend != null) {
            // Create an intent to send the location data to the police activity
            Intent intent = new Intent(this, parentsNotification.class);
            intent.putExtra("latitude", currentLocationSend.getLatitude());
            intent.putExtra("longitude", currentLocationSend.getLongitude());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLocationToParents() {
        if (currentLocationSend != null) {
            // Create an intent to send the location data to the police activity
            Intent intent = new Intent(this, parentsNotification.class);
            intent.putExtra("latitude", currentLocationSend.getLatitude());
            intent.putExtra("longitude", currentLocationSend.getLongitude());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void notification() {
        String channelId = "emergency_notifications";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.notification);
        builder.setContentTitle("Emergency");
        builder.setContentText("Emergency situation!");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Emergency Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());
    }
}
