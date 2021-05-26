package com.example.caloriecare.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.example.caloriecare.DBrequest.MonthlogRequest;
import com.example.caloriecare.DBrequest.getLogsRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.calendar.DayDecorator;
import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.calendar.SaturdayDecorator;
import com.example.caloriecare.calendar.SundayDecorator;
import com.example.caloriecare.calendar.TextDecorator;
import com.example.caloriecare.main.DietData;
import com.example.caloriecare.main.ExerciseData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class CalendarFragment extends Fragment {

    private String userID;
    private HashMap<String, DietData> AllDietData;
    private HashMap<String, ExerciseData> AllExerciseData;

    private String getDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private String getToday(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return getDay(date);
    }
    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = ((MainActivity)getActivity()).getUserID();
        AllDietData = ((MainActivity)getActivity()).getDietCategory().getAllFoodCategory();
        AllExerciseData = ((MainActivity)getActivity()).getExerciseCategory().getAllExerciseCategory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        Button btnGraph = v.findViewById(R.id.button5);
        Button btnCalendar = v.findViewById(R.id.button6);

        btnCalendar.setBackgroundColor(Color.WHITE);
        btnCalendar.setEnabled(false);
        btnGraph.setBackgroundColor(Color.parseColor("#FFEB3B"));
        btnGraph.setEnabled(true);

        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GraphFragment graphFragment = new GraphFragment();
                FragmentTransaction transaction = ((MainActivity)getActivity()).getfragmentManager().beginTransaction();
                transaction.replace(R.id.main_layout, graphFragment).commitAllowingStateLoss();
            }
        });

        MaterialCalendarView materialCalendarView = (MaterialCalendarView)v.findViewById(R.id.calendarView);

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new DayDecorator()
        );

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    ArrayList<DayLog> daylogs = new ArrayList<>();

                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
                        JSONObject temp;
                        String date;
                        double intake, burn;
                        for(int i=0;i<jsonArray.length();i++){
                            temp = jsonArray.getJSONObject(i);
                            date = temp.getString("date");
                            intake = temp.getDouble("intake");
                            burn = temp.getDouble("burn");

                            daylogs.add(new DayLog(date, intake, burn));
                        }

                        for(int i=0; i<daylogs.size(); i++){
                            ArrayList<Integer> t = daylogs.get(i).yymmdd();
                            CalendarDay day = CalendarDay.from(t.get(0) ,t.get(1)-1, t.get(2));
                            materialCalendarView.addDecorators(
                                    new TextDecorator(daylogs.get(i).getIntake(),"DIET", Collections.singleton(day)),
                                    new TextDecorator(daylogs.get(i).getBurn(),"EXERCISE", Collections.singleton(day))
                            );
                        }
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
        MonthlogRequest monthlogRequest = new MonthlogRequest(userID, getToday(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(monthlogRequest);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                            ad.setIcon(R.mipmap.ic_logo);
                            ad.setTitle(getDay(date.getDate()));

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                JSONArray jsonArray = jsonObject.getJSONArray("logs");
                                ad.setMessage(jsonArray.toString());
//                                for(int i=0;i<jsonArray.length();i++){
//                                    JSONObject temp = jsonArray.getJSONObject(i);
//                                    long logID = temp.getLong("logID");
//                                    int type = temp.getInt("type");
//                                    String code = temp.getString("code");
//                                    double volume = temp.getDouble("volume");
//                                    double calorie = temp.getDouble("calorie");
//                                }

                            } else {
                                Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                                return;
                            }

                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            ad.show();
                            materialCalendarView.clearSelection();

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                };
                getLogsRequest daylogRequest = new getLogsRequest(userID, getDay(date.getDate()), responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(daylogRequest);
            }

        });
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            ArrayList<DayLog> daylogs = new ArrayList<>();

                            if (success) {
                                JSONArray jsonArray = jsonObject.getJSONArray("logs");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject temp = jsonArray.getJSONObject(i);
                                    String date = temp.getString("date");
                                    double intake = temp.getDouble("intake");
                                    double burn = temp.getDouble("burn");

                                    daylogs.add(new DayLog(date, intake, burn));
                                }

                                for(int i=0; i<daylogs.size(); i++){
                                    ArrayList<Integer> t = daylogs.get(i).yymmdd();
                                    CalendarDay day = CalendarDay.from(t.get(0) ,t.get(1)-1, t.get(2));
                                    materialCalendarView.addDecorators(
                                            new TextDecorator(daylogs.get(i).getIntake(),"DIET", Collections.singleton(day)),
                                            new TextDecorator(daylogs.get(i).getBurn(),"EXERCISE", Collections.singleton(day))
                                    );
                                }
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

                MonthlogRequest monthlogRequest = new MonthlogRequest(userID, getDay(date.getDate()), responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(monthlogRequest);
            }
        });
        materialCalendarView.setTileHeightDp(72);

        return v;
    }
}