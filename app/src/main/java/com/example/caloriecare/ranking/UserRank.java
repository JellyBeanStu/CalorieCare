package com.example.caloriecare.ranking;

import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.fragment.RankingFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRank {
    private String ID, name, profile;
    private int age;
    private boolean gender;
    private double burnWeek, dayWeek;
    private double burnMonth, dayMonth;
    private double rankCalorie;

    public UserRank(){}
    public UserRank(String ID, String name, int age, boolean gender, String profile,
                    double burnWeek, double dayWeek, double burnMonth, double dayMonth, double rankCalorie){
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.profile = profile;
        this.burnWeek = burnWeek;
        this.dayWeek = dayWeek;
        this.burnMonth = burnMonth;
        this.dayMonth = dayMonth;
        this.rankCalorie = rankCalorie;
    }


    public void pushRank(double rankCalorie){this.rankCalorie = rankCalorie;}
    public int getAge(){return this.age;}
    public String getID(){return this.ID;}
    public String getName(){return this.name;}
    public String getProfile(){return this.profile;}
    public boolean getGender(){return this.gender;}
    public double getBurnWeek(){return this.burnWeek;}
    public double getDayWeek(){return this.dayWeek;}
    public double getBurnMonth(){return this.burnMonth;}
    public double getDayMonth(){return this.dayMonth;}
    public double getRankCalorie(){return this.rankCalorie;}
}
