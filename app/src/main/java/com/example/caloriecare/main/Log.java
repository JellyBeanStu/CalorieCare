package com.example.caloriecare.main;

public class Log {
    String type, code;
    double volume, calorie;

    public Log(){

    }
    public Log(String type, String code, double volume, double calorie){
        this.type = type;
        this.code = code;
        this.volume = volume;
        this.calorie = calorie;
    }

    public double getCalorie() {
        return calorie;
    }

    public double getVolume() {
        return volume;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
