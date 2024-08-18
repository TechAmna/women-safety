package com.example.womenssafety;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerContact extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerContactAdapter adapter;
    ArrayList<ContactModel> arrContacts = new ArrayList<>();
    FloatingActionButton buttonAdd;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_contact);
        fetchContacts();


        back = findViewById(R.id.backGo);
        recyclerView = findViewById(R.id.contact_row);
        buttonAdd = findViewById(R.id.btnOpenDialog);
        back.setOnClickListener(v -> {
            Intent intent= new Intent(RecyclerContact.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        buttonAdd.setOnClickListener(v -> {
            Dialog dialog = new Dialog(RecyclerContact.this);
            dialog.setContentView(R.layout.add_update);
            EditText editName = dialog.findViewById(R.id.getName);
            EditText editNumber = dialog.findViewById(R.id.getNumber);
            Button button = dialog.findViewById(R.id.btnAction);
            button.setOnClickListener(v1 -> {
                        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference reference = rootNode.getReference("users").child("contacts");
                        String name = editName.getText().toString().trim();
                        String number = editNumber.getText().toString().trim();
                        if (!name.isEmpty() && !number.isEmpty()) {
                            ContactModel newContact = new ContactModel(name, number);
                            reference.child(number).setValue(newContact).addOnCompleteListener(
                                    task -> {
                                        if (task.isSuccessful()) {


                                            arrContacts.add(newContact);
                                            adapter.notifyItemInserted(arrContacts.size() - 1);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(RecyclerContact.this,
                                                    "Failed to add", Toast.LENGTH_SHORT).show();
                                        }

                                    });
                        } else {
                            Toast.makeText(RecyclerContact.this, "Both name and number must be provided.", Toast.LENGTH_SHORT).show();
                        }
                    });
            dialog.show();
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerContactAdapter(this, arrContacts);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can make the call
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchContacts() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users").child("contacts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrContacts.clear(); // Clear the list before adding new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ContactModel contact = dataSnapshot.getValue(ContactModel.class);
                    if (contact != null) {
                        arrContacts.add(contact);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecyclerContact.this, "Failed to fetch contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }



}






