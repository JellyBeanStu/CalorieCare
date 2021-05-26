package com.example.caloriecare.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.getAllUserLogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.ranking.SpinnerAdapter;
import com.example.caloriecare.ranking.RankListAdapter;
import com.example.caloriecare.ranking.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class RankingFragment extends Fragment {

    private String myID;
    private int rank;
    private RankListAdapter mAdapter;
    private RecyclerView recyclerView;
    public TextView noRankView;
    RecyclerView.LayoutManager mLayout;

    HashMap<String, User> users = new HashMap<>();
    List<User> AllUserRank = new ArrayList<>();
    List<User> ThisConditionRank = new ArrayList<>();
    List<User> ViewUserRank = new ArrayList<>();



    private String rankChk = "week"; // week = false || month = true
    private int genderChk = 0; // 전체 = 0 || 남성 = 1 || 여성 = 2
    private int ageChk = 0; // 0 = 전체 || 1 = 10대 이하 || 2 = 20대 || 3 = 30대 || 4 = 40대 || 5 = 50대 || 6 = 60대 이상

    private RadioGroup rgRank;
    private ToggleButton toggleButton;
    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<String> ageAdapter;

    private LinearLayout myRankLayout;
    private TextView myName;
    private TextView myCalorie;
    private TextView myRank;
    private TextView myRatio;

    private Spinner spGender;
    private Spinner spAge;
    List<String> itemGender = new ArrayList(Arrays.asList(new String[]{"전체", "남성", "여성"}));
    List<String> itemAge = new ArrayList(Arrays.asList(new String[]{"전체", "10대 이하", "20대", "30대", "40대", "50대", "60대 이상"}));

    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myID = ((MainActivity)getActivity()).getUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);

        recyclerView = v.findViewById(R.id.recycle_view);
        noRankView = v.findViewById(R.id.empty);
        rgRank = v.findViewById(R.id.rgRank);
        toggleButton = v.findViewById(R.id.toggle_btn);
        spGender = v.findViewById(R.id.sp_gender);
        spAge = v.findViewById(R.id.sp_age);

        myRankLayout = v.findViewById(R.id.myRank);
        myRank = v.findViewById(R.id.user_rank);
        myName = v.findViewById(R.id.user_name);
        myCalorie = v.findViewById(R.id.user_calorie);
        myRatio = v.findViewById(R.id.user_ratio);

        genderAdapter = new SpinnerAdapter(v.getContext(),android.R.layout.simple_spinner_dropdown_item, itemGender);
        ageAdapter = new SpinnerAdapter(v.getContext(),android.R.layout.simple_spinner_dropdown_item, itemAge);
        spGender.setAdapter(genderAdapter);
        spAge.setAdapter(ageAdapter);

        getData("week");

        return v;
    }

    private void getData(String type){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @NonNull
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String userID, userName, userEmail, userBirth, userProfile;
                    boolean userGender;
                    double burn, dayCalorie;
                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        JSONArray jsonArray_log = jsonObject.getJSONArray("logs");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            //log 는 jsonArray 형태로 가져와짐
                            JSONObject temp = jsonArray.getJSONObject(i);

                            userID = temp.getString("userID");           // 유저 아이디
                            userName = temp.getString("userName");       // 유저 이름
                            userEmail = temp.getString("userEmail");     // 유저 이메일
                            userBirth = temp.getString("userBirth");     // 유저 생일
                            userGender = temp.getBoolean("userGender"); // 유저 성별
                            userProfile = temp.getString("userProfile"); // 유저 프로필 사진

                            users.put(userID, new User(userID, userName, userEmail, userBirth, userGender, userProfile));
                        }
                        for (int i = 0; i < jsonArray_log.length(); i++) {
                            //log 는 jsonArray 형태로 가져와짐
                            JSONObject temp_log = jsonArray_log.getJSONObject(i);

                            userID = temp_log.getString("userID");
                            burn = temp_log.getDouble("burn");               // 당일 소모 칼로리
                            dayCalorie = temp_log.getDouble("dayCalorie");   // 당일 총 사용 칼로리

                            users.get(userID).pushLog(burn, dayCalorie);
                        }
                        sortingRank();
                        getRankView();

                        rgRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if(checkedId == R.id.btn_week){
                                    rankChk = "week";
                                } else{
                                    rankChk = "month";
                                }
                                getData(rankChk);
                            }
                        });
                        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override   // position 으로 몇번째 것이 선택됬는지 값을 넘겨준다
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                genderChk = position;
                                getRankView();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override   // position 으로 몇번째 것이 선택됬는지 값을 넘겨준다
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ageChk = position;
                                getRankView();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {}
                        });
                        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                sortingRank();
                                getRankView();
                            }
                        });

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
        getAllUserLogRequest allUserLogRequestRequest = new getAllUserLogRequest(type, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(allUserLogRequestRequest);
    }

    private void setListView() {
        mLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);
    }
    public void checkForEmptyUsers() {
        if(ViewUserRank.size()>0) {noRankView.setVisibility(View.GONE);}
        else{noRankView.setVisibility(View.VISIBLE);}
    }
    public void onAttach(Context context) {super.onAttach(context);}

    public void sortingRank(){
        List<User> rank = new ArrayList<>();
        for (String ID : users.keySet()) {
            rank.add(users.get(ID));
        }
        Collections.sort(rank, new Comparator<User>() {
            public int compare(User obj1, User obj2)
            {
                if(toggleButton.isChecked())
                    return Double.compare(obj1.getAll(),obj2.getAll());
                else return Double.compare(obj2.getBurn(),obj1.getBurn());
            }
        });
        AllUserRank = rank;
    }
    public void CheckCondition(){
        List<User> temp1 = new ArrayList<>();
        int gender, age;

        if(genderChk == 0){
            temp1 = AllUserRank;
        }else{
            for(int i=0;i<AllUserRank.size();i++){
                gender = AllUserRank.get(i).getGender()?1:0;
                if(genderChk == gender+1){
                    temp1.add(AllUserRank.get(i));
                }
            }
        }

        List<User> temp2 = new ArrayList<>();
        if(ageChk == 0){
            temp2 = temp1;
        }else{
            for(int i=0;i<temp1.size();i++){
                if(temp1.get(i).getID().equals(myID))
                    rank = i;

                age = temp1.get(i).getAge()/10;
                if(age==0) age=1;
                if(age == ageChk){
                    temp2.add(temp1.get(i));
                }
            }
        }
        ThisConditionRank = temp2;
    }
    public void setMyRank(){
        myRank.setText("");
        myName.setText("");
        myCalorie.setText("");
        myRatio.setText("");

        int myAge = users.get(myID).getAge()/10;
        if(myAge==0) myAge=1;
        int myGender = users.get(myID).getGender()?1:0 + 1;

        boolean gender=false, age=false;

        if(genderChk==0 || myGender==genderChk)
            gender = true;
        if(ageChk==0 || myAge==ageChk)
            age = true;

        if(gender && age){
            if(toggleButton.isChecked() && users.get(myID).getAll() == 0 ||
                    !toggleButton.isChecked() && users.get(myID).getBurn() == 0){
                myCalorie.setText("오늘의 칼로리를 입력해주세요");
            }else{
                String rank, result;
                double ratio;
                for (int i=0; i<ThisConditionRank.size(); i++){
                    if (ThisConditionRank.get(i).getID().equals(myID)){
                        myRankLayout.setVisibility(View.VISIBLE);
                        myName.setText(users.get(myID).getName());
                        myName.setSingleLine();

                        rank = Integer.toString( i+ 1);
                        myRank.setText(rank);

                        if(toggleButton.isChecked())
                            result = String.format("%.1f",ThisConditionRank.get(i).getAll())+ " Kcal";
                        else result = String.format("%.1f",ThisConditionRank.get(i).getBurn())+ " Kcal";
                        myCalorie.setText(result);
                        ratio = (i+1) / ThisConditionRank.size() * 100;
                        myRatio.setText(String.format("%.1f",ratio) + " %");
                        return;
                    }
                }
            }
        }else{
            myRankLayout.setVisibility(View.GONE);
        }
    }
    public void checkZero(){
        List<User> rank = new ArrayList<>();
        for(int i=0;i<ThisConditionRank.size();i++){
            if(toggleButton.isChecked()){
                if(ThisConditionRank.get(i).getAll() != 0)
                    rank.add(ThisConditionRank.get(i));
            }else{
                if(ThisConditionRank.get(i).getBurn() != 0)
                    rank.add(ThisConditionRank.get(i));
            }
        }
        ViewUserRank = rank;
    }
    public void getRankView(){
        CheckCondition();
        checkZero();
        setMyRank();

        mAdapter = new RankListAdapter(getContext(),toggleButton.isChecked(),ViewUserRank);
        setListView();
        checkForEmptyUsers();
    }
}