package com.example.caloriecare.graph;

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
import android.widget.EditText;
import android.widget.TextView;

import com.example.caloriecare.R;
import com.example.caloriecare.main.DietFragment;

import org.w3c.dom.Text;

public class DateRangeFragment extends DialogFragment {

    private EditText et_begin, et_end;
    private int mYear =0, mMonth=0, mDay=0;
    private String begin="2021-05-01", end="2021-05-01";
    private DateRangeFragment.OutputListener listener;
    public interface OutputListener { void onSaveComplete(String begin, String end); }

    public DateRangeFragment() {
        // Required empty public constructor
    }
    public static DateRangeFragment newInstance(String begin, String end, DateRangeFragment.OutputListener listener) {
        DateRangeFragment fragment = new DateRangeFragment();
        fragment.begin = begin;
        fragment.end = end;
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
        View v = inflater.inflate(R.layout.fragment_date_range, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        et_begin = (EditText)v.findViewById(R.id.begin);
        et_end = (EditText)v.findViewById(R.id.end);

        et_begin.setText(begin);
        et_end.setText(end);

        v.findViewById(R.id.dateRangeBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        v.findViewById(R.id.dateRangeEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                begin = et_begin.getText().toString();
                end = et_end.getText().toString();
                listener.onSaveComplete(begin, end);
                dismiss();
            }
        });

        return v;
    }

}