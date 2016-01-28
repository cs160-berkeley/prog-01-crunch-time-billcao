package com.billcao.crunchtime;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Exercise {

    // Amount to burn 100 calories for 150lb person
    public static HashMap<String, Double> exerciseFactors = new HashMap<String, Double>() {
        {
            put("pushup", (double) 350);
            put("situp", (double) 200);
            put("squats", (double) 225);
            put("leg-lift", (double) 25);
            put("plank", (double) 25);
            put("jumping jacks", (double) 10);
            put("pullup", (double) 100);
            put("cycling", (double) 12);
            put("walking", (double) 20);
            put("jogging", (double) 12);
            put("swimming", (double) 13);
            put("stair-climbing", (double) 15);
        }
    };

    public static double caloriesBurned(String exercise, double repsOrMinutes) {
        double caloriesBurned = (repsOrMinutes / exerciseFactors.get(exercise)) * 100;
        return caloriesBurned;
    }

    public static HashMap<String, Double> convertExercise(String exercise, double repsOrMinutes) {
        HashMap<String, Double> convertedExercises = new HashMap<String, Double>();
        Set exerciseSet = exerciseFactors.entrySet();
        Iterator exerciseIterator = exerciseSet.iterator();
        while (exerciseIterator.hasNext()) {
            Map.Entry factor = (Map.Entry) exerciseIterator.next();
            String exerciseKey = (String) factor.getKey();
            double exerciseValue = (double) factor.getValue();
            double conversionFactor = repsOrMinutes / exerciseFactors.get(exercise);
            double equivalentRepsOrMinutes = conversionFactor * exerciseValue;

            // TODO: Don't add in if exerciseKey == exercise
            convertedExercises.put(exerciseKey, equivalentRepsOrMinutes);
        }

        return convertedExercises;
    }

}
