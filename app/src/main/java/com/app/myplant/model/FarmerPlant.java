package com.app.myplant.model;

public class FarmerPlant {
    private String key;
    private Farmer farmer;
    private Plant plant;
    private double qnt;
    private double price;


    public FarmerPlant() {
    }

    public FarmerPlant(String key, Farmer farmer, Plant plant, double qnt, double price) {
        this.key = key;
        this.farmer = farmer;
        this.plant = plant;
        this.qnt = qnt;
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public double getQnt() {
        return qnt;
    }

    public void setQnt(double qnt) {
        this.qnt = qnt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
