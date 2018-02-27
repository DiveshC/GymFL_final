package com.divesh.gymfl;

import android.content.res.Resources;
import android.widget.ArrayAdapter;

import java.util.Date;
import java.util.Random;
import java.util.logging.Handler;

import static android.provider.Settings.Global.getString;

/**
 * Created by drchu on 2018-02-18.
 */


public class exercise {

    private String[] chest = {
            "Bench Press",
            "Inclined Bench Press",
            "Machine Press",
            "Inclined dumbbell press",
            "Dips",
            "Pec machine"};
    private String[] back = {
            "Lat pull downs",
            "Barbell Deadlift",
            "Bent-Over Barbell Deadlift",
            "Wide-Grip Pull-ups",
            "Standing T-bar Row",
            "Cable Row"};
    private String[] arms = {
            "Standing Curls",
            "Spider Curls",
            "Overhead Cable curls",
            "Tricep Pull downs",
            "Skull crushers",
            "Dumbbell Shoulder Press",
            "Side Delt Raises",
            "Military Press"};
    private String[] legs = {
            "Barbell Squats",
            "Leg Press",
            "Calf Raises",
            "Lunges",
            "Leg Extensions",
            "Romanian Deadlift",
            "Standing Calf raises",
            "Lying leg raises"};
    private Date now = new Date();
    public String getExercise(){
        int day = now.getDay();
        String routine="nothing";
        Random randomGenerator = new Random();
        int randomNumber;
        switch(day){
            case 0:
                randomNumber= randomGenerator.nextInt(legs.length);
                routine = legs[randomNumber];
                break;
            case 1:
                randomNumber= randomGenerator.nextInt(chest.length);
                routine = chest[randomNumber];
                break;
            case 3:
                randomNumber= randomGenerator.nextInt(back.length);
                routine = back[randomNumber];
                break;
            case 5:
                randomNumber= randomGenerator.nextInt(arms.length);
                routine = arms[randomNumber];
                break;
        }
        return routine;
    }
    public String getSchedule(){
        int day = now.getDay();
        String plan="Break Day!";
            switch (day) {
                case 0:
                    plan = "Leg Day! Have Fun!";
                    break;
                case 1:
                    plan = "Chest Day!Flex those Pecs!";
                    break;
                case 3:
                    plan = "Back Day.";
                    break;
                case 5:
                    plan = "Arms for Today. Curls for girls eh?";
                    break;
            }
        return plan;
    }
}
