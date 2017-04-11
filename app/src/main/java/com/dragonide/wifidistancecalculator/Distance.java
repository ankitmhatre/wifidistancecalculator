package com.dragonide.wifidistancecalculator;

/**
 * Created by Ankit on 4/11/2017.
 */

public class Distance {
    private double freq, strength, distance;
    private String name;

    public Distance() {

    }

    public Distance(double freq, double strength, double distance, String name) {
        this.freq = freq;
        this.strength = strength;
        this.distance = distance;
        this.name = name;
    }

    public double getFreq() {
        return freq;
    }

    public void setTitle(double freq) {
        this.freq = freq;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
