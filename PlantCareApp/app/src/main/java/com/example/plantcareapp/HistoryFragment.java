package com.example.plantcareapp;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseFirestore db;
    private List<String> plants;
    private List<String> dates;//testing
    private ArrayList<Plant> plantArrayList;
    private List<String> plantNames;

    TextView result;
    //COPIED FROM HOME
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HistoryAdapter recyclerViewAdapter;
    private androidx.appcompat.widget.SearchView searchView;
    TextView noResults;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_history, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        plants = new ArrayList<>(); //testing
        dates = new ArrayList<>();
        plantArrayList=new ArrayList<>();
        plantNames=((HomeActivity) getActivity()).getNames();


        db.collection("UserPlants")
                .whereEqualTo("email", user.getEmail())
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //plants.add(document.getString("plant"));
                                dates.add((DateFormat.getDateInstance().format(document.getTimestamp("date").toDate())).toString());
                                int plantIndex = ((HomeActivity) getActivity()).getNames().indexOf(document.getString("plant"));
                                plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(plantIndex));

                                /*for (int i=0; i<plantNames.size();i++){
                                    if(plantNames.get(i).equals(document.getString("plant"))){
                                        plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(i));
                                    }
                                }*/

                                //result.setText(results.get(1));

                                Log.d("tag", document.getId() + " => " + document.getData());
                                //Toast.makeText(getActivity(), results.get(1),
                                //Toast.LENGTH_SHORT).show();
                            }
                            recyclerViewAdapter = new HistoryAdapter(getActivity(), plantArrayList, dates);

                            recyclerView.setAdapter(recyclerViewAdapter);

                            recyclerView.setHasFixedSize(true);
                            /*for (int i=0; i<plants.size(); i++){
                                for (int j=0;j<plantNames.size();j++){
                                    //int index;
                                    if (plants.get(i).equals(plantNames.get(j))){
                                        plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(j));
                                        break;
                                    }
                                }
                            }*/

                            /*for (int i=0; i<plants.size();i++){
                                plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(0));
                                //dates.add("date");
                            }*/
                            //result.setText(results.get(3));
                            //Toast.makeText(HomeActivity.this, "Got data",
                            //Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                            //getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();
                        } else {
                            //progressBar.setVisibility(View.GONE);
                            /*Toast.makeText(HomeActivity.this, "Connect to internet and try again.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);*/
                        }
                    }
                });

        //Toast.makeText(getActivity(), String.valueOf(dates.size()),
                //Toast.LENGTH_SHORT).show();

        /*for (int i=0; i<plants.size(); i++){
            for (int j=0;j<plantNames.size();j++){
                //int index;
                if (plants.get(i).equals(plantNames.get(j))){
                    plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(0));
                    break;
                }
            }
        }
        Toast.makeText(getActivity(), String.valueOf(plantArrayList.size()),
                Toast.LENGTH_SHORT).show();*/
        /*for (int i=0; i<plantNames.size();i++){
            plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(0));
            dates.add("date");
        }*/
        //plantArrayList.add(((HomeActivity) getActivity()).getPlantArrayList().get(0));
        //dates.add("date");

        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getActivity(), String.valueOf(plantArrayList.size()),
                Toast.LENGTH_SHORT).show();

        noResults = view.findViewById(R.id.noSearchResults);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        //COME BACK TO THIS - SEARCHING
        /*searchView=view.findViewById(R.id.search);
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

        });*/

        //plantNames=((HomeActivity) getActivity()).getNames();
        //plantBotanical=((HomeActivity) getActivity()).getBotanicalNames();
        //plantTemperature=((HomeActivity) getActivity()).getTemperature();
        //plantWater=((HomeActivity) getActivity()).getWater();
        // plantSunlight=((HomeActivity) getActivity()).getSunlight();
        //plantHumidity=((HomeActivity) getActivity()).getHumidity();
        //plantImageURL=((HomeActivity) getActivity()).getImageURL();

        //recyclerViewAdapter = new RecylerViewAdapter(plantNames, plantBotanical, plantTemperature, plantWater, plantSunlight, plantHumidity, plantImageURL, getActivity());




    }

    }
