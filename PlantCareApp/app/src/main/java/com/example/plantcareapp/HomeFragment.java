package com.example.plantcareapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecylerViewAdapter recyclerViewAdapter;
    private List<String> plantNames;
    private List<String> plantBotanical;
    private List<String> plantTemperature;
    private List<String> plantWater;
    private List<String> plantSunlight;
    private List<String> plantHumidity;
    private List<String> plantImageURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        plantNames=((HomeActivity) getActivity()).getNames();
        plantBotanical=((HomeActivity) getActivity()).getBotanicalNames();
        plantTemperature=((HomeActivity) getActivity()).getTemperature();
        plantWater=((HomeActivity) getActivity()).getWater();
        plantSunlight=((HomeActivity) getActivity()).getSunlight();
        plantHumidity=((HomeActivity) getActivity()).getHumidity();
        plantImageURL=((HomeActivity) getActivity()).getImageURL();

        recyclerViewAdapter = new RecylerViewAdapter(plantNames, plantBotanical, plantTemperature, plantWater, plantSunlight, plantHumidity, plantImageURL, getActivity());

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setHasFixedSize(true);




        return v;
    }
}