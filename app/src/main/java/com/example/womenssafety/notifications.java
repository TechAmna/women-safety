package com.example.womenssafety;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class notifications extends AppCompatActivity{
    EditText textViewMessage;

    private double latitude;
    private double longitude;
    GoogleMap mMap;
    Fragment location;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(notifications.this, police.class);
                                    }
                                });
        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra("MESSAGE");
            Location location = getIntent().getParcelableExtra("location");

            // Display or process the message as needed
            // For example, show it in a TextView
            textViewMessage.setText(message);

        }
        if (location != null) {
            // Proceed to show location on the map
            showLocationOnMap();
        } else {
            // Handle case where location is null
            Toast.makeText(this, "Location data not found", Toast.LENGTH_SHORT).show();
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void showLocationOnMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.w_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(googleMap -> {
            mMap= googleMap;


            // Add a marker at the received location and move the camera
            LatLng location = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(location).title("Woman's Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


        });
    }

}