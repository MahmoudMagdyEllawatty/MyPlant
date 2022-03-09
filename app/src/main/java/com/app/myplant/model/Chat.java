package com.app.myplant.model;

import java.util.ArrayList;

public class Chat {
    private String key;
    private User user;
    private Farmer farmer;
    private ArrayList<ChatDetails> chats;

    public Chat() {
    }

    public Chat(String key, User user, Farmer farmer, ArrayList<ChatDetails> chats) {
        this.key = key;
        this.user = user;
        this.farmer = farmer;
        this.chats = chats;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public ArrayList<ChatDetails> getChats() {
        return chats;
    }

    public void setChats(ArrayList<ChatDetails> chats) {
        this.chats = chats;
    }

    public static class ChatDetails{
        private String msg;
        private String filePath;
        private String date;
        private int side;

        public ChatDetails() {
        }

        public ChatDetails(String msg, String filePath, String date, int side) {
            this.msg = msg;
            this.filePath = filePath;
            this.date = date;
            this.side = side;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getSide() {
            return side;
        }

        public void setSide(int side) {
            this.side = side;
        }
    }
}
