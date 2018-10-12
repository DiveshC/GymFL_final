package com.divesh.gymfl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class fragment_schedule extends Fragment {
    String[] Schedule={"Rest","Rest","Rest","Rest","Rest","Rest","Rest"};
    String[] workout_Options = {"Legs","Chest","Back","Arms","Cardio","Core", "Sports", "Rest"};
    private Spinner WorkoutOptions;
    private Spinner WorkoutOptions2;
    private Spinner WorkoutOptions3;
    private Spinner WorkoutOptions4;
    private Spinner WorkoutOptions5;
    private Spinner WorkoutOptions6;
    private Spinner WorkoutOptions7;
    private Button updater;
    private exercise mExercise;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        mExercise = new exercise(getActivity());
        updater= view.findViewById(R.id.update);
        WorkoutOptions = view.findViewById(R.id.routine_spinner);
        WorkoutOptions2 = view.findViewById(R.id.routine_spinner2);
        WorkoutOptions3 = view.findViewById(R.id.routine_spinner3);
        WorkoutOptions4 = view.findViewById(R.id.routine_spinner4);
        WorkoutOptions5 = view.findViewById(R.id.routine_spinner5);
        WorkoutOptions6 = view.findViewById(R.id.routine_spinner6);
        WorkoutOptions7 = view.findViewById(R.id.routine_spinner7);

        // Apply the adapter to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item,workout_Options
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        WorkoutOptions.setAdapter(adapter);
        WorkoutOptions2.setAdapter(adapter);
        WorkoutOptions3.setAdapter(adapter);
        WorkoutOptions4.setAdapter(adapter);
        WorkoutOptions5.setAdapter(adapter);
        WorkoutOptions6.setAdapter(adapter);
        WorkoutOptions7.setAdapter(adapter);

        String[] scheduler = mExercise.getSchedule();

        //setting default values of spinners based on shared preferences
        int array_position = adapter.getPosition(scheduler[0]);
        WorkoutOptions.setSelection(array_position);
        array_position = adapter.getPosition(scheduler[1]);
        WorkoutOptions2.setSelection(array_position);
        array_position = adapter.getPosition(scheduler[2]);
        WorkoutOptions3.setSelection(array_position);
        array_position = adapter.getPosition(scheduler[3]);
        WorkoutOptions4.setSelection(array_position);
        array_position = adapter.getPosition(scheduler[4]);
        WorkoutOptions5.setSelection(array_position);
        array_position = adapter.getPosition(scheduler[5]);
        WorkoutOptions6.setSelection(array_position);
        array_position = adapter.getPosition(scheduler[6]);
        WorkoutOptions7.setSelection(array_position);


        //when update button is clicked
        View.OnClickListener Listener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences shareRoutine = getActivity().getSharedPreferences("Routine", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shareRoutine.edit();
                editor.putString("Sunday",WorkoutOptions.getSelectedItem().toString());
                editor.putString("Monday",WorkoutOptions2.getSelectedItem().toString());
                editor.putString("Tuesday",WorkoutOptions3.getSelectedItem().toString());
                editor.putString("Wednesday", WorkoutOptions4.getSelectedItem().toString());
                editor.putString("Thursday",WorkoutOptions5.getSelectedItem().toString());
                editor.putString("Friday",WorkoutOptions6.getSelectedItem().toString());
                editor.putString("Saturday",WorkoutOptions7.getSelectedItem().toString());
                editor.apply();
                Toast toast = Toast.makeText(getActivity(), "Schedule Updated!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 900);
                toast.show();
            }
        };
        updater.setOnClickListener(Listener);

        return view;
    }
}
