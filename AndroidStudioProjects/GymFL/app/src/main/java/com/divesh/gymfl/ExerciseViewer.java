package com.divesh.gymfl;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.Calendar;

public class ExerciseViewer extends AppCompatActivity {
    public String TAG = "ExerciseViewer";
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private exerciseAdapter mAdapter;
    private TextView ExerciseGroup;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    //string arrays for exercise
    private String[] Legs;
    private String[] Chest;
    private String[] Back;
    private String[] Arms;
    private String[] Cardio;
    private String[] Scheduler;
    private String routine;
    private ImageView appBarImage;
    private exercise mExercise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mExercise = new exercise(this);
        Scheduler = mExercise.getSchedule();
        Bundle value = getIntent().getExtras();
        if(value!=null){
            routine = value.getString("key");
        }
        setTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercise_viewer);


        mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        mCollapsingToolbarLayout.setTitle(routine);
        appBarImage = (ImageView) findViewById(R.id.app_bar_image);


        //getting all exercise value; legs, back, chest etc...
        Resources res = getResources();



        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(this);


        setupAdapter();

        setOnItemListener();







    }

    //Item on click
    public void setOnItemListener(){
        if(mAdapter!=null)
        {
            mAdapter.setOnItemClick(new exerciseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    //Now you can access [Form] data
                    onStart();
                }
            });
        }
    }

    private void openScheduler() {
        Intent intent = new Intent(this, RoutinePlanner.class);
        startActivity(intent);
    }

    //start add exercise activity

    private void openHome(){
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }

    public String[] readfile(String filename){
        String[] addedExercises={};
        try {
            FileInputStream fileOut = this.openFileInput(filename);
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
    //function to add two string arrays
    public static String[] add(String[] a, String[] b){
        String[] newStr=new String[a.length+b.length];
        System.arraycopy(a, 0,newStr, 0, a.length);
        System.arraycopy(b, 0, newStr, a.length, b.length);
        return newStr;
    }

    public void setupAdapter(){
        //routine value is base on what the "all exercise" fragment sends
        switch (routine) {
            case "Legs":
                mAdapter = new exerciseAdapter(readfile("Legs"),"Legs", this);
                appBarImage.setImageResource(R.drawable.legs);
                break;
            case "Chest":
                mAdapter = new exerciseAdapter(readfile("Chest"), "Chest", this);
                appBarImage.setImageResource(R.drawable.chest);
                break;
            case "Back":
                mAdapter = new exerciseAdapter(readfile("Back"), "Back", this);
                appBarImage.setImageResource(R.drawable.back);
                break;
            case "Arms":
                mAdapter = new exerciseAdapter(readfile("Arms"), "Arms", this);
                appBarImage.setImageResource(R.drawable.biceps);
                break;
            case "Cardio":
                mAdapter = new exerciseAdapter(readfile("Cardio"), "Cardio", this);
                appBarImage.setImageResource(R.drawable.heart);
                break;
            case "Core":
                mAdapter = new exerciseAdapter(readfile("Core"), "Core", this);
                appBarImage.setImageResource(R.drawable.core_poster);
                break;
            case "Sports":
                mAdapter = new exerciseAdapter(readfile("Sports"), "Sports", this);
                appBarImage.setImageResource(R.drawable.sports_poster);
                break;
        }
    }
    public void setTheme(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;
        switch(routine){
            case "Legs":
                setTheme(R.style.LegTheme);
                break;
            case "Chest":
                setTheme(R.style.ChestTheme);
                break;
            case "Back":
                setTheme(R.style.BackTheme);
                break;
            case "Arms":
                setTheme(R.style.ArmsTheme);
                break;
            case "Cardio":
                setTheme(R.style.CardioTheme);
                break;
            case "Core":
                setTheme(R.style.CoreTheme);
                break;
            case "Sports":
                setTheme(R.style.SportsTheme);
                break;
            case "Rest":
                setTheme(R.style.AppTheme);
                break;
        }
    }
    @Override
    public void onStart() {

        super.onStart();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(true);
    }
}
