package com.example.caloriecare.ranking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriecare.R;

import java.util.List;

public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {
    public Context mContext;
    public List<User> mUsers;
    public boolean type = true;
    Bitmap bitmap;


    public RankListAdapter(Context Context, boolean type, List<User> userRanks)
    {
        this.mContext = Context;
        this.mUsers = userRanks;
        this.type = type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView rank;
        public TextView name;
        public TextView calorie;
        public ImageView image;

        public ViewHolder(View view)
        {
            super(view);
            rank = view.findViewById(R.id.user_rank);
            image = view.findViewById(R.id.user_image);
            name = view.findViewById(R.id.user_name);
            calorie = view.findViewById(R.id.user_calorie);
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
        String rank = Integer.toString(position + 1);
        ImageLoadTask profile = new ImageLoadTask(mUsers.get(position).getProfile(),holder.image);
        profile.execute();

        String kcal ="";
        if(type)
            kcal = String.format("%.1f",mUsers.get(position).getAll())+" Kcal";
        else kcal = String.format("%.1f",mUsers.get(position).getBurn()) + " Kcal";

        holder.rank.setText(rank);
        holder.name.setText(mUsers.get(position).getName());
        holder.calorie.setText(kcal);

        holder.rank.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"bmjua.ttf"));
        holder.name.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"bmjua.ttf"));
        holder.calorie.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"bmjua.ttf"));

        holder.rank.setTextColor(Color.BLACK);
        holder.name.setTextColor(Color.BLACK);
        holder.calorie.setTextColor(Color.BLACK);
        holder.rank.setTextSize(20);
        holder.name.setTextSize(18);
        holder.name.setSingleLine();
        holder.calorie.setTextSize(20);

        LinearLayout.LayoutParams rankAttr = (LinearLayout.LayoutParams)holder.rank.getLayoutParams();
        LinearLayout.LayoutParams imageAttr = (LinearLayout.LayoutParams)holder.image.getLayoutParams();
        LinearLayout.LayoutParams nameAttr = (LinearLayout.LayoutParams)holder.name.getLayoutParams();
        LinearLayout.LayoutParams calorieAttr = (LinearLayout.LayoutParams)holder.calorie.getLayoutParams();

        rankAttr.width = 50;
        rankAttr.leftMargin = 50;
        imageAttr.width = 175;
        imageAttr.height = 175;
        imageAttr.leftMargin = 0;
        nameAttr.leftMargin = 40;
        calorieAttr.rightMargin = 50;

        holder.rank.setLayoutParams(rankAttr);
        holder.image.setLayoutParams(imageAttr);
        holder.name.setLayoutParams(nameAttr);
        holder.calorie.setLayoutParams(calorieAttr);

    }

    @Override
    public int getItemCount() { return mUsers.size(); }
}
