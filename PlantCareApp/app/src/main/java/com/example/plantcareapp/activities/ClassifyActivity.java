package com.example.plantcareapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.plantcareapp.R;
import com.example.plantcareapp.utilities.Classify;

import java.io.IOException;

/**
 * Activity for classifying plants for guest users
 */
public class ClassifyActivity extends AppCompatActivity {

    private Button photoButton, uploadButton;
    private int imageDims = 224;
    private String plantSpecies;
    private ActivityResultLauncher<Intent> galleryResultLaunch, cameraResultLaunch;

    /**
     * Executes when activity is created - retrieves items and sets listeners
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        photoButton = findViewById(R.id.photoButton);
        uploadButton = findViewById(R.id.uploadButton);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraResultLaunch.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryResultLaunch.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));

            }
        });

        cameraResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // When camera intent is launched, get data as bitmap and rescale to correct size for classification model
                        if (result.getResultCode() == RESULT_OK && result.getData()!=null) {

                            Bitmap img = (Bitmap) result.getData().getExtras().get("data");
                            int dims = Math.min(img.getWidth(), img.getHeight());

                            img = ThumbnailUtils.extractThumbnail(img, dims, dims);
                            img = Bitmap.createScaledBitmap(img, imageDims, imageDims, false);

                            // Classify image using Classify utility class
                            try{
                                plantSpecies = Classify.classifyImage(img, getApplicationContext());
                            }catch(IOException e){
                                e.printStackTrace();
                                Toast.makeText(ClassifyActivity.this, "Classification Failed.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Open plant information activity for classified plant
                            openNewActivity();
                        }
                    }
                });

        galleryResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // When gallery intent is launched, get data as bitmap and rescale to correct size for classification model
                        if(result.getResultCode() == RESULT_OK && result.getData()!=null) {
                            Bitmap img = null;
                            Intent data = result.getData();
                            Uri uri = data.getData();

                            try {
                                img = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            img = Bitmap.createScaledBitmap(img, imageDims, imageDims, false);
                            // Classify image using Classify utility class
                            try{
                                plantSpecies = Classify.classifyImage(img, getApplicationContext());
                            }catch(IOException e){
                                e.printStackTrace();
                                Toast.makeText(ClassifyActivity.this, "Classification Failed.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Open plant information activity for classified plant
                            openNewActivity();
                        }
                    }
                });
    }

    /**
     * Method to open activity to display plant information
     * Passes the plant name
     */
    public void openNewActivity(){
        Intent intent = new Intent (this, PlantInformationActivity.class);
        intent.putExtra("name",plantSpecies);
        this.startActivity(intent);
    }

}