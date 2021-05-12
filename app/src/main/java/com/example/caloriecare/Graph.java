package com.example.caloriecare;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Graph extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //데이터 입력, db에서 데이터 입력받는 값으로 y값 수정해야함
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));
        entries.add(new Entry(6, 12));
        entries.add(new Entry(7, 11));
        entries.add(new Entry(8, 9));
        entries.add(new Entry(9, 7));
        entries.add(new Entry(10, 4));
        entries.add(new Entry(11, 8));
        entries.add(new Entry(12, 6));

        //입력 데이터 삽입 및 그래프 다자인
        LineDataSet lineDataSet = new LineDataSet(entries, "일 평균 칼로리");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setValueTextSize(13);


        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        //x축 디자인
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(12,true);

        //y축 왼쪽 디자인 및 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setDrawAxisLine(true);
        yLAxis.setDrawGridLines(false);
        yLAxis.setDrawLabels(false);

        //y축 오른쪽 디자인 및 설정
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        //description 부분 디자인 및 설정
        Description description = new Description();
        description.setText("x : 월, y : 평균 칼로리");
        description.setTextSize(8);

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }
}