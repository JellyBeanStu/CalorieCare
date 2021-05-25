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

public class DietActivity extends AppCompatActivity {
    String userID;
    DietCategory category;

    ArrayAdapter<String> largeAdapter, mediumAdapter, smallAdapter, foodAdapter;
    Spinner largeCategory, mediumCategory, smallCategory, foodCategory;
    String selectedLarge, selectedMedium, selectedSmall;
    List<DietData> nowFoodList;
    DietData selectedFood;

    double input = 0;
    double result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_input);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        readExcel excel = new readExcel(DietActivity.this);
        category = excel.readDietExcel();

        largeCategory = (Spinner) findViewById(R.id.large_category_diet);
        mediumCategory = (Spinner) findViewById(R.id.medium_category_diet);
        smallCategory = (Spinner) findViewById(R.id.small_category_diet);
        foodCategory = (Spinner) findViewById(R.id.food_category_diet);

        initLargeCategory(true);

        largeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLarge = largeCategory.getSelectedItem().toString();
                initMediumCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mediumCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMedium = mediumCategory.getSelectedItem().toString();
                initSmallCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        smallCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSmall = smallCategory.getSelectedItem().toString();
                initFoodCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        foodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFood = nowFoodList.get(position);
                clear();
                EditText times = (EditText)findViewById(R.id.grams);
                times.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length()==0) input = 0;
                        else input = Double.parseDouble(s.toString());
                        result = selectedFood.getCalorie() * input;
                        TextView calorie = (TextView)findViewById(R.id.calorie_intake);
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

        Button btnBack =(Button)findViewById(R.id.diet_back);
        Button btnEnter =(Button)findViewById(R.id.diet_enter);
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
                                Toast.makeText(DietActivity.this, "저장 완료!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("calorie", result);
                                setResult(RESULT_OK,intent);
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
                setLogRequest setlogRequest = new setLogRequest(userID, "DIET", selectedFood.getCode(),input, result, responseListener);
                RequestQueue queue = Volley.newRequestQueue(DietActivity.this);
                queue.add(setlogRequest);

            }
        });
    }

    public void clear(){
        TextView calorie = (TextView)findViewById(R.id.calorie_intake);
        TextView val = (TextView)findViewById(R.id.grams);
        calorie.setText("        Kcal");
        val.setText("");
    }
    private void initFoodCategory(){
        nowFoodList = category.getDietDataList(selectedSmall);
        foodAdapter = new ArrayAdapter<String>(DietActivity.this, android.R.layout.simple_spinner_item, category.getDietNameList(selectedSmall));
        foodCategory.setAdapter(foodAdapter);
        clear();
    }
    private void initSmallCategory(){
        smallAdapter = new ArrayAdapter<String>(DietActivity.this, android.R.layout.simple_spinner_item, category.getSmallCategory(selectedMedium));
        smallCategory.setAdapter(smallAdapter);
        selectedSmall = smallCategory.getSelectedItem().toString();
        initFoodCategory();
    }
    private void initMediumCategory(){
        mediumAdapter = new ArrayAdapter<String>(DietActivity.this, android.R.layout.simple_spinner_item, category.getMediumCategory(selectedLarge));
        mediumCategory.setAdapter(mediumAdapter);
        selectedMedium = mediumCategory.getSelectedItem().toString();
        initSmallCategory();
    }
    private void initLargeCategory(boolean isFirst){
        largeAdapter = new ArrayAdapter<String>(DietActivity.this, android.R.layout.simple_spinner_item, category.getLargeCategory());
        if(isFirst) largeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        largeCategory.setAdapter(largeAdapter);
        selectedLarge = largeCategory.getSelectedItem().toString();
        initMediumCategory();
    }
}