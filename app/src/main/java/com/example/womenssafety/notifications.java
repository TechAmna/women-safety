package com.example.womenssafety;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notifications extends AppCompatActivity {
    private LocationAdapter locationAdapter;
    private MessageAdapter messageAdapter;
    private List<LocationHelperClass> locationList;
    private List<MessageModel> messageList;

    private DatabaseReference locationReference, messageReference, nameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        RecyclerView locationRecyclerView = findViewById(R.id.location_recycler);
        RecyclerView messageRecyclerView = findViewById(R.id.notification_rec);

        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationList = new ArrayList<>();
        messageList = new ArrayList<>();

        locationAdapter = new LocationAdapter(locationList);
        messageAdapter = new MessageAdapter (messageList);

        locationRecyclerView.setAdapter(locationAdapter);
        messageRecyclerView.setAdapter(messageAdapter);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        locationReference = firebaseDatabase.getReference("users").child("locations");
        messageReference = firebaseDatabase.getReference("users").child("messages");
        nameRef = firebaseDatabase.getReference("users");


        fetchLocationData();
        fetchName();
        fetchMessageData();
    }

    private void fetchLocationData() {
        locationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LocationHelperClass location = dataSnapshot.getValue(LocationHelperClass.class);
                    locationList.add(location);
                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(notifications.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMessageData() {
        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(notifications.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchName(){
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModel userHelper = dataSnapshot.getValue(MessageModel.class);
                    messageList.add(userHelper);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(notifications.this, "No name provided", Toast.LENGTH_SHORT).show();

            }
        });

    }
}


