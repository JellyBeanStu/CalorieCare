package com.example.caloriecare.ranking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.caloriecare.R;
import com.example.caloriecare.calendar.DayLog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {
    public Context mContext;
    public HashMap<Integer, RankVal> mUsers;
    Bitmap bitmap;


    public RankListAdapter(Context Context, HashMap<Integer, RankVal> userRanks)
    {
        this.mContext = Context;
        this.mUsers = userRanks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView edt_rank;
        public TextView edt_name;
        public TextView edt_calorie;
        public ImageView edt_image;

        public ViewHolder(View view)
        {
            super(view);

            edt_rank = view.findViewById(R.id.user_rank);
            edt_image = view.findViewById(R.id.user_image);
            edt_name = view.findViewById(R.id.user_name);
            edt_calorie = view.findViewById(R.id.user_calorie);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_element,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String a = Integer.toString(position + 1);
        holder.edt_rank.setText(a);

        ImageLoadTask task = new ImageLoadTask(mUsers.get(position).getProfile(),holder.edt_image);
        task.execute();

        holder.edt_name.setText(mUsers.get(position).getName());

        String s = String.format("%.1f",mUsers.get(position).getRankCalorie());
        holder.edt_calorie.setText(s);
    }

    @Override
    public int getItemCount() { return mUsers.size(); }
}
