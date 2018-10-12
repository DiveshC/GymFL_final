package com.divesh.gymfl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.parseColor;
import static com.divesh.gymfl.MyAdapter.TAG;

public class GraphCardAdapter extends RecyclerView.Adapter<GraphCardAdapter.ViewHolder> {
    public  String value;
    public String TitleVal;
    public Context mContext;
    public String addString;
    public FileOutputStream outputStream;
    private exercise mExercise;
    private String color;


    //onclick
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> goalSeries;
    public List<GraphCards> mGraphCards;
    public GraphCardAdapter(List<GraphCards> graphCards, Context context){
        this.mGraphCards = graphCards;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        GraphCardAdapter.ViewHolder holder = new GraphCardAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GraphCardAdapter.ViewHolder holder, final int position) {
        final GraphCards graphCard = mGraphCards.get(position);
        mExercise = new exercise(mContext);
        graphCard.setGraphview(holder.dataPlot);

        //set text fields
        holder.Title.setText(graphCard.getText());
        String goalString = "Goal: "+graphCard.getGoal()+ " " + graphCard.getUnits();
        String currentState = "Current: " + graphCard.getCurrentState() + " " +graphCard.getUnits();
        holder.goal.setText(goalString);
        holder.status.setText(currentState);

        //imageviews
        holder.up.setVisibility(View.GONE);
        holder.down.setVisibility(View.GONE);
        if(graphCard.getTrend()){
            holder.up.setVisibility(View.VISIBLE);
        } else {
            holder.down.setVisibility(View.VISIBLE);
        }


        if(graphCard.getText().equals("Weight")) {
            color = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }else{
            color = mExercise.getThemebySchedule();
        }
        holder.Title.setTextColor(parseColor(color));
        String colorgrid = "#" +Integer.toHexString(ContextCompat.getColor(mContext, R.color.cardview_dark_background));
        holder.dataPlot.getGridLabelRenderer().setHorizontalLabelsColor(parseColor(color));
        holder.dataPlot.getGridLabelRenderer().setVerticalLabelsColor(parseColor(color));
        holder.dataPlot.getGridLabelRenderer().setGridColor(parseColor(colorgrid));
        holder.dataPlot.getGridLabelRenderer().setHighlightZeroLines(false);
        holder.dataPlot.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        holder.dataPlot.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        holder.dataPlot.getGridLabelRenderer().reloadStyles();
        // set date label formatter
        holder.dataPlot.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(mContext));
        holder.dataPlot.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        holder.dataPlot.getViewport().setScalable(true);



        // add data listener
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null)
                {
                    mListener.onItemClick(view,position);
                    value = holder.submitVal.getText().toString();
                    TitleVal = graphCard.getText();
                    boolean isInt;
                    //make sure value is not empty string
                    if(value!="") {
                        //check if input is string based or integer
                        try {
                            int valint = Integer.parseInt(value);
                            isInt = true;
                        } catch (NumberFormatException e) {
                            isInt = false;
                        }
                        if (isInt) {
                            //protect against io exception
                            try {
                                //x value based on current date, y-value based on input
                                Calendar calendar = Calendar.getInstance();
                                Date date = calendar.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                addString = dateFormat.format(date)+";"+value+",";
                                outputStream = mContext.openFileOutput(TitleVal, Context.MODE_APPEND);
                                outputStream.write(addString.getBytes());

                                if(Integer.parseInt(value) >= getGoal(graphCard)){
                                    Toast toast =Toast.makeText(mContext, "You met your Goal!! Set your next target", Toast.LENGTH_LONG);
                                    toast.show();
                                }else {
                                    Toast toast =Toast.makeText(mContext, "Added data. Scroll through to view changes.", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                outputStream.close();


                            } catch (Exception e) {

                            }

                            graphCard.setData(readfile(TitleVal));
                            graphCard.resetSeries();
                            graphCard.setGraphview(holder.dataPlot);
                        } else {
                            Toast toast = Toast.makeText(mContext, "Try numbers", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        // replot new data
                        if(graphCard.getText().equals("Weight")) {
                            color = "#" + Integer.toHexString(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                        }else{
                            color = mExercise.getThemebySchedule();
                        }
                        String colorgrid = "#" +Integer.toHexString(ContextCompat.getColor(mContext, R.color.cardview_dark_background));









//
//

                    }
                }
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, RoutinePlanner.class);
//                intent.putExtra("key", 3);
//                mContext.startActivity(intent);
                //hide graph and trend views
                holder.goal.setVisibility(View.GONE);
                holder.status.setVisibility(View.GONE);
                holder.up.setVisibility(View.GONE);
                holder.down.setVisibility(View.GONE);
                holder.dataPlot.setVisibility(View.GONE);
                holder.update.setVisibility(View.GONE);
                holder.remove.setVisibility(View.GONE);
                //make edit fields visible
                holder.backToMain.setVisibility(View.VISIBLE);
                holder.log.setVisibility(View.VISIBLE);
                holder.submitVal.setVisibility(View.VISIBLE);
                holder.submit.setVisibility(View.VISIBLE);
                holder.goalText.setVisibility(View.VISIBLE);
                holder.submitGoal.setVisibility(View.VISIBLE);
                holder.submitGoalButton.setVisibility(View.VISIBLE);
                holder.removeDataPoint.setVisibility(View.VISIBLE);

            }
        });
        //when back button is clicked show the graphview and status view
        holder.backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SHOW graph and trend views
                holder.goal.setVisibility(View.VISIBLE);
                holder.status.setVisibility(View.VISIBLE);
                if(graphCard.getTrend()){
                    holder.up.setVisibility(View.VISIBLE);
                } else {
                    holder.down.setVisibility(View.VISIBLE);
                }
                holder.dataPlot.setVisibility(View.VISIBLE);
                holder.update.setVisibility(View.VISIBLE);
                holder.remove.setVisibility(View.VISIBLE);
                //make edit fields visible
                holder.backToMain.setVisibility(View.GONE);
                holder.log.setVisibility(View.GONE);
                holder.submitVal.setVisibility(View.GONE);
                holder.submit.setVisibility(View.GONE);
                holder.goalText.setVisibility(View.GONE);
                holder.submitGoal.setVisibility(View.GONE);
                holder.submitGoalButton.setVisibility(View.GONE);
                holder.removeDataPoint.setVisibility(View.GONE);
            }
        });
        //when update goal button is clicked save the new shared preference
        holder.submitGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int newGoal = Integer.parseInt(holder.submitGoal.getText().toString());
                    TitleVal = holder.Title.getText().toString();
                    SharedPreferences preferences = mContext.getSharedPreferences(graphCard.getPreferenceTitle(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(graphCard.getText(), newGoal);
                    editor.apply();
                    Toast toast = Toast.makeText(mContext, "Updated Goal", Toast.LENGTH_LONG);
                    toast.show();
                }catch (NumberFormatException e){
                    Toast toast = Toast.makeText(mContext, "Try real numbers!", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        //hides card from view, does not actual remove the preference ...
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(graphCard.getText().equals("Weight")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.confirmation3)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    TitleVal = holder.Title.getText().toString();
                                    SharedPreferences preferences = mContext.getSharedPreferences(graphCard.getPreferenceTitle(), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(graphCard.getText(), 0);
                                    editor.apply();
                                    //clear file data

                                    File dir = mContext.getFilesDir();
                                    File file = new File(dir, TitleVal);
                                    boolean deleted = file.delete();
                                    Toast toast = Toast.makeText(mContext, "See changes upon restarting", Toast.LENGTH_LONG);
                                    toast.show();

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog

                                }
                            }).show();
                    // Create the AlertDialog object and return it
                    builder.create();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.confirmation)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    TitleVal = holder.Title.getText().toString();
                                    SharedPreferences preferences = mContext.getSharedPreferences(graphCard.getPreferenceTitle(), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt(graphCard.getText(), 0);
                                    editor.apply();
                                    //clear file data

                                    File dir = mContext.getFilesDir();
                                    File file = new File(dir, TitleVal);
                                    boolean deleted = file.delete();
                                    Toast toast = Toast.makeText(mContext, "See changes upon restarting", Toast.LENGTH_LONG);
                                    toast.show();

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog

                                }
                            }).show();
                    // Create the AlertDialog object and return it
                    builder.create();

                }



            }
        });
        //removes last added point from graph and files
        holder.removeDataPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.confirmation2)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] removed = graphCard.removeLastPoint();
                                graphCard.setData(removed);
                                graphCard.resetSeries();
                                graphCard.setGraphview(holder.dataPlot);


                                Toast toast = Toast.makeText(mContext, "Datapoint deleted", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        }).show();
                // Create the AlertDialog object and return it
                builder.create();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGraphCards.size();
    }

    public String getEditText(){
        return value;
    }
    public String getTitle(){
        return TitleVal;
    }

    //creating objects

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Title;
        public TextView goal;
        public TextView status;
        public TextView log;
        public TextView goalText;
        public EditText submitVal;
        public EditText submitGoal;
        public GraphView dataPlot;
        public Button submit;
        public Button submitGoalButton;
        public Button backToMain;
        public Button update;
        public Button remove;
        public Button removeDataPoint;
        public ConstraintLayout card;
        public ImageView up;
        public ImageView down;
        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.title);
            submitVal = itemView.findViewById(R.id.editText);
            dataPlot = itemView.findViewById(R.id.graphView);
            submit = itemView.findViewById(R.id.submit);
            card = itemView.findViewById(R.id.Layout_card);
            update = itemView.findViewById(R.id.button);
            remove = itemView.findViewById(R.id.button2);
            goal = itemView.findViewById(R.id.goal);
            status = itemView.findViewById(R.id.status);
            up = itemView.findViewById(R.id.trendUp);
            down = itemView.findViewById(R.id.trendDown);
            submitGoal = itemView.findViewById(R.id.editText2);
            submitGoalButton = itemView.findViewById(R.id.submit2);
            backToMain = itemView.findViewById(R.id.button3);
            log = itemView.findViewById(R.id.textView4);
            goalText = itemView.findViewById(R.id.updateGoal);
            removeDataPoint = itemView.findViewById(R.id.removeLast);
        }
    }



    //graph creator




    public int getGoal(GraphCards dataCard){
        SharedPreferences goals = mContext.getSharedPreferences(dataCard.getPreferenceTitle(), Context.MODE_PRIVATE);
        int y = goals.getInt(dataCard.getText(),0);
        return y;
    }
    public void setOnItemClick(OnItemClickListener listener)
    {
        this.mListener=listener;
    }

    public String[] readfile(String filename){
        String[] addedGoals={};
        try {
            FileInputStream fileOut = mContext.openFileInput(filename);
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
}
