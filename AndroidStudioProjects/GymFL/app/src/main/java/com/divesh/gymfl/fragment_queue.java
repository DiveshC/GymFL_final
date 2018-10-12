package com.divesh.gymfl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class fragment_queue extends Fragment {
    public static final String TAG = "fragment_queue";
    public Button buttonUpdate;
    public RecyclerView queueRecycler;
    public RecyclerView.LayoutManager mLayoutManager;
    public String[] nothing_in_queue = {"Nothing in queue", "start your workout!"};
    public MyAdapter mAdapter = new MyAdapter(nothing_in_queue, this.getActivity());
    public ItemTouchHelperCallBack callback = new ItemTouchHelperCallBack();
    public ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    private String[] Scheduler;
    private boolean bool;

    //EXERCISE OBJECT DECALARATION
    private exercise mExercise;


    //Interface to send string array between fragments
    fragment_queue.sendQueueHome mSendQueue;

    public interface sendQueueHome{
        public void sendDataHome(String[] message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            mSendQueue = (sendQueueHome) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_que,container,false);
        mExercise = new exercise(getActivity());
        queueRecycler = view.findViewById(R.id.queue_recycler);
        buttonUpdate = view.findViewById(R.id.updateButton);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        queueRecycler.setLayoutManager(mLayoutManager);
        queueRecycler.setAdapter(mAdapter);
        //setup drap and drop helper
        callback.setAdapter(mAdapter);
        touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(queueRecycler);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] newShuffled = mAdapter.getString();
                if((newShuffled.length!=0) ) {
                    if((!newShuffled[0].equals("Nothing in queue"))&&(!newShuffled[1].equals("start your workout!"))) {
                        mSendQueue.sendDataHome(newShuffled);
                        Toast toast = Toast.makeText(getActivity(), "Updated", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Cannot update, try starting your workout or shuffling again", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };
        buttonUpdate.setOnClickListener(listener);

        Scheduler = mExercise.getSchedule();
        setButtonColor();

        return view;
    }



    protected void displayReceivedData(String[] queueArray)
    {
        nothing_in_queue = queueArray;
        mAdapter = new MyAdapter(queueArray, this.getActivity());
        queueRecycler.setAdapter(mAdapter);
        //setup drapg and drop helper
        callback.setAdapter(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(queueRecycler);

    }

    public void setButtonColor(){
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK)-1;
        Resources mResources = getResources();
        switch(Scheduler[day]){
            case "Legs":
                break;
            case "Chest":
                buttonUpdate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.redbutton));
                break;
            case "Back":
                buttonUpdate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.greenbutton));
                break;
            case "Arms":
                buttonUpdate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bluebutton));
                break;
            case "Cardio":
                buttonUpdate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.peachbutton));
                break;
            case "Core":
                buttonUpdate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.orangebutton));
                break;
            case "Sports":
                buttonUpdate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.purplebutton));
                break;
            case "Rest":
                break;
        }
    }



}
