package com.example.plantcareapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantcareapp.ml.Plantmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClassifyFragment extends Fragment {

    Button camera, gallery, information;
    ImageView imageView;
    TextView result, confidence;
    int imageSize = 224;
    private String species, percent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_classify,container,false);

        camera = v.findViewById(R.id.button);
        gallery = v.findViewById(R.id.button2);
        //information = v.findViewById(R.id.information_button);

        result = v.findViewById(R.id.result);
        confidence = v.findViewById(R.id.confidence);
        imageView = v.findViewById(R.id.imageView);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 3);
                    }else{
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
        // Inflate the layout for this fragment

        return v;
    }

    public void openNewActivity(){
        Intent intent = new Intent (getActivity(), PlantInformationActivity.class);
        int plantIndex=((HomeActivity) getActivity()).getNames().indexOf(species);
        intent.putExtra("name",((HomeActivity) getActivity()).getNames().get(plantIndex));
        intent.putExtra("botanical",((HomeActivity) getActivity()).getBotanicalNames().get(plantIndex));
        intent.putExtra("temperature",((HomeActivity) getActivity()).getTemperature().get(plantIndex));
        intent.putExtra("water",((HomeActivity) getActivity()).getWater().get(plantIndex));
        intent.putExtra("sunlight",((HomeActivity) getActivity()).getSunlight().get(plantIndex));
        intent.putExtra("humidity",((HomeActivity) getActivity()).getHumidity().get(plantIndex));
        intent.putExtra("url",((HomeActivity) getActivity()).getImageURL().get(plantIndex));
        intent.putExtra("confidence",percent);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode==3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                //imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                //classifyImage(image);
                Classify.classifyImage(image, getActivity().getApplicationContext());
                species=Classify.result;
                percent = Classify.accuracy;
                openNewActivity();
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                //classifyImage(image);
                Classify.classifyImage(image, getActivity().getApplicationContext());
                species=Classify.result;
                //confidence.setText("Confidence: "+ Classify.accuracy);
                percent = Classify.accuracy;
                openNewActivity();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}