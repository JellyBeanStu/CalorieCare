package com.example.caloriecare.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    String userID;

    SetSpinner spinner;
    ArrayAdapter<String> mainAdapter;
    List<ArrayAdapter<String>> subAdapter;
    Spinner mainCategory, subCategory;

    Data selected;
    double input = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_input);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        spinner = new SetSpinner(true);

        mainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getExerciseCategory());
        subAdapter = new ArrayList<>();

        subAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringExercise(0)));
        subAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringExercise(1)));

        mainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainCategory = (Spinner) findViewById(R.id.main_category_exercise);
        subCategory = (Spinner) findViewById(R.id.sub_category_exercise);

        mainCategory.setAdapter(mainAdapter);
        subCategory.setAdapter(subAdapter.get(0));

        mainCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategory.setAdapter(subAdapter.get(position));
                TextView calorie = (TextView)findViewById(R.id.calorie_burn);
                calorie.setText("#### Kcal");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = spinner.getExercise().get(position);
                EditText time = (EditText)findViewById(R.id.time);

                time.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        input = Double.parseDouble(s.toString());
                        double result = selected.getPerCalorie() * input;
                        TextView calorie = (TextView)findViewById(R.id.calorie_burn);
                        calorie.setText(Double.toString(result) + " Kcal");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {

                    } });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        Button btnBack =(Button)findViewById(R.id.exercise_back);
        Button btnEnter =(Button)findViewById(R.id.exercise_enter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

    }


}