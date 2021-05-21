package com.example.caloriecare.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.getTodaylogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.main.DietActivity;
import com.example.caloriecare.main.ExerciseActivity;
import com.example.caloriecare.main.ReceiptActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userID;
    ConstraintLayout exercise, diet, receipt;
    TextView exerciseText, dietText, receiptText, BMRText;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userID = ((MainActivity)getActivity()).getUserID();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        exercise = v.findViewById(R.id.exerciseLayout);
        diet = v.findViewById(R.id.dietLayout);
        receipt = v.findViewById(R.id.todayLayout);

        exerciseText = v.findViewById(R.id.exercise_kcal);
        dietText = v.findViewById(R.id.diet_kcal);
        receiptText = v.findViewById(R.id.day_kcal);
        BMRText = v.findViewById(R.id.BMR_kcal);

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExerciseActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DietActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReceiptActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                         String intake = String.format("%.1f", jsonObject.getDouble("intake"));
                         String burn = String.format("%.1f", jsonObject.getDouble("burn"));
                         String dayCalorie= String.format("%.1f", jsonObject.getDouble("dayCalorie"));
                         String BMR= String.format("%.1f", jsonObject.getDouble("BMR"));

                         double value = Double.parseDouble(dayCalorie);
                         if(value > 0) receiptText.setTextColor(Color.parseColor("#0c2461"));
                         else if(value < 0) receiptText.setTextColor(Color.parseColor("#b71540"));
                         else receiptText.setTextColor(Color.parseColor("#2f3640"));

                         exerciseText.setText(burn + " Kcal");
                         dietText.setText(intake + " Kcal");
                         receiptText.setText(dayCalorie + " Kcal");
                         BMRText.setText(BMR + " Kcal");
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
        getTodaylogRequest gettodaylogRequest = new getTodaylogRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(gettodaylogRequest);


        return v;
    }
}