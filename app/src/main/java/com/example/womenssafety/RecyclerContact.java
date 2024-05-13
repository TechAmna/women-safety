package com.example.womenssafety;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerContact extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerContactAdapter adapter;
    DatabaseReference databaseRef;
    ArrayList<ContactModel> arrContacts = new ArrayList<>();
    FloatingActionButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_contact);
        recyclerView = findViewById(R.id.contact_row);
        buttonAdd = findViewById(R.id.btnOpenDialog);
         buttonAdd.setOnClickListener(v -> {
            try {
                Dialog dialog = new Dialog(RecyclerContact.this);
                dialog.setContentView(R.layout.add_update);
                EditText editName = dialog.findViewById(R.id.getName);
                EditText editNumber = dialog.findViewById(R.id.getNumber);
                Button button = dialog.findViewById(R.id.btnAction);
                button.setOnClickListener(v1 -> {
                    try {
                        String name = "", number = "";
                        if (!editName.getText().toString().isEmpty()) {
                            name = editName.getText().toString();
                        } else {
                            Toast.makeText(RecyclerContact.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                            return; // Return here to prevent further execution
                        }
                        if (!editNumber.getText().toString().isEmpty()) {
                            number = editNumber.getText().toString();
                        } else {
                            Toast.makeText(RecyclerContact.this, "Please Enter Number", Toast.LENGTH_SHORT).show();
                            return; // Return here to prevent further execution
                        }
                        ContactModel contact = new ContactModel(name, number);
                        databaseRef = FirebaseDatabase.getInstance().getReference("contacts");
                        databaseRef.child(name).setValue(contact)
                                .addOnCompleteListener(task -> {
                                    editName.setText("");
                                    editNumber.setText("");
                                    Toast.makeText(RecyclerContact.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                });
                        adapter.notifyItemInserted(arrContacts.size() - 1);
                        recyclerView.scrollToPosition(arrContacts.size() - 1);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RecyclerContact.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RecyclerContact.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });





        arrContacts.add(new ContactModel("Aiman", "999776655"));
        arrContacts.add(new ContactModel("John", "999776655"));
        arrContacts.add(new ContactModel("Leena", "999776655"));
        arrContacts.add(new ContactModel("Zara", "999776655"));
        arrContacts.add(new ContactModel("Adam", "999776655"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerContactAdapter(this, arrContacts);
        recyclerView.setAdapter(adapter);



    }



}






