package com.example.caloriecare.ranking;

import android.graphics.Bitmap;

public class RankVal {
    private String ID, name;
    private String profile;
    private int age;
    private boolean gender;
    private double rankCalorie;

    public RankVal(){}

    public RankVal(String ID, String name, int age, boolean gender, String profile,
                   double rankCalorie){
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.profile = profile;
        this.rankCalorie = rankCalorie;
    }

    public int getAge(){return this.age;}
    public String getID(){return this.ID;}
    public String getName(){return this.name;}
    public String getProfile(){return this.profile;}
    public boolean getGender(){return this.gender;}
    public double getRankCalorie(){return this.rankCalorie;}
}
