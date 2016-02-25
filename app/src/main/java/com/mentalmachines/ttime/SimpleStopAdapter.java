package com.mentalmachines.ttime;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by emezias on 1/20/16.
 * This is a simple adapter to fill in the stop,
 * scheduled time and estimated next arrival
 */

public class SimpleStopAdapter extends RecyclerView.Adapter<SimpleStopAdapter.StopViewHolder> {
    public static final String TAG = "SimpleStopAdapter";
    final public StopData[] mItems;
    final int mTextColor;

    //public SimpleStopAdapter(String[] data, int resource) {
    public SimpleStopAdapter(StopData[] data, int textColor) {
        super();
        mItems = data;
        mTextColor = textColor;
        //drawableResource = resource = -1;
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
        if(mTextColor > 0) {
            ((TextView) view.findViewById(R.id.stop_desc)).setTextColor(mTextColor);
        }

        return new StopViewHolder(view);
    }

    /**
     * Map a stop in mItems to a recycler view entry on the list
     * The mStopDescription view has a tag holding the parent view group
     * @param holder, viewholder, required for recycler view
     * @param position, the position in the list
     */
    @Override
    public void onBindViewHolder(final StopViewHolder holder, int position) {
        if(mItems == null || mItems.length < position) {
            Log.w(TAG, "bad position sent to adapter " + position);
            holder.mStopDescription.setText("");
        } else {
            //setting a tag on the parent view for the two buttons to read
            ((View) holder.mStopDescription.getTag()).setTag(mItems[position]);
            holder.mStopDescription.setText(mItems[position].stopName);
            if(mItems[position].stopAlert != null) {
                holder.mAlertBtn.setVisibility(View.VISIBLE);
                holder.mAlertBtn.setTag(mItems[position].stopAlert);
            } else {
                holder.mAlertBtn.setVisibility(View.GONE);
            }
            //holder.mCompass.setTag(mItems[position]);
                   // TODO disappear this if there is no predictive data for the route
            holder.mETA.setText("Actual time: ?, in ? minutes and ? in ? minutes");
        }
    }

    public class StopViewHolder extends RecyclerView.ViewHolder {
        public final TextView mStopDescription;
        public final TextView mETA;
        public final ImageButton mAlertBtn;
        public final View mCompass;

        public StopViewHolder(View itemView) {
            super(itemView);
            mStopDescription = (TextView) itemView.findViewById(R.id.stop_desc);
            mETA = (TextView) itemView.findViewById(R.id.stop_eta);
            mCompass = itemView.findViewById(R.id.stop_mapbtn);
            mAlertBtn = (ImageButton) itemView.findViewById(R.id.stop_alert_btn);
            mStopDescription.setTag(itemView);
        }
    }

    public final static String[] mStopProjection = new String[] {
            DBHelper.KEY_STOPNM, DBHelper.KEY_STOPID, DBHelper.KEY_STOPLN, DBHelper.KEY_STOPLT, DBHelper.KEY_ALERT_ID
    };

    public static class StopData {
        String stopName; //to display
        String stopId; //to check for alerts
        String stopLat, stopLong; //to open Map
        String stopAlert = null;
    }

    /**
     * This method will help instantiate the route fragment from a db query of the route name
     * @param c - query reslut
     * @return the list of stop data to be displayed
     */
    public static StopData[] makeStopsList(Cursor c) {
        if(c.getCount() > 0 && c.moveToFirst()) {
            final StopData[] tmp = new StopData[c.getCount()];
            StopData data;
            for(int dex = 0; dex < tmp.length; dex++) {
                data = new StopData();
                data.stopName = c.getString(0);
                data.stopId = c.getString(1);
                data.stopLong = c.getString(2);
                data.stopLat = c.getString(3);
                data.stopAlert = c.getString(4);
                tmp[dex] = data;
                c.moveToNext();
            }
            return tmp;
        }
        Log.w(TAG, "bad cursor, no array");
        return null;
    }
}
