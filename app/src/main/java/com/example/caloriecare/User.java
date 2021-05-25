package com.example.caloriecare;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class User {
    private String ID, name, email, profile, birth;
    private boolean gender;
    private double height, weight, BMR, intake, burn, dayCalorie;
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
        this.BMR = data.getBMR();
        this.intake = data.getIntake();
        this.burn = data.getBurn();
        this.dayCalorie = data.getDayCalorie();
        this.age = data.getAge();
    }
    public User(String ID, String name, String email, String profile, String birth, boolean gender, double height, double weight, double BMR, double intake, double burn, double dayCalorie, String date){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.profile = profile;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.BMR = BMR;
        this.intake = intake;
        this.burn = burn;
        this.dayCalorie = dayCalorie;
        this.date = date;
        if(this.height<=0) this.height=170;
        if(this.weight<=0) this.weight=50;
        setAge();
        calculateBMR();
    }
    public int getAge() {
        return age;
    }
    public double getBMR() {
        return BMR;
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
        calculateBMR();
    }

    public void setBMR(double BMR) {
        this.BMR = BMR;
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
        calculateBMR();
    }

    public void setIntake(double intake) {
        this.intake = intake;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        calculateBMR();
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
            age = age+1;
        }
        this.age = age;
    }

    public void calculateBMR(){
        this.BMR = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
    }
}

