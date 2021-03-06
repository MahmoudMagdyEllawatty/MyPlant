package com.app.myplant.model;

public class Farmer {
    private String key;
    private String name;
    private String email;
    private String password;
    private String shopName;

    public Farmer() {
    }

    public Farmer(String key, String name, String email, String password, String shopName) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.password = password;
        this.shopName = shopName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
