package com.example.caloriecare.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.example.caloriecare.DBrequest.updateProfileRequest;
import com.example.caloriecare.LoginActivity;
import com.example.caloriecare.MainActivity;
import com.example.caloriecare.R;
import com.example.caloriecare.User;
import com.example.caloriecare.profile.DatePickerFragment;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private User myData;
    private double height, weight;
    private String birth;

    ImageView img_profile;
    TextView text_email, text_birth;
    EditText text_name, text_height, text_weight;
    ToggleButton tbtn_gender;

    Animation startAnimation;
    Button btn_save;
    boolean blink;
    boolean isChangeGender, isChangeBirth, isChangeName, isChangeHeight, isChangeWeight;

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myData = new User(((MainActivity)getActivity()).getMyData());
        blink = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        isChangeGender = isChangeBirth = isChangeName = isChangeHeight = isChangeWeight = false;

        img_profile = v.findViewById(R.id.profile_user_profile);
        text_name = v.findViewById(R.id.profile_text_name);
        text_email = v.findViewById(R.id.profile_text_email);
        text_birth = v.findViewById(R.id.profile_text_birth);
        text_height = v.findViewById(R.id.profile_text_height);
        text_weight = v.findViewById(R.id.profile_text_weight);
        tbtn_gender = v.findViewById(R.id.profile_btn_gender);

        if(myData.getProfile() != "null")
            Glide.with(getActivity()).load(myData.getProfile()).into(img_profile);//?????????

        text_email.setText(myData.getEmail());
        text_name.setText(myData.getName());
        text_birth.setText(myData.getBirth());
        tbtn_gender.setChecked(myData.getGender());
        text_height.setText(String.format("%.1f", myData.getHeight()));
        text_weight.setText(String.format("%.1f", myData.getWeight()));
        LinearLayout layout_birth = v.findViewById(R.id.layout_birth);
        btn_save = v.findViewById(R.id.btn_save);

        height = myData.getHeight();
        weight = myData.getWeight();
        birth = myData.getBirth();


        text_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                isChangeName = !edit.toString().equals(myData.getName());
                setBlink(isChange());
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
        layout_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DatePickerFragment dialog = DatePickerFragment.newInstance(birth, new DatePickerFragment.OutputListener() {
                    @Override
                    public void onSaveComplete(String date) {
                        if(birth.equals(date)) return;

                        birth = date;
                        text_birth.setText(birth);

                        isChangeBirth = !birth.equals(myData.getBirth());
                        setBlink(isChange());
                    }
                });
                dialog.show(getParentFragmentManager(), "addDatePickerDialog");
            }
        });
        text_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String str;
                if(edit.length() == 0) str="0";
                else str = edit.toString();

                height = Double.parseDouble(str);
                height = Math.round(height*10)/10.0;
                int tempA = (int) Math.round(height*10);
                int tempB = (int) Math.round(myData.getHeight()*10);

                isChangeHeight = (tempA != tempB);
                setBlink(isChange());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        text_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String str;
                if(edit.length() == 0) str="0";
                else str = edit.toString();

                weight = Double.parseDouble(str);
                weight = Math.round(weight*10)/10.0;

                int tempA = (int) Math.round(weight*10);
                int tempB = (int) Math.round(myData.getWeight()*10);

                isChangeWeight = (tempA != tempB);
                setBlink(isChange());
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
        tbtn_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                isChangeGender = (myData.getGender() != tbtn_gender.isChecked());
                setBlink(isChange());
            }
        });

        startAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.blink_animation);
        btn_save.clearAnimation();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBlink(false);

                myData.setName(text_name.getText().toString());
                myData.setGender(tbtn_gender.isChecked());
                myData.setBirth(text_birth.getText().toString());
                myData.setHeight(height);
                myData.setWeight(weight);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success1 = jsonObject.getBoolean("success1");
                            boolean success2 = jsonObject.getBoolean("success2");
                            if (success1 && success2) {
                                ((MainActivity)getActivity()).setMyData(myData);
                                Toast.makeText(getActivity(),"?????? ??????!",Toast.LENGTH_LONG).show();
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
                updateProfileRequest updateprofileRequest = new updateProfileRequest(myData.getID(),myData.getName(),myData.getBirth(),myData.getGender(),myData.getHeight(),myData.getWeight(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(updateprofileRequest);
            }
        });
        v.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getActivity(),"????????????!",Toast.LENGTH_SHORT).show();
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
                        .setMessage("?????? ?????????????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getActivity(), "???????????? ????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "??????????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getActivity(), "????????? ????????? ???????????????. ?????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getActivity(), "???????????? ?????? ???????????????. ?????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(getActivity(), "??????????????? ?????????????????????", Toast.LENGTH_SHORT).show();
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
                                        SignoutRequest signoutRequest = new SignoutRequest(myData.getID(), responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                                        queue.add(signoutRequest);
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        return v;
    }
    public void setBlink(boolean flag){
        btn_save.setClickable(flag);
        if(flag){
            btn_save.startAnimation(startAnimation);
        }else{
            btn_save.clearAnimation();
        }
    }

    public boolean isChange(){
        return (isChangeGender|| isChangeBirth|| isChangeName|| isChangeHeight|| isChangeWeight);
    }
}