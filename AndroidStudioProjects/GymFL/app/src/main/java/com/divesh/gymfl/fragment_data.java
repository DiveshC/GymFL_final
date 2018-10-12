package com.divesh.gymfl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.divesh.gymfl.GraphCardAdapter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class fragment_data extends Fragment {
    public static final String TAG ="fragment_data";
    public RecyclerView RVcards;
    public List<GraphCards> mGraphCards;
    public String[] Scheduler;
    public String[] graphData;
    //general string array
    private String[] Legs;
    private String[] Chest;
    private String[] Back;
    private String[] Arms;
    private String[] Cardio;
    public GraphCardAdapter adapter;

    //file values
    public String addString;
    public FileOutputStream outputStream;
    public exercise mExercise;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        RVcards = view.findViewById(R.id.RVcards);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        RVcards.setLayoutManager(llm);
        Resources res = getResources();


        mExercise = new exercise(getActivity());

        mGraphCards = mExercise.getCards();


        adapter = new GraphCardAdapter(mGraphCards, getActivity());
        RVcards.setAdapter(adapter);
        setOnItemListener();

        //prevent keyboard from popping up without user clicking edit text

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        return view;
    }

    public void setOnItemListener(){
        if(adapter!=null)
        {
            adapter.setOnItemClick(new GraphCardAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GraphCards clickedCard=mGraphCards.get(position);

                    //Now you can access [Form] data

                }
            });
        }
    }


}
