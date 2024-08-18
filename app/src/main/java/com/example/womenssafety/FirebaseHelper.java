package com.example.womenssafety;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private final DatabaseReference databaseReference;

    public FirebaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("contacts");
    }

    public void updateContact(String id, String newName, String newNumber) {
        Map<String, Object> contactUpdates = new HashMap<>();
        contactUpdates.put("name", newName);
        contactUpdates.put("number", newNumber);

        databaseReference.child(id).updateChildren(contactUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseHelper", "Contact updated successfully.");
                    } else {
                        Log.e("FirebaseHelper", "Failed to update contact.", task.getException());
                    }
                });
    }
    public void deleteContact(String number) {
        databaseReference.child(number).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirebaseHelper", "Contact deleted successfully.");
                    } else {
                        Log.e("FirebaseHelper", "Failed to delete contact.", task.getException());
                    }
                });
    }
}

