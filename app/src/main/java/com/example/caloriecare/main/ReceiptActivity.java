package com.example.caloriecare.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.getLogsRequest;
import com.example.caloriecare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptActivity extends AppCompatActivity {

    private String userID;

    private String getToday(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    private void addText(String text){
        TextView newText = new TextView(ReceiptActivity.this);
        newText.setText(text);
        newText.setTextSize(12);

     //   listview.addView(newText);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_calorie);
        setTitle("");
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("logs");
                        jsonArray.toString();
//                                for(int i=0;i<jsonArray.length();i++){
//                                    JSONObject temp = jsonArray.getJSONObject(i);
//                                    long logID = temp.getLong("logID");
//                                    int type = temp.getInt("type");
//                                    String code = temp.getString("code");
//                                    double volume = temp.getDouble("volume");
//                                    double calorie = temp.getDouble("calorie");
//                                }

                    } else {
                        Toast.makeText(ReceiptActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    Toast.makeText(ReceiptActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        getLogsRequest daylogRequest = new getLogsRequest(userID, getToday(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(ReceiptActivity.this);
        queue.add(daylogRequest);
    }
}