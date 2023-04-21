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
import android.widget.ImageView;
import android.widget.TextView;
import com.example.plantcareapp.R;
import com.example.plantcareapp.helpers.Classify;

import java.io.IOException;

public class ClassifyActivity extends AppCompatActivity {

    private Button camera, gallery;
    private int imageSize = 224;
    private String plant;
    private ActivityResultLauncher<Intent> galleryResultLaunch, cameraResultLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        camera = findViewById(R.id.photoButton);
        gallery = findViewById(R.id.uploadButton);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraResultLaunch.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
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
                        if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                            Bitmap image = (Bitmap) result.getData().getExtras().get("data");
                            int dimension = Math.min(image.getWidth(), image.getHeight());
                            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                            //classifyImage(image);
                            Classify.classifyImage(image, getApplicationContext());
                            plant = Classify.result;
                            //result.setText(Classify.result);
                            openNewActivity();
                        }
                    }
                });

        galleryResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK && result.getData()!=null) {
                            Intent data = result.getData();
                            Uri dat = data.getData();
                            Bitmap image = null;
                            try {
                                image = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), dat);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                            Classify.classifyImage(image, getApplicationContext());
                            plant = Classify.result;
                            openNewActivity();
                        }
                    }
                });

    }

    public void openNewActivity(){
        Intent intent = new Intent (this, PlantInformationActivity.class);
        intent.putExtra("name",plant);
        this.startActivity(intent);
    }

}