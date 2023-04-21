package com.example.plantcareapp.fragments;

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

import com.example.plantcareapp.models.Plant;
import com.example.plantcareapp.R;
import com.example.plantcareapp.activities.HomeActivity;
import com.example.plantcareapp.adapters.HomeAdapter;

import java.util.ArrayList;


/**
 * Home fragment
 * Displays all of the plants in the database
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomeAdapter recyclerViewAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    private ArrayList<Plant> plantArrayList;
    private TextView numberOfResults;

    // Required public empty constructor
    public HomeFragment(){}

    /**
     * Create new instance of HomeFragment
     * @return a new instance of HomeFragment
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    /**
     * Executes when fragment is created
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate layout once fragment has been created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home,container,false);
        return v;
    }

    /**
     * Retrieves items and sets listeners once view has been created
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        numberOfResults = view.findViewById(R.id.searchResults);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Gets plant data
        plantArrayList=((HomeActivity) getActivity()).getPlantArrayList();

        // Sets up recycler view with 2 columns in grid format
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewAdapter = new HomeAdapter(getActivity(), plantArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);

        // Sets up search view to filter results
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
    }

    /**
     * Method to filter plants based on search bar input
     * @param text search view input
     */
    private void filterPlants(String text){

        ArrayList<Plant>updatedPlants = new ArrayList<>();

        // Find if search view input matches any plants
        for (int i=0; i<plantArrayList.size(); i++){

            if(plantArrayList.get(i).getName().toLowerCase().contains(text.toLowerCase())){
                updatedPlants.add(plantArrayList.get(i));
            }
        }
        // Update recycler view
        recyclerViewAdapter.setFilteredPlants(updatedPlants);

        // Update text displaying number of results upon searching
        if (updatedPlants.size()<plantArrayList.size()){
            numberOfResults.setVisibility(View.VISIBLE);
            numberOfResults.setText(String.valueOf(updatedPlants.size())+" results found.");
        }
        else{
            numberOfResults.setVisibility(View.GONE);
        }
    }
}