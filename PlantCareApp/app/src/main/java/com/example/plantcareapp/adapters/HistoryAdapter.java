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
 * HistoryAdapter holds data to populate grid recycler view
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Plant>plantList;
    private ArrayList<String>dates;

    // Constructor gets data from HistoryFragment
    public HistoryAdapter(Context context, ArrayList<Plant> plantList, ArrayList<String> dates) {
        this.context = context;
        this.plantList = plantList;
        this.dates = dates;
    }

    /**
     * Inflates layout for each item in recycler view
     */
    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_history_card, parent, false);
        HistoryAdapter.MyViewHolder myViewHolder = new HistoryAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    /**
     * Each item is bound to view and displayed at specified position
     * Listeners are also set on each item to open new activity with plant information extras
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        // Set plant name, date and image
        holder.plantName.setText(plant.getName());
        holder.plantDate.setText(dates.get(position));
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
        TextView plantDate;
        ImageView plantImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName=itemView.findViewById(R.id.plantName);
            plantDate=itemView.findViewById(R.id.plantDate);
            plantImage=itemView.findViewById(R.id.plantImage);
        }
    }

    /**
     * Gets updated data when search view is used
     */
    public void setFilteredPlants(ArrayList<Plant> newPlantList, ArrayList<String> newDates){
        this.plantList=newPlantList;
        this.dates=newDates;
        notifyDataSetChanged();
    }
}
