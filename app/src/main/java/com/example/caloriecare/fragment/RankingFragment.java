package com.example.caloriecare.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.caloriecare.DBrequest.AtoBDaylogsRequest;
import com.example.caloriecare.DBrequest.getAllUserLogRequest;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.calendar.DayLog;
import com.example.caloriecare.ranking.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private String userID;

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
        userID = ((MainActivity)getActivity()).getUserID();

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

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        HashMap<String, User> users = new HashMap<>();

                        for(int i=0;i<jsonArray.length();i++){
                            //log 는 jsonArray 형태로 가져와짐
                            JSONObject temp = jsonArray.getJSONObject(i);

                            String userID = temp.getString("userID");
                            String userName = temp.getString("userName");
                            String userEmail = temp.getString("userEmail");
                            String userBirth = temp.getString("userBirth");
                            boolean userGender = temp.getBoolean("userGender");
                            String userProfile = temp.getString("userProfile");

                            users.put(userID, new User(userID,userName,userEmail,userBirth,userGender,userProfile));
                        }

                        jsonArray = jsonObject.getJSONArray("logs");
                        for(int i=0;i<jsonArray.length();i++){
                            //log 는 jsonArray 형태로 가져와짐
                            JSONObject temp = jsonArray.getJSONObject(i);

                            String userID = temp.getString("userID");       // 해당 데이터의 날짜
                            String logDate = temp.getString("logDate");
                            double intake = temp.getDouble("intake");       // 당일 섭취 칼로리
                            double burn = temp.getDouble("burn");           // 당일 소모 칼로리
                            double dayCalorie = temp.getDouble("dayCalorie");// 당일 총 사용 칼로리

                            users.get(userID).pushLog(new DayLog(logDate,intake,burn,dayCalorie));
                        }

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
        getAllUserLogRequest allUserLogRequestRequest = new getAllUserLogRequest("week", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(allUserLogRequestRequest);

        return v;
    }
}