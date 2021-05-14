package com.example.caloriecare.DBrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://118.67.135.180/CalorieCare/kakaoLogin.php";
    private Map<String, String> map;

    public LoginRequest(String name, String email,String profile, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("profile", profile);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}