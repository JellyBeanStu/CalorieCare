package com.example.caloriecare.main;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class readExcel {
    Workbook wb;
    Sheet sheet;
    Context context;

    public readExcel(Context context){
        this.context = context;
    }

    public DietCategory readDietExcel(){
        DietCategory category = new DietCategory();
        try {
            InputStream is = context.getResources().getAssets().open("diet.xls");
            wb = Workbook.getWorkbook(is);
            if (wb != null) {
                sheet = wb.getSheet(0);

                HashMap<String, HashSet<String>> largeCategory = new HashMap<>(); //large to medium
                HashMap<String, HashSet<String>> mediumCategory = new HashMap<>(); //medium to small
                HashMap<String, HashSet<String>> smallCategory = new HashMap<>(); //small to code
                HashMap<String, DietData> allDietCategory = new HashMap<>();        //code to data

                if(sheet != null){
                    int rowIndexStart=1;
                    int rowTotal = sheet.getColumn(0).length;

                    for(int row=rowIndexStart;row<rowTotal;row++){

                        String code = sheet.getCell(0,row).getContents();
                        String large = sheet.getCell(1,row).getContents();
                        String medium = sheet.getCell(2,row).getContents();
                        String small = sheet.getCell(3,row).getContents();
                        String name = sheet.getCell(4,row).getContents();

                        DietData data = new DietData(code,large,medium,small,name,
                                Double.parseDouble(sheet.getCell(5,row).getContents()),
                                sheet.getCell(6,row).getContents(),
                                Double.parseDouble(sheet.getCell(7,row).getContents()));

                        if(!largeCategory.containsKey(large))
                            largeCategory.put(large,new HashSet<>());
                        if(!mediumCategory.containsKey(medium))
                            mediumCategory.put(medium,new HashSet<>());
                        if(!smallCategory.containsKey(small))
                            smallCategory.put(small,new HashSet<>());

                        largeCategory.get(large).add(medium);
                        mediumCategory.get(medium).add(small);
                        smallCategory.get(small).add(code);
                        allDietCategory.put(code,data);
                    }
                    category.setLargeCategory(largeCategory);
                    category.setMediumCategory(mediumCategory);
                    category.setSmallCategory(smallCategory);
                    category.setAllFoodCategory(allDietCategory);
                }

            }
        }catch(IOException | BiffException e){
            System.out.println(e);
        }
        return category;
    }

    public ExerciseCategory readExerciseExcel() {
        ExerciseCategory category = new ExerciseCategory();
        try {
            InputStream is = context.getResources().getAssets().open("exercise.xls");
            wb = Workbook.getWorkbook(is);
            if (wb != null) {
                sheet = wb.getSheet(0);
                HashMap<String, HashSet<String>> exerciseCategory = new HashMap<>(); //category to code
                HashMap<String, ExerciseData> allExerciseCategory = new HashMap<>();        //code to data

                if (sheet != null) {
                    int rowIndexStart = 0;
                    int rowTotal = sheet.getColumn(0).length;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        String code = sheet.getCell(0, row).getContents();
                        String cate = sheet.getCell(1, row).getContents();
                        String name = sheet.getCell(2, row).getContents();
                        double calorie = Double.parseDouble(sheet.getCell(3, row).getContents());

                        ExerciseData data = new ExerciseData(code, cate, name, calorie);

                        if (!exerciseCategory.containsKey(cate))
                            exerciseCategory.put(cate, new HashSet<>());

                        exerciseCategory.get(cate).add(code);
                        allExerciseCategory.put(code, data);
                    }
                    category.setCategory(exerciseCategory);
                    category.setAllExerciseCategory(allExerciseCategory);
                }
            }
        }catch(IOException | BiffException e){
                System.out.println(e);
        }
        return category;
    }
}
