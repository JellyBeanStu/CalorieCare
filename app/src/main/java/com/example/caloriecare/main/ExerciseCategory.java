package com.example.caloriecare.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExerciseCategory {

    private HashMap<String, ExerciseData> AllExerciseCategory; //code to Data
    private HashMap<String, HashSet<String>> category; // category to code

    public ExerciseCategory(){
        category = new HashMap<>();
        AllExerciseCategory = new HashMap<>();
    }
    public void setAllExerciseCategory(HashMap<String, ExerciseData> allExerciseCategory) {
        AllExerciseCategory = allExerciseCategory;
    }
    public void setCategory(HashMap<String, HashSet<String>> category) {
        this.category = category;
    }

    public List<String> getCategory() {
        ArrayList<String> temp = new ArrayList<String>(category.keySet());
        Collections.sort(temp);
        return temp;
    }
    public HashMap<String, ExerciseData> getAllExerciseCategory() {
        return AllExerciseCategory;
    }

    public List<ExerciseData> getExerciseDataList(String small){
        List<ExerciseData> exerciseCategory = new ArrayList<>();
        List<String> temp = new ArrayList<String>(category.get(small));    //CODE List
        for(int i=0;i<temp.size();i++)
            exerciseCategory.add(AllExerciseCategory.get(temp.get(i)));
        Collections.sort(exerciseCategory);
        return exerciseCategory;
    }
    public List<String> getExerciseNameList(String small){
        List<String> exerciseCategory = new ArrayList<>();
        List<String> temp = new ArrayList<String>(category.get(small));    //CODE List
        for(int i=0;i<temp.size();i++)
            exerciseCategory.add(AllExerciseCategory.get(temp.get(i)).getName());
        Collections.sort(exerciseCategory);
        return exerciseCategory;
    }

}
