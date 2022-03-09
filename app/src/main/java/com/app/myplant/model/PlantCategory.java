package com.app.myplant.model;

public class PlantCategory {
    private String key;
    private String name;
    private String imageURL;


    public PlantCategory() {
    }


    public PlantCategory(String key, String name, String imageURL) {
        this.key = key;
        this.name = name;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
