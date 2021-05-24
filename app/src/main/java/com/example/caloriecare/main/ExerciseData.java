package com.example.caloriecare.main;

public class ExerciseData extends Data implements Comparable<ExerciseData>{

    private String code, category, name;
    private double calorie;

    public ExerciseData(){
        this.code="";
        this.name="";
        this.calorie = 0;
    }
    public ExerciseData(String code,String category, String name, double calorie){
        this.code = code;
        this.name = name;
        this.calorie = calorie;
    }

    public String getCode() {
        return code;
    }
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }
    public double getCalorie() {
        return calorie;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(ExerciseData o) {
        return this.getName().compareTo(o.getName());
    }
}
