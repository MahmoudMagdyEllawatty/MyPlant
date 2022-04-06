package com.app.myplant.model;

public class Instruction {
    private String key;
    private String title;
    private String description;
    private String imageURL;
    private PlantCategory plantCategory;

    public Instruction() {
    }

    public Instruction(String key, String title, String description, String imageURL, PlantCategory plantCategory) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.plantCategory = plantCategory;
    }

    public PlantCategory getPlantCategory() {
        return plantCategory;
    }

    public void setPlantCategory(PlantCategory plantCategory) {
        this.plantCategory = plantCategory;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
