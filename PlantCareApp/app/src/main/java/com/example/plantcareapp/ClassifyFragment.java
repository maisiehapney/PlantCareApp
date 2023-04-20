package com.example.plantcareapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.plantcareapp.ml.Plantmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Collections2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifyFragment extends Fragment {

    Button camera, gallery, information;
    ImageView imageView;
    TextView result, confidence;
    int imageSize = 224;
    private String species, percent;
    ActivityResultLauncher<Intent> galleryResultLaunch, cameraResultLaunch;
    FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseFirestore db;

    public ClassifyFragment(){}

    public static ClassifyFragment newInstance() {
        return new ClassifyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_classify,container,false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        camera = view.findViewById(R.id.photoButton);
        gallery = view.findViewById(R.id.uploadButton);
        //information = v.findViewById(R.id.information_button);

        //result = v.findViewById(R.id.result);
        //confidence = v.findViewById(R.id.confidence);
        imageView = view.findViewById(R.id.imageView);

        cameraResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == getActivity().RESULT_OK && result.getData()!=null) {
                            Bitmap image = (Bitmap) result.getData().getExtras().get("data");
                            int dimension = Math.min(image.getWidth(), image.getHeight());
                            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                            //imageView.setImageBitmap(image);

                            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                            //classifyImage(image);
                            Classify.classifyImage(image, getActivity().getApplicationContext());
                            species = Classify.result;
                            percent = Classify.accuracy;
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
                        if(result.getResultCode() == getActivity().RESULT_OK && result.getData()!=null) {
                            Intent data = result.getData();
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
                            species = Classify.result;
                            percent=Classify.accuracy;
                            uploadResult();
                            //result.setText(Classify.result);
                            openNewActivity();
                        }
                    }
                });

        // Inflate the layout for this fragment
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
        Intent intent = new Intent (getActivity(), PlantInformationActivity.class);
        int plantIndex=((HomeActivity) getActivity()).getNames().indexOf(species);
        Plant plant = ((HomeActivity) getActivity()).getPlantArrayList().get(plantIndex);
        intent.putExtra("name",plant.name);
        intent.putExtra("botanical",plant.botanicalName);
        intent.putExtra("temperature",plant.temperature);
        intent.putExtra("water",plant.water);
        intent.putExtra("sunlight",plant.sunlight);
        intent.putExtra("humidity",plant.humidity);
        intent.putExtra("url",plant.imageURL);
        intent.putExtra("confidence",percent);
        getActivity().startActivity(intent);
    }

    public void uploadResult(){
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("plant", species);
        data.put("date", FieldValue.serverTimestamp());

        db.collection("UserPlants")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}