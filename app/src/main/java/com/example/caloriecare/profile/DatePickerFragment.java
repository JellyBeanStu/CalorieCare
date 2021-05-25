package com.example.caloriecare.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.example.caloriecare.R;
import com.example.caloriecare.main.DietFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends DialogFragment {

    private int mYear =0, mMonth=0, mDay=0;
    private String mDate ="2000-01-01";

    private DatePickerFragment.OutputListener listener;
    public interface OutputListener { void onSaveComplete(String date); }


    public void setyymmdd(String birth){
        mYear = Integer.parseInt(birth.substring(0,4));
        mMonth = Integer.parseInt(birth.substring(5,7))-1;
        mDay = Integer.parseInt(birth.substring(8,10));

    }

    public DatePickerFragment() {
        // Required empty public constructor
    }
    public static DatePickerFragment newInstance(String birth, DatePickerFragment.OutputListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.mDate = birth;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_picker, container, false);


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setyymmdd(mDate);

        v.findViewById(R.id.vDateEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSaveComplete(mDate);
                dismiss();
            }
        });

        DatePicker datePicker = v.findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);

        return v;
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