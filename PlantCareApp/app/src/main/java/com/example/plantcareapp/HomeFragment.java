package com.example.plantcareapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecylerViewAdapter recyclerViewAdapter;
    //private SearchView searchView;
    private androidx.appcompat.widget.SearchView searchView;
   /* private List<String> plantNames;
    private List<String> plantBotanical;
    private List<String> plantTemperature;
    private List<String> plantWater;
    private List<String> plantSunlight;
    private List<String> plantHumidity;
    private List<String> plantImageURL;*/
    ArrayList<Plant> plantArrayList;
    //private List<String> plantNames;
    TextView noResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home,container,false);

        /*recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);*/

        /*plantNames=((HomeActivity) getActivity()).getNames();
        plantBotanical=((HomeActivity) getActivity()).getBotanicalNames();
        plantTemperature=((HomeActivity) getActivity()).getTemperature();
        plantWater=((HomeActivity) getActivity()).getWater();
        plantSunlight=((HomeActivity) getActivity()).getSunlight();
        plantHumidity=((HomeActivity) getActivity()).getHumidity();
        plantImageURL=((HomeActivity) getActivity()).getImageURL();*/
        plantArrayList=((HomeActivity) getActivity()).getPlantArrayList();
        //plantNames=((HomeActivity) getActivity()).getNames();

       /* recyclerViewAdapter = new RecylerViewAdapter(plantNames, plantBotanical, plantTemperature, plantWater, plantSunlight, plantHumidity, plantImageURL, getActivity());

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setHasFixedSize(true);*/
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noResults = view.findViewById(R.id.noSearchResults);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        searchView=view.findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPlants(newText);
                return true;
            }

        });

        //plantNames=((HomeActivity) getActivity()).getNames();
        //plantBotanical=((HomeActivity) getActivity()).getBotanicalNames();
        //plantTemperature=((HomeActivity) getActivity()).getTemperature();
        //plantWater=((HomeActivity) getActivity()).getWater();
       // plantSunlight=((HomeActivity) getActivity()).getSunlight();
        //plantHumidity=((HomeActivity) getActivity()).getHumidity();
        //plantImageURL=((HomeActivity) getActivity()).getImageURL();

        //recyclerViewAdapter = new RecylerViewAdapter(plantNames, plantBotanical, plantTemperature, plantWater, plantSunlight, plantHumidity, plantImageURL, getActivity());

        recyclerViewAdapter = new RecylerViewAdapter(getActivity(), plantArrayList);

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setHasFixedSize(true);


    }
   /* private void filterPlants(String text){
        List<String>updatedPlantNames = new ArrayList<>();
        List<String> updatedPlantBotanical= new ArrayList<>();
        List<String> updatedPlantTemperature= new ArrayList<>();
        List<String> updatedPlantWater= new ArrayList<>();
        List<String> updatedPlantSunlight= new ArrayList<>();
        List<String> updatedPlantHumidity= new ArrayList<>();
        List<String>updatedPlantImageURL = new ArrayList<>();

        for (int i=0; i<plantNames.size(); i++){
            if(plantNames.get(i).toLowerCase().contains(text.toLowerCase())){
                updatedPlantNames.add(plantNames.get(i));
                updatedPlantImageURL.add(plantImageURL.get(i));
                updatedPlantBotanical.add(plantBotanical.get(i));
                updatedPlantTemperature.add(plantTemperature.get(i));
                updatedPlantWater.add(plantWater.get(i));
                updatedPlantSunlight.add(plantSunlight.get(i));
                updatedPlantHumidity.add(plantHumidity.get(i));
            }
        }

        if(updatedPlantNames.isEmpty()){
            //Deal with
        }
        else{
            recyclerViewAdapter.setFilteredPlants(updatedPlantNames,updatedPlantBotanical,updatedPlantTemperature, updatedPlantWater,updatedPlantSunlight, updatedPlantHumidity, updatedPlantImageURL);

        }
    }*/

    private void filterPlants(String text){

        List<Plant>updatedPlants = new ArrayList<>();

        for (int i=0; i<plantArrayList.size(); i++){

            if(plantArrayList.get(i).name.toLowerCase().contains(text.toLowerCase())){
                updatedPlants.add(plantArrayList.get(i));
            }
        }
        recyclerViewAdapter.setFilteredPlants(updatedPlants);

        if (updatedPlants.size()<plantArrayList.size()){
            noResults.setVisibility(View.VISIBLE);
            noResults.setText(String.valueOf(updatedPlants.size())+" results found.");
        }
        else{
            noResults.setVisibility(View.GONE);
        }

        /*recyclerViewAdapter.setFilteredPlants(updatedPlants);
        if(updatedPlants.isEmpty()){
            noResults.setVisibility(View.VISIBLE);
        }
        else{
            //noResults.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
            noResults.setText(String.valueOf(updatedPlants.size())+" results found.");
        }*/


        /*if(updatedPlants.isEmpty()){
            //Deal with
        }
        else{
            recyclerViewAdapter.setFilteredPlants(updatedPlants);

        }*/
    }
}