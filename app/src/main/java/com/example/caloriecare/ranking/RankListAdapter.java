package com.example.caloriecare.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriecare.R;

import java.util.HashMap;
import java.util.List;

public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {
    private Context mContext;
    private HashMap<String, User> users = new HashMap<>();
    private List<HashMap<String, User>> mMapList;



    public RankListAdapter(Context Context, List<HashMap<String, User>> MapList)
    {
        this.mContext = Context;
        this.mMapList = MapList;
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
        mMapList.get(position);
        /*
        Uri uri = Uri.parse(mMapList.getProfile());
        holder.edt_rank.setText(mMapList.getID());
        holder.edt_image.setImageURI(uri);
        holder.edt_name.setText(mMapList.getName());
         */
    }



    @Override
    public int getItemCount() { return users.size(); }
}
