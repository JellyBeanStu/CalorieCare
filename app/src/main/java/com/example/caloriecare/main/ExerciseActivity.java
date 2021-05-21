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

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    String userID;

    SetSpinner spinner;
    ArrayAdapter<String> largeAdapter;
    List<ArrayAdapter<String>> mediumAdapter;
    List<ArrayAdapter<String>> smallAdapter;
    Spinner largeCategory, mediumCategory, smallCategory;

    Data selected;
    double input = 0;
    double result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_input);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        spinner = new SetSpinner(true);

        largeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getExerciseCategory());
        smallAdapter = new ArrayList<>();

        smallAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringExercise(0)));
        smallAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringExercise(1)));

        largeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        largeCategory = (Spinner) findViewById(R.id.large_category_exercise);
        smallCategory = (Spinner) findViewById(R.id.small_category_exercise);

        largeCategory.setAdapter(largeAdapter);
        smallCategory.setAdapter(smallAdapter.get(0));

        largeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                smallCategory.setAdapter(smallAdapter.get(position));
                TextView calorie = (TextView)findViewById(R.id.calorie_burn);
                calorie.setText("#### Kcal");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        smallCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        result = selected.getPerCalorie() * input;
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

                setLogRequest setlogRequest = new setLogRequest(userID, "EXERCISE", selected.getCode(),input,result, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ExerciseActivity.this);
                queue.add(setlogRequest);

            }
        });

    }

}