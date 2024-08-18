package com.example.womenssafety;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseUser user;
    private CardView logout, policeAddress, safety, contact, location;
    private ImageButton emergencyButton;
    boolean isPermissionGranted;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private final int GPS_REQUEST_CODE = 1;

    TextView update;
    DatabaseReference messagesRef;

    public MainActivity() {}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.user);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {

             textView.setText(user.getEmail());

        } else {
            // No user is logged in
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish();

        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            getLastLocation();
        }



        initializeViews();
        initiateMap();
        setupListeners();

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkMyPermission();
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                        // Send location to Firebase
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child("locations");
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                        LocationHelperClass locationModel = new LocationHelperClass(latitude, longitude, timestamp);
                        databaseRef.push().setValue(locationModel);
                    }
                });
    }




    private void checkMyPermission() {

        Dexter.withContext(this).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION).withListener(
                new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        isPermissionGranted = true;

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), "");
                        intent.setData(uri);
                        startActivity(intent);
                        finish();

                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }

                }).check();


    }

    private void initializeViews() {
        logout = findViewById(R.id.logout);
        policeAddress = findViewById(R.id.police_add);
        safety = findViewById(R.id.safetyTips);
        contact = findViewById(R.id.contacts);
        location = findViewById(R.id.w_location);
        update = findViewById(R.id.update_pro);

        emergencyButton = findViewById(R.id.emergency);

    }

    private void setupListeners() {
        logout.setOnClickListener(v -> signOut());
        update.setOnClickListener(v -> navigateTo(updateProfile.class));

        policeAddress.setOnClickListener(v -> openWebPage());
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

    private void openWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pcsw.punjab.gov.pk/important_helplines"));
        startActivity(intent);
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    private void handleEmergency() {
        //  sendLocationToPolice and sendLocationToParents


        getLastLocation();
        notification();
        sendEmergencyMessage();

    }



    private void initiateMap() {
        if (isPermissionGranted) {
            if (isGPSEnable()) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.police_location);
                assert mapFragment != null;
                mapFragment.getMapAsync((OnMapReadyCallback) this);
            }
        }
    }
    private boolean isGPSEnable(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnable){
            return true;
        }else{

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permission")
                    .setMessage("GPS is required for this app to work. Please enable GPS")
                    .setPositiveButton("Yes", (dialogInterface, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        }
        return false;
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

    private void sendEmergencyMessage() {
        messagesRef = FirebaseDatabase.getInstance().getReference("users").child("messages");


                        String messageId = messagesRef.push().getKey();
                        String name =user.getEmail();
                        if (messageId != null) {
                            Map<String, String> data = new HashMap<>();
                            data.put(String.valueOf(user), name);
                            data.put("type", "emergency");
                            data.put("message", "Emergency situation detected!");

                            FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("police_topic")
                                    .setData(data)
                                    .build());
                            messagesRef.child(messageId).setValue(data)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                                        }
                                    });
        }
    }
}