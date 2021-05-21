package com.example.caloriecare.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.caloriecare.DBrequest.SignoutRequest;
import com.example.caloriecare.DBrequest.getUserDataRequest;
import com.example.caloriecare.DBrequest.updateProfileRequest;
import com.example.caloriecare.LoginActivity;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.profile.DatePickerActivity;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userID, userName, userProfileImg, userEmail, userBirth;
    private boolean userGender;
    private int age;
    private double weight, height, BMR;

    ImageView img_profile;
    TextView text_name, text_email, text_birth, text_bmr;
    EditText text_height, text_weight;
    ToggleButton tbtn_gender;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView img_profile = v.findViewById(R.id.profile_user_profile);
        text_name = v.findViewById(R.id.profile_text_name);
        text_email = v.findViewById(R.id.profile_text_email);
        text_birth = v.findViewById(R.id.profile_text_birth);
        text_height = v.findViewById(R.id.profile_text_height);
        text_weight = v.findViewById(R.id.profile_text_weight);
        text_bmr = v.findViewById(R.id.profile_text_bmr);
        tbtn_gender = v.findViewById(R.id.profile_btn_gender);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        userName = jsonObject.getString("userName");
                        userEmail = jsonObject.getString("userEmail");
                        userProfileImg = jsonObject.getString("userProfile");
                        userGender = jsonObject.getInt("userGender")!=0;
                        userBirth = jsonObject.getString("userBirth");

                        height = jsonObject.getDouble("height");
                        weight = jsonObject.getDouble("weight");
                        BMR = jsonObject.getDouble("BMR");

                        setAge();

                        if(userProfileImg != "null")
                            Glide.with(getActivity()).load(userProfileImg).into(img_profile);//이미지

                        text_name.setText(userName); // 이름
                        text_email.setText(userEmail); // 이메일
                        text_birth.setText(userBirth);
                        tbtn_gender.setChecked(userGender);

                        if(height !=0)
                            text_height.setText(Double.toString(height));
                        if(weight !=0)
                            text_weight.setText(Double.toString(weight));
                        text_bmr.setText(String.format("%.2f",BMR) + " Kcal");

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
        getUserDataRequest userDataRequest = new getUserDataRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(userDataRequest);

        LinearLayout layout_birth = v.findViewById(R.id.layout_birth);
        layout_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), DatePickerActivity.class);
                intent.putExtra("userBirth", userBirth);
                startActivityForResult(intent, 1);
            }
        });
        text_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                height = Double.parseDouble(s);
                calculateBMR();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        text_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String s = edit.toString();
                weight = Double.parseDouble(s);
                calculateBMR();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        v.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = text_name.getText().toString();
                userGender = tbtn_gender.isChecked();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success1 = jsonObject.getBoolean("success1");
                            boolean success2 = jsonObject.getBoolean("success2");
                            if (success1 && success2) {
                                Toast.makeText(getActivity(),"저장 완료!",Toast.LENGTH_LONG).show();
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
                updateProfileRequest updateprofileRequest = new updateProfileRequest(userID,userName,userBirth,userGender,height,weight,BMR, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(updateprofileRequest);
            }
        });
        v.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback()
                {
                    @Override
                    public void onCompleteLogout()
                    {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

            }
        });
        v.findViewById(R.id.btn_signout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new AlertDialog.Builder(getActivity())
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getActivity(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getActivity(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getActivity(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    @Override
                                    public void onSuccess(Long result) {
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {
                                                        Toast.makeText(getActivity(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);

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
                                        SignoutRequest signoutRequest = new SignoutRequest(userID, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                                        queue.add(signoutRequest);
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode==1){
            String date = data.getStringExtra("date");
            userBirth = date;
            text_birth.setText(date);
            setAge();
            calculateBMR();
        }
    }
    public void setAge(){
        Calendar calendar = new GregorianCalendar();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int userYear = Integer.parseInt(userBirth.substring(0,4));
        int userMonth = Integer.parseInt(userBirth.substring(5,7));
        int userDay = Integer.parseInt(userBirth.substring(8,10));

        age = mYear-userYear-1;
        if(mMonth > userMonth || (mMonth == userMonth && mDay>userDay)){
            age = age+1;
        }
    }
    public void calculateBMR(){
        BMR = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        text_bmr.setText(String.format("%.2f", BMR) + " Kcal");
    }
}