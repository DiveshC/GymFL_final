package com.divesh.gymfl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.FileInputStream;
import java.util.Calendar;

public class exercisePlay extends AppCompatActivity implements fragment_home.sendQueue, fragment_queue.sendQueueHome{
    private static final String TAG ="exercisePLay";
    private SectionPageAdapter  mSectionPageAdapter;
    private ConstraintLayout parent;
    private TabLayout tabLayout;
    public String[] Scheduler;
    //general string array
    private String[] Legs;
    private String[] Chest;
    private String[] Back;
    private String[] Arms;
    private String[] Cardio;
    ViewPager mViewPager;

    private fragment_home mFragment_home;

    //service
    play_service mPlay_service;
    boolean mBound = false;
    public Intent serviceIntent;

    private exercise mExercise;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mExercise = new exercise(this);
        Scheduler = mExercise.getSchedule();
        setTheme();
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: test");
        setContentView(R.layout.activity_exercise_play);
        //tab view
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        exercise mExercise = new exercise(this);
        String plan = mExercise.getDay();

        tabLayout.getTabAt(2).setIcon(R.drawable.ic_sort_black_24dp);
        tabLayout.getTabAt(1).setText(plan);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_trending_up_black_24dp);

        mViewPager.setCurrentItem(1);





        parent = (ConstraintLayout) findViewById(R.id.parent);
        getSchedule(parent);

        //arrays of all exercises
        Legs = readfile("Legs");
        Chest = readfile("Chest");
        Back = readfile("Back");
        Arms = readfile("Arms");
        Cardio = readfile("Cardio");

        //start service
        serviceIntent = new Intent(this, play_service.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        serviceIntent.setAction(play_service.ACTION_START_FOREGROUND_SERVICE);
        serviceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startService(serviceIntent);



    }

    public String[] readfile(String filename){
        String[] addedGoals={};
        try {
            FileInputStream fileOut = this.openFileInput(filename);
            int c;
            String fileToString="";
            while( (c = fileOut.read()) != -1){
                if(Character.toString((char)c)!="") {
                    fileToString = fileToString + Character.toString((char) c);
                }
            }
            addedGoals = fileToString.split(",");
            fileOut.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return addedGoals;
    }
    public String getSchedule(ConstraintLayout parent){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;
        String plan="Break Day!";
        switch(Scheduler[day]){
            case "Legs":
                setTheme(R.style.LegTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient2));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.primarygradient));
                plan = "Legs";
                break;
            case "Chest":
                setTheme(R.style.ChestTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient3));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.redGradient));
                plan = "Chest";
                break;
            case "Back":
                setTheme(R.style.BackTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient6));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.greenGradient));
                plan = "Back";
                break;
            case "Arms":
                setTheme(R.style.ArmsTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient4));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.bluegradient));
                plan = "Arms";
                break;
            case "Cardio":
                setTheme(R.style.CardioTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient5));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.peachGradient));
                plan = "Cardio";
                break;
            case "Core":
                setTheme(R.style.CoreTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient7));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.orangegradient));
                plan = "Cardio";
                break;
            case "Sports":
                setTheme(R.style.SportsTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient8));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.purplegradient));
                plan = "Rest";
                break;
            case "Rest":
                setTheme(R.style.LegTheme);
                parent.setBackground(ContextCompat.getDrawable(this, R.drawable.gradient));
                plan = "Rest";
                break;
        }
        return plan;
    }

    public void setTheme(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;
        switch(Scheduler[day]){
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


    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        mFragment_home = new fragment_home();
        adapter.addFragment(new fragment_data());
        adapter.addFragment(mFragment_home);
        adapter.addFragment(new fragment_queue());

        viewPager.setAdapter(adapter);
    }

    @Override
    public void sendData(String[] message){
        String tag = "android:switcher:" + R.id.container + ":" +2;
        fragment_queue fragmentQue = (fragment_queue)getSupportFragmentManager().findFragmentByTag(tag);
        fragmentQue.displayReceivedData(message);


    }

    public void sendDataHome(String[] message){
        String tag = "android:switcher:" + R.id.container + ":" +1;
        fragment_home fragmentHome = (fragment_home) getSupportFragmentManager().findFragmentByTag(tag);
        fragmentHome.displayReceivedData(message);


    }

    //service

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            play_service.LocalBinder binder = (play_service.LocalBinder) service;
            mPlay_service = binder.getService();
            mFragment_home.serviceConnected(mPlay_service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };






    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        stopService(serviceIntent);
        mBound = false;
    }


}
