package com.example.plantcareapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {

    List<String> plantNames;
    List<String> plantBotanical;
    List<String> plantTemperature;
    List<String> plantWater;
    List<String> plantSunlight;
    List<String> plantHumidity;
    List<String> plantImageURL;
    Context context;

    public RecylerViewAdapter(List<String> plantNames, List<String> plantBotanical, List<String> plantTemperature, List<String> plantWater, List<String> plantSunlight, List<String> plantHumidity, List<String> plantImageURL, Context context) {
        this.plantNames = plantNames;
        this.plantBotanical = plantBotanical;
        this.plantTemperature = plantTemperature;
        this.plantWater = plantWater;
        this.plantSunlight = plantSunlight;
        this.plantHumidity = plantHumidity;
        this.plantImageURL = plantImageURL;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(plantNames.get(position));
        Glide.with(context)
                .load(plantImageURL.get(position))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return plantNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
