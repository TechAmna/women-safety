package com.example.womenssafety;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;






public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
     List<MessageModel> messageList;




    MessageAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;

    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        holder.messageTextView.setText(message.getMessage());
        MessageModel userName = messageList.get(position);
        holder.nameTextView.setText(userName.getUserName());

    }

    @Override
    public int getItemCount() {return messageList.size();


    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        TextView nameTextView;


        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message);
            nameTextView = itemView.findViewById(R.id.nameofWomen);
        }
        }
    }



