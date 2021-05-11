package com.example.caloriecare.calendar;

import java.util.ArrayList;

public class DayLog {

    private final String date;
    private final float intake;
    private final float burn;

    public DayLog(){
        date = "2021-05-10";
        intake = 0;
        burn = 0;
    }
    public DayLog(String date, int intake, int burn){
        this.date = date;
        this.intake = intake;
        this.burn = burn;
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
    public float getIntake(){
        return this.intake;
    }
    public float getBurn(){
        return this.burn;
    }

}
