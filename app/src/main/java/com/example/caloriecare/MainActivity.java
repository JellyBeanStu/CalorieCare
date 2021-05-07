package com.example.caloriecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("main");
    }
    public void Click1(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void Click2(View v){
        Intent intent = new Intent(this,RankingActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void Click3(View v){
        Intent intent = new Intent(this,StatisticActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void Click4(View v){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

}