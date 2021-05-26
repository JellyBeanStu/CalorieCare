package com.example.caloriecare.ranking;

import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.main.DietData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class User{
    private String ID, name, email, birth, profile;
    private int age;
    private boolean gender;

    private double burn, all;

    public User(){

    }
    public User(String ID, String name, String email, String birth, boolean gender, String profile){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
        this.profile = profile;
        this.age = setAge();
        this.burn = 0;
        this.all = 0;
    }

    public void pushLog(double burn, double all){
        this.burn += burn;
        this.all += all;
    }
    public int getAge(){
        return age;
    }
    public String getID(){  return this.ID; }
    public String getName(){return this.name;}
    public String getEmail(){return this.email;}
    public String getProfile(){return this.profile;}
    public String getBirth (){return this.birth;}
    public boolean getGender(){return this.gender;}
    public int setAge(){
        Calendar calendar = new GregorianCalendar();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int userYear = Integer.parseInt(birth.substring(0,4));
        int userMonth = Integer.parseInt(birth.substring(5,7));
        int userDay = Integer.parseInt(birth.substring(8,10));

        int age = mYear-userYear-1;
        if(mMonth > userMonth || (mMonth == userMonth && mDay>userDay)){
            age++;
        }
        return  age;
    }

    public double getAll() {
        return all;
    }
    public double getBurn() {
        return burn;
    }

}
