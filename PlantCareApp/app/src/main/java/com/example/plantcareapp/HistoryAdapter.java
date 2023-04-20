package com.example.plantcareapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    Context context;
    List<Plant> plantList;
    List<String>dates;

    public HistoryAdapter(Context context, ArrayList<Plant> plantList, List<String> dates) {
        this.context = context;
        this.plantList = plantList;
        this.dates = dates;
    }

    public void setFilteredPlants(List<Plant> newPlantList, List<String> newDates){
        this.plantList=newPlantList;
        this.dates=newDates;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_card, parent, false);
        HistoryAdapter.MyViewHolder myViewHolder = new HistoryAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        holder.textView.setText(plant.getName());
        holder.textview2.setText(dates.get(position));
        Glide.with(context)
                .load(plant.imageURL)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlantInformationActivity.class);
                intent.putExtra("name",plant.getName());
                intent.putExtra("botanical",plant.getBotanicalName());
                intent.putExtra("temperature",plant.getTemperature());
                intent.putExtra("water",plant.getWater());
                intent.putExtra("sunlight",plant.getSunlight());
                intent.putExtra("humidity",plant.getHumidity());
                intent.putExtra("url",plant.getImageURL());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        TextView textview2;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.plantName);
            textview2=itemView.findViewById(R.id.plantDate);
            imageView=itemView.findViewById(R.id.plantImage);
        }
    }
}
