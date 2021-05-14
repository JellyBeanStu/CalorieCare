package com.example.caloriecare.calendar;

public class BodyData {
    private String logDate;
    private double height, weight, BMR;

    public BodyData(String logDate, double height, double weight, double BMR){
        this.logDate = logDate;
        this.height = height;
        this.weight = weight;
        this.BMR = BMR;
    }

    public String getLogDate() {
        return logDate;
    }
    public double getWeight() {
        return weight;
    }
    public double getHeight() {
        return height;
    }
    public double getBMR() {
        return BMR;
    }
}
