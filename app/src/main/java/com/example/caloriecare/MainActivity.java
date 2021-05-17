package com.example.caloriecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.caloriecare.fragment.*;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String userID;
    private FragmentManager fragmentManager;

    public String getUserID(){
        return this.userID;
    }
    public FragmentManager getfragmentManager(){return this.fragmentManager;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        boolean isExist = intent.getBooleanExtra("isExistingUser",true);

        FragmentTransaction transaction;
        fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);

        MainFragment mainFragment = new MainFragment();
        RankingFragment rankingFragment = new RankingFragment();
        CalendarFragment calendarFragment = new CalendarFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        transaction = fragmentManager.beginTransaction();
        if(isExist){
            transaction.replace(R.id.main_layout, mainFragment).commitAllowingStateLoss();
            bottomNavigationView.setSelectedItemId(R.id.item_home);
        }
        else{
            transaction.replace(R.id.main_layout, profileFragment).commitAllowingStateLoss();
            bottomNavigationView.setSelectedItemId(R.id.item_profile);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(item.getItemId()){
                    case R.id.item_home:
                        transaction.replace(R.id.main_layout, mainFragment).commitAllowingStateLoss();
                        break;
                    case R.id.item_rank:
                        transaction.replace(R.id.main_layout, rankingFragment).commitAllowingStateLoss();
                        break;
                    case R.id.item_statistic:
                        transaction.replace(R.id.main_layout, calendarFragment).commitAllowingStateLoss();
                        break;
                    case R.id.item_profile:
                        transaction.replace(R.id.main_layout, profileFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }
}