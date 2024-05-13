package com.example.womenssafety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Parents extends AppCompatActivity {
    CardView logout, notification;
    FirebaseAuth auth;
   private TextView textView;
    //ImageView update;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parents);
        auth = FirebaseAuth.getInstance();
        updateUI(auth.getCurrentUser());

        //update = findViewById(R.id.update_parents);
        //update.setOnClickListener(v -> setUpdate());
        notification = findViewById(R.id.alert);
        notification.setOnClickListener(v -> {
            Intent intent = new Intent(Parents.this, parentsNotifications.class);
            startActivity(intent);
            finish();
        });
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        });
        textView = findViewById(R.id.user_name);


/*        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    /*public void setUpdate() {

        Dialog dialog = new Dialog(Parents.this);
        dialog.setContentView(R.layout.update_profile);
        EditText getEmail = findViewById(R.id.new_email);
        EditText getPassword = findViewById(R.id.new_pass);
        Button button = findViewById(R.id.btnAction);
        button.setOnClickListener(v1 -> {
            String newEmail = "", newPassword = "";
            if (!getEmail.getText().toString().isEmpty()) {
                newEmail = getEmail.getText().toString();

            } else {
                Toast.makeText(Parents.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
            if (!getPassword.getText().toString().isEmpty()) {
                newPassword = getPassword.getText().toString();

            } else {
                Toast.makeText(Parents.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

            }
        });*/
       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Password updated successfully
                        Toast.makeText(Parents.this, "Password update Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Password update failed
                        Toast.makeText(Parents.this, "Password update failed", Toast.LENGTH_SHORT).show();
                    }
                });
        AuthCredential credential = EmailAuthProvider.getCredential(newEmail, newPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User re-authenticated successfully, now update email or password
                        Toast.makeText(Parents.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Re-authentication failed
                        Toast.makeText(Parents.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }*/
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            textView.setText(user.getEmail()); // Display user email
        } else {
            startActivity(new Intent(this, login.class));
            finish();
        }
    }




}