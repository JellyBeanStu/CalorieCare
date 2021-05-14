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
import com.example.caloriecare.DBrequest.getDaylogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.calendar.DayDecorator;
import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.calendar.SaturdayDecorator;
import com.example.caloriecare.calendar.SundayDecorator;
import com.example.caloriecare.calendar.TextDecorator;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userID;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = ((MainActivity)getActivity()).getUserID();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        // Inflate the layout for this fragment

        Button btnGraph = v.findViewById(R.id.button5);
        Button btnCalendar = v.findViewById(R.id.button6);

        btnCalendar.setBackgroundColor(Color.WHITE);
        btnCalendar.setEnabled(false);
        btnGraph.setBackgroundColor(Color.LTGRAY);
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
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String date = temp.getString("date");
                            double intake = temp.getDouble("intake");
                            double burn = temp.getDouble("burn");
                            double BMR = temp.getDouble("BMR");
                            double dayCalorie = temp.getDouble("dayCalorie");

                            daylogs.add(new DayLog(date, intake, burn));
                        }

                        for(int i=0; i<daylogs.size(); i++){
                            ArrayList<Integer> t = daylogs.get(i).yymmdd();
                            CalendarDay day = CalendarDay.from(t.get(0) ,t.get(1)-1, t.get(2));
                            materialCalendarView.addDecorators(
                                    new TextDecorator(daylogs.get(i).getIntake(),true, Collections.singleton(day)),
                                    new TextDecorator(daylogs.get(i).getBurn(),false, Collections.singleton(day))
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
                getDaylogRequest daylogRequest = new getDaylogRequest(userID, getDay(date.getDate()), responseListener);
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
                                    double BMR = temp.getDouble("BMR");
                                    double dayCalorie = temp.getDouble("dayCalorie");

                                    daylogs.add(new DayLog(date, intake, burn));
                                }

                                for(int i=0; i<daylogs.size(); i++){
                                    ArrayList<Integer> t = daylogs.get(i).yymmdd();
                                    CalendarDay day = CalendarDay.from(t.get(0) ,t.get(1)-1, t.get(2));
                                    materialCalendarView.addDecorators(
                                            new TextDecorator(daylogs.get(i).getIntake(),true, Collections.singleton(day)),
                                            new TextDecorator(daylogs.get(i).getBurn(),false, Collections.singleton(day))
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