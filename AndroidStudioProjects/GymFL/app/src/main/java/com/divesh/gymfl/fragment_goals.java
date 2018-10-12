package com.divesh.gymfl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import static com.divesh.gymfl.MyAdapter.TAG;

public class fragment_goals extends Fragment {
    String[] workout_Options = {"Legs","Chest","Back","Arms","Core"};
    public Button enterWeight;
    public Button liftWeightButton;
    public Button setMetric;
    public Button enterSets;
    public Spinner weightMetric;
    public EditText weight;
    public EditText liftWeight;
    public EditText setsGoal;
    private Spinner routine;
    private Spinner exerciseSpinner;
    private TextView mTextView;
    public String selectedRoutine;
    public String[] exerciselist;
    public String addString;
    public FileOutputStream outputStream;
    private exercise mExercise;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        mExercise = new exercise(getActivity());
        //buttons
        liftWeightButton = view.findViewById(R.id.setWeightButton);
        enterWeight = view.findViewById(R.id.setWeight);
        weight = view.findViewById(R.id.weight);
        setMetric = view.findViewById(R.id.setter);
        enterSets = view.findViewById(R.id.enterSet);
        //spinners
        routine = view.findViewById(R.id.spinnerRoutine);
        exerciseSpinner = view.findViewById(R.id.spinnerExercise);
        weightMetric = view.findViewById(R.id.weightMetric);

        //textviews
        mTextView = view.findViewById(R.id.textView5);
        liftWeight = view.findViewById(R.id.exerciseWeight);

        //edittext
        setsGoal = view.findViewById(R.id.sets);

        //setting body weight goal

        View.OnClickListener weightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String weightGoal = weight.getText().toString();
                    int goalint = Integer.parseInt(weightGoal);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Goals", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Weight", goalint);
                    editor.apply();
                    try {
                        //first date data point
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        addString = dateFormat.format(date)+";0,";
                        Toast toast = Toast.makeText(getActivity(), "Your goal is set", Toast.LENGTH_LONG);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e){
                    Toast toast = Toast.makeText(getActivity(), "Try inputing a real number!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };
        enterWeight.setOnClickListener(weightListener);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item,workout_Options
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        routine.setAdapter(adapter);


        // adding goals for a specific exercise//


        routine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //first check which routine is selected to minimize the number of exercises in the last spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoutine = routine.getSelectedItem().toString();

                exerciselist = mExercise.getExercise(selectedRoutine);
                ArrayAdapter<String> adapterExercises = new ArrayAdapter<String>(
                        getActivity(),R.layout.spinner_item,exerciselist
                );
                //find the exercises related to the first spinners value
                adapterExercises.setDropDownViewResource(R.layout.spinner_item);
                exerciseSpinner.setAdapter(adapterExercises);


                //set value when button is clicked for the exercise
                View.OnClickListener exerciseListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            String exercise = exerciseSpinner.getSelectedItem().toString();
                            String goal = liftWeight.getText().toString();
                            int goalNum = Integer.parseInt(goal);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(routine.getSelectedItem().toString(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //edit shared preference value
                            editor.putInt(exercise, goalNum);
                            editor.apply();
                            //create exercise data file
                            try {
                                //first date data point
                                Calendar calendar = Calendar.getInstance();
                                Date date = calendar.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                addString = dateFormat.format(date)+";0,";
                                File file = new File(getActivity().getFilesDir(), exercise);
                                Toast toast = Toast.makeText(getActivity(), "Your goal is set", Toast.LENGTH_LONG);
                                toast.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }catch(NumberFormatException e){
                            Toast toast = Toast.makeText(getActivity(), "try using numbers", Toast.LENGTH_LONG);
                            toast.show();
                        }



                    }

                };
                liftWeightButton.setOnClickListener(exerciseListener);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Setting units either in pounds or kilos

        String[] units = {"lbs","kgs"};
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_item,units
        );
        weightAdapter.setDropDownViewResource(R.layout.spinner_item);
        weightMetric.setAdapter(weightAdapter);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = weightMetric.getSelectedItem().toString();
                SharedPreferences preferences = getActivity().getSharedPreferences("Goals", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Metric", value);
                editor.apply();
                Toast toast = Toast.makeText(getActivity(), "Units updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
        setMetric.setOnClickListener(listener);


        // Setting the number of "Sets" to perform during a workout session

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value  = Integer.parseInt(setsGoal.getText().toString());
                SharedPreferences preferences = getActivity().getSharedPreferences("Goals" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("setsValue", value);
                editor.apply();
                Toast toast = Toast.makeText(getActivity(), "Sets goal updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        enterSets.setOnClickListener(listener2);

        return view;
    }


    //function to add two string arrays
    public static String[] add(String[] a, String[] b){
        String[] newStr=new String[a.length+b.length];
        System.arraycopy(a, 0,newStr, 0, a.length);
        System.arraycopy(b, 0, newStr, a.length, b.length);
        return newStr;
    }
    //get string of exersise

}
