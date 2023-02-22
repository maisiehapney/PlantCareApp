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
    TextView result;
    int imageSize = 224;
    private String species;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_classify,container,false);

        camera = v.findViewById(R.id.button);
        gallery = v.findViewById(R.id.button2);
        information = v.findViewById(R.id.information_button);

        result = v.findViewById(R.id.result);
        imageView = v.findViewById(R.id.imageView);

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(), PlantInformationActivity.class);
                int plantIndex=((HomeActivity) getActivity()).getNames().indexOf(species);
                intent.putExtra("name",((HomeActivity) getActivity()).getNames().get(plantIndex));
                intent.putExtra("botanical",((HomeActivity) getActivity()).getBotanicalNames().get(plantIndex));
                intent.putExtra("temperature",((HomeActivity) getActivity()).getTemperature().get(plantIndex));
                intent.putExtra("water",((HomeActivity) getActivity()).getWater().get(plantIndex));
                intent.putExtra("sunlight",((HomeActivity) getActivity()).getSunlight().get(plantIndex));
                intent.putExtra("humidity",((HomeActivity) getActivity()).getHumidity().get(plantIndex));
                intent.putExtra("url",((HomeActivity) getActivity()).getImageURL().get(plantIndex));
                getActivity().startActivity(intent);

            }
        });

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

    public void classifyImage(Bitmap image){
        try {
            //Model model = Model.newInstance(getApplicationContext());
            Plantmodel model = Plantmodel.newInstance(getActivity().getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize *imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;

            for (int i = 0; i<imageSize; i++){
                for (int j=0; j<imageSize; j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }


            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Plantmodel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float [] confidences = outputFeature0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String [] classes = {"Boston Fern", "Bunny Ear Cactus", "Chinese Money Plant", "Dragon Tree", "Jade Plant", "Orchid", "Peace Lily", "Peacock Plant",  "Rubber Plant", "Snake Plant"};
            result.setText(classes[maxPos]);

            species = classes[maxPos];
            //int plantIndex=((HomeActivity) getActivity()).getNames().indexOf(classes[maxPos]);
            //Log.d("tag", ((HomeActivity) getActivity()).getInfo().get(plantIndex));

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode==3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}