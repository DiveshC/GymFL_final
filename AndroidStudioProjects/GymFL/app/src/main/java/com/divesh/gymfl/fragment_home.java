package com.divesh.gymfl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.support.v4.media.app.NotificationCompat.MediaStyle;


import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.parseColor;

public class fragment_home extends Fragment {
    private static final String TAG ="fragment_home";
    private TextView schedule;
    private TextView exerciseText;

    //buttons
    private ImageButton playButton;
    private ImageButton shuffleButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private ImageButton pauseButton;
    private ImageButton removeSetButton;
    private int counter;
    public String[] Routine;
    private String[] Scheduler;

    //general string array
    private String[] Legs;
    private String[] Chest;
    private String[] Back;
    private String[] Arms;
    private String[] Cardio;

    //shuffled string array
    private String[] shuffled;

    //Graph variables
    private GraphView mGraphView;
    private ImageButton AddData;
    private int[] xValues={};
    private int[] yValues={};
    private int x;
    private int y;
    private String[] colour;
    PointsGraphSeries<DataPoint> series;
    Calendar now = Calendar.getInstance();
    final int initial =  now.get(Calendar.MINUTE);
    final int initialHour = now.get(Calendar.HOUR_OF_DAY);
    //progress bar
    private ProgressBar mProgressBar;
    private int setCounter = 0;
    private TextView mTextView;

    //tabs
    private SectionPageAdapter  mSectionPageAdapter;
    private ViewPager mViewPager;
    //chronometer
    private Chronometer mChronometer;
    private boolean running;
    private int click = 0;
    //service
    play_service mPlay_service;
    boolean mBound = false;
    public Intent serviceIntent;
    public Boolean mShouldUnbind;

    //goal of number of sets
    public int maxSet;
    public String[] week = {"Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"};

    //exercise object
    private exercise mExercise;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    Context context;
    FileOutputStream outputStream;

    //Interface to send string array between fragments
    sendQueue mSendQueue;

    public interface sendQueue{
        public void sendData(String[] message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            mSendQueue = (sendQueue) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view  = inflater.inflate(R.layout.fragment_home, container, false);

        //exercise object

        mExercise = new exercise(getActivity());
        Scheduler = mExercise.getSchedule();
        //String resources being accessed
        Resources res = getResources();
        String[] options = res.getStringArray(R.array.workoutOptions);
        Routine = res.getStringArray(R.array.key_schedule);

        context =getActivity();



        //text views from main activity

        schedule = view.findViewById(R.id.sample_text);
        exerciseText = view.findViewById(R.id.Exercise);
        mTextView = view.findViewById(R.id.textProgress);

        //buttons

        shuffleButton = view.findViewById(R.id.shuffleButton);
        previousButton = view.findViewById(R.id.previousButton);
        playButton = view.findViewById(R.id.playButton);
        nextButton = view.findViewById(R.id.nextButton);
        AddData = view.findViewById(R.id.completeSetButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        removeSetButton = view.findViewById(R.id.remove);
        setButtonColor();

        // disable buttons
        shuffleButton.setEnabled(false);
        AddData.setEnabled(false);
        nextButton.setVisibility(View.GONE);
        previousButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.GONE);

        //Graphview from mainactivity
        mProgressBar = view.findViewById(R.id.progressSets);

        //chronometer
        mChronometer = view.findViewById(R.id.chronometer);
        running = false;
        if (!running) {
            mChronometer.start();
            running = true;
        }

        //retrieve previously set set count if the workout is being resumed from earlier..
//        SharedPreferences weekPerformance = getActivity().getSharedPreferences("WeekPerformance", MODE_PRIVATE);
//        Calendar now = Calendar.getInstance();
//        int day = now.get(Calendar.DAY_OF_WEEK)-1;
//        setCounter = weekPerformance.getInt(week[day],0);
        //start each workout at 0

        setCounter = 0;

        //unique colour object, changes with each exercise

        //Using key_schedule resource to output routine for the current date
        schedule.setText(mExercise.getRoutine());
        exerciseText.setText("Start your workout");
        exerciseText.setTextColor(parseColor("#ffffff"));


        //On click listener for switching exercises
        View.OnClickListener Listener = new View.OnClickListener() {
            public void onClick(View v) {
                shuffled = mExercise.getShuffled();
                mPlay_service.setRoutine(shuffled);
                mPlay_service.updateNotification();
                colours newColour = new colours(shuffled.length);
                colour = newColour.getColour();
                mSendQueue.sendData(shuffled);
                exerciseText.setText(shuffled[0]);
            }
        };
        counter=0;


        //when going to next exercise
        View.OnClickListener ListenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter + 1;
                if (counter>=shuffled.length){
                    counter=0;
                }
                //set text view to next exercise in que
                if(shuffled != null) {
                    exerciseText.setText(shuffled[counter]);

                    mPlay_service.setCounter(counter);
                    mPlay_service.updateNotification();
                }
            }
        };


        //when going to previous exercise
        View.OnClickListener ListenerPrevious = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter - 1;
                if (counter<0){
                    counter=shuffled.length-1;
                }
                //set text view to next exercise in que
                exerciseText.setText(shuffled[counter]);

                mPlay_service.setCounter(counter);
                mPlay_service.updateNotification();
            }
        };


        //play button performs same function as shuffle button
        View.OnClickListener ListenerPlay = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if clicked more than once...if so then dont re shuffle, just resume...
                click = click + 1;

                //enable buttons
                shuffleButton.setEnabled(true);
                previousButton.setEnabled(true);
                nextButton.setEnabled(true);
                AddData.setEnabled(true);

                //HIDE PLAY and SHOW PAUSE
                playButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                if(click<=1) {
                    // First time being clicked
                    //start clock if not already running
                    if (!running) {
                        mChronometer.start();
                        running = true;
                    }
                    //start service

                    shuffled = mPlay_service.getRoutine();
                    counter = mPlay_service.getCounter();

                    //start exercises
                    colours newColour = new colours(shuffled.length);
                    colour = newColour.getColour();
                    mSendQueue.sendData(shuffled);
                    exerciseText.setText(shuffled[counter]);
                    exerciseText.setTextColor(parseColor(getColor()[1]));




                }else{
                    //start clock if not already running
                    if (!running) {
                        mChronometer.start();
                        running = true;
                    }
                }
            }
        };

        View.OnClickListener listenerPause = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HIDE Pause and SHOW Play
                playButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                previousButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);

                //pause clock if not already running
                if(running){
                    mChronometer.stop();
                    running=false;
                }
            }
        };

        //add to set count
        View.OnClickListener listenerAdd = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCounter = setCounter +1;
                mPlay_service.setSetCounter(setCounter);
                SharedPreferences goals = getActivity().getSharedPreferences("Goals", MODE_PRIVATE);
                maxSet = goals.getInt("setsValue", 20);
                int progress = setCounter *100 /maxSet;
                Log.d(TAG, "onClick: "+progress);
                mProgressBar.setProgress(progress);
                mTextView.setText("Set: " + setCounter +"/"+maxSet);
            }
        };

        //reduce to set count
        View.OnClickListener listenerRemove = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setCounter > 0 ) {
                    setCounter = setCounter - 1;
                }
                mPlay_service.setSetCounter(setCounter);
                SharedPreferences goals = getActivity().getSharedPreferences("Goals", MODE_PRIVATE);
                maxSet = goals.getInt("setsValue", 20);
                int progress = setCounter *100 /maxSet;
                Log.d(TAG, "onClick: "+progress);
                mProgressBar.setProgress(progress);
                mTextView.setText("Set: " + setCounter +"/"+maxSet);
            }
        };
        shuffleButton.setOnClickListener(Listener);
        nextButton.setOnClickListener(ListenerNext);
        previousButton.setOnClickListener(ListenerPrevious);
        pauseButton.setOnClickListener(listenerPause);
        playButton.setOnClickListener(ListenerPlay);
        AddData.setOnClickListener(listenerAdd);
        removeSetButton.setOnClickListener(listenerRemove);



        return view;
    }

    public void displayReceivedData(String[] message){
        shuffled = message;
        int len = shuffled.length;
        if(mPlay_service != null) {
            mPlay_service.setRoutine(message);
            mPlay_service.updateNotification();
            if(counter >= len) {
                counter = len -1;
                mPlay_service.setCounter(counter);
            }
        }
        
        

        exerciseText.setText(shuffled[counter]);
    }








    public void setButtonColor(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;
        Resources mResources = getResources();
        switch(Scheduler[day]){
            case "Legs":
                break;
            case "Chest":
                playButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redbutton));
                pauseButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redbutton));
                break;
            case "Back":
                playButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.greenbutton));
                pauseButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.greenbutton));
                break;
            case "Arms":
                playButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bluebutton));
                pauseButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bluebutton));
                break;
            case "Cardio":
                playButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.peachbutton));
                pauseButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.peachbutton));
                break;
            case "Core":
                playButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.orangebutton));
                pauseButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.orangebutton));
                break;
            case "Sports":
                playButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.purplebutton));
                pauseButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.purplebutton));
                break;
            case "Rest":
                break;
        }
    }

    public String[] getColor(){
         Calendar now = Calendar.getInstance();
         int day = now.get(Calendar.DAY_OF_WEEK)-1;
         Resources mResources = getResources();
         String color = "#ff3399";
         String color2="481331";

         switch(Scheduler[day]){
             case "Legs":
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.primarygradient));
                 color2 ="#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                 break;
             case "Chest":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.red));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.redGradient));
                 break;
             case "Back":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.green));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.greenGradient));
                 break;
             case "Arms":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.Blue));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.bluegradient));
                 break;
             case "Cardio":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.peach));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.peachGradient));
                 break;
             case "Core":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.orange));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.orangegradient));
                 break;
             case "Sports":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.purple));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.purplegradient));
                 break;
             case "Rest":
                 color2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                 color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.gradientStart));
                 break;
         }
         String[] colors = new String[] {color,color2};
        return colors;
     }


    //function to add two string arrays
    public static String[] add(String[] a, String[] b){
        String[] newStr=new String[a.length+b.length];
        System.arraycopy(a, 0,newStr, 0, a.length);
        System.arraycopy(b, 0, newStr, a.length, b.length);
        return newStr;
    }

    //life cycle functions

    @Override
    public void onStart() {
        super.onStart();


        //when activity is started update text view with the changes from notification if any have been made
        if(mPlay_service != null) {
            if(shuffled!=null) {
                String exerciseUpdate = shuffled[mPlay_service.getCounter()];
                exerciseText.setText(exerciseUpdate);
                counter = mPlay_service.getCounter();
                shuffled = mPlay_service.getRoutine();
                setCounter = mPlay_service.getSetCounter();
            }else{
                shuffled = mPlay_service.getRoutine();
                counter = mPlay_service.getCounter();
                String exerciseUpdate = shuffled[mPlay_service.getCounter()];
                exerciseText.setText(exerciseUpdate);
                counter = mPlay_service.getCounter();
                shuffled = mPlay_service.getRoutine();
                setCounter = mPlay_service.getSetCounter();
            }
            mPlay_service.updateNotification();

        }

        SharedPreferences goals = getActivity().getSharedPreferences("Goals", MODE_PRIVATE);
        maxSet = goals.getInt("setsValue", 20);
        int progress = setCounter *100 /maxSet;
        mProgressBar.setProgress(progress);
        mTextView.setText("Set: " + setCounter +"/"+maxSet);
    }

    @Override
    public void onStop(){
        super.onStop();
        // save the number of sets completed on specific date to show progress on home screen
        Calendar calendar = Calendar.getInstance();
        int day  = calendar.get(Calendar.DAY_OF_WEEK);
        SharedPreferences preferences = getActivity().getSharedPreferences("WeekPerformance", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        switch (day){
            case 1:
                editor.putInt("Sunday", setCounter);
                editor.apply();

                break;
            case 2:
                editor.putInt("Monday", setCounter);
                editor.apply();

                break;
            case 3:
                editor.putInt("Tuesday", setCounter);
                editor.apply();

                break;
            case 4:
                editor.putInt("Wednesday", setCounter);
                editor.apply();

                break;
            case 5:
                editor.putInt("Thursday", setCounter);
                editor.apply();
                break;
            case 6:
                editor.putInt("Friday", setCounter);
                editor.apply();
                break;
            case 7:
                editor.putInt("Saturday", setCounter);
                editor.apply();
                break;
        }


    }

   public void serviceConnected(play_service mPlay_service) {
        this.mPlay_service = mPlay_service;
        // ...
    }










    /** Defines callbacks for service binding, passed to bindService() */


}
