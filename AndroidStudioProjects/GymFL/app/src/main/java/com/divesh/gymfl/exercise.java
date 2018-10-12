package com.divesh.gymfl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by drchu on 2018-02-18.
 */


public class exercise {
    private static final String TAG = "Exercise_class" ;
    private Calendar now = Calendar.getInstance();
    private int day = now.get(Calendar.DAY_OF_WEEK)-1;
    private Context mContext;
    //create a merged array of added exercises and exercise resources
    private String[] Legs;
    private String[] Chest;
    private String[] Back;
    private String[] Arms;
    private String[] Cardio;
    private String[] Core;
    private String[] Sports;
    private List<GraphCards> mGraphCards;
    private String[] schedule;
    //constructor
    exercise(Context context){
        this.mContext = context;
        //sharedpreferences

        SharedPreferences shareRoutine = mContext.getSharedPreferences("Routine", MODE_PRIVATE);
        String Sunday=shareRoutine.getString("Sunday","Legs");
        String Monday=shareRoutine.getString("Monday", "Arms");
        String Tuesday=shareRoutine.getString("Tuesday","Rest");
        String Wednesday=shareRoutine.getString("Wednesday", "Back");
        String Thursday=shareRoutine.getString("Thursday", "Rest");
        String Friday=shareRoutine.getString("Friday","Arms");
        String Saturday=shareRoutine.getString("Saturday","Rest");
        schedule = new String[]{Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday};

        //create a merged array of added exercises and exercise resources
        Legs = readfile("Legs");
        Chest = readfile("Chest");
        Back = readfile("Back");
        Arms = readfile("Arms");
        Cardio = readfile("Cardio");
        Core = readfile("Core");
        Sports = readfile("Sports");
    }





    public String[] getSchedule(){
        return schedule;
    }

    public String getDay(){
        return schedule[day];
    }

    public String getRoutine(){

        String plan="Break Day!";
        switch(schedule[day]){
            case "Legs":
                plan = "Leg Day! Have fun!";
                break;
            case "Chest":
                plan = "Chest Day everday";
                break;
            case "Back":
                plan = "Get ready for a killer Back Day!";
                break;
            case "Arms":
                plan = "Arms";
                break;
            case "Cardio":
                plan = "Cardio ... not my personal best but ok.";
                break;
            case "Core":
                plan = "Core for the win";
                break;
            case "Sports":
                plan = "Athletic eh? Play the sport You want";
                break;
            case "Rest":
                plan = "U can relax for now....";
                break;
        }
        return plan;
    }

    public Bitmap getBitmap(){
        Resources mResources = mContext.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(
                mResources,
                R.drawable.rest_square
        );
        switch(schedule[day]){
            case "Legs":
                //get bitmap image

                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.legs_poster_square
                );
                break;
            case "Chest":
                //get bitmap image

                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.chest_poster_square
                );
                break;
            case "Back":
                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.back_poster_square
                );
                break;
            case "Arms":
                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.biceps_poster_square
                );
                break;
            case "Cardio":
                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.heart_poster_square
                );
                break;

            case "Core":
                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.core_square
                );
                break;
            case "Sports":
                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.sports_square
                );
                break;
            case "Rest":
                bitmap = BitmapFactory.decodeResource(
                        mResources,
                        R.drawable.rest_square
                );
                break;
        }
        return bitmap;
    }

    public void createCards(String[] a, String b){
        String primary ="#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        String accent ="#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorAccent));
        mGraphCards.add(new GraphCards(readfile("Weight"), "Weight", "Goals",mContext,  primary, accent));
        if(a!=null) {
            SharedPreferences preferences = mContext.getSharedPreferences(b, MODE_PRIVATE);
            for (int i = 0; i < a.length; i++) {
                int value = preferences.getInt(a[i], 0);
                if(value != 0) {
                    try {
                        //add a graph card with file data
                        String[] graphData = readfile(a[i]);
                        mGraphCards.add(new GraphCards(graphData, a[i], b,mContext, getThemebySchedule(), getThemeAccentbySchedule()));

                    } catch (Exception e) {
                        Log.d(TAG, "null file");

                    }
                }
            }
        }

    }

    public List<GraphCards> getCards(){
        mGraphCards = new ArrayList<>();
        switch(schedule[day]){
            case "Legs":
                createCards(Legs, "Legs");
                break;
            case "Chest":
                createCards(Chest, "Chest" );
                break;
            case "Back":
                createCards(Back, "Back" );
                break;
            case "Arms":
                createCards(Arms, "Arms");
                break;
            case "Cardio":
                createCards(Cardio, "Cardio");
                break;
            case "Core":
                createCards(Core, "Core");
                break;
            case "Sports":
                createCards(Sports, "Sports");
                break;
            case "Rest":
                createCards(null, null);
                break;
        }
        return mGraphCards;
    }
    public String getThemeColour(String exercise){
        String colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        switch(exercise){
            case "Legs":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                break;
            case "Chest":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.red));
                break;
            case "Back":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.greenDarker));
                break;
            case "Arms":

                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.blueVariant));
                break;
            case "Cardio":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                break;
            case "Core":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.red));
                break;
            case "Sports":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.purpledarker));
                break;
            case "Rest":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.green));
                break;
        }

        return colour;
    }

    public String[] getExercise(String stringarray){
        String[] exercise_array = {"Select a routine"};
        switch (stringarray){
            case "Legs":
                exercise_array = Legs;
                break;
            case "Chest":
                exercise_array= Chest;
                break;
            case "Back":
                exercise_array = Back;
                break;
            case "Arms":
                exercise_array = Arms;
                break;
            case "Cardio":
                exercise_array = Cardio;
                break;
            case "Core":
                exercise_array =  Core;
                break;
            case "Sports":
                exercise_array = Sports;
                break;
        }
        return exercise_array;
    }

    // takes file and removes each exercise and places them in a string array
    public String[] readfile(String filename){
        String[] addedExercises={};
        try {
            FileInputStream fileOut = mContext.openFileInput(filename);
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

    //Returns random exercises
    public String[] getShuffled(){

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;
        String[] shuffled={};
        String[] restDay = {"cool it"};
        shuffle shuffler = new shuffle();
        //shuffle exercises
        switch(schedule[day]){
            case "Legs":
                shuffler.setshuffle(Legs);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Chest":
                shuffler.setshuffle(Chest);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Back":
                shuffler.setshuffle(Back);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Arms":
                shuffler.setshuffle(Arms);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Cardio":
                shuffler.setshuffle(Cardio);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Core":
                shuffler.setshuffle(Core);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Sports":
                shuffler.setshuffle(Sports);
                shuffled = shuffler.getRoutine_shuffle();
                break;
            case "Rest":
                shuffled = restDay;
        }
        return shuffled;
    }


    //Returns theme color based on your schedule
    public String getThemebySchedule(){

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;

        //shuffle exercises
        String colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        switch(schedule[day]){
            case "Legs":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                break;
            case "Chest":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.red));
                break;
            case "Back":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.teal));
                break;
            case "Arms":

                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.Blue));
                break;
            case "Cardio":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                break;
            case "Core":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.red));
                break;
            case "Sports":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.purple));
                break;
            case "Rest":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.green));
                break;
        }
        return colour;
    }

    public String getThemeAccentbySchedule(){

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;

        //shuffle exercises
        String colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorAccent));
        switch(schedule[day]){
            case "Legs":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorAccent));
                break;
            case "Chest":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.redGradient));
                break;
            case "Back":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.greenGradient));
                break;
            case "Arms":

                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.bluegradient));
                break;
            case "Cardio":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.peach));
                break;
            case "Core":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.peachGradient));
                break;
            case "Sports":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.purplegradient));
                break;
            case "Rest":
                colour = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.teal));
                break;
        }
        return colour;
    }

    //function to add two string arrays
    public static String[] add(String[] a, String[] b){
        String[] newStr=new String[a.length+b.length];
        System.arraycopy(a, 0,newStr, 0, a.length);
        System.arraycopy(b, 0, newStr, a.length, b.length);
        return newStr;
    }
}
