package com.divesh.gymfl;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ItemTouchHelperCallBack.ItemTouchHelperAdapter {
    public static final String TAG ="RecyclerViewAdapter";
    private String[] mText;
    private Context mContext;
    private final List<String> mItems = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

//    public MyAdapter() {
//        mItems.addAll(Arrays.asList(mText));
//    }
    //constructor
    public MyAdapter(String[] text, Context context) {
        mItems.addAll(Arrays.asList(text));
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mTextView.setText(mItems.get(position));

//        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Log.d(TAG, "onClick:" +mText[position]);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }


    //elements
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        ConstraintLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textValue);
            parentLayout = itemView.findViewById(R.id.parent);
        }
    }
    public String[] getString(){
        mText = mItems.toArray(new String[0]);
        return mText;
    }
}
