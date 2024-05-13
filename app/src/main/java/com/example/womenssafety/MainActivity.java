package com.example.womenssafety;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/** @noinspection ALL*/
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private CardView logout, policeAddress, safety, contact, location;
    private TextView textView;
    private ImageButton emergencyButton;

    private  Location currentLocationSend;
    ImageView update;


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
        update = findViewById(R.id.update_pro);
    }

    private void setupListeners() {
        logout.setOnClickListener(v -> signOut());
        policeAddress.setOnClickListener(v -> openWebPage());
        safety.setOnClickListener(v -> navigateTo(safetyTips.class));
        contact.setOnClickListener(v -> navigateTo(RecyclerContact.class));
        location.setOnClickListener(v -> navigateTo(police_location.class));
        emergencyButton.setOnClickListener(v -> handleEmergency());
        update.setOnClickListener(v -> setUpdate());

    }

    private void signOut() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, login.class));
        finish();
    }

    private void openWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pcsw.punjab.gov.pk/important_helplines"));
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
        sendEmergencyMessage();

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
        currentLocationSend =  getIntent().getParcelableExtra("location");

        if (currentLocationSend != null) {
            // Create an intent to send the location data to the police activity
            Intent intent = new Intent(this, parentsNotifications.class);
            intent.putExtra("latitude", currentLocationSend.getLatitude());
            intent.putExtra("longitude", currentLocationSend.getLongitude());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLocationToParents() {
        currentLocationSend =  getIntent().getParcelableExtra("location");
        if (currentLocationSend != null) {
            // Create an intent to send the location data to the police activity
            Intent intent = new Intent(this, parentsNotifications.class);
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
    public void setUpdate(){

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.update_profile);
        EditText getEmail = findViewById(R.id.new_email);
        EditText getPassword = findViewById(R.id.new_pass);
        Button button = findViewById(R.id.btnAction);
        button.setOnClickListener(v1 -> {
            String newEmail = "", newPassword = "";
            if (!getEmail.getText().toString().isEmpty()) {
                newEmail = getEmail.getText().toString();

            } else {
                Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
            if (!getPassword.getText().toString().isEmpty()) {
                newPassword = getPassword.getText().toString();

            } else {
                Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

            }
            dialog.dismiss();
        });
        dialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String newEmail = "";
        assert user != null;
        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Email address updated successfully
                        Toast.makeText(this, "Email update successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email address update failed
                        Toast.makeText(this, "Email update failed", Toast.LENGTH_SHORT).show();
                    }
                });
        String newPassword = "";

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password updated successfully
                            Toast.makeText(MainActivity.this, "Password update Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Password update failed
                            Toast.makeText(MainActivity.this, "Password update failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AuthCredential credential = EmailAuthProvider.getCredential(newEmail, newPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User re-authenticated successfully, now update email or password
                            Toast.makeText(MainActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Re-authentication failed
                            Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void sendEmergencyMessage() {
        Map<String, String> data = new HashMap<>();
        data.put("type", "emergency");
        data.put("message", "Emergency situation detected!");

        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("police_topic")
                .setData(data)
                .build());
    }




}