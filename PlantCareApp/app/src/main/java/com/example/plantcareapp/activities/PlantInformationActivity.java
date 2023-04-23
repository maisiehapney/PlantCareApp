package com.example.plantcareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.plantcareapp.R;

/**
 * Activity for displaying plant information and care guides
 */
public class PlantInformationActivity extends AppCompatActivity {

    private TextView name, botanical, temperature, water, sunlight, humidity, confidence, loginInfo, identification;
    private ImageView plantImage;

    /**
     * Executes when activity is created - retrieves items and displays information
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_information);
        name = findViewById(R.id.title);
        botanical = findViewById(R.id.botanical);
        temperature = findViewById(R.id.temperature);
        water = findViewById(R.id.water);
        sunlight = findViewById(R.id.sunlight);
        humidity = findViewById(R.id.humidity);
        plantImage=findViewById(R.id.imageView);
        confidence=findViewById(R.id.confidence);
        loginInfo=findViewById(R.id.loginInformation);
        identification=findViewById(R.id.identification);
        displayPlantInformation();
    }

    /**
     * Method to display correct plant information depending on information received
     */
    private void displayPlantInformation(){

        // Get extras from previous activity
        Bundle extras=getIntent().getExtras();
        String plantName=extras.getString("name");
        String plantBotanical=extras.getString("botanical");
        String plantTemperature=extras.getString("temperature");
        String plantWater=extras.getString("water");
        String plantSunlight=extras.getString("sunlight");
        String plantHumidity=extras.getString("humidity");
        String plantURL = extras.getString("url");
        String plantConfidence = extras.getString("confidence");

        // Set plant name
        name.setText(plantName);

        // Display information if not null - values will be null for guest classification results
        if(plantBotanical!=null && plantTemperature!=null && plantWater!=null && plantSunlight!=null &&plantHumidity!=null &&plantURL!=null){
            Glide.with(this)
                    .load(plantURL)
                    .into(plantImage);
            botanical.setText(plantBotanical);
            temperature.setText(plantTemperature);
            water.setText(plantWater);
            sunlight.setText(plantSunlight);
            humidity.setText(plantHumidity);

            // If plant confidence is not null display confidence of classification
            if (plantConfidence != null){
                confidence.setText("Confidence: "+plantConfidence);
                confidence.setVisibility(View.VISIBLE);
                identification.setText("Identified as: "+plantName);
                identification.setVisibility(View.VISIBLE);
            }
            // Else hide confidence - classification has not been made
            else{
                confidence.setVisibility(View.GONE);
            }
         // Display login information to guest users
        }else{
            loginInfo.setVisibility(View.VISIBLE);
            identification.setText("Identified as: "+plantName);
            identification.setVisibility(View.VISIBLE);
        }
    }
}