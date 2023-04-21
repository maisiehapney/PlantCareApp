package com.example.plantcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plantcareapp.fragments.HomeFragment;
import com.example.plantcareapp.models.Plant;
import com.example.plantcareapp.fragments.ProfileFragment;
import com.example.plantcareapp.R;
import com.example.plantcareapp.fragments.ClassifyFragment;
import com.example.plantcareapp.fragments.HistoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Home activity which all fragments are attached to
 * Retrieves plant data from database, controls fragments and application navigation
 */
public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private BottomNavigationView navigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private ClassifyFragment classifyFragment = new ClassifyFragment();
    private HistoryFragment historyFragment = new HistoryFragment();
    private ProgressBar progressBar;
    private ArrayList<Plant> plantArrayList;
    private ArrayList<String> plantNames;

    /**
     * If user is on home fragment and back is pressed, exit application to device home screen
     */
    @Override
    public void onBackPressed(){
        if (navigationView.getSelectedItemId()== R.id.home){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }else{
            navigationView.setSelectedItemId(R.id.home);
        }
    }

    /**
     * Executes when activity is created - retrieves items, gets plant data from database and sets up navigation menu
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.home);
        progressBar=findViewById(R.id.progressBar);
        plantNames = new ArrayList<>();
        plantArrayList=new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        // Get plant data from database
        getPlantData();

        // Set up navigation menu for navigating between fragments
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();
                        return true;
                    case R.id.scan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, classifyFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, profileFragment).commit();
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, historyFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void getPlantData(){
        db.collection("Plants").orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Data successfully retrieved - store data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String plantName = document.getString("name");
                                String plantBotanical = document.getString("botanicalName");
                                String plantTemperature = document.getString("temperature");
                                String plantWater = document.getString("water");
                                String plantSunlight = document.getString("sunlight");
                                String plantHumidity = document.getString("humidity");
                                String plantImageURL = document.getString("imageURL");
                                plantNames.add(plantName);
                                plantArrayList.add(new Plant(plantName, plantBotanical, plantTemperature, plantWater, plantSunlight, plantHumidity, plantImageURL));
                            }
                            // Dismiss progress bar and inflate home fragment
                            progressBar.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();

                        } else {
                            // Failed to get data - dismiss progress bar, display toast message and exit to main activity
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(HomeActivity.this, "Connect to internet and try again.",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        }
                    }
                });
    }

    /**
     * Method for getting plant information
     * @return plant type arraylist storing all plant information
     */
    public ArrayList<Plant> getPlantArrayList() {
        return plantArrayList;
    }

    /**
     * Method used for indexing plants by name
     * @return string arraylist storing all plant names
     */
    public List<String> getNames() {
        return plantNames;
    }

}