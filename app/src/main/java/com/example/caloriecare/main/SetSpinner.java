package com.example.caloriecare.main;

import java.util.ArrayList;
import java.util.List;

public class SetSpinner {
    private List<String> exerciseCategory;
    private List<String> dietCategory;
    private List<Data> exercise;
    private List<Data> diet;

    public SetSpinner(boolean type){
        if(type){
            exerciseCategory = new ArrayList<>();
            exercise = new ArrayList<>();
            setExsercise();
        }else{
            dietCategory = new ArrayList<>();
            diet = new ArrayList<>();
            setDiet();
        }
    }
    public void setExsercise(){
        
        //유산소
        exerciseCategory.add("유산소");
        exercise.add(new Data("조깅","EX0001",20,0));
        exercise.add(new Data("달리기","EX0002",30,0));
        exercise.add(new Data("걷기","EX0003",10,0));
        //무산소
        exerciseCategory.add("무산소");
        exercise.add(new Data("벤치프레스","EX0004",50,1));
        exercise.add(new Data("레그프레스","EX0005",50,1));
        exercise.add(new Data("데드리프트","EX0006",50,1));

    }
    public void setDiet(){
        //한식
        dietCategory.add("한식");
        diet.add(new Data("라면","FO0001",100,0));
        diet.add(new Data("김밥","FO0002",50,0));
        diet.add(new Data("비빔밥","FO0003",200,0));
        //일식
        dietCategory.add("일식");
        diet.add(new Data("초밥","FO0004",300,1));
        diet.add(new Data("회","FO0005",10,1));
        diet.add(new Data("라멘","FO0006",500,1));
        //중식
        dietCategory.add("중식");
        diet.add(new Data("짜장면","FO0007",500,2));
        diet.add(new Data("짬뽕","FO0008",400,2));
        diet.add(new Data("탕수육","FO0009",500,2));
        //양식
        dietCategory.add("양식");
        diet.add(new Data("짜장면","FO0007",500,3));
        diet.add(new Data("짬뽕","FO0008",400,3));
        diet.add(new Data("탕수육","FO0009",500,3));
    }
    public List<Data> getExercise(){
        return this.exercise;
    }
    public List<Data> getDiet() {
        return this.diet;
    }
    public List<String> getExerciseCategory(){
        return this.exerciseCategory;
    }
    public List<String> getDietCategory() {
        return dietCategory;
    }
    public List<String> getStringExercise(int category){
        List<String> temp = new ArrayList<>();
        for(int i=0; i<exercise.size(); i++){
            if(exercise.get(i).getCategory() == category)
                temp.add(exercise.get(i).getName());
        }
        return temp;
    }
    public List<String> getStringDiet(int category){
        List<String> temp = new ArrayList<>();
        for(int i=0; i<diet.size(); i++){
            if(diet.get(i).getCategory() == category)
            temp.add(diet.get(i).getName());
        }
        return temp;
    }
}
