package com.cuit.ai.bean;

public class Chatntity {
    private String currentUserName;
    private  String message;

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Chatntity{" +
                "currentUserName='" + currentUserName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
