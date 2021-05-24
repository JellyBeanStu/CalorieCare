package com.example.caloriecare.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DietCategory {

    private HashMap<String, DietData> AllDietCategory;          //code to Data

    private HashMap<String, HashSet<String>> largeCategory; //large to medium
    private HashMap<String, HashSet<String>> mediumCategory; //medium to small
    private HashMap<String, HashSet<String>> smallCategory; //small to code

    //현재 선택된 애들

    public DietCategory(){
        largeCategory = new HashMap<>();
        mediumCategory = new HashMap<>();
        smallCategory = new HashMap<>();

        AllDietCategory = new HashMap<>();
    }

    public void setLargeCategory(HashMap<String, HashSet<String>> category){
        this.largeCategory = category;
    }
    public void setMediumCategory(HashMap<String, HashSet<String>> category){
        this.mediumCategory = category;
    }
    public void setSmallCategory(HashMap<String, HashSet<String>> category){
        this.smallCategory = category;
    }
    public void setAllFoodCategory(HashMap<String, DietData> allFoodCategory) {
        AllDietCategory = allFoodCategory;
    }
    public List<String> getLargeCategory() {
        ArrayList<String> temp = new ArrayList<String>(largeCategory.keySet());
        Collections.sort(temp);
        return temp;
    }
    public List<String> getMediumCategory(String large) {
        ArrayList<String> temp = new ArrayList<String>(largeCategory.get(large));
        Collections.sort(temp);
        return temp;
    }
    public List<String> getSmallCategory(String medium) {
        ArrayList<String> temp = new ArrayList<String>(mediumCategory.get(medium));
        Collections.sort(temp);
        return temp;
    }
    public HashMap<String, DietData> getAllFoodCategory() {
        return AllDietCategory;
    }
    public List<DietData> getDietDataList(String small){
        List<DietData> dietCategory = new ArrayList<>();
        List<String> temp = new ArrayList<String>(smallCategory.get(small));    //CODE List
        for(int i=0;i<temp.size();i++)
            dietCategory.add(AllDietCategory.get(temp.get(i)));
        Collections.sort(dietCategory);
        return dietCategory;
    }
    public List<String> getDietNameList(String small){
        List<String> dietCategory = new ArrayList<>();
        List<String> temp = new ArrayList<String>(smallCategory.get(small));    //CODE List
        for(int i=0;i<temp.size();i++){
            DietData data = AllDietCategory.get(temp.get(i));
            dietCategory.add(data.getName());
        }
        Collections.sort(dietCategory);
        return dietCategory;
    }
}
