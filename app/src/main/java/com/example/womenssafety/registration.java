package com.example.womenssafety;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView textView;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        initializeUI();

        buttonReg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Retrieve username and password entered by the user

                    String email, password;
                    email = String.valueOf(editTextEmail.getText());
                    password = String.valueOf(editTextPassword.getText());


                    // Once validated, check the selected user type
                    String selectedUserType = spinner.getSelectedItem().toString();

                    // Perform login based on user type
                    if (selectedUserType.equals("Women")) {

                        // Create a new Location object
                 /*   Location dummyLocation = new Location("dummy");  // "dummy" is just a provider name

// Set the latitude and longitude of the dummy location
                    dummyLocation.setLatitude(40.712776);   // Example latitude (e.g., New York City)
                    dummyLocation.setLongitude(-74.005974); // Example longitude (e.g., New York City)

// Optionally, you can set additional parameters
                    dummyLocation.setAccuracy(100); // 100 meters accuracy
                    dummyLocation.setTime(System.currentTimeMillis()); // Current time as the timestamp
                    dummyLocation.setAltitude(30); // 30 meters above sea level
*/
                        // Start womenActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("Location", dummyLocation);
                        startActivity(intent);
                        finish();
                    } if (selectedUserType.equals("Police")) {
                        // Start policeActivity
                        Intent intent = new Intent(getApplicationContext(), police.class);
                        startActivity(intent);
                        finish();
                    }
                    if (selectedUserType.equals("Parent/Guardian")) {
                        Intent intent = new Intent(getApplicationContext(), Parents.class);
                        startActivity(intent);
                    }
                    if (selectedUserType.isEmpty()) {
                        Toast.makeText(registration.this, "Select user type ", Toast.LENGTH_SHORT).show();
                    }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Create a new user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(registration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(registration.this, login.class));
                                    finish();
                                } else {
                                    Toast.makeText(registration.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d(String.valueOf(registration.this), "Registration failed: " + task.getException().getMessage());
                                }
                            }
                        });
            }
        });
    }

    private void initializeUI() {
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btnReg);
        progressBar = findViewById(R.id.progress);
        textView = findViewById(R.id.loginNow);
        spinner = findViewById(R.id.select);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registration.this, login.class));
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // This method can stay empty
    }
}
