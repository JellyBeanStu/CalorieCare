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
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DietActivity extends AppCompatActivity {
    String userID;

    SetSpinner spinner;
    ArrayAdapter<String> mainAdapter;
    List<ArrayAdapter<String>> subAdapter;
    Spinner mainCategory, subCategory;

    Data selected;
    double input = 0;
    double result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_input);
        setTitle("");
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        spinner = new SetSpinner(false);

        mainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getDietCategory());
        subAdapter = new ArrayList<>();

        subAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringDiet(0)));
        subAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringDiet(1)));
        subAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringDiet(2)));
        subAdapter.add(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner.getStringDiet(3)));

        mainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainCategory = (Spinner) findViewById(R.id.main_category_diet);
        subCategory = (Spinner) findViewById(R.id.sub_category_diet);

        mainCategory.setAdapter(mainAdapter);
        subCategory.setAdapter(subAdapter.get(0));

        mainCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategory.setAdapter(subAdapter.get(position));
                TextView calorie = (TextView)findViewById(R.id.calorie_intake);
                calorie.setText("#### Kcal");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = spinner.getDiet().get(position);
                EditText time = (EditText)findViewById(R.id.grams);

                time.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                        input = Double.parseDouble(s.toString());
                        result = selected.getPerCalorie() * input;
                        TextView calorie = (TextView)findViewById(R.id.calorie_intake);
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


        Button btnBack =(Button)findViewById(R.id.diet_back);
        Button btnEnter =(Button)findViewById(R.id.diet_enter);
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
                                Toast.makeText(DietActivity.this, "저장 완료!", Toast.LENGTH_SHORT).show();

                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("error"),Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(DietActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };

                setLogRequest setlogRequest = new setLogRequest(userID, "DIET", selected.getCode(),input, result, responseListener);
                RequestQueue queue = Volley.newRequestQueue(DietActivity.this);
                queue.add(setlogRequest);

            }
        });
    }


}