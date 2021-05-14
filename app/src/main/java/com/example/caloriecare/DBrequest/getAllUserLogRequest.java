package com.example.caloriecare.DBrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getAllUserLogRequest extends StringRequest {

    final static private String URL = "http://118.67.135.180/CalorieCare/getAllUserLogs.php";
    private Map<String, String> map;

    public getAllUserLogRequest(String type, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("type", type);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}