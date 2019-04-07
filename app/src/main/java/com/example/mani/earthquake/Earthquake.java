package com.example.mani.earthquake;

public class Earthquake {

    private String magnitude;
    private String location;
    private String unixTime;

    public Earthquake(String magnitude, String location, String unixTime) {
        this.magnitude = magnitude;
        this.location = location;
        this.unixTime = unixTime;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }
}
