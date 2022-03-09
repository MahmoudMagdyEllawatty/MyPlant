package com.app.myplant.model;

public class Order {
    private String key;
    private String date;
    private String time;
    private FarmerPlant farmerPlant;
    private User user;
    private int state;

    public Order() {
    }


    public Order(String key, String date, String time, FarmerPlant farmerPlant, User user, int state) {
        this.key = key;
        this.date = date;
        this.time = time;
        this.farmerPlant = farmerPlant;
        this.user = user;
        this.state = state;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public FarmerPlant getFarmerPlant() {
        return farmerPlant;
    }

    public void setFarmerPlant(FarmerPlant farmerPlant) {
        this.farmerPlant = farmerPlant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
