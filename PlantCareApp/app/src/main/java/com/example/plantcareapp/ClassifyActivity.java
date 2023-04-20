package com.example.plantcareapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.plantcareapp.ml.Model;
import com.example.plantcareapp.ml.Plantmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClassifyActivity extends AppCompatActivity {

    Button camera, gallery, register;
    ImageView imageView;
    TextView result;
    int imageSize = 224;
    private String plant;
    ActivityResultLauncher<Intent> galleryResultLaunch, cameraResultLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        cameraResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                            Bitmap image = (Bitmap) result.getData().getExtras().get("data");
                            int dimension = Math.min(image.getWidth(), image.getHeight());
                            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                            imageView.setImageBitmap(image);

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
                            imageView.setImageBitmap(image);

                            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                            //classifyImage(image);
                            Classify.classifyImage(image, getApplicationContext());
                            plant = Classify.result;
                            //result.setText(Classify.result);
                            openNewActivity();
                        }
                    }
                });

        camera = findViewById(R.id.photoButton);
        gallery = findViewById(R.id.uploadButton);
        //register = findViewById(R.id.register_button);

        //result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraResultLaunch.launch(cameraIntent);

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryResultLaunch.launch(galleryIntent);

            }
        });
    }

    public void openNewActivity(){
        Intent intent = new Intent (this, PlantInformationActivity.class);
        intent.putExtra("name",plant);
        this.startActivity(intent);
    }

}