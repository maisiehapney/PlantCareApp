package com.example.plantcareapp.models;

import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * Plant model containing information relating to plants
 */
public class Plant {
    private String name, botanicalName, temperature, water, sunlight, humidity, imageURL;

    public Plant(){}

    public Plant(String name, String botanicalName, String temperature, String water, String sunlight, String humidity, String imageURL) {
        this.name = name;
        this.botanicalName = botanicalName;
        this.temperature = temperature;
        this.water = water;
        this.sunlight = sunlight;
        this.humidity = humidity;
        this.imageURL = imageURL;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBotanicalName() {
        return botanicalName;
    }

    public void setBotanicalName(String botanicalName) {
        this.botanicalName = botanicalName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
