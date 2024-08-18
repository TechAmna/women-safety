package com.example.womenssafety;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;


public class police extends AppCompatActivity {

    CardView notification, location, police_safety_tips, logout;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView;
    TextView update;

    public police(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);
        textView = findViewById(R.id.userAcc);

        update = findViewById(R.id.updateProfile);
        update.setOnClickListener(v -> navigateTo(updateProfile.class));

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        System.out.println("Fetching FCM registration token failed");
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    System.out.println(token);
                    Toast.makeText(police.this, token, Toast.LENGTH_SHORT).show();

                });
        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);


        user = auth.getCurrentUser();
        if (user != null) {



            textView.setText(user.getEmail());


        } else {
            // No user is logged in
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish();
        }





        notification = findViewById(R.id.notification_background);
        notification.setOnClickListener(v -> navigateTo(notifications.class));


        location = findViewById(R.id.location_police);
        location.setOnClickListener(v -> navigateTo(police_location.class));


        logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        });
        police_safety_tips = findViewById(R.id.safetyTips);
        police_safety_tips.setOnClickListener(v -> navigateTo(safetyTips.class));

    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(police.this, cls);
        startActivity(intent);
    }


}






























