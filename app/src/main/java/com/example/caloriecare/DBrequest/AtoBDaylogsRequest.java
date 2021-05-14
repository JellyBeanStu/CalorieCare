package com.example.caloriecare.DBrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AtoBDaylogsRequest extends StringRequest {

    final static private String URL = "http://118.67.135.180/CalorieCare/getAtoBDaylogs.php";
    private Map<String, String> map;

    public AtoBDaylogsRequest(String userID, String begin, String end, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("begin", begin);
        map.put("end", end);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}