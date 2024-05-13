package com.example.womenssafety;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {

    Context context;

    ArrayList<ContactModel> arrContacts;
    public int position;
    DatabaseReference databaseRef;
    RecyclerContactAdapter adapter;

    RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContacts){
        this.context= context;
        this.arrContacts= arrContacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.contacts_row, parent,false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        this.position = position;

        holder.txtNumber.setText(arrContacts.get(this.position).number);
        holder.txtName.setText(arrContacts.get(this.position).name);
        holder.row.setOnLongClickListener(v -> {

            holder.getAdapterPosition();
            Dialog dialog= new Dialog(context);
            dialog.setContentView(R.layout.add_update);
            EditText editName = dialog.findViewById(R.id.getName);
            EditText editNumber = dialog.findViewById(R.id.getNumber);
            Button btnAction = dialog.findViewById(R.id.btnAction);
            btnAction.setText(R.string.updates);
            TextView txtTitle= dialog.findViewById(R.id.textView3);
            txtTitle.setText(R.string.update_contact);
            editName.setText(arrContacts.get(holder.getAdapterPosition()).name);
            editNumber.setText(arrContacts.get(holder.getAdapterPosition()).number);
            btnAction.setOnClickListener(v1 -> {


                String name = "", number = "";
                if (!editName.getText().toString().isEmpty()) {
                    name = editName.getText().toString();
                } else {
                    Toast.makeText(context, "Please Enter Contact Name", Toast.LENGTH_SHORT).show();
                }

                if (!editNumber.getText().toString().isEmpty()) {
                    number = editNumber.getText().toString();
                } else {
                    Toast.makeText(context, "Please Enter Contact Number", Toast.LENGTH_SHORT).show();
                }
                holder.getAdapterPosition();
                arrContacts.set(holder.getAdapterPosition(), new ContactModel(name, number));

                notifyItemChanged(holder.getAdapterPosition());
                update();

                dialog.dismiss();



            });
            dialog.show();
            return true;
        });
        holder.row.setOnClickListener(v -> {



            holder.getAdapterPosition();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Contact");
            builder.setMessage("Are you sure want to delete");
            builder.setIcon(R.drawable.del);
            builder.setPositiveButton("yes", (dialog, which) -> {
                arrContacts.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                delete();


            });
            builder.setNegativeButton("no", (dialog, which) -> {

            });
            builder.show();


        });
    }
    @Override
    public int getItemCount() {
        return arrContacts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtNumber;

        LinearLayout row;
        public ViewHolder(View a){
            super(a);
            txtName=itemView.findViewById(R.id.txtContact);
            txtNumber=itemView.findViewById(R.id.txtNumber);
            row = itemView.findViewById(R.id.row);
        }


    }
    public void update() {
        String contactId = "contact id";
        String updatedName = "Updated Name";
        String updatedNumber = "Updated Number";

        // Get a reference to the contact node in Firebase
        DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference("contacts").child(contactId);

        // Create a map to hold the updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", updatedName);
        updatedData.put("number", updatedNumber);

// Update the contact data in Firebase
        contactRef.updateChildren(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Contact updated successfully
                        Toast.makeText(context, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update contact
                        Toast.makeText(context, "Failed to update contact", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating contact", e);
                    }
                });
    }
    public  void delete(){
        // Assuming you have the contact ID (key) of the contact you want to delete
        String contactId = "contact_id"; // Replace with the actual contact ID

// Get a reference to the contact node in Firebase
        DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference("contacts").child(contactId);

// Delete the contact from Firebase
        contactRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Contact deleted successfully
                        Toast.makeText(context, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete contact
                        Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error deleting contact", e);
                    }
                });

    }



}
