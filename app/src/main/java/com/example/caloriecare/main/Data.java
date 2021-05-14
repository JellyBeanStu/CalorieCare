package com.example.caloriecare.main;

public class Data {
    private String name, code;
    private double perCalorie;
    private int category;
    public Data(String name, String code, double perCalorie, int category){
        this.name = name;
        this.code = code;
        this.perCalorie = perCalorie;
        this.category = category;
    }
    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }
    public double getPerCalorie() {
        return perCalorie;
    }
    public int getCategory() {
        return category;
    }
}
