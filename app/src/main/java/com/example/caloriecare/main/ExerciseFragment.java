package com.example.caloriecare.main;

import android.content.Intent;
import android.graphics.Color;
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
 * Use the {@link ExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseFragment extends DialogFragment {

    private ExerciseFragment.OutputListener listener;
    public interface OutputListener { void onSaveComplete(double val); }

    String userID;
    double weight;
    ExerciseCategory category;

    ArrayAdapter<String> allCategoryAdapter, exerciseAdapter;
    Spinner allCategory, exerciseCategory;
    String selectedCategory;

    List<ExerciseData> nowExerciseList;
    ExerciseData selectedExercise;

    EditText times;
    TextView calorie, perCalorie, unit;

    double input = 0;
    double result = 0;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public static ExerciseFragment newInstance(String userID, double weight, ExerciseFragment.OutputListener listener) {
        ExerciseFragment fragment = new ExerciseFragment();

        fragment.userID = userID;
        fragment.weight = weight;
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
        View v = inflater.inflate(R.layout.fragment_exercise, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Inflate the layout for this fragment
        allCategory = (Spinner) v.findViewById(R.id.all_category_exercise);
        exerciseCategory = (Spinner) v.findViewById(R.id.exercise_category_exercise);
        times = (EditText)v.findViewById(R.id.time);
        calorie = (TextView)v.findViewById(R.id.calorie_burn);
        perCalorie = (TextView)v.findViewById(R.id.perCalorie);
        category = ((MainActivity)getActivity()).getExerciseCategory();

        initAllCategory(true);

        allCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = allCategory.getSelectedItem().toString();
                initExerciseCategory();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        exerciseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExercise = nowExerciseList.get(position);
                perCalorie.setText(String.format("%.2f",selectedExercise.getCalorie()*3*weight)+"kcal / 30분");
                clear();
                times.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.length()==0) input = 0;
                        else input = Double.parseDouble(s.toString());
                        result = selectedExercise.getCalorie() * input * weight/10;
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

        Button btnBack = (Button)v.findViewById(R.id.exercise_back);
        Button btnEnter = (Button)v.findViewById(R.id.exercise_enter);
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
                    Toast.makeText(getActivity(), "시간을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
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
                                    Toast.makeText(getActivity(),jsonObject.getString("error"),Toast.LENGTH_LONG).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };
                    setLogRequest setlogRequest = new setLogRequest(userID, "EXERCISE", selectedExercise.getCode(),input, result, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(setlogRequest);
                }


            }
        });

        return v;
    }

    public void clear(){
        calorie.setText("        Kcal");
        times.setText("");
    }
    private void initExerciseCategory(){
        nowExerciseList = category.getExerciseDataList(selectedCategory);
        exerciseAdapter = new SpinnerAdapter(true, getActivity(), android.R.layout.simple_spinner_dropdown_item, category.getExerciseNameList(selectedCategory));

        exerciseCategory.setAdapter(exerciseAdapter);
        clear();
    }
    private void initAllCategory(boolean isFirst){
        allCategoryAdapter = new SpinnerAdapter(false, getActivity(), android.R.layout.simple_spinner_dropdown_item, category.getCategory());
        allCategory.setAdapter(allCategoryAdapter);
        selectedCategory = allCategory.getSelectedItem().toString(); //에러
        initExerciseCategory();
    }
}