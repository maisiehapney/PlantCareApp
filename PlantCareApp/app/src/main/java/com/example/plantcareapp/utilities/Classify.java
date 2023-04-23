package com.example.plantcareapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.plantcareapp.constants.PlantConstants;
import com.example.plantcareapp.ml.Plantmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility class for classifying images across application
 */
public class Classify {
    private final static int imageDims = 224;
    public static float confidencePercent;

    // Required private constructor for utilities
    private Classify(){}

    /**
     * Method which classifies a plant image using the TensorFlow Lite model
     * @param img bitmap image to classify
     * @param ctx context
     * @return String classification result
     */
    public static String classifyImage(Bitmap img, Context ctx) throws IOException{
        try {
            String result;
            Plantmodel model = Plantmodel.newInstance(ctx);

            // Creates inputs for reference
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

            // Byte buffer to store pixel values of bitmap
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * imageDims * imageDims * 3);
            buffer.order(ByteOrder.nativeOrder());

            // Get the RGB values individually of each pixel and add to byte buffer
            int [] pixelValues;
            pixelValues= new int[imageDims*imageDims];
            img.getPixels(pixelValues, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
            int pixelNumber = 0;
            for (int i = 0; i<imageDims; i++){
                for (int j=0; j<imageDims; j++){
                    int currentPixel = pixelValues[pixelNumber++];
                    // R value
                    buffer.putFloat(((currentPixel >> 16) & 0xFF) * (1.f));
                    //G value
                    buffer.putFloat(((currentPixel >> 8) & 0xFF) * (1.f));
                    //B value
                    buffer.putFloat((currentPixel & 0xFF) * (1.f));
                }
            }

            // Load byte buffer into TensorBuffer input feature
            inputFeature0.loadBuffer(buffer);

            // Runs model and gets result
            Plantmodel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Find classification result through index of highest confidence score
            float [] confidenceArray = outputFeature0.getFloatArray();
            float highestConfidence = 0;
            int plantIndex = 0;
            for (int i = 0; i < confidenceArray.length; i++) {
                if (confidenceArray[i] > highestConfidence) {
                    highestConfidence = confidenceArray[i];
                    plantIndex = i;
                }
            }

            // Get final result and confidence score
            result = PlantConstants.getPlants().get(plantIndex);
            confidencePercent = highestConfidence;

            // Close model resources and return result
            model.close();
            return result;

        } catch (IOException e) {
            // Handle exception
            throw new IOException("Failed to classify image", e);
        }
    }

    /**
     * Method to calculate accuracy of classification
     * @return String accuracy of classification result
     */
    public static String getAccuracy(){
        String accuracy;
        if(confidencePercent>=0.75){
            accuracy = "High";
        }else if(confidencePercent<0.75 && confidencePercent>=0.50){
            accuracy ="Moderate";
        }
        else{
            accuracy ="Low";
        }
        return accuracy;
    }

}
