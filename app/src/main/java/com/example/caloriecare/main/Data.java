package com.example.caloriecare.main;

public class Data {
    private String code, name, unit;
    private double calorie;

    public Data(){

    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public String getCode() {
        return code;
    }

    public double getCalorie() {
        return calorie;
    }

    public String getName() {
        return name;
    }
}
