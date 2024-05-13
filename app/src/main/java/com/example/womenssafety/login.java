package com.example.womenssafety;

import static android.content.ContentValues.TAG;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference usersRef;


    TextInputEditText editTextEmail;
    TextInputEditText editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView textView;
    Spinner spinner;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("users");
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
       // DatabaseReference usersRef = databaseReference.child("users");
        // Get the selected user type from the spinner
/*        String userType = spinner.getSelectedItem().toString();

// Get a reference to the Firebase database
       DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

// Get a reference to the node corresponding to the selected user type
        DatabaseReference userTypeRef = usersRef.child(userType);

// Generate a unique user ID
        String userId = userTypeRef.push().getKey();

// Create a User object or a map containing the user's information
        User user = new User(userId, editTextEmail,editTextPassword); // Replace with your user model

// Save the user's information to the database under the corresponding user type node
        assert userId != null;
        userTypeRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // User data saved successfully
                    Toast.makeText(login.this, "User data saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to save user data
                    Toast.makeText(login.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error saving user data", e);
                }); */



        spinner = findViewById(R.id.select);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btnLogin);
        textView = findViewById(R.id.registerNow);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), registration.class);
            startActivity(intent);
            finish();
        });
        buttonLogin.setOnClickListener(v -> {

            // Retrieve email and password entered by the user

            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());


            // Once validated, check the selected user type
            String selectedUserType = spinner.getSelectedItem().toString();

            // Perform login based on user type
            if (selectedUserType.equals("Women")) {


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
                Toast.makeText(login.this, "Select user type ", Toast.LENGTH_SHORT).show();
            }


            progressBar.setVisibility(View.VISIBLE);


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(login.this, "Enter email", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(intent);
                finish();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(login.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Toast.makeText(login.this, "Login successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
//                                    finish();
                        } else {


                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });


        });
    }

    private void saveUser(String userType, String Password, String Email) {
        // Get a unique key for the user
        String userId = String.valueOf(usersRef.child(userType));

        // Create a new user object
        User user = new User(Password, Email);

        // Save the user to the database under the specified user type node
        usersRef.child(userType).child(userId).setValue(user);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Select user type", Toast.LENGTH_SHORT).show();

    }
}

// User model class
class User {
    private final String password;
    private final String email;
    private String Password;
    private String Email;


    public User(String email, String password) {

        this.password = password;
        this.email = email;
    }

    public User(String userId, TextInputEditText editTextEmail, TextInputEditText editTextPassword) {
        password = "";
        email = "";
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = Email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = Password;
    }

    public String child(String userType) {


        // Default constructor required for Firebase

        return userType;
    }
}