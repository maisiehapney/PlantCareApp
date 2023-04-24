package com.example.plantcareapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantcareapp.activities.HomeActivity;
import com.example.plantcareapp.models.Plant;
import com.example.plantcareapp.R;
import com.example.plantcareapp.adapters.HistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * History fragment
 * Displays users' previous classification results
 */
public class HistoryFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ArrayList<String> dates;//testing
    private ArrayList<Plant> plantArrayList;
    private RecyclerView recyclerView;
    private HistoryAdapter recyclerViewAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    private TextView numberOfResults;
    private ProgressBar progressBar;

    // Required public empty constructor
    public HistoryFragment(){}

    /**
     * Create new instance of HistoryFragment
     * @return a new instance of HistoryFragment
     */
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
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
        View v =inflater.inflate(R.layout.fragment_history, container, false);
        return v;
    }

    /**
     * Retrieves items and sets listeners once view has been created
     * Also retrieves user history data from Firebase
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        dates = new ArrayList<>();
        plantArrayList=new ArrayList<>();
        progressBar=view.findViewById(R.id.progressBar);
        numberOfResults = view.findViewById(R.id.searchResults);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Gets users previous classification results from database
        getPlantHistory();

        // Sets up recycler view with 2 columns in grid format
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Sets up search view to filter results
        searchView=view.findViewById(R.id.search);
        searchView.clearFocus();

    }

    /**
     * Method to get users plant history
     * Queries database to find entries with current user's email, then sorts resulting plants by date
     */
    private void getPlantHistory(){
        db.collection("UserPlants")
                .whereEqualTo("email", user.getEmail())
                .orderBy("date", Query.Direction.DESCENDING)
                .get(Source.SERVER) // Only get data if online
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // For each result, get corresponding plant data and store in plantArrayList
                                // Also store date of each result
                                int plantIndex = ((HomeActivity) getActivity()).getNames().indexOf(document.getString("plant"));
                                plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(plantIndex));
                                dates.add((DateFormat.getDateInstance().format(document.getTimestamp("date").toDate())).toString());
                            }

                            // Dismiss progress bar, display recycler view of users plant history and set up search view
                            progressBar.setVisibility(View.GONE);
                            if (plantArrayList.size()>0){
                                recyclerViewAdapter = new HistoryAdapter(getActivity(), plantArrayList, dates);
                                recyclerView.setAdapter(recyclerViewAdapter);
                                recyclerView.setHasFixedSize(true);
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
                            }else{
                                // If there are no results, display message
                                numberOfResults.setVisibility(View.VISIBLE);
                                numberOfResults.setText("No history to show.");
                            }
                        } else {
                            // Failed to get data - dismiss progress bar and display toast message
                            progressBar.setVisibility(View.GONE);
                            numberOfResults.setVisibility(View.VISIBLE);
                            numberOfResults.setText("History could not be loaded.\n Connect to the internet and try again.");
                        }
                    }
                });
    }

    /**
     * Method to filter plants based on search bar input
     * @param text search view input
     */
    private void filterPlants(String text){

        ArrayList<Plant>updatedPlants=new ArrayList<>();
        ArrayList<String>updatedDates=new ArrayList<>();

        // Find if search view input matches any plants
        for (int i=0; i<plantArrayList.size(); i++){
            if(plantArrayList.get(i).getName().toLowerCase().contains(text.toLowerCase())){
                updatedPlants.add(plantArrayList.get(i));
                updatedDates.add(dates.get(i));
            }
        }
        // Update recycler view
        recyclerViewAdapter.setFilteredPlants(updatedPlants, updatedDates);

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
