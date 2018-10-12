package com.divesh.gymfl;


import android.content.Intent;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class shuffle {
    public String[] routine_shuffle;
    public String[] routine_unshuffled;
    Random random = new Random();
    int randomNum;
    public void setshuffle(String[] routine){
        this.routine_shuffle= new String[routine.length];
        this.routine_unshuffled =routine;
    }
    public String[] getRoutine_shuffle(){
        int len = routine_shuffle.length;
        for (int i=0; i<len; i++){
           int len_new = routine_unshuffled.length;
           randomNum = random.nextInt(len_new);
           routine_shuffle[i]=routine_unshuffled[randomNum];
           routine_unshuffled = ArrayUtils.remove(routine_unshuffled,randomNum);
        }
        return routine_shuffle;
    }

}
