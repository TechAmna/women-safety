package com.example.womenssafety;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;

public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
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



        setContentView(R.layout.activity_registration);
        spinner = findViewById(R.id.select2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mAuth= FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progress);
        editTextEmail= findViewById(R.id.email);
        editTextPassword= findViewById(R.id.password);
        buttonReg= findViewById(R.id.btnReg);
        textView= findViewById(R.id.loginNow);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        });



        buttonReg.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email= String.valueOf(editTextEmail.getText());
            password= String.valueOf(editTextPassword.getText());
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
                Toast.makeText(registration.this, "Select user type ", Toast.LENGTH_SHORT).show();
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(registration.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(registration.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(registration.this, "Account created.",
                                        Toast.LENGTH_SHORT).show();


                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(registration.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Select user type ", Toast.LENGTH_SHORT).show();

    }
}