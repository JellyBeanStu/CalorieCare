package com.example.caloriecare.calendar;

import java.util.ArrayList;

public class DayLog {
    //그 날의 결과값
    private final String date;
    private final double intake; //
    private final double burn;
    private final double dayCalorie; //

    public DayLog(){
        date = "2021-05-10";
        intake = 0;
        burn = 0;
        dayCalorie = 0;
    }
    public DayLog(String date, double intake, double burn){
        this.date = date;
        this.intake = intake;
        this.burn = burn;
        this.dayCalorie = 0;
    }
    public DayLog(String date, double intake, double burn, double dayCalorie){
        this.date = date;
        this.intake = intake;
        this.burn = burn;
        this.dayCalorie = dayCalorie;
    }

    public ArrayList<Integer> yymmdd(){
        ArrayList<Integer> result = new ArrayList<Integer>();

        result.add(Integer.parseInt(this.date.substring(0,4)));
        result.add(Integer.parseInt(this.date.substring(5,7)));
        result.add(Integer.parseInt(this.date.substring(8,10)));

        return result;
    }

    public String getDate(){
        return this.date;
    }
    public double getIntake(){
        return this.intake;
    }
    public double getBurn(){
        return this.burn;
    }
    public double getDayCalorie(){
        return this.dayCalorie;
    }

}
