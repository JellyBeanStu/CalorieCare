package com.example.caloriecare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.calendar.SaturdayDecorator;
import com.example.caloriecare.calendar.SundayDecorator;
import com.example.caloriecare.calendar.TextDecorator;

import java.util.ArrayList;
import java.util.Collections;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        MaterialCalendarView materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

        ArrayList<DayLog> temp = new ArrayList<DayLog>();

        temp.add(new DayLog("2021-05-15",1000,0));
        temp.add(new DayLog("2021-05-18",1000,10000));
        temp.add(new DayLog("2021-05-20",0,0));


        for(int i=0; i<temp.size(); i++){
            ArrayList<Integer> t = temp.get(i).yymmdd();
            CalendarDay day = CalendarDay.from(t.get(0) ,t.get(1)-1, t.get(2));

            materialCalendarView.addDecorators(
                    new TextDecorator(temp.get(i).getIntake(),true, Collections.singleton(day)),
                    new TextDecorator(temp.get(i).getBurn(),false, Collections.singleton(day))
            );
        }

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

                AlertDialog.Builder ad = new AlertDialog.Builder(StatisticActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("제목");
                ad.setMessage(date.toString());

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                materialCalendarView.clearSelection();
            }
        });
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