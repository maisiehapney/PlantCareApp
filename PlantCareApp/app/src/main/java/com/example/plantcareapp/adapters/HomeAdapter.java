package com.example.plantcareapp.adapters;

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
import com.example.plantcareapp.models.Plant;
import com.example.plantcareapp.activities.PlantInformationActivity;
import com.example.plantcareapp.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    Context context;
    List<Plant> plantList;


    public HomeAdapter(Context context, ArrayList<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    public void setFilteredPlants(List<Plant> newPlantList){
        this.plantList=newPlantList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder; //come back to this - video
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        holder.textView.setText(plant.getName());
        Glide.with(context)
                .load(plant.getImageURL())
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
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.plantName);
            imageView=itemView.findViewById(R.id.plantImage);
        }
    }
}
