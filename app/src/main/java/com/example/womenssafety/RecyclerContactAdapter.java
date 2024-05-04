package com.example.womenssafety;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {
    Context context;

    ArrayList<contactModel> arrContacts;
    public int position;
    private DatabaseReference databaseReference;

    RecyclerContactAdapter(Context context, ArrayList<contactModel> arrContacts){
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.position = position;
        holder.txtNumber.setText(arrContacts.get(this.position).number);
        holder.txtName.setText(arrContacts.get(this.position).name);
        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //int onClickedPosition = holder.getAdapterPosition();
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
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


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
                        arrContacts.set(holder.getAdapterPosition(), new contactModel(name, number));
                        String contactId = databaseReference.push().getKey();
                        assert contactId != null;
                        databaseReference.child(contactId).setValue(name, number);
                        notifyItemChanged(holder.getAdapterPosition());
                        dialog.dismiss();



                    }

                });
                dialog.show();
                return true;
            }
        });
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               // int onClickedPosition= holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Contact");
                builder.setMessage("Are you sure want to delete");
                builder.setIcon(R.drawable.del);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrContacts.remove(holder.getAdapterPosition());
                        String contactId = databaseReference.push().getKey();
                        assert contactId != null;
                        databaseReference.child(contactId).removeValue();
                        notifyItemRemoved(holder.getAdapterPosition());


                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


            }
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
}
