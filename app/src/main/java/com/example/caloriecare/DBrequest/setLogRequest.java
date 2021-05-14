package com.example.caloriecare.DBrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class setLogRequest extends StringRequest {

    final static private String URL = "http://118.67.135.180/CalorieCare/setLog.php";
    private Map<String, String> map;

    public setLogRequest(String userID, boolean type, String code, double volume, double calorie, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("type", Boolean.toString(type));
        map.put("code", code);
        map.put("volume", Double.toString(volume));
        map.put("calorie", Double.toString(calorie));

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}