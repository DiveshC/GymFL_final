package com.divesh.gymfl;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

/**
 * Created by drchu on 2018-02-24.
 */

public class colours {
    //theme related colours
    public String[] colours={"#ffffff","#ffe6f2","#ffb3d9","#ff66b3","#ff0080","#cc0066","#99004d"};
    //vibrant colours
//    public String[] colours={
//            "#ff0000",
//            "#ff9933",
//            "#ccff66",
//            "#009933",
//            "#00ffcc",
//            "#66ccff",
//            "#6666ff",
//            "#ff3399",
//            "#990033",
//            "#66ff33",
//            "#009999",
//            "#660066"};
    public String[] coloursShuffled = colours;
    public int desired_len;
    public colours(int x){
        desired_len = x;
    }
    Random random = new Random();
    int randomNum;

    public String[] getColour(){
        int len = colours.length;
        for (int i=0; i<len; i++){
            int len_new = colours.length;
            randomNum = random.nextInt(len_new);
            coloursShuffled[i]=colours[randomNum];
            colours = ArrayUtils.removeElement(colours,colours[randomNum]);
        }
        if (desired_len>len){
            int excess = desired_len-len;
            String[] extension = new String[excess];
            for (int j=0; j<excess;j++){
                extension[j] = coloursShuffled[j];
            }
            coloursShuffled = ArrayUtils.addAll(coloursShuffled, extension);
        }
        return coloursShuffled;
    }
}
