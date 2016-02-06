package com.billcao.crunchtime;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        final AutoCompleteTextView exercisesView = (AutoCompleteTextView) findViewById(R.id.exercise_type);
        final String[] exercises = Exercise.exerciseFactors.keySet().toArray(new String[Exercise.exerciseFactors.keySet().size()]);
        Arrays.sort(exercises);
        final ArrayAdapter<String> exercisesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exercises);
        exercisesView.setAdapter(exercisesAdapter);
        final TextView caloriesBurnedMessage = (TextView) findViewById(R.id.calories_burned);

        final ArrayList<String> exerciseArrayList = new ArrayList<String>(Exercise.exerciseFactors.keySet());
        Collections.sort(exerciseArrayList);
        final ArrayAdapter<String> theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exerciseArrayList);
        final ListView exerciseList = (ListView) findViewById(R.id.exercise_list);
        exerciseList.setAdapter(theAdapter);
        final TextView exerciseMessage = (TextView) findViewById(R.id.exerciseMessage);
        Button convertButton = (Button) findViewById(R.id.convert_button);
        final EditText numRepsView = (EditText) findViewById(R.id.number_reps);
        exerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exerciseString = String.valueOf(parent.getItemAtPosition(position));
                String exercisePicked = "You selected " + exerciseString;
                String[] exerciseStringArray = exerciseString.split(" ");
                if (exerciseStringArray.length > 2) {
                    String duration = exerciseStringArray[exerciseStringArray.length - 2];
                    numRepsView.setText(duration);
                }

                exercisesView.setText(exercises[position]);

                Toast.makeText(MainActivity.this, exercisePicked, Toast.LENGTH_SHORT).show();
            }
        });


        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset exerciseArrayList
                for (int i = 0; i < exercises.length; i++) {
                    String e = exercises[i];
                    exerciseArrayList.set(i, e);
                }

                String exercise = exercisesView.getText().toString().toLowerCase();
                try {
                    double numRepsOrMinutes = Double.parseDouble(numRepsView.getText().toString());
                    double calorieDisplay = Exercise.caloriesBurned(exercise, numRepsOrMinutes);

                    caloriesBurnedMessage.setText("You have burned " + df.format(calorieDisplay) + " calories, or " + df.format(calorieDisplay/154) + " beers!");
                    exerciseMessage.setText("Which is equivalent to:");

                    HashMap<String, Double> convertedExercises = Exercise.convertExercise(exercise, numRepsOrMinutes);

                    for (int i = 0; i < exerciseArrayList.size(); i++) {
                        String e = exerciseArrayList.get(i);
                        exerciseArrayList.set(i, capitalize(e) + " for " + df.format(convertedExercises.get(e)) + " " + Exercise.exerciseLabels.get(e));
                    }

                    theAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    if (!Arrays.asList(exercises).contains(exercise)) {
                        Toast.makeText(MainActivity.this, "Unsupported exercise, please choose one from the list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid minutes/reps, champ", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
