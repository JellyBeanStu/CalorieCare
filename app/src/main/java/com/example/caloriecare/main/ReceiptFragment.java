package com.example.caloriecare.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.deleteLogRequest;
import com.example.caloriecare.DBrequest.getLogsRequest;
import com.example.caloriecare.DBrequest.getUserDataRequest;
import com.example.caloriecare.DBrequest.setLogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.User;
import com.example.caloriecare.fragment.CalendarFragment;
import com.example.caloriecare.fragment.MainFragment;
import com.example.caloriecare.fragment.ProfileFragment;
import com.example.caloriecare.fragment.RankingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReceiptFragment extends DialogFragment {

    private ReceiptFragment.OutputListener listener;
    public interface OutputListener { void onSaveComplete(double burn ,double intake); }

    String userID;
    String date;
    boolean canDelete;

    double burnSum, intakeSum, calorieSum;
    TextView BurnSum, IntakeSum, CalorieSum;

    private HashMap<String, DietData> AllDietData;
    private HashMap<String, ExerciseData> AllExerciseData;

    private List<Log> logs;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    public static ReceiptFragment newInstance(String userID, String date, boolean canDelete, OutputListener listener) {
        ReceiptFragment fragment = new ReceiptFragment();
        fragment.userID = userID;
        fragment.date = date;
        fragment.canDelete = canDelete;

        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_receipt, container, false);

        AllDietData = ((MainActivity)getActivity()).getDietCategory().getAllFoodCategory();
        AllExerciseData = ((MainActivity)getActivity()).getExerciseCategory().getAllExerciseCategory();

        BurnSum = (TextView)v.findViewById(R.id.BurnSum);
        IntakeSum = (TextView)v.findViewById(R.id.IntakeSum);
        CalorieSum = (TextView)v.findViewById(R.id.CalorieSum);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
                        JSONObject temp;

                        String name="", type="", code="", unit="", logid="";
                        double volume=0, calorie=0;
                        boolean flag = true;
                        LinearLayout layout= new LinearLayout(getContext());

                        burnSum = jsonObject.getDouble("burn");
                        intakeSum = jsonObject.getDouble("intake");
                        calorieSum = intakeSum - burnSum;
                        setSumText();

                        for(int i=0;i<jsonArray.length();i++){
                            temp = jsonArray.getJSONObject(i);
                            logid = temp.getString("logID");
                            type = temp.getString("type");
                            code = temp.getString("code");
                            volume = temp.getDouble("volume");
                            calorie = temp.getDouble("calorie");

                            if(type.equals("DIET")) {
                                if(!AllDietData.containsKey(code)){
                                    name = "WRONG CODE";
                                    unit = "g";
                                }else{
                                    name = AllDietData.get(code).getName();
                                    unit = AllDietData.get(code).getUnit();
                                }
                                layout = (LinearLayout)v.findViewById(R.id.intakeLog);
                                flag = true;
                            }
                            else if(type.equals("EXERCISE")){
                                if(!AllExerciseData.containsKey(code)){
                                    name = "WRONG CODE";
                                }else{
                                    name = AllExerciseData.get(code).getName();
                                }
                                unit = "분";
                                layout = (LinearLayout)v.findViewById(R.id.burnLog);
                                flag = false;
                            }
                            makeLogs(layout, name, logid, volume, unit, flag, calorie);
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
        getLogsRequest daylogRequest = new getLogsRequest(userID, date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(daylogRequest);

        return v;
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

    private void makeLogs(LinearLayout linear, String name, String logid, double volume, String unit, boolean type, double calorie){
        LinearLayout covered = new LinearLayout(getContext());
        TextView nameText = new TextView(getContext());
        TextView volumeText = new TextView(getContext());
        TextView calorieText = new TextView(getContext());
        ImageView delete = new ImageView(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 30;
        covered.setLayoutParams(layoutParams);
        covered.setGravity(Gravity.CENTER);

        nameText.setText(name);
        volumeText.setText(Integer.toString((int)volume)+unit);
        calorieText.setText(String.format("%.1f", calorie) + " Kcal");
        delete.setImageResource(R.drawable.close);

        layoutParams = new LinearLayout.LayoutParams(260, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 0;
        nameText.setLayoutParams(layoutParams);
        nameText.setTextColor(Color.BLACK);
        nameText.setTextSize(17);
        nameText.setGravity(Gravity.CENTER);
        nameText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"bmjua.ttf"));

        layoutParams = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 15;
        volumeText.setLayoutParams(layoutParams);
        volumeText.setTextSize(14);
        volumeText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"bmjua.ttf"));

        layoutParams = new LinearLayout.LayoutParams(240, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 10;
        calorieText.setLayoutParams(layoutParams);
        calorieText.setTextColor(Color.BLACK);
        calorieText.setTextSize(17);
        calorieText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"bmjua.ttf"));

        layoutParams = new LinearLayout.LayoutParams(35,35);
        delete.setLayoutParams(layoutParams);  // imageView layout 설정
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                if(type){
                                    intakeSum -= calorie;
                                }
                                else{
                                    burnSum -= calorie;
                                }
                                calorieSum = intakeSum - burnSum;
                                setSumText();

                                linear.removeView(covered);
                                listener.onSaveComplete(burnSum, intakeSum);
                                Toast.makeText(getActivity(),"삭제 완료!",Toast.LENGTH_LONG).show();
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
                deleteLogRequest deletelog = new deleteLogRequest(logid, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(deletelog);
            }
        });

        covered.addView(nameText);
        covered.addView(volumeText);
        covered.addView(calorieText);
        if(canDelete)
            covered.addView(delete);

        linear.addView(covered);

    }
    private void setSumText(){
        BurnSum.setText(String.format("%.1f",burnSum) + " Kcal");
        IntakeSum.setText(String.format("%.1f",intakeSum) + " Kcal");
        CalorieSum.setText(String.format("%.1f",calorieSum) + " Kcal");

        if(calorieSum < 0)
            CalorieSum.setTextColor(getResources().getColor(R.color.burn));
        else if(calorieSum > 0)
            CalorieSum.setTextColor(getResources().getColor(R.color.intake));
        else{
            CalorieSum.setTextColor(Color.BLACK);
        }
    }
}