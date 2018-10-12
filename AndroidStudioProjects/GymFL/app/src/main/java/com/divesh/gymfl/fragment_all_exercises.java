package com.divesh.gymfl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_all_exercises extends Fragment {
    private Button Legs;
    private Button Chest;
    private Button Back;
    private Button Arms;
    private Button Cardio;
    private Button Core;
    private Button Sports;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_exercises, container, false);

        Legs = view.findViewById(R.id.Legs);
        Chest = view.findViewById(R.id.Chest);
        Back = view.findViewById(R.id.Back);
        Arms = view.findViewById(R.id.Arms);
        Cardio = view.findViewById(R.id.Cardio);
        Core= view.findViewById(R.id.Core);
        Sports = view.findViewById(R.id.Sports);



        View.OnClickListener listenerLegs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Legs";
                startActivity(Value);

            }
        };

        View.OnClickListener listenerChest = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Chest";
                startActivity(Value);

            }
        };

        View.OnClickListener listenerBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Back";
                startActivity(Value);

            }
        };

        View.OnClickListener listenerArms = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Arms";
                startActivity(Value);

            }
        };

        View.OnClickListener listenerCardio = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Cardio";
                startActivity(Value);

            }
        };
        View.OnClickListener listenerCore = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Core";
                startActivity(Value);

            }
        };

        View.OnClickListener listenerSports = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Value = "Sports";
                startActivity(Value);

            }
        };


        Legs.setOnClickListener(listenerLegs);
        Chest.setOnClickListener(listenerChest);
        Back.setOnClickListener(listenerBack);
        Arms.setOnClickListener(listenerArms);
        Cardio.setOnClickListener(listenerCardio);
        Core.setOnClickListener(listenerCore);
        Sports.setOnClickListener(listenerSports);

        return view;
    }
    public void startActivity(String value){
        Intent intent = new Intent(getActivity(), ExerciseViewer.class);
        intent.putExtra("key", value);
        startActivity(intent);
    }




}
