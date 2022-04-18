package com.app.myplant.model;

public class Plant {
    private String key;
    private String name;
    private PlantCategory category;
    private String imageURL;
    private String sunExposure;
    private String soilType;
    private String bloomTime;
    private String color;
    private String nativeArea;
    private String toxicity;
    private String water;

    public Plant() {
    }

    public Plant(String key, String name, PlantCategory category, String imageURL, String sunExposure, String soilType, String bloomTime, String color, String nativeArea, String toxicity, String water) {
        this.key = key;
        this.name = name;
        this.category = category;
        this.imageURL = imageURL;
        this.sunExposure = sunExposure;
        this.soilType = soilType;
        this.bloomTime = bloomTime;
        this.color = color;
        this.nativeArea = nativeArea;
        this.toxicity = toxicity;
        this.water = water;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public String getBloomTime() {
        return bloomTime;
    }

    public void setBloomTime(String bloomTime) {
        this.bloomTime = bloomTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNativeArea() {
        return nativeArea;
    }

    public void setNativeArea(String nativeArea) {
        this.nativeArea = nativeArea;
    }

    public String getToxicity() {
        return toxicity;
    }

    public void setToxicity(String toxicity) {
        this.toxicity = toxicity;
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

    public String getSunExposure() {
        return sunExposure;
    }

    public void setSunExposure(String sunExposure) {
        this.sunExposure = sunExposure;
    }
}
