package com.example.caloriecare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SubActivity extends AppCompatActivity {

    private String strNick, strProfileImg, strEmail;
    DatePicker mDate;
    TextView mTxtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        strEmail = intent.getStringExtra("email");
        strProfileImg = intent.getStringExtra("profileImg");

        TextView tv_nick = findViewById(R.id.tv_nickName);
        TextView tv_email = findViewById(R.id.tv_email);

        ImageView tv_profile = findViewById(R.id.tv_profile);

        tv_nick.setText(strNick); //닉네임
        tv_email.setText(strEmail); // 이메일

        Glide.with(this).load(strProfileImg).into(tv_profile);//이미지

        mDate = (DatePicker)findViewById(R.id.datepicker);
        mTxtDate = (TextView)findViewById(R.id.txtdate);
        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTxtDate.setText(String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth));
            }
        });

        findViewById(R.id.btnnow).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                String result = String.format("%d년 %d월 %d일", mDate.getYear(), mDate.getMonth()+1, mDate.getDayOfMonth());
                Toast.makeText(SubActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }); //  생년월일

        EditText num1 = (EditText)findViewById(R.id.height);
        EditText num2 = (EditText)findViewById(R.id.weight);
        EditText result = (EditText)findViewById(R.id.bmi);
        Button bmi_cal = (Button)findViewById(R.id.bmi_cal);

        bmi_cal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                float n1, n2, bmi;
                n1 = Float.parseFloat(num1.getText().toString()); // 키
                n2 = Float.parseFloat(num2.getText().toString()); // 몸무게
                bmi = n2/(n1/100*n1/100);

                result.setText("BMI : " + bmi);
            }
        }); // bmi 계산


        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback()
                {
                    @Override
                    public void onCompleteLogout()
                    {
                        // 로그아웃 성공시 수행하는 지점
                        finish(); // 현재 액티비티 종료
                    }
                });
            }
        });

        findViewById(R.id.btnSignout).setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               new AlertDialog.Builder(SubActivity.this)
                       .setMessage("탈퇴하시겠습니까?")
                       .setPositiveButton("네", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                   @Override
                                   public void onFailure(ErrorResult errorResult) {
                                       int result = errorResult.getErrorCode();

                                       if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                           Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                       } else {
                                           Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                       }
                                   }

                                   @Override
                                   public void onSessionClosed(ErrorResult errorResult) {
                                       Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(SubActivity.this, LoginActivity.class);
                                       startActivity(intent);
                                       finish();
                                   }

                                   @Override
                                   public void onNotSignedUp() {
                                       Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(SubActivity.this, LoginActivity.class);
                                       startActivity(intent);
                                       finish();
                                   }

                                   @Override
                                   public void onSuccess(Long result) {
                                       Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                       Intent intent = new Intent(SubActivity.this, LoginActivity.class);
                                       startActivity(intent);
                                       finish();
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
        }); //  회원 탈퇴 기능
    }
}
