package com.example.plantcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    ArrayList<Plant> plantArrayList;
    private ArrayList<String> plantNames;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*if(savedInstanceState==null){

        }*/

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.home);
        progressBar=findViewById(R.id.progressBar);


        plantNames = new ArrayList<>();
        plantArrayList=new ArrayList<>();

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

                            }

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

}