package com.example.caloriecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.getUserDataRequest;
import com.example.caloriecare.fragment.*;

import com.example.caloriecare.main.DietCategory;
import com.example.caloriecare.main.ExerciseCategory;
import com.example.caloriecare.main.readExcel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private User myData;
    private FragmentManager fragmentManager;
    private DietCategory dietCategory;
    private ExerciseCategory exerciseCategory;

    public void setMyData(User myData) {
        this.myData = myData;
    }
    public User getMyData() {
        return myData;
    }
    public String getUserID(){
        return this.myData.getID();
    }
    public DietCategory getDietCategory(){return this.dietCategory;}
    public ExerciseCategory getExerciseCategory(){return this.exerciseCategory;}

    public FragmentManager getfragmentManager(){return this.fragmentManager;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myData = new User();
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        boolean isExist = intent.getBooleanExtra("isExistingUser",true);

        readExcel excel = new readExcel(MainActivity.this);
        dietCategory = excel.readDietExcel();
        exerciseCategory = excel.readExerciseExcel();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        String userName = jsonObject.getString("userName");
                        String userEmail = jsonObject.getString("userEmail");
                        String userProfileImg = jsonObject.getString("userProfile");
                        boolean userGender = jsonObject.getInt("userGender")!=0;
                        String userBirth = jsonObject.getString("userBirth");

                        double height = jsonObject.getDouble("height");
                        double weight = jsonObject.getDouble("weight");
                        double BMR = jsonObject.getDouble("BMR");

                        double intake = jsonObject.getDouble("intake");
                        double burn = jsonObject.getDouble("burn");
                        double dayCalorie = intake - burn - BMR;

                        myData = new User(userID, userName, userEmail, userProfileImg, userBirth, userGender,height, weight, BMR, intake,burn,dayCalorie);

                        FragmentTransaction transaction;
                        fragmentManager = getSupportFragmentManager();
                        transaction = fragmentManager.beginTransaction();

                        MainFragment mainFragment = new MainFragment();
                        RankingFragment rankingFragment = new RankingFragment();
                        CalendarFragment calendarFragment = new CalendarFragment();
                        ProfileFragment profileFragment = new ProfileFragment();

                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
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

                    } else {
                        Toast.makeText(MainActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        getUserDataRequest userDataRequest = new getUserDataRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(userDataRequest);
    }
}