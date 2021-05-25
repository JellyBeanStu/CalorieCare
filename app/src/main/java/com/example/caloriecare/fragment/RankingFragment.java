package com.example.caloriecare.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.getAllUserLogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.ranking.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String myID;


    // Rank
    private RankListAdapter mAdapter;
    private RecyclerView recyclerView;
    public TextView noRankView;
    RecyclerView.LayoutManager mLayout;
    HashMap<Integer, User> users = new HashMap<>();
    HashMap<Integer, UserRank> userRank = new HashMap<>();
    HashMap<Integer, RankVal> insertionRank;
    HashMap<Integer, RankVal> insertionAllRank;
    HashMap<Integer, RankVal> storeRank = new HashMap<>();

    private boolean rankChk = false; // week = false || month = true
    private boolean toggleChk = false; // 운동 = false || 전체 = true
    private int genderChk = 0; // 전체 = 0 || 남성 = 1 || 여성 = 2
    private int ageChk = 0; // 0 = 전체 || 1 = 10대 이하 || 2 = 20대 || 3 = 30대 || 4 = 40대 || 5 = 50대 || 6 = 60대 이상
    Bitmap bitmap;

    private RadioGroup rgRank;
    private ToggleButton toggleButton;
    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<String> ageAdapter;

    private TextView myName;
    private TextView myCalorie;
    private TextView myRank;
    private TextView myRatio;
    private ImageView myProfile;

    private Spinner spGender;
    private Spinner spAge;
    String[] itemGender = {"전체", "남성", "여성"};
    String[] itemAge = {"전체", "10대 이하", "20대", "30대", "40대", "50대", "60대 이상"};

    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myID = ((MainActivity)getActivity()).getUserID();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        myRank = v.findViewById(R.id.user_rank);
        myName = v.findViewById(R.id.user_name);
        myProfile = v.findViewById(R.id.user_image);
        myCalorie = v.findViewById(R.id.user_calorie);
        myRatio = v.findViewById(R.id.user_ratio);

        genderAdapter = new ArrayAdapter(v.getContext(),android.R.layout.simple_spinner_dropdown_item,itemGender);
        ageAdapter = new ArrayAdapter<>(v.getContext(),android.R.layout.simple_spinner_dropdown_item,itemAge);
        spGender.setAdapter(genderAdapter);
        spAge.setAdapter(ageAdapter);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @NonNull
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        JSONArray jsonArray_log = jsonObject.getJSONArray("logs");

                        for(int i = 0;i<jsonArray.length();i++){
                            //log 는 jsonArray 형태로 가져와짐
                            JSONObject temp = jsonArray.getJSONObject(i);

                            int userKey = i;  // 유저 키 <- 이걸로 다른 변수와 연결해서 가져오기
                            String userID = temp.getString("userID");           // 유저 아이디
                            String userName = temp.getString("userName");       // 유저 이름
                            String userEmail = temp.getString("userEmail");     // 유저 이메일
                            String userBirth = temp.getString("userBirth");     // 유저 생일
                            boolean userGender = temp.getBoolean("userGender"); // 유저 성별
                            String userProfile = temp.getString("userProfile"); // 유저 프로필 사진

                            users.put(userKey, new User(userID,userName,userEmail,userBirth,userGender,userProfile));

                            for(int a=0;a<jsonArray_log.length();a++){
                                //log 는 jsonArray 형태로 가져와짐
                                JSONObject temp_log = jsonArray_log.getJSONObject(a);

                                String usID = temp_log.getString("userID");
                                String logDate = temp_log.getString("logDate");         // 해당 데이터의 날짜
                                double intake = temp_log.getDouble("intake");           // 당일 섭취 칼로리
                                double burn = temp_log.getDouble("burn");               // 당일 소모 칼로리
                                double dayCalorie = temp_log.getDouble("dayCalorie");   // 당일 총 사용 칼로리

                                if (users.get(userKey).getID().equals(usID)){
                                    users.get(userKey).pushLog(new DayLog(logDate,intake,burn,dayCalorie));
                                }
                            }

                        }

                        for (int i=0; i<users.size();i++){
                            double weekBurnSum = 0;
                            double monthBurnSum = 0;
                            double weekDaySum = 0;
                            double monthDaySum = 0;

                            int rankKey = i;

                            int koreanAge = 1;
                            int worldAge = 0; // 만 나이

                            SimpleDateFormat ageFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date nowDate = new Date();
                            String birthString = users.get(i).getBirth();
                            String nowString = ageFormat.format(nowDate);

                            int nowYear = Integer.parseInt(nowString.substring(0, 4));
                            int nowMonth = Integer.parseInt(nowString.substring(5, 7));
                            int nowDay = Integer.parseInt(nowString.substring(8, 10));

                            int birthYear = Integer.parseInt(birthString.substring(0, 4));
                            int birthMonth = Integer.parseInt(birthString.substring(5, 7));
                            int birthDay = Integer.parseInt(birthString.substring(8, 10));

                            worldAge = nowYear - birthYear;
                            koreanAge = worldAge + 1;
                            if (nowMonth < birthMonth || (nowMonth == birthMonth && nowDay < birthDay)) { // 생년월일 "월"이 지났는지 체크 // 생년월일 "일"이 지났는지 체크 생일 안지났으면 (만나이 - 1)
                                worldAge--;
                                koreanAge++;
                            }

                            for (int j=0; j<users.get(i).getDaylogs().size();j++){  // 데이로그에 들어있는 데이터들을 날짜별로 더하기 위한 반복문
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                Date logDate = null;
                                Date weekDate = new Date();
                                Date monthDate = new Date();

                                String logString = users.get(i).getDaylogs().get(j).getDate();
                                String weekString = dateFormat.format(weekDate);
                                String monthString = dateFormat.format(monthDate);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(weekDate);
                                cal.add(Calendar.DATE, -7);
                                weekString = dateFormat.format(cal.getTime());
                                cal = Calendar.getInstance();
                                cal.setTime(monthDate);
                                cal.add(Calendar.MONTH, -1);
                                monthString = dateFormat.format(cal.getTime());


                                try {
                                    logDate = dateFormat.parse(logString);
                                    weekDate = dateFormat.parse(weekString);
                                    monthDate = dateFormat.parse(monthString);
                                } catch (Exception e) {}

                                if (logDate.after(weekDate)){   // 오늘로부터 일주일 전의 날짜보다 이후에 작성된것만 더함
                                    if (users.get(i).getDaylogs().size() != 0){
                                        weekBurnSum += users.get(i).getDaylogs().get(j).getBurn();
                                        weekDaySum += users.get(i).getDaylogs().get(j).getDayCalorie();
                                    }else{
                                        weekBurnSum = 0.0;
                                        weekDaySum = 0.0;
                                    }
                                }
                                if (logDate.after(monthDate)){   // 오늘로부터 한달 전의 날짜보다 이후에 작성된것만 더함
                                    if (users.get(i).getDaylogs().size() != 0){
                                        monthBurnSum += users.get(i).getDaylogs().get(j).getBurn();
                                        monthDaySum += users.get(i).getDaylogs().get(j).getDayCalorie();
                                    }else{
                                        monthBurnSum = 0.0;
                                        monthDaySum = 0.0;
                                    }
                                }
                            }

                            userRank.put(rankKey, new UserRank(users.get(i).getID(),users.get(i).getName(), worldAge,
                                    users.get(i).getGender(),users.get(i).getProfile(), weekBurnSum,weekDaySum,monthBurnSum,monthDaySum, 0.0));
                        }

                        getRankView();

                        rgRank.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if(checkedId == R.id.btn_week){
                                    rankChk = false;
                                } else{
                                    rankChk = true;
                                }
                                getRankView();
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
                                if (isChecked) {
                                    // The toggle is enabled`
                                    toggleChk = true;
                                } else {
                                    // The toggle is disabled
                                    toggleChk = false;
                                }
                                getRankView();
                            }
                        });

                        // hashMap 에 유저의 데이터가 저장된 상황
                        //
                        //
                        // 위 데이터를 각 유저별로 정리하고, 순서를 매기기
                        // 드롭다운 목록등을 사용해 성별별로, 나이대별로 표기 가능하게
                        // 전체 칼로리, 소모한 칼로리만으로 따로 볼 수 있게
                        //
                        // 랭킹 프로필사진 이름  칼로리량        순으로 표기
                        //
                        //
                        //
                        //System.out.println( users.size() + " 유저 | 유저랭크 " + userRank.size());
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
        getAllUserLogRequest allUserLogRequestRequest = new getAllUserLogRequest("month", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(allUserLogRequestRequest);

        return v;
    }

    private void setListView()
    {
        mLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayout);
        recyclerView.setAdapter(mAdapter);
    }

    public void checkForEmptyUsers()
    {
        if(insertionRank.size()>0) {noRankView.setVisibility(View.GONE);}
        else{noRankView.setVisibility(View.VISIBLE);}
    }
    public void onAttach(Context context) {super.onAttach(context);}


    public void sortingRank(){
        UserRank tempRank;
        for (int i=0; i<userRank.size(); i++){
            for (int j=0; j<userRank.size();j++){
                if (!rankChk && !toggleChk){ // rankChk == false && toggleChk == false -> BurnWeek
                    if (userRank.get(i).getBurnWeek()>userRank.get(j).getBurnWeek()){
                        //System.out.println( i+1 + " 이름 : " + userRank.get(i).getName() + " -> " + userRank.get(i).getBurnWeek())
                        tempRank = userRank.get(i);
                        userRank.put(i, userRank.get(j));
                        userRank.put(j, tempRank);
                    }
                    storeRank.put(j,new RankVal(userRank.get(j).getID(),userRank.get(j).getName(),userRank.get(j).getAge(),
                            userRank.get(j).getGender(),userRank.get(j).getProfile(), userRank.get(j).getBurnWeek()));

                }
                else if (!rankChk && toggleChk){ // rankChk == false && toggleChk == true -> DayWeek
                    if (userRank.get(i).getDayWeek()<userRank.get(j).getDayWeek()){
                        tempRank = userRank.get(i);
                        userRank.put(i, userRank.get(j));
                        userRank.put(j, tempRank);
                    }
                    storeRank.put(j,new RankVal(userRank.get(j).getID(),userRank.get(j).getName(),userRank.get(j).getAge(),
                            userRank.get(j).getGender(),userRank.get(j).getProfile(), userRank.get(j).getDayWeek()*-1));

                }else if (rankChk && !toggleChk){ // rankChk == true && toggleChk == false -> BurnMonth
                    if (userRank.get(i).getBurnMonth()>userRank.get(j).getBurnMonth()){
                        tempRank = userRank.get(i);
                        userRank.put(i, userRank.get(j));
                        userRank.put(j, tempRank);
                    }
                    storeRank.put(j,new RankVal(userRank.get(j).getID(),userRank.get(j).getName(),userRank.get(j).getAge(),
                            userRank.get(j).getGender(),userRank.get(j).getProfile(), userRank.get(j).getBurnMonth()));

                }else{ // rankChk == true && toggleChk == true -> DayMonth
                    if (userRank.get(i).getDayMonth()<userRank.get(j).getDayMonth()){
                        tempRank = userRank.get(i);
                        userRank.put(i, userRank.get(j));
                        userRank.put(j, tempRank);
                    }
                    storeRank.put(j,new RankVal(userRank.get(j).getID(),userRank.get(j).getName(),userRank.get(j).getAge(),
                            userRank.get(j).getGender(),userRank.get(j).getProfile(), userRank.get(j).getDayMonth()*-1 ));
                }
            }

        }

    }

    public void InsertionRank(){
        int a = 0;
        int b = 0;
        insertionAllRank = new HashMap<>();
        insertionRank = new HashMap<>();
        for (int i = 0; i< storeRank.size(); i++){
            if (genderChk == 1 && storeRank.get(i).getGender() == false){
                switch (ageChk){
                    case 0:
                        if (insertionRank.size() < 30){
                            insertionRank.put(a,storeRank.get(i));
                            a++;
                        }insertionAllRank.put(b,storeRank.get(i));
                        b++;
                        break;
                    case 1:
                        if (storeRank.get(i).getAge() < 20 && storeRank.get(i).getAge() >= 0){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 2:
                        if (storeRank.get(i).getAge() < 30 && storeRank.get(i).getAge() >= 20){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 3:
                        if (storeRank.get(i).getAge() < 40 && storeRank.get(i).getAge() >= 30){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 4:
                        if (storeRank.get(i).getAge() < 50 && storeRank.get(i).getAge() >= 40){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 5:
                        if (storeRank.get(i).getAge() < 60 && storeRank.get(i).getAge() >= 50){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 6:
                        if (storeRank.get(i).getAge() >= 60){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                }
            }else if (genderChk == 2 && userRank.get(i).getGender() == true){
                switch (ageChk){
                    case 0:
                        if (insertionRank.size() < 30){
                            insertionRank.put(a,storeRank.get(i));
                            a++;
                        }insertionAllRank.put(b,storeRank.get(i));
                        b++;
                        break;
                    case 1:
                        if (storeRank.get(i).getAge() < 20 && storeRank.get(i).getAge() >= 0){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 2:
                        if (storeRank.get(i).getAge() < 30 && storeRank.get(i).getAge() >= 20){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 3:
                        if (storeRank.get(i).getAge() < 40 && storeRank.get(i).getAge() >= 30){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 4:
                        if (storeRank.get(i).getAge() < 50 && storeRank.get(i).getAge() >= 40){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 5:
                        if (storeRank.get(i).getAge() < 60 && storeRank.get(i).getAge() >= 50){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 6:
                        if (storeRank.get(i).getAge() >= 60){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                }
            }else if(genderChk == 0){
                switch (ageChk){
                    case 0:
                        if (insertionRank.size() < 30){
                            insertionRank.put(a,storeRank.get(i));
                            a++;
                        }insertionAllRank.put(b,storeRank.get(i));
                        b++;
                        break;
                    case 1:
                        if (storeRank.get(i).getAge() < 20 && storeRank.get(i).getAge() >= 0){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 2:
                        if (storeRank.get(i).getAge() < 30 && storeRank.get(i).getAge() >= 20){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 3:
                        if (storeRank.get(i).getAge() < 40 && storeRank.get(i).getAge() >= 30){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 4:
                        if (storeRank.get(i).getAge() < 50 && storeRank.get(i).getAge() >= 40){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 5:
                        if (storeRank.get(i).getAge() < 60 && storeRank.get(i).getAge() >= 50){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                    case 6:
                        if (storeRank.get(i).getAge() >= 60){
                            if (insertionRank.size() < 30){
                                insertionRank.put(a,storeRank.get(i));
                                a++;
                            }insertionAllRank.put(b,storeRank.get(i));
                            b++;
                        }
                        break;
                }
            }
        }

        for(Integer i : insertionAllRank.keySet()){
            RankVal value = insertionAllRank.get(i);
            System.out.println(i + " : " + value);
        }
    }

    public void getRankView(){
        sortingRank();
        InsertionRank();
        setMyRank();
        mAdapter = new RankListAdapter(getContext(),insertionRank); //,user_img);
        setListView();
        checkForEmptyUsers();
    }
    public void setMyRank(){
        double ratio=0;
        boolean foundMe = false;
        boolean realFoundMe = false;
        for (int i=0; i<insertionAllRank.size(); i++){
            System.out.println(insertionAllRank.size());
            if (insertionAllRank.get(i).getID().equals(myID)){
                realFoundMe = true;
                foundMe = true;

                String a = Integer.toString( i+ 1);
                myRank.setText(a);
                myName.setText(insertionAllRank.get(i).getName());
                ImageLoadTask task = new ImageLoadTask(insertionAllRank.get(i).getProfile(),myProfile);
                task.execute();
                ratio = (i+1.0) / insertionAllRank.size() * 100;
                myCalorie.setText(String.format("%.1f",insertionAllRank.get(i).getRankCalorie()));
                myRatio.setText(String.format("%.1f",ratio) + "%");
            }
        }
        if (!foundMe){
            for (int i=0; i<storeRank.size(); i++){
                if (storeRank.get(i).getID().equals(myID)){
                    realFoundMe = true;
                    myRank.setText(" - ");
                    myName.setText(storeRank.get(i).getName());
                    ImageLoadTask task = new ImageLoadTask(storeRank.get(i).getProfile(),myProfile);
                    task.execute();
                    myCalorie.setText(" - ");
                    myRatio.setText(" - ");
                }
            }
        }
        if (!realFoundMe){
            myRank.setText(" - ");
            myName.setText("비회원");
            myProfile.setImageResource(R.drawable.ic_profile_user);
            myCalorie.setText(" - ");
            myRatio.setText(" - ");
        }

    }
}
