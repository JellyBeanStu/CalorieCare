package com.example.caloriecare.DBrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class updateProfileRequest extends StringRequest {

    final static private String URL = "http://118.67.135.180/CalorieCare/updateProfile.php";
    private Map<String, String> map;


    public updateProfileRequest(String userID, String userName, String userBirth,  boolean userGender, double height, double weight, double BMR, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userName",userName);
        map.put("userBirth",userBirth);
        map.put("userGender",Boolean.toString(userGender));
        map.put("height",Double.toString(height));
        map.put("weight",Double.toString(weight));
        map.put("BMR",Double.toString(BMR));

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}