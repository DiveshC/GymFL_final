package com.divesh.gymfl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.OutputStream;


public class fragment_add_exercises extends Fragment {
    private Spinner muscleGroup;
    private EditText newExercise;
    private Button Enter;
    String[] workout_Options = {"Legs","Chest","Back","Arms","Cardio", "Core", "Sports"};
    private OutputStream outputStream;
    private Context context = getActivity();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_exercises, container, false);

        newExercise = view.findViewById(R.id.newExercise);
        muscleGroup = view.findViewById(R.id.muscleGroup);
        Enter = view.findViewById(R.id.Enter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item,workout_Options
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        muscleGroup.setAdapter(adapter);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SpinnerValue = muscleGroup.getSelectedItem().toString();
                if(SpinnerValue!="Rest"){
                    String addString = newExercise.getText().toString();
                    if(autoCorrect(addString, getActivity())==true){
                        Toast toast = Toast.makeText(getActivity(),"Avoid commas or empty entries", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 900);
                        toast.show();
                    } else if (distinction(addString, readfile(SpinnerValue), context) == true){
                        Toast toast = Toast.makeText(getActivity(), "This exercise already exists, think of something new!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 900);
                        toast.show();
                    } else {
                        try {
                            outputStream = getActivity().openFileOutput(SpinnerValue, Context.MODE_APPEND);
                            addString = addString + ",";
                            outputStream.write(addString.getBytes());
                            outputStream.close();
                            Toast toast = Toast.makeText(getActivity(), "Exercise added!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 900);
                            toast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        };
        Enter.setOnClickListener(listener);
        return view;
    }

    public boolean autoCorrect(String string, Context context){
        boolean bool = false;
        if(string.equals("") || string == null){
            bool = true;
        }

        for(int i=0; i < string.length(); i++){
            char stringChar =string.charAt(i);
            if(stringChar == ','){
                bool= true;
                break;
            }
        }
        return bool;
    }
    public boolean distinction(String string, String[] fileArray, Context context){
        boolean bool=false;
        for(int j =0; j<fileArray.length; j++){
            if(string.matches(fileArray[j])){
                bool = true;
                break;
            }
        }
        return bool;
    }

    public String[] readfile(String filename){
        String[] addedExercises={};
        try {
            FileInputStream fileOut = getActivity().openFileInput(filename);
            int c;
            String fileToString="";
            while( (c = fileOut.read()) != -1){
                if(Character.toString((char)c)!="") {
                    fileToString = fileToString + Character.toString((char) c);
                }
            }
            addedExercises = fileToString.split(",");
            fileOut.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return addedExercises;
    }
}
