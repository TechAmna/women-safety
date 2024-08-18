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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    TextInputEditText userName, editTextEmail;
    TextInputEditText editTextPassword;
    Button buttonReg;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    TextView textView;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Spinner spinner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        spinner = findViewById(R.id.select2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        // get all values
        FirebaseUser user = mAuth.getCurrentUser();
        userName = findViewById(R.id.userNameReg);
        editTextEmail = findViewById(R.id.emailReg);
        editTextPassword = findViewById(R.id.passwordReg);
        buttonReg = findViewById(R.id.btnReg);
        textView = findViewById(R.id.loginNow);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        });
        buttonReg.setOnClickListener(v -> {


            // Retrieve email and password entered by the user

            String name, email, password;

            name = String.valueOf(userName.getText());
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());
            userHelperClass helperClass = new userHelperClass(name, email, password);
            reference.child(name).setValue(helperClass);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(registration.this, "Login successful.",
                                            Toast.LENGTH_SHORT).show();


                                    // Once validated, check the selected user type
                                    String selectedUserType = spinner.getSelectedItem().toString();

                                    // Perform login based on user type
                                    if (selectedUserType.equals("Women")) {


                                        // Start womenActivity
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                        startActivity(intent);
                                        finish();
                                    } else if (selectedUserType.equals("Police")) {
                                        // Start policeActivity
                                        Intent intent = new Intent(getApplicationContext(), police.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (selectedUserType.equals("Parent/Guardian")) {
                                        Intent intent = new Intent(getApplicationContext(), Parents.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    if (selectedUserType.isEmpty()) {
                                        Toast.makeText(registration.this, "Select user type ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {


                                    Toast.makeText(registration.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                    });



            progressBar.setVisibility(View.VISIBLE);


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(registration.this, "Enter email", Toast.LENGTH_SHORT).show();

            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(registration.this, "Enter password", Toast.LENGTH_SHORT).show();

            }




        });
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








