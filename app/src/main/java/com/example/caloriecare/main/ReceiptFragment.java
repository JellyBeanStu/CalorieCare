package com.example.caloriecare.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.getLogsRequest;
import com.example.caloriecare.DBrequest.setLogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReceiptFragment extends DialogFragment {

    String userID;

    private HashMap<String, DietData> AllDietData;
    private HashMap<String, ExerciseData> AllExerciseData;

    private List<Log> logs;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    public static ReceiptFragment newInstance(String userID) {
        ReceiptFragment fragment = new ReceiptFragment();
        fragment.userID = userID;
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
        logs = new ArrayList<>();

        Button btnBack = (Button)v.findViewById(R.id.receipt_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject temp = jsonArray.getJSONObject(i);
                            String type = temp.getString("type");
                            String code = temp.getString("code");
                            double volume = temp.getDouble("volume");
                            double calorie = temp.getDouble("calorie");

                            logs.add(new Log(type,code,volume, calorie));
                        }

                    } else {
                        Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                // 동적 생성
                 for(int i=0;i<logs.size();i++){
                     String type = logs.get(i).getType();
                     String code = logs.get(i).getCode();
                     Data data = new Data();
                     //parent
                     if(type.equals("DIET")) {
                         data = AllDietData.get(code);
                         //parent = intake
                     }
                     else if(type.equals("EXERCISE")){
                         data = AllExerciseData.get(code);
                         //parent = burn
                     }
                     data.getName();
                     data.getCalorie();
                     data.getUnit();
                 }
            }
        };
        getLogsRequest daylogRequest = new getLogsRequest(userID, getToday(), responseListener);
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
}