package com.example.caloriecare.DBrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class updateProfileRequest extends StringRequest {

    final static private String URL = "http://118.67.135.180/CalorieCare/updateProfile.php";
    private Map<String, String> map;

    public updateProfileRequest(String userID, String title, String log, String image, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("title",title);
        map.put("log", log);
        map.put("image", image);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}