package com.example.womenssafety;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateProfile extends AppCompatActivity {
    // Define the instance variables
    private FirebaseDatabase rootNode;
    private DatabaseReference databaseReference;
    private EditText updateName, updateEmail, updatePassword;


    public updateProfile() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        // Enable the back arrow
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        // Initialize Firebase
        rootNode = FirebaseDatabase.getInstance();
        databaseReference = rootNode.getReference("users");

        // Initialize UI elements
        updateName = findViewById(R.id.names);
        updateEmail = findViewById(R.id.new_email);
        updatePassword = findViewById(R.id.new_pass);
        Button updateButton = findViewById(R.id.update_pro);

        // Set the update button click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation();

            }
        });

        // Retrieve and display data
        retrieveDataFromFirebase();
    }



    private void retrieveDataFromFirebase() {
        DatabaseReference usersInfo = rootNode.getReference("users");

        usersInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHelperClass user = dataSnapshot.getValue(userHelperClass.class);
                if (user != null) {
                    updateName.setText(user.getName());
                    updateEmail.setText(user.getEmail());
                    updateName.setText(user.getPassword());


                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("updateProfile", "Failed to read value.", databaseError.toException());
                updateName.setText("Failed to read name.");
                updateEmail.setText("Failed to read Email");
                updatePassword.setText("Failed to get Password");
            }
        });
    }

    private void updateUserInformation() {
        String newName = updateName.getText().toString().trim();
        String newEmail = updateEmail.getText().toString().trim();
        String newPass = updatePassword.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            if (!newEmail.isEmpty()) {
                user.updateEmail(newEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateEmailInDatabase(newEmail);
                                    Toast.makeText(updateProfile.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                                    updateEmail.setText(newEmail);
                                } else {
                                    Log.e("MainActivity", "Error updating email", task.getException());
                                    Toast.makeText(updateProfile.this, "Error updating email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            if (!newPass.isEmpty()) {
                user.updatePassword(newPass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(updateProfile.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    updatePassword.setText(newPass);
                                } else {
                                    Log.e("MainActivity", "Error updating password",
                                            task.getException());
                                    Toast.makeText(updateProfile.this,
                                            "Error updating password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                // Update display name
                UserProfileChangeRequest profileUpdates =
                        new UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build();

                user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(updateProfile.this,
                                "Name updated", Toast.LENGTH_SHORT).show();
                        saveToDatabase(newEmail, newPass, newName);
                    } else {
                        Toast.makeText(updateProfile.this,
                                "Failed to update name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Log.e("MainActivity", "User is null");
            Toast.makeText(updateProfile.this,
                    "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateEmailInDatabase(String newEmail) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(userId).child("email").setValue(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email updated in the database
                        } else {
                            // Handle error
                        }
                    }
                });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button event
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToDatabase( String email, String password, String name) {
        // Create a user profile map
        userHelperClass userProfile = new userHelperClass(name, email, password);

        // Save the user profile to the database
        databaseReference.child(password).setValue(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(updateProfile.this, "Profile updated in database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(updateProfile.this, "Failed to update profile in database", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
