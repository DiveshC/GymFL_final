package com.divesh.gymfl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.parseColor;
import static com.divesh.gymfl.MyAdapter.TAG;

public class GraphCards {
    private String[] data;
    private String mText;
    private String preferenceTitle;
    private LineGraphSeries series= new LineGraphSeries<>();
    private LineGraphSeries goalSeries= new LineGraphSeries<>();
    private String color;
    private String colorAccent;
    private Context mContext;
    private GraphView dataPlot;
    private FileOutputStream outputStream;

    //constructor
    GraphCards(String[] data, String Text, String preferenceTitle, Context context, String color, String colorAccent){
        this.data = data;
        this.mText = Text;
        this.preferenceTitle = preferenceTitle;
        this.mContext = context;
        this.colorAccent = colorAccent;
        this.color = color;
    }





    public void setData(String[] data){
        this.data=data;
    }
    public String getPreferenceTitle(){
        return this.preferenceTitle;
    }

    public String[] getData(){
        return this.data;
    }

    public String[] getX(){
        String[] x = new String[] {};
        if( data != null) {
            int len = this.data.length;
            x = new String[len];
            for (int i = 0; i < len; i++) {
                String[] dataxy = this.data[i].split(";");
                x[i] = dataxy[0];
            }
        }
        return x;
    }

    public int getMinValueY() {
        int[] array = getY();
        int minValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            }
        }
        return minValue;
    }

    public int getMaxValueY() {
        int[] array = getY();
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }

    public int[] getY(){
        int[] y = new int[] {};
        if(data != null ) {
            int len = this.data.length;
            y = new int[len];
            for (int i = 0; i < len; i++) {
                String[] dataxy = this.data[i].split(";");
                y[i] = Integer.parseInt(dataxy[1]);
            }
        }
        return y;
    }

    public String getText(){
        return this.mText;
    }

    //series

    public LineGraphSeries getSeries(){

        String[] xVal = getX();
        int[] yVal = getY();
        if(xVal != null) {
            for (int i = 0; i < xVal.length; i++) {
                try {
                    Date x = new SimpleDateFormat("dd/MM/yyyy").parse(xVal[i]);
                    int y = yVal[i];
                    this.series.appendData(new DataPoint(x, y), true, 100);
                } catch (Exception e) {

                }
            }
        }
        series.setColor(parseColor(color));
        series.setDataPointsRadius(10);
        series.setDrawDataPoints(true);
        series.setThickness(8);
        return this.series;
    }

    private DataPoint[] generateData() {
        String[] xVal = getX();
        int[] yVal = getY();
        DataPoint[] values = new DataPoint[xVal.length];
        DataPoint v;
        for (int i=0; i<xVal.length; i++) {
            try {
                Date x = new SimpleDateFormat("dd/MM/yyyy").parse(xVal[i]);
                int y = yVal[i];
                v = new DataPoint(x, y);
                values[i] = v;
            } catch (Exception e) {

            }
        }
        return values;
    }
    public LineGraphSeries getGoalSeries(){

        SharedPreferences goals = mContext.getSharedPreferences(preferenceTitle, Context.MODE_PRIVATE);
        int y = goals.getInt(mText,0);
        String[] cardData = getX();
        Date min;
        Date max;

//
        try {
            min = new SimpleDateFormat("dd/MM/yyyy").parse(cardData[0]);
            max = new SimpleDateFormat("dd/MM/yyyy").parse(cardData[cardData.length - 1]);
            goalSeries = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(min, y),
                    new DataPoint(max, y),});
        }catch(Exception e){

        }

        goalSeries.setColor(parseColor(colorAccent));
        goalSeries.setThickness(16);
        return goalSeries;
    }
    public DataPoint[] goalData(){

        SharedPreferences goals = mContext.getSharedPreferences(preferenceTitle, Context.MODE_PRIVATE);
        int y = goals.getInt(mText,0);
        DataPoint[] values = new DataPoint[2];
        DataPoint v;
        if(getX()!=null) {
            String[] cardData = getX();
            Date min;
            Date max;

//
            try {
                min = new SimpleDateFormat("dd/MM/yyyy").parse(cardData[0]);
                max = new SimpleDateFormat("dd/MM/yyyy").parse(cardData[cardData.length - 1]);
                values[0] = new DataPoint(min, y);
                values[1] = new DataPoint(max, y);
            } catch (Exception e) {

            }
        }

        return values;

    }
    public int getGoal(){
        SharedPreferences goals = mContext.getSharedPreferences(preferenceTitle, Context.MODE_PRIVATE);
        int y = goals.getInt(mText,0);
        return y;
    }
    public int getCurrentState(){
        int[] yVal = getY();
        int current = 0;
        if (yVal.length !=0) {
            current = yVal[yVal.length - 1];
        }
        return current;
    }

    public boolean getTrend(){
        int[] yVal = getY();
        int yLen = yVal.length;
        boolean trend = false;
        if(yLen >=2 ) {


            if (yVal[yLen - 1] >= yVal[yLen - 2]) {
                trend = true;
            }
        }
        return trend;
    }
    public String getUnits(){
        SharedPreferences units = mContext.getSharedPreferences("Goals", Context.MODE_PRIVATE);
        String preferedUnit = units.getString("Metric", "kgs");
        return preferedUnit;
    }
    public void resetSeries(){
        if(this.data != null) {
            this.series.resetData(generateData());
            this.goalSeries.resetData(goalData());
        }
    }

    //set up graphview
    public void setGraphview(GraphView dataPlot){
        this.dataPlot = dataPlot;
        dataPlot.addSeries(getSeries());
        dataPlot.addSeries(getGoalSeries());
    }


    public String[] removeLastPoint(){
        String newData[]=null;
        if(this.data != null) {
            if (this.data.length != 0) {
                int len = this.data.length;
                if (len == 1) {
                    //if there's only one point left just delete the whole file
                    File dir = mContext.getFilesDir();
                    File file = new File(dir, this.mText);
                    boolean deleted = file.delete();
                } else {
                    newData = new String[len - 1];
                    String replaceNew = "";

                    for (int i = 0; i < len - 1; i++) {
                        if (replaceNew.equals("")) {
                            replaceNew = this.data[i] + ",";
                        }
                        newData[i] = this.data[i];
                        replaceNew = replaceNew + this.data[i] + ",";
                    }
                    try {
                        outputStream = mContext.openFileOutput(this.mText, Context.MODE_PRIVATE);
                        outputStream.write(replaceNew.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return newData;
    }



}
