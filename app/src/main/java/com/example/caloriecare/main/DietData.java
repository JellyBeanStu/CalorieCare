package com.example.caloriecare.main;

import java.io.Serializable;

public class DietData implements Comparable<DietData>{
    private String code, name, unit;
    private double calorie, servingSize;
    private String large, medium, small;


    public DietData(){
        this.code="";
        this.name="";
        this.unit="";
        this.calorie = 0;
        this.servingSize = 0;
        this.large = "";
        this.medium="";
        this.small="";
    }
    public DietData(String code, String large, String medium, String small, String name,double servingSize,String unit, double calorie){
        this.code = code;
        this.large = large;
        this.medium = medium;
        this.small = small;
        this.name = name;
        this.servingSize = servingSize;
        this.unit = unit;
        this.calorie = calorie;
    }
    public String getCode() {
        return code;
    }
    public String getLarge() {
        return large;
    }
    public String getMedium() {
        return medium;
    }
    public String getSmall() {
        return small;
    }
    public String getName() {
        return name;
    }
    public double getServingSize() {
        return servingSize;
    }
    public String getUnit() {
        return unit;
    }
    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int compareTo(DietData o) {
        return this.getName().compareTo(o.getName());
    }
}
