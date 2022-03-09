package com.app.myplant.model;

public class Plant {
    private String key;
    private String name;
    private PlantCategory category;
    private String imageURL;
    private String details;

    public Plant() {
    }

    public Plant(String key, String name, PlantCategory category, String imageURL, String details) {
        this.key = key;
        this.name = name;
        this.category = category;
        this.imageURL = imageURL;
        this.details = details;
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

    public PlantCategory getCategory() {
        return category;
    }

    public void setCategory(PlantCategory category) {
        this.category = category;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
