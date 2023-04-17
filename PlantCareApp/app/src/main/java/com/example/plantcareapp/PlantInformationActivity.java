package com.example.plantcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PlantInformationActivity extends AppCompatActivity {

    TextView name, botanical, temperature, water, sunlight, humidity, confidence, loginInfo, identification;
    ImageView plantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_information);

        //information = findViewById(R.id.info);
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


        Bundle extras=getIntent().getExtras();
        String plantName=extras.getString("name");
        String plantBotanical=extras.getString("botanical");
        String plantTemperature=extras.getString("temperature");
        String plantWater=extras.getString("water");
        String plantSunlight=extras.getString("sunlight");
        String plantHumidity=extras.getString("humidity");
        String plantURL = extras.getString("url");
        String plantConfidence = extras.getString("confidence");

        //String url = "https://www.tutorialspoint.com/images/tp-logo-diamond.png";
        /*Glide.with(this)
                .load(plantURL)
                .into(plantImage);*/

        name.setText(plantName);
        if(plantBotanical!=null && plantTemperature!=null && plantWater!=null && plantSunlight!=null &&plantHumidity!=null &&plantURL!=null){
            Glide.with(this)
                    .load(plantURL)
                    .into(plantImage);
            botanical.setText(plantBotanical);
            temperature.setText(plantTemperature);
            water.setText(plantWater);
            sunlight.setText(plantSunlight);
            humidity.setText(plantHumidity);
            if (plantConfidence != null){
                confidence.setText("Confidence: "+plantConfidence);
                confidence.setVisibility(View.VISIBLE);
                //name.setText("Identified as: " + plantName);
                identification.setText("Identified as: "+plantName);
                identification.setVisibility(View.VISIBLE);
            }
            else{
                confidence.setVisibility(View.GONE);
                //name.setText(plantName);
            }
        }else{
            loginInfo.setVisibility(View.VISIBLE);
            //name.setText("Identified as: " + plantName);
            identification.setText("Identified as: "+plantName);
            identification.setVisibility(View.VISIBLE);
        }
        /*botanical.setText(plantBotanical);
        temperature.setText(plantTemperature);
        water.setText(plantWater);
        sunlight.setText(plantSunlight);
        humidity.setText(plantHumidity);
        if (plantConfidence != null){
            confidence.setText("Confidence: "+plantConfidence);
            confidence.setVisibility(View.VISIBLE);
        }
        else{
            confidence.setVisibility(View.GONE);
        }*/

    }
}