package com.example.caloriecare.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.caloriecare.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> items = new ArrayList<>();
    boolean big;
    public SpinnerAdapter(boolean big, final Context context, final int textViewResourceId, final List<String> objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.big = big;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false);

        }

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items.get(position));
        tv.setTextColor(Color.parseColor("#1e272e"));
        tv.setTextSize(16);
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(),"bmjua.ttf"));

        return convertView;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items.get(position));

        if(big){
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(22);
            tv.setSingleLine(false);

        }else{
            tv.setTextColor(Color.parseColor("#1e272e"));
            tv.setTextSize(18);
        }

        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(),"bmjua.ttf"));

        return convertView;

    }

}