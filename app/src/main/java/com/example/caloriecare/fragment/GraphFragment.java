package com.example.caloriecare.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.AtoBDaylogsRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.calendar.BodyData;
import com.example.caloriecare.calendar.DayLog;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphFragment extends Fragment {

    private String userID;
    private LineChart lineChart;

    private String getDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private String getToday(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return getDay(date);
    }

    public GraphFragment() {
        // Required empty public constructor
    }

    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = ((MainActivity)getActivity()).getUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        Button btnGraph = v.findViewById(R.id.button5);
        Button btnCalendar = v.findViewById(R.id.button6);

        btnCalendar.setBackgroundColor(Color.parseColor("#FFEB3B"));
        btnCalendar.setEnabled(true);
        btnGraph.setBackgroundColor(Color.WHITE);
        btnGraph.setEnabled(false);

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarFragment calendarFragment = new CalendarFragment();
                FragmentTransaction transaction = ((MainActivity)getActivity()).getfragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout, calendarFragment).commitAllowingStateLoss();
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
                        List<DayLog> daylogs = new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject temp = jsonArray.getJSONObject(i);

                            String logDate = temp.getString("logDate");     // 해당 데이터의 날짜
                            double intake = temp.getDouble("intake");       // 당일 칼로리 섭취량
                            double burn = temp.getDouble("burn");           // 당일 칼로리 소모량
                            double dayCalorie = temp.getDouble("dayCalorie");// 당일 총 칼로리

                            daylogs.add(new DayLog(logDate,intake,burn,dayCalorie));
                        }
                        
                        jsonArray = jsonObject.getJSONArray("bodyData");
                        List<BodyData> bodyData = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject temp = jsonArray.getJSONObject(i);

                            String logDate = temp.getString("logDate");     // 해당 데이터의 날짜
                            double height = temp.getDouble("height");       // 당일 키
                            double weight = temp.getDouble("weight");       // 당일 체중
                            double BMR = temp.getDouble("BMR");             // 당일 BMR (기초대사량)

                            bodyData.add(new BodyData(logDate,height,weight,BMR));
                        }
                        
                        // daylogs 일별 로그
                        // bodyData 일별 바디데이터
                        // log 가져온걸로 차트 데이터 입력


                        // 주, 월, 년, 기간선택
                        // 그래프1
                        // 총 칼로리, 섭취칼로리, 소모칼로리 한 그래프안에서 (밑에 체크박스 통해 보이는거 선택가능하게)
                        //
                        // 그래프2
                        // 키 체중 BMR 그래프 위와 마찬가지
                        //
                        //





                    } else {
                        Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        AtoBDaylogsRequest atobdaylogsRequest = new AtoBDaylogsRequest(userID, "","", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(atobdaylogsRequest);



        lineChart = v.findViewById(R.id.chart);

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

        return v;
    }
}