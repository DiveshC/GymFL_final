package com.divesh.gymfl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class fragment_navigation extends Fragment {
    public Button schedule;
    public Button gCalendar;
    public Button goals;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        schedule = view.findViewById(R.id.schedule);
        gCalendar = view.findViewById(R.id.calendar);
        goals = view.findViewById(R.id.goals);

        View.OnClickListener scheduleListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoutinePlanner)getActivity()).setViewPager(1);

            }
        };

        View.OnClickListener calendarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoutinePlanner)getActivity()).setViewPager(2);

            }
        };

        View.OnClickListener goalsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RoutinePlanner)getActivity()).setViewPager(3);
            }
        };

        schedule.setOnClickListener(scheduleListener);
        gCalendar.setOnClickListener(calendarListener);
        goals.setOnClickListener(goalsListener);


        return view;
    }
}
