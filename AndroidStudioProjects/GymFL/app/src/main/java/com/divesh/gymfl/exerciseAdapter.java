package com.divesh.gymfl;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.divesh.gymfl.MyAdapter.TAG;

public class exerciseAdapter extends RecyclerView.Adapter<exerciseAdapter.ViewHolder> {
    private String[] list;
    private String routine;
    private Context mContext;
    private FileOutputStream outputStream;
    private String addString;
    //onclick
    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public exerciseAdapter(String[] list, String routine, Context context) {
        this.list = list;
        this.routine = routine;
        this.mContext = context;
    }

    public void setlist(String[] list){
        this.list=list;
    }

    @Override
    public exerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list,parent,false);
        exerciseAdapter.ViewHolder holder = new exerciseAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final exerciseAdapter.ViewHolder holder, final int position) {
        holder.mTextView.setText(list[position]);
        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(view, position);
                    String[] listNew = ArrayUtils.removeElement(list, list[position]);
                    addString = getString(listNew);
                    setlist(listNew);
//                    try{
//                        holder.mTextView.setText(list[position]);
//                    }catch(ArrayIndexOutOfBoundsException e){
//                        holder.mTextView.setText("");
//                        holder.mImageButton.setEnabled(false);
//                    }

                    Log.d(TAG, "added new string");
                    try {
                        outputStream = mContext.openFileOutput(routine, Context.MODE_PRIVATE);
                        outputStream.write(addString.getBytes());
                        Log.d(TAG, "onClick: File overwritten");
                        outputStream.close();


                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        ImageButton mImageButton;
        ConstraintLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.ExerciseText);
            mImageButton =itemView.findViewById(R.id.imageButton);
            parentLayout = itemView.findViewById(R.id.parent);
        }
    }

    //

    public void setOnItemClick(OnItemClickListener listener)
    {
        this.mListener=listener;
    }

    //String array to String
    public String getString(String[] array){
        String newString="";
        for(int i=0; i<array.length; i++){
            newString = newString +array[i]+",";
        }

        return newString;
    }
}
