package com.example.plantcareapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.MyViewHolder> {

    /*List<String> plantNames;
    List<String> plantBotanical;
    List<String> plantTemperature;
    List<String> plantWater;
    List<String> plantSunlight;
    List<String> plantHumidity;
    List<String> plantImageURL;*/
    Context context;

    public RecylerViewAdapter(Context context, ArrayList<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    List<Plant> plantList;

    /*public RecylerViewAdapter(List<String> plantNames, List<String> plantBotanical, List<String> plantTemperature, List<String> plantWater, List<String> plantSunlight, List<String> plantHumidity, List<String> plantImageURL, Context context) {
        this.plantNames = plantNames;
        this.plantBotanical = plantBotanical;
        this.plantTemperature = plantTemperature;
        this.plantWater = plantWater;
        this.plantSunlight = plantSunlight;
        this.plantHumidity = plantHumidity;
        this.plantImageURL = plantImageURL;
        this.context = context;
    }*/

    /* COME BACK TO THIS - IT IS FOR FILTERING
    public void setFilteredPlants(List<String> newPlantNames, List<String> newPlantBotanical, List<String> newPlantTemperature, List<String> newPlantWater, List<String> newPlantSunlight, List<String> newPlantHumidity, List<String> newPlantImageURL){
        this.plantNames = newPlantNames;
        this.plantBotanical=newPlantBotanical;
        this.plantTemperature=newPlantTemperature;
        this.plantWater=newPlantWater;
        this.plantSunlight=newPlantSunlight;
        this.plantHumidity=newPlantHumidity;
        this.plantImageURL=newPlantImageURL;
        notifyDataSetChanged();
    }*/

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

        holder.textView.setText(plant.name);
        Glide.with(context)
                .load(plant.imageURL)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlantInformationActivity.class);
                intent.putExtra("name",plant.name);
                intent.putExtra("botanical",plant.botanicalName);
                intent.putExtra("temperature",plant.temperature);
                intent.putExtra("water",plant.water);
                intent.putExtra("sunlight",plant.sunlight);
                intent.putExtra("humidity",plant.humidity);
                intent.putExtra("url",plant.imageURL);
                context.startActivity(intent);
            }
        });


        /*holder.textView.setText(plantNames.get(position));
        Glide.with(context)
                .load(plantImageURL.get(position))
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();

                Intent intent = new Intent(context, PlantInformationActivity.class);
                intent.putExtra("name",plantNames.get(pos));
                intent.putExtra("botanical",plantBotanical.get(pos));
                intent.putExtra("temperature",plantTemperature.get(pos));
                intent.putExtra("water",plantWater.get(pos));
                intent.putExtra("sunlight",plantSunlight.get(pos));
                intent.putExtra("humidity",plantHumidity.get(pos));
                intent.putExtra("url",plantImageURL.get(pos));
                context.startActivity(intent);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        //return plantNames.size();
        return plantList.size();
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
