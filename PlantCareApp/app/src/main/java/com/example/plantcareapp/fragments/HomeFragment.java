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
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomeAdapter recyclerViewAdapter;
    //private SearchView searchView;
    private androidx.appcompat.widget.SearchView searchView;
    ArrayList<Plant> plantArrayList;
    TextView noResults;

    public HomeFragment(){}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home,container,false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plantArrayList=((HomeActivity) getActivity()).getPlantArrayList();
        noResults = view.findViewById(R.id.searchResults);
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

        recyclerViewAdapter = new HomeAdapter(getActivity(), plantArrayList);

        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setHasFixedSize(true);


    }

    private void filterPlants(String text){

        List<Plant>updatedPlants = new ArrayList<>();

        for (int i=0; i<plantArrayList.size(); i++){

            if(plantArrayList.get(i).getName().toLowerCase().contains(text.toLowerCase())){
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

    }
}