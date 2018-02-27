package com.divesh.gymfl;

import java.util.Random;

/**
 * Created by drchu on 2018-02-24.
 */

public class colours {
    //theme related colours
    String[] colours={"#ffffff","#ffe6f2","#ffb3d9","#ff66b3","#ff0080","#cc0066","#99004d"};
    //vibrant colours
    /*String[] colours={
            "#ff0000",
            "#ff9933",
            "#ccff66",
            "#009933",
            "#00ffcc",
            "#66ccff",
            "#6666ff",
            "#ff3399",
            "#990033",
            "#66ff33",
            "#009999",
            "#660066"};*/
    public String getColour(){
        Random randomGenerator = new Random();
        int randomNum;
        randomNum= randomGenerator.nextInt(colours.length);
        return colours[randomNum];
    }
}
