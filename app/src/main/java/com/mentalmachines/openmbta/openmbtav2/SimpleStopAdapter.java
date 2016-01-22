package com.mentalmachines.openmbta.openmbtav2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by emezias on 1/20/16.
 * This is a simple adapter to fill in the stop,
 * scheduled time and estimated next arrival
 */

public class SimpleStopAdapter extends RecyclerView.Adapter<SimpleStopAdapter.StopViewHolder> {
    public static final String TAG = "SimpleStopAdapter";
    String[] mItems;

    public SimpleStopAdapter(String[] data) {
        super();
        mItems = data;
    }

    @Override
    public int getItemCount() {
        if(mItems == null) {
            return 0;
        } else {
            return mItems.length;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.t_stop, parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StopViewHolder holder, int position) {
        if(mItems == null || mItems.length < position) {
            Log.w(TAG, "bad position sent to adapter " + position);
            holder.mStopTiming.setText("");
        } else {
            holder.mStopTiming.setText(mItems[position]
                    + "\n7:30pm");
            holder.mETA.setText("Now");
        }
        if(position == 3) {
            holder.mImage.setImageResource(android.R.drawable.ic_dialog_alert);
        }
    }

    public class StopViewHolder extends RecyclerView.ViewHolder {
        public final TextView mStopTiming;
        public final TextView mETA;
        public final ImageView mImage;

        public StopViewHolder(View itemView) {
            super(itemView);
            mStopTiming = (TextView) itemView.findViewById(R.id.stop_desc);
            mETA = (TextView) itemView.findViewById(R.id.stop_eta);
            mImage = (ImageView) itemView.findViewById(R.id.stop_img);
        }
    }

}
