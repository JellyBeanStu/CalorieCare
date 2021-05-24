package com.example.caloriecare.fragment;

import android.content.Intent;
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
import com.example.caloriecare.main.DietCategory;
import com.example.caloriecare.main.DietFragment;
import com.example.caloriecare.main.ExerciseFragment;
import com.example.caloriecare.main.ReceiptActivity;
import com.example.caloriecare.main.ReceiptFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFragment extends Fragment {

    private User myData;

    ConstraintLayout exercise, diet, receipt;
    TextView exerciseText, dietText, receiptText, BMRText;
    DietCategory category;

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
        BMRText = v.findViewById(R.id.BMR_kcal);

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseFragment dialog = ExerciseFragment.newInstance(myData.getID(), myData.getWeight(), new ExerciseFragment.OutputListener() {
                    @Override
                    public void onSaveComplete(double result) {
                        String today = getToday();
                        if(today.equals(myData.getDate())){
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
                        if(today.equals(myData.getDate())){
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
//                Intent intent = new Intent(getActivity(), ReceiptActivity.class);
//                intent.putExtra("userID", myData.getID());
//                startActivity(intent);

                ReceiptFragment dialog = ReceiptFragment.newInstance(myData.getID());
                dialog.show(getParentFragmentManager(), "addReceiptDialog");

            }
        });

        dietText.setText(String.format("%.1f",myData.getIntake())+ " Kcal");
        exerciseText.setText(String.format("%.1f",myData.getBurn())+ " Kcal");
        setReceiptCalorie();
        BMRText.setText(String.format("%.1f", myData.getBMR()) + " Kcal");

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
        double BMR = myData.getBMR();
        myData.setDayCalorie(intake - burn - BMR);
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

}