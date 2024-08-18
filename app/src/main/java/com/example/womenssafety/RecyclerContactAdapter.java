package com.example.womenssafety;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {

    Context context;
    private static String contactId;
    private final FirebaseHelper firebaseHelper;

    ArrayList<ContactModel> arrContacts;
    RecyclerContactAdapter adapter;

    RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContacts) {
        this.context = context;
        this.arrContacts = arrContacts;
        firebaseHelper = new FirebaseHelper();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contacts_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel contact = arrContacts.get(position);
        holder.txtName.setText(contact.name);
        holder.txtNumber.setText(contact.number);

        holder.row.setOnLongClickListener(v -> {


            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                showUpdateDialog(pos);
            }
            return true;
        });

        holder.row.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                showDeleteDialog(pos);
                //deleteContact();
            }
        });
        holder.callBtn.setOnClickListener(v -> makeCall(contact.getNumber()));
    }

    private void showUpdateDialog(int position) {
        ContactModel contact = arrContacts.get(position);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_update);
        EditText editName = dialog.findViewById(R.id.getName);
        EditText editNumber = dialog.findViewById(R.id.getNumber);
        Button btnAction = dialog.findViewById(R.id.btnAction);
        btnAction.setText("Update");
        TextView txtTitle = dialog.findViewById(R.id.textView3);
        txtTitle.setText("Update Contact");
        editName.setText(contact.name);
        editNumber.setText(contact.number);

        btnAction.setOnClickListener(v -> {

            String name = editName.getText().toString();
            String number = editNumber.getText().toString();
            // Update in Firebase
            firebaseHelper.updateContact(contact.getNumber(), name, number);

            if (!name.isEmpty() && !number.isEmpty()) {
                arrContacts.set(position, new ContactModel(name, number));
                notifyItemChanged(position);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Name and Number cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ContactModel contact = arrContacts.get(position);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            firebaseHelper.deleteContact(contact.getNumber());
            arrContacts.remove(position);
            notifyItemRemoved(position);
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void makeCall(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(callIntent);
            }
        } else {
            Toast.makeText(context, "Phone number is empty", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return arrContacts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtNumber;
      Button callBtn;

        LinearLayout row;


        public ViewHolder(View a) {
            super(a);
            txtName = itemView.findViewById(R.id.txtContact);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            row = itemView.findViewById(R.id.row);
            callBtn = itemView.findViewById(R.id.btnCall);



        }


    }

}









