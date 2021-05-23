package com.example.caloriecare.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.caloriecare.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerActivity extends AppCompatActivity {

    private int mYear =0, mMonth=0, mDay=0;
    private String mDate ="2000-01-01";

    public void setyymmdd(String birth){
        mYear = Integer.parseInt(birth.substring(0,4));
        mMonth = Integer.parseInt(birth.substring(5,7))-1;
        mDay = Integer.parseInt(birth.substring(8,10));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mDate = intent.getStringExtra("userBirth");

        setContentView(R.layout.activity_date_picker);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setyymmdd(mDate);

        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);
    }
    public void ClickYes(View v){
        Intent intent = new Intent();
        intent.putExtra("date",mDate);
        setResult(RESULT_OK,intent);
        finish();
    }
    DatePicker.OnDateChangedListener mOnDateChangedListener = new DatePicker.OnDateChangedListener(){
        @Override
        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm+1;
            mDay = dd;

            mDate = Integer.toString(mYear)+"-";
            if(mMonth<10)
                mDate += "0"+Integer.toString(mMonth)+"-";
            else mDate += Integer.toString(mMonth)+"-";
            if(mDay<10)
                mDate += "0"+Integer.toString(mDay);
            else mDate += Integer.toString(mDay);
        }
    };
}
