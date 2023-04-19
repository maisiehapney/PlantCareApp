package com.example.plantcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseFirestore db;
    BottomNavigationView navigationView;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    ClassifyFragment classifyFragment = new ClassifyFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    ProgressBar progressBar;
    /*private List<String> plantNames;
    private List<String> plantBotanical;
    private List<String> plantTemperature;
    private List<String> plantWater;
    private List<String> plantSunlight;
    private List<String> plantHumidity;
    private List<String> plantImageURL;*/
    ArrayList<Plant> plantArrayList;
    private List<String> plantNames;

    @Override
    public void onBackPressed(){
        if (navigationView.getSelectedItemId()==R.id.home){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }else{
            navigationView.setSelectedItemId(R.id.home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.home);
        progressBar=findViewById(R.id.progressBar);

       /* getSupportFragmentManager().beginTransaction().replace(R.id.frame, classifyFragment).commit();

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
                }
                return false;
            }
        });*/

        plantNames = new ArrayList<>();
        /*plantNames = new ArrayList<>();
        plantBotanical = new ArrayList<>();
        plantTemperature = new ArrayList<>();
        plantWater = new ArrayList<>();
        plantSunlight = new ArrayList<>();
        plantHumidity = new ArrayList<>();
        plantImageURL = new ArrayList<>();*/
        plantArrayList=new ArrayList<Plant>();

        //Might not need this
        if(user==null){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);

        }

        progressBar.setVisibility(View.VISIBLE);

        db.collection("Plants").orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                /*plantNames.add(document.getString("name"));
                                plantBotanical.add(document.getString("botanicalName"));
                                plantTemperature.add(document.getString("temperature"));
                                plantWater.add(document.getString("water"));
                                plantSunlight.add(document.getString("sunlight"));
                                plantHumidity.add(document.getString("humidity"));
                                plantImageURL.add(document.getString("imageURL"));*/
                                String plantName = document.getString("name");
                                String plantBotanical = document.getString("botanicalName");
                                String plantTemperature = document.getString("temperature");
                                String plantWater = document.getString("water");
                                String plantSunlight = document.getString("sunlight");
                                String plantHumidity = document.getString("humidity");
                                String plantImageURL = document.getString("imageURL");

                                plantNames.add(plantName);
                                plantArrayList.add(new Plant(plantName, plantBotanical, plantTemperature, plantWater, plantSunlight, plantHumidity, plantImageURL));

                                Log.d("tag", document.getId() + " => " + document.getData());
                                //Toast.makeText(HomeActivity.this, "Got data",
                                        //Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(HomeActivity.this, "Got data",
                                    //Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(HomeActivity.this, "Connect to internet and try again.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();

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

    public ArrayList<Plant> getPlantArrayList() {
        return plantArrayList;
    }

    public List<String> getNames() {
        return plantNames;
    }

    /* public List<String> getNames() {
        return plantNames;
    }

    public List<String> getBotanicalNames() {
        return plantBotanical;
    }

    public List<String> getTemperature() {
        return plantTemperature;
    }

    public List<String> getWater() {
        return plantWater;
    }

    public List<String> getSunlight() {
        return plantSunlight;
    }

    public List<String> getHumidity() {
        return plantHumidity;
    }

    public List<String> getImageURL() {
        return plantImageURL;
    }*/
}