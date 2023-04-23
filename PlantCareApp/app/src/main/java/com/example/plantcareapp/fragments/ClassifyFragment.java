package com.example.plantcareapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.plantcareapp.activities.HomeActivity;
import com.example.plantcareapp.models.Plant;
import com.example.plantcareapp.activities.PlantInformationActivity;
import com.example.plantcareapp.R;
import com.example.plantcareapp.utilities.Classify;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment for classifying plants for signed in users
 */
public class ClassifyFragment extends Fragment {

    private Button photoButton, uploadButton;
    private int imageDims = 224;
    private String plantSpecies, confidence;
    private ActivityResultLauncher<Intent> galleryResultLaunch, cameraResultLaunch;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    // Required public empty constructor
    public ClassifyFragment(){}

    /**
     * Create new instance of ClassifyFragment
     * @return a new instance of ClassifyFragment
     */
    public static ClassifyFragment newInstance() {
        return new ClassifyFragment();
    }

    /**
     * Executes when fragment is created
     * Assigns activity result contracts before view is created
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // When camera intent is launched, get data as bitmap and rescale to correct size for classification model
                        if (result.getResultCode() == getActivity().RESULT_OK && result.getData()!=null) {

                            Bitmap img = (Bitmap) result.getData().getExtras().get("data");
                            int dims = Math.min(img.getWidth(), img.getHeight());

                            img = ThumbnailUtils.extractThumbnail(img, dims, dims);
                            img = Bitmap.createScaledBitmap(img, imageDims, imageDims, false);

                            // Classify image using Classify utility class
                            try{
                                plantSpecies = Classify.classifyImage(img, getActivity().getApplicationContext());
                                confidence = Classify.getAccuracy();
                            }catch(IOException e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Classification Failed.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Upload result and open plant information activity for classified plant
                            uploadResult();
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
                        if(result.getResultCode() == getActivity().RESULT_OK && result.getData()!=null) {
                            Bitmap img = null;
                            Intent data = result.getData();
                            Uri uri = data.getData();

                            try {
                                img = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            img = Bitmap.createScaledBitmap(img, imageDims, imageDims, false);
                            // Classify image using Classify utility class
                            try{
                                plantSpecies = Classify.classifyImage(img, getActivity().getApplicationContext());
                                confidence = Classify.getAccuracy();
                            }catch(IOException e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Classification Failed.",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            // Upload result and open plant information activity for classified plant
                            uploadResult();
                            openNewActivity();
                        }
                    }
                });
    }

    /**
     * Inflate layout once fragment has been created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_classify,container,false);
        return v;
    }

    /**
     * Retrieves items and sets listeners once view has been created
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        photoButton = view.findViewById(R.id.photoButton);
        uploadButton = view.findViewById(R.id.uploadButton);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraResultLaunch.launch(cameraIntent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryResultLaunch.launch(galleryIntent);
            }
        });
    }

    /**
     * Method to open activity to display plant information
     * Passes all of the relevant plant care information which was retrieved from the database
     */
    public void openNewActivity(){
        Intent intent = new Intent (getActivity(), PlantInformationActivity.class);
        int plantIndex=((HomeActivity) getActivity()).getNames().indexOf(plantSpecies);
        Plant plant = ((HomeActivity) getActivity()).getPlantArrayList().get(plantIndex);
        intent.putExtra("name",plant.getName());
        intent.putExtra("botanical",plant.getBotanicalName());
        intent.putExtra("temperature",plant.getTemperature());
        intent.putExtra("water",plant.getWater());
        intent.putExtra("sunlight",plant.getSunlight());
        intent.putExtra("humidity",plant.getHumidity());
        intent.putExtra("url",plant.getImageURL());
        intent.putExtra("confidence",confidence);
        getActivity().startActivity(intent);
    }

    /**
     * Method to upload and save classification result to user's history
     * Firstly creates the data map before saving to database
     */
    public void uploadResult(){
        // Create map with user email, classified plant species, and current timestamp
        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("plant", plantSpecies);
        data.put("date", FieldValue.serverTimestamp());

        // Save data to UserPlants collection
        db.collection("UserPlants")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Saving data failed - display toast message.
                        Toast.makeText(getActivity(), "Error saving result.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}