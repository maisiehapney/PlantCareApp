package com.example.plantcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private List<String> plantNames;
    private List<String> plantBotanical;
    private List<String> plantTemperature;
    private List<String> plantWater;
    private List<String> plantSunlight;
    private List<String> plantHumidity;
    private List<String> plantImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.home);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();

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
        });

        plantNames = new ArrayList<>();
        plantBotanical = new ArrayList<>();
        plantTemperature = new ArrayList<>();
        plantWater = new ArrayList<>();
        plantSunlight = new ArrayList<>();
        plantHumidity = new ArrayList<>();
        plantImageURL = new ArrayList<>();

        //Might not need this
        if(user==null){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);

        }

        db.collection("Plants").orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                plantNames.add(document.getString("name"));
                                plantBotanical.add(document.getString("botanicalName"));
                                plantTemperature.add(document.getString("temperature"));
                                plantWater.add(document.getString("water"));
                                plantSunlight.add(document.getString("sunlight"));
                                plantHumidity.add(document.getString("humidity"));
                                plantImageURL.add(document.getString("imageURL"));

                                Log.d("tag", document.getId() + " => " + document.getData());
                                //Toast.makeText(HomeActivity.this, "Got data",
                                        //Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(HomeActivity.this, "Got data",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("tag", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    public List<String> getNames() {
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
    }
}