package com.example.womenssafety;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerContact extends AppCompatActivity {
    RecyclerContactAdapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton btnOpenDialog;

    ArrayList<contactModel> arrayContacts= new ArrayList<>();
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_contact);
        back = findViewById(R.id.backGo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecyclerContact.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        recyclerView=findViewById(R.id.contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(RecyclerContact.this);
                dialog.setContentView(R.layout.add_update);
                EditText editName = dialog.findViewById(R.id.getName);
                EditText editNumber = dialog.findViewById(R.id.getNumber);
                Button btnAction = dialog.findViewById(R.id.btnAction);

                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        // check if field is empty
                        String name = "", number= "";
                        if (!editName.getText().toString().isEmpty()) {
                          name = editName.getText().toString();
                        } else {
                            Toast.makeText(RecyclerContact.this, "Please Enter Contact Name", Toast.LENGTH_SHORT).show();
                        }

                        if (!editNumber.getText().toString().isEmpty()) {
                            number = editNumber.getText().toString();
                        } else {
                            Toast.makeText(RecyclerContact.this, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                        }
                        DatabaseReference contactRef  = FirebaseDatabase.getInstance().getReference(("contacts"));
                        String contactId = contactRef.push().getKey();
                        contactModel contactModel = new contactModel("Amina", "092345");
                        assert contactId != null;
                        contactRef.child(contactId).setValue(contactModel);

                        arrayContacts.add(new contactModel(name, number));
                        adapter.notifyItemInserted(arrayContacts.size() - 1);
                        recyclerView.scrollToPosition(arrayContacts.size() - 1);
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerContactAdapter(this, arrayContacts);
        recyclerView.setAdapter(adapter);

    }
}