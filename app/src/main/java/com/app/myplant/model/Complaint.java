package com.app.myplant.model;

public class Complaint {
    private String key;
    private String title;
    private String text;
    private User user;
    private String reply;

    public Complaint() {
    }


    public Complaint(String key, String title, String text, User user, String reply) {
        this.key = key;
        this.title = title;
        this.text = text;
        this.user = user;
        this.reply = reply;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
