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

/**
 * HomeAdapter holds data to populate grid recycler view
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Plant> plantList;

    // Constructor gets data from HomeFragment
    public HomeAdapter(Context context, ArrayList<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
    }

    /**
     * Inflates layout for each item in recycler view
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /**
     * Each item is bound to view and displayed at specified position
     * Listeners are also set on each item to open new activity with plant information extras
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        // Set plant name and image
        holder.plantName.setText(plant.getName());
        Glide.with(context)
                .load(plant.getImageURL())
                .into(holder.plantImage);

        // Set on click listener that sends selected plant data to next activity
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

    /**
     * Gets the number of items in the recycler view
     * @return number of items in recycler view
     */
    @Override
    public int getItemCount() {

        return plantList.size();
    }

    /**
     * Sets view layout for each item
     */
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView plantName;
        ImageView plantImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName=itemView.findViewById(R.id.plantName);
            plantImage=itemView.findViewById(R.id.plantImage);
        }
    }

    /**
     * Gets updated data when search view is used
     */
    public void setFilteredPlants(ArrayList<Plant> newPlantList){
        this.plantList=newPlantList;
        notifyDataSetChanged();
    }
}
