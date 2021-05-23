package com.example.caloriecare.ranking;

import com.example.caloriecare.calendar.DayLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String ID, name, email, birth, profile;
    private boolean gender;
    private List<DayLog> daylogs;
    private List<Double> calorie;

    public User(){
    }
    public User(String ID, String name, String email, String birth, boolean gender, String profile){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
        this.profile = profile;
        this.daylogs = new ArrayList<DayLog>();
    }

    public void pushLog(DayLog log){
        daylogs.add(log);
    }
    public int getAge(){
        return 1;
    }
    public String getID(){  return this.ID; }
    public String getName(){return this.name;}
    public String getEmail(){return this.email;}
    public String getProfile(){return this.profile;}
    public String getBirth (){return this.birth;}
    public boolean getGender(){return this.gender;}
    public List<DayLog> getDaylogs(){return this.daylogs;}

}
