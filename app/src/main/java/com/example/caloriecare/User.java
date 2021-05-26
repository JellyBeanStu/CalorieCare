package com.example.caloriecare;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class User {
    private String ID, name, email, profile, birth;
    private boolean gender;
    private double height, weight, intake, burn, dayCalorie;
    private int age;
    private String date;
    public User(){
    }
    public User(User data){
        this.date = data.getDate();
        this.ID = data.getID();
        this.name = data.getName();
        this.email = data.getEmail();
        this.profile = data.getProfile();
        this.birth = data.getBirth();
        this.gender = data.getGender();
        this.height = data.getHeight();
        this.weight = data.getWeight();
        this.intake = data.getIntake();
        this.burn = data.getBurn();
        this.dayCalorie = data.getDayCalorie();
        this.age = data.getAge();
    }
    public User(String ID, String name, String email, String profile, String birth, boolean gender, double height, double weight, double intake, double burn, double dayCalorie, String date){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.profile = profile;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.intake = intake;
        this.burn = burn;
        this.dayCalorie = dayCalorie;
        this.date = date;
        if(this.height<=0) this.height=170;
        if(this.weight<=0) this.weight=50;
        setAge();
    }
    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }
    public double getWeight() {
        return weight;
    }
    public boolean getGender(){return gender;}
    public double getBurn() {
        return burn;
    }

    public double getDayCalorie() {
        return dayCalorie;
    }

    public double getIntake() {
        return intake;
    }

    public String getBirth() {
        return birth;
    }

    public String getEmail() {
        return email;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setBirth(String birth) {
        this.birth = birth;
        setAge();
    }

    public void setBurn(double burn) {
        this.burn = burn;
    }

    public void setDayCalorie(double dayCalorie) {
        this.dayCalorie = dayCalorie;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setIntake(double intake) {
        this.intake = intake;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setAge(){
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
        this.age = age;
    }

}

