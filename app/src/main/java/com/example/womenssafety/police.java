package com.example.womenssafety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;


public class police extends AppCompatActivity {

    CardView notification, location, police_safety_tips, logout;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView;
    ImageView update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {


                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        System.out.println(token);
                        Toast.makeText(police.this, token, Toast.LENGTH_SHORT).show();

                    }
                });
        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        textView = findViewById(R.id.userAcc);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();

        } else {
            textView.setText(user.getEmail());
        }


        notification = findViewById(R.id.notification_background);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(police.this, parentsNotifications.class);
                startActivity(intent);
                finish();
            }
        });
        // String notification = getIntent().getStringExtra("Notification");


        location = findViewById(R.id.location_police);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(police.this, police_location.class);
                startActivity(intent);
                finish();

            }
        });


        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
        police_safety_tips = findViewById(R.id.safetyTips);
        police_safety_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(police.this, com.example.womenssafety.police_safety_tips.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void setUpdate(){

        Dialog dialog = new Dialog(police.this);
        dialog.setContentView(R.layout.update_profile);
        EditText getEmail = findViewById(R.id.new_email);
        EditText getPassword = findViewById(R.id.new_pass);
        Button button = findViewById(R.id.btnAction);
        button.setOnClickListener(v1 -> {
            String newEmail = "", newPassword = "";
            if (!getEmail.getText().toString().isEmpty()) {
                newEmail = getEmail.getText().toString();

            } else {
                Toast.makeText(police.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
            if (!getPassword.getText().toString().isEmpty()) {
                newPassword = getPassword.getText().toString();

            } else {
                Toast.makeText(police.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(police.this, "Password update Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Password update failed
                            Toast.makeText(police.this, "Password update failed", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(police.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Re-authentication failed
                            Toast.makeText(police.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
























