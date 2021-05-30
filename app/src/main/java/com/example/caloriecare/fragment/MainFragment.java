package com.example.caloriecare.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.User;
import com.example.caloriecare.main.DietFragment;
import com.example.caloriecare.main.ExerciseFragment;
import com.example.caloriecare.main.ReceiptFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFragment extends Fragment {

    private User myData;
    ConstraintLayout exercise, diet, receipt;
    TextView exerciseText, dietText, receiptText, RecommendedText, BMRText;

    public MainFragment() {
        // Required empty public constructor
    }
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        myData = new User(((MainActivity)getActivity()).getMyData());

        exercise = v.findViewById(R.id.exerciseLayout);
        diet = v.findViewById(R.id.dietLayout);
        receipt = v.findViewById(R.id.todayLayout);

        exerciseText = v.findViewById(R.id.exercise_kcal);
        dietText = v.findViewById(R.id.diet_kcal);
        receiptText = v.findViewById(R.id.day_kcal);
        RecommendedText = v.findViewById(R.id.recommended_calorie);
        BMRText = v.findViewById(R.id.bmr);

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseFragment dialog = ExerciseFragment.newInstance(myData.getID(), myData.getWeight(), new ExerciseFragment.OutputListener() {
                    @Override
                    public void onSaveComplete(double result) {
                        String today = getToday();
                        if(!today.equals(myData.getDate())){
                            myData.setIntake(0);
                            myData.setBurn(0);
                            myData.setDate(today);
                        }
                        setExerciseCalorie(result);
                    }
                });
                dialog.show(getParentFragmentManager(), "addExerciseDialog");
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DietFragment dialog = DietFragment.newInstance(myData.getID(), new DietFragment.OutputListener() {
                    @Override
                    public void onSaveComplete(double result) {
                        String today = getToday();
                        if(!today.equals(myData.getDate())){
                            myData.setIntake(0);
                            myData.setBurn(0);
                            myData.setDate(today);
                        }
                        setDietCalorie(result);
                    }
                });
                dialog.show(getParentFragmentManager(), "addDietDialog");
            }
        });
        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiptFragment dialog = ReceiptFragment.newInstance(myData.getID(), new ReceiptFragment.OutputListener() {
                    @Override
                    public void onSaveComplete(double burn, double intake) {
                        myData = ((MainActivity)getActivity()).getMyData();
                        myData.setBurn(burn);
                        myData.setIntake(intake);
                        myData.setDayCalorie(intake-burn);
                        ((MainActivity)getActivity()).setMyData(myData);

                        dietText.setText(String.format("%.1f",myData.getIntake())+ " Kcal");
                        exerciseText.setText(String.format("%.1f",myData.getBurn())+ " Kcal");
                        setReceiptCalorie();
                    }
                });
                dialog.show(getParentFragmentManager(), "addReceiptDialog");
            }
        });

        dietText.setText(String.format("%.1f",myData.getIntake())+ " Kcal");
        exerciseText.setText(String.format("%.1f",myData.getBurn())+ " Kcal");
        setReceiptCalorie();

        BMRText.setText(String.format("%.1f",calculateBMR())+" Kcal");
        RecommendedText.setText(String.format("%.1f",calculateRecommendedCalorie())+" Kcal");
        return v;
    }
    public void setExerciseCalorie(double addCalorie){
        double burn = myData.getBurn();
        myData.setBurn(burn + addCalorie);
        exerciseText.setText(String.format("%.1f",myData.getBurn())+ " Kcal");
        setReceiptCalorie();
    }
    public void setDietCalorie(double addCalorie){
        double intake = myData.getIntake();
        myData.setIntake(intake + addCalorie);
        dietText.setText(String.format("%.1f",myData.getIntake())+ " Kcal");
        setReceiptCalorie();
    }
    public void setReceiptCalorie(){
        double burn = myData.getBurn();
        double intake = myData.getIntake();
        myData.setDayCalorie(intake - burn);
        double dayCalorie = myData.getDayCalorie();
        String temp = "";
        if(dayCalorie > 0){
            receiptText.setTextColor(Color.parseColor("#0c2461"));
            temp = " ";
        }
        else if(dayCalorie < 0) receiptText.setTextColor(Color.parseColor("#b71540"));
        else receiptText.setTextColor(Color.parseColor("#2f3640"));

        receiptText.setText(temp+String.format("%.1f",dayCalorie)+ " Kcal");

        ((MainActivity)getActivity()).setMyData(myData);
    }
    private String getDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private String getToday(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return getDay(date);
    }
    private double calculateRecommendedCalorie(){
        double recommendedWeight, Recommended;
        int k;
        if(myData.getGender()){
            k = 21;
        }else{
            k = 22;
        }
        recommendedWeight = k * myData.getHeight() * myData.getHeight() / 10000;
        Recommended = recommendedWeight * 30;
        return Recommended;
    }
    private double calculateBMR(){
        double BMR;
        if(myData.getGender()){
            BMR = 655.1 + (9.56*myData.getWeight()) + (1.85*myData.getHeight()) - (4.68*myData.getAge());
        }else{
            BMR = 66.47 + (13.75*myData.getWeight()) + (5*myData.getHeight()) - (6.76*myData.getAge());
        }
        return BMR;
    }

}