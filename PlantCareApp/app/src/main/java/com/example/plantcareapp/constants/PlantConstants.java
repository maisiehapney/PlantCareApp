package com.example.plantcareapp.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constant plant values used as the 10 classes for plant classification throughout application
 */
public class PlantConstants {

    private static List<String> plants;

    public static List<String> getPlants(){
        plants = Arrays.asList("Boston Fern", "Bunny Ear Cactus", "Chinese Money Plant", "Dragon Tree", "Jade Plant", "Orchid", "Peace Lily", "Peacock Plant",  "Rubber Plant", "Snake Plant");
        return plants;
    }
}
