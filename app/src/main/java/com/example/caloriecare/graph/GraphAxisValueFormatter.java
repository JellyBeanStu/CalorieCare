package com.example.caloriecare.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class GraphAxisValueFormatter extends ValueFormatter {
    private List<String> mValues;

    // 생성자 초기화
    public GraphAxisValueFormatter(List<String> values){
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis){
        return mValues.get((int) value);
    }
}