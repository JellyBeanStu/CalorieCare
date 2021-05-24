package com.example.caloriecare.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.setLogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DietFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DietFragment extends DialogFragment {

    private OutputListener listener;
    public interface OutputListener { void onSaveComplete(double val); }

    String userID;
    DietCategory category;

    ArrayAdapter<String> largeAdapter, mediumAdapter, smallAdapter, foodAdapter;
    String selectedLarge, selectedMedium, selectedSmall;
    List<DietData> nowFoodList;
    DietData selectedFood;

    Spinner largeCategory, mediumCategory, smallCategory, foodCategory;
    TextView calorie, servingSize, unit;
    EditText grams;

    double input = 0;
    double result=0;

    public DietFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DietFragment newInstance(String userID, OutputListener listener) {
        DietFragment fragment = new DietFragment();
        fragment.userID = userID;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diet, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        category = ((MainActivity)getActivity()).getDietCategory();

        largeCategory = (Spinner) v.findViewById(R.id.large_category_diet);
        mediumCategory = (Spinner) v.findViewById(R.id.medium_category_diet);
        smallCategory = (Spinner) v.findViewById(R.id.small_category_diet);
        foodCategory = (Spinner) v.findViewById(R.id.food_category_diet);
        calorie = (TextView)v.findViewById(R.id.calorie_intake);
        grams = (EditText)v.findViewById(R.id.grams);
        unit = (TextView)v.findViewById(R.id.unit);
        servingSize = (TextView)v.findViewById(R.id.food_servingSize);
        initLargeCategory(true);

        largeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLarge = largeCategory.getSelectedItem().toString();
                initMediumCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mediumCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMedium = mediumCategory.getSelectedItem().toString();
                initSmallCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        smallCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSmall = smallCategory.getSelectedItem().toString();
                initFoodCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        foodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFood = nowFoodList.get(position);
                servingSize.setText(selectedFood.getCalorie()+"kcal / " + selectedFood.getServingSize() +selectedFood.getUnit());
                unit.setText(selectedFood.getUnit());
                clear();
                grams.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length()==0) input = 0;
                        else input = Double.parseDouble(s.toString());
                        result = (selectedFood.getCalorie()/selectedFood.getServingSize()) * input;
                        calorie.setText(String.format("%.1f",result) + " Kcal");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    } });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnBack =(Button)v.findViewById(R.id.diet_back);
        Button btnEnter =(Button)v.findViewById(R.id.diet_enter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result == 0){
                    Toast.makeText(getActivity(), "섭취량를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getActivity(), "저장 완료!", Toast.LENGTH_SHORT).show();
                                    listener.onSaveComplete(result);
                                    dismiss();

                                } else {
                                    Toast.makeText(getActivity(), jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };
                    setLogRequest setlogRequest = new setLogRequest(userID, "DIET", selectedFood.getCode(), input, result, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(setlogRequest);
                }
            }
        });

        return v;
    }

    public void clear(){
        calorie.setText("        Kcal");
        grams.setText("");
    }
    private void initFoodCategory(){
        nowFoodList = category.getDietDataList(selectedSmall);
        foodAdapter = new SpinnerAdapter(true, getActivity(), android.R.layout.simple_spinner_dropdown_item, category.getDietNameList(selectedSmall));
        foodCategory.setAdapter(foodAdapter);
        clear();
    }
    private void initSmallCategory(){
        smallAdapter = new SpinnerAdapter(false, getActivity(), android.R.layout.simple_spinner_dropdown_item, category.getSmallCategory(selectedMedium));
        smallCategory.setAdapter(smallAdapter);
        selectedSmall = smallCategory.getSelectedItem().toString();
        initFoodCategory();
    }
    private void initMediumCategory(){
        mediumAdapter = new SpinnerAdapter(false, getActivity(), android.R.layout.simple_spinner_dropdown_item, category.getMediumCategory(selectedLarge));
        mediumCategory.setAdapter(mediumAdapter);
        selectedMedium = mediumCategory.getSelectedItem().toString();
        initSmallCategory();
    }
    private void initLargeCategory(boolean isFirst){
        largeAdapter = new SpinnerAdapter(false, getActivity(), android.R.layout.simple_spinner_dropdown_item, category.getLargeCategory());
        largeCategory.setAdapter(largeAdapter);
        selectedLarge = largeCategory.getSelectedItem().toString();
        initMediumCategory();
    }
}