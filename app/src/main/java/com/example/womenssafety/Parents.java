package com.example.womenssafety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class Parents extends AppCompatActivity {
    CardView logout, notification;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView;
   TextView update;
    public Parents(){}


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parents);
        update = findViewById(R.id.update_parents);
        update.setOnClickListener(v -> navigateTo(updateProfile.class));

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            textView = findViewById(R.id.user_name);
            textView.setText((CharSequence) user.getEmail());

        } else {
            // No user is logged in
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish();
        }



        notification = findViewById(R.id.alert);
        notification.setOnClickListener(v -> navigateTo(notifications.class));

        logout = findViewById(R.id.logout);

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        });




    }
    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(Parents.this, cls);
        startActivity(intent);
    }
}