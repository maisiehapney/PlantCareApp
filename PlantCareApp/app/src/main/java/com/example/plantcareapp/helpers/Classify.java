package com.example.plantcareapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.plantcareapp.constants.PlantConstants;
import com.example.plantcareapp.ml.Plantmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Classify {
    private final static int imageSize = 224;
    public static String result, accuracy;

    public static void classifyImage(Bitmap image, Context ctx){
        try {

            Plantmodel model = Plantmodel.newInstance(ctx);

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

            result = PlantConstants.getPlants().get(maxPos);
            getAccuracy(maxConfidence);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public static void getAccuracy(float percent){
        if(percent>=0.75){
            accuracy = "High";
        }else if(percent<0.75 && percent>=0.50){
            accuracy="Moderate";
        }
        else{
            accuracy="Low";
        }
    }


}
