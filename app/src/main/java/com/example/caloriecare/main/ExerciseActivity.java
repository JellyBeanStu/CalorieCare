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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.setLogRequest;
import com.example.caloriecare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    String userID;
    double weight;
    ExerciseCategory category;

    ArrayAdapter<String> allCategoryAdapter, exerciseAdapter;
    Spinner allCategory, exerciseCategory;
    String selectedCategory;

    List<ExerciseData> nowExerciseList;
    ExerciseData selectedExercise;

    double input = 0;
    double result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_input);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        weight = intent.getDoubleExtra("weight",50);

        readExcel excel = new readExcel(ExerciseActivity.this);
        category = excel.readExerciseExcel();

        allCategory = (Spinner) findViewById(R.id.all_category_exercise);
        exerciseCategory = (Spinner) findViewById(R.id.exercise_category_exercise);

        initAllCategory(true);

        allCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = allCategory.getSelectedItem().toString();
                initExerciseCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        exerciseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExercise = nowExerciseList.get(position);
                clear();
                EditText times = (EditText)findViewById(R.id.time);
                times.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length()==0) input = 0;
                        else input = Double.parseDouble(s.toString());
                        result = selectedExercise.getCalorie() * input * weight;
                        TextView calorie = (TextView)findViewById(R.id.calorie_burn);
                        calorie.setText(String.format("%.1f",result) + " Kcal");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    } });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnBack = (Button)findViewById(R.id.exercise_back);
        Button btnEnter = (Button)findViewById(R.id.exercise_enter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("calorie", 0);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(ExerciseActivity.this, "저장 완료!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("calorie", result);
                                setResult(RESULT_OK,intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("error"),Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ExerciseActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };
                setLogRequest setlogRequest = new setLogRequest(userID, "EXERCISE", selectedExercise.getCode(),input, result, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ExerciseActivity.this);
                queue.add(setlogRequest);

            }
        });
    }

    public void clear(){
        TextView calorie = (TextView)findViewById(R.id.calorie_burn);
        TextView val = (TextView)findViewById(R.id.time);
        calorie.setText("        Kcal");
        val.setText("");
    }
    private void initExerciseCategory(){
        nowExerciseList = category.getExerciseDataList(selectedCategory);
        exerciseAdapter = new ArrayAdapter<String>(ExerciseActivity.this, android.R.layout.simple_spinner_item, category.getExerciseNameList(selectedCategory));
        exerciseCategory.setAdapter(exerciseAdapter);
        clear();
    }
    private void initAllCategory(boolean isFirst){
        allCategoryAdapter = new ArrayAdapter<String>(ExerciseActivity.this, android.R.layout.simple_spinner_item, category.getCategory());
        if(isFirst) allCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        allCategory.setAdapter(allCategoryAdapter);
        selectedCategory = allCategory.getSelectedItem().toString(); //에러
        initExerciseCategory();
    }
}