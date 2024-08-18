package com.example.womenssafety;

public class MessageModel {
    private String userId;
    private String message;
    private String userName;

    public MessageModel() {
        // Default constructor required for Firebase
    }

    public MessageModel(String userId, String message, String userName) {
        this.userId = userId;
        this.message = message;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public MessageModel(String message){
        this.message= message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

