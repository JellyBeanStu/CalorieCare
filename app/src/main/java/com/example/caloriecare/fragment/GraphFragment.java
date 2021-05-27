package com.example.caloriecare.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.AtoBDaylogsRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;

import com.example.caloriecare.graph.DateRangeFragment;
import com.example.caloriecare.graph.NDSpinner;
import com.example.caloriecare.ranking.SpinnerAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class GraphFragment extends Fragment {

    private String userID;
    private LineChart lineChart;
    private ArrayAdapter<String> typeAdapter;
    private TextView tv_begin, tv_end;
    LineData chartData = new LineData();

    private String dateBegin="2021-05-24", dateEnd="2021-05-30";

    List<String> itemType = new ArrayList(Arrays.asList(new String[]{"이번주", "이번달", "올해", "직접 선택"}));

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = v.findViewById(R.id.chart);

        Button btnGraph = v.findViewById(R.id.button5);
        Button btnCalendar = v.findViewById(R.id.button6);
        tv_begin = (TextView)v.findViewById(R.id.beginDate);
        tv_end = (TextView)v.findViewById(R.id.endDate);
    //    Spinner sptype = v.findViewById(R.id.spinner);
        NDSpinner sptype = new NDSpinner(getContext());
        sptype.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        
        ((LinearLayout)v.findViewById(R.id.graph_inner_layout)).addView(sptype);

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

        typeAdapter = new SpinnerAdapter(v.getContext(),android.R.layout.simple_spinner_dropdown_item, itemType);
        sptype.setAdapter(typeAdapter);
        sptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override   // position 으로 몇번째 것이 선택됬는지 값을 넘겨준다
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chartData = new LineData();
                switch(position){
                    case 0:
                        getThisWeek();
                        break;
                    case 1:
                        getThisMonth();
                        break;
                    case 2:
                        getThisYear();
                        break;
                    case 3:
                        getChoice();
                        break;
                }
                if(position != 3){
                    createChart();
                    tv_begin.setText(dateBegin);
                    tv_end.setText(dateEnd);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return v;
    }
    public String dayafter(String date) throws ParseException {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date to = transFormat.parse(date);

        Calendar day = Calendar.getInstance();
        day.setTime(to);
        day.add(Calendar.DATE , 1);
        String beforeDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(day.getTime());
        return beforeDate;
    }
    public void getThisWeek(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, (2-c.get(Calendar.DAY_OF_WEEK)));
        dateBegin = getDay(c.getTime());
        c.add(Calendar.DATE, (8-c.get(Calendar.DAY_OF_WEEK)));
        dateEnd = getDay(c.getTime());
    }
    public void getThisMonth(){
        Calendar c = Calendar.getInstance();
        int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        c.set(year,month,1);
        dateBegin = getDay(c.getTime());
        c.set(year,month,day);
        dateEnd = getDay(c.getTime());
    }
    public void getThisYear(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        c.set(year,0,1);
        dateBegin = getDay(c.getTime());
        c.set(year,11,31);
        dateEnd = getDay(c.getTime());
    }
    public void getChoice(){
        DateRangeFragment AtoB = DateRangeFragment.newInstance(dateBegin, dateEnd, new DateRangeFragment.OutputListener() {
            @Override
            public void onSaveComplete(String begin, String end) {
                dateBegin = begin;
                dateEnd = end;

                chartData = new LineData();
                createChart();

                tv_begin.setText(dateBegin);
                tv_end.setText(dateEnd);
            }
        });
        AtoB.show(getParentFragmentManager(), "addDateRangeDialog");
    }


    //x date, y value
    public void createLine(List<Entry> entries, String labelName, int color){
        LineDataSet lineDataSet = new LineDataSet(entries, labelName);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), color));

        lineDataSet.setValueTextSize(30); //값 텍스트 크기
        lineDataSet.setLineWidth(3); //줄 두께
        lineDataSet.setFillAlpha(90); //투명도 채우기 65
        lineDataSet.setCircleRadius(5f); //데이터점 반지름 5f로
        lineDataSet.setCircleColor(ContextCompat.getColor(getContext(), color));

        chartData.addDataSet(lineDataSet);
        chartData.setValueTextColor(ContextCompat.getColor(getContext(), color));
        chartData.setValueTextSize(9);

        lineChart.setData(chartData);

    }
    public void createChart() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
                        List<Entry> intakeEntry = new ArrayList<>();
                        List<Entry> burnEntry = new ArrayList<>();
                        List<Entry> allEntry = new ArrayList<>();
                        JSONObject temp;
                        String logDate;
                        double intake, burn, all;

                        for(int i=0;i<jsonArray.length();i++){
                            temp = jsonArray.getJSONObject(i);

                            logDate = temp.getString("logDate");     // 해당 데이터의 날짜
                            intake = temp.getDouble("intake");       // 당일 칼로리 섭취량
                            burn = temp.getDouble("burn");           // 당일 칼로리 소모량
                            all = temp.getDouble("dayCalorie");// 당일 총 칼로리

                            intakeEntry.add(new Entry(i, (float) intake));
                            burnEntry.add(new Entry(i, (float) burn));
                            allEntry.add(new Entry(i, (float) all));
                        }
                        createLine(intakeEntry,"섭취 칼로리", R.color.intake);
                        createLine(burnEntry, "소모 칼로리", R.color.burn);
                        createLine(allEntry, "총 칼로리", R.color.black);

                        XAxis xAxis = lineChart.getXAxis();

                        lineChart.setDescription(null);


                        lineChart.invalidate();

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
        AtoBDaylogsRequest atobdaylogsRequest = new AtoBDaylogsRequest(userID, dateBegin, dateEnd, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(atobdaylogsRequest);
    }
    private String getDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private String getToday(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return getDay(date);
    }
}