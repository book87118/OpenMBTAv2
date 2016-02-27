package com.mentalmachines.ttime.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mentalmachines.ttime.DBHelper;
import com.mentalmachines.ttime.R;
import com.mentalmachines.ttime.SimpleStopAdapter;
import com.mentalmachines.ttime.SimpleStopAdapter.StopData;

public class RouteFragment extends Fragment{
	/**
	 * A fragment representing a train or bus line
	 */
    private static final String LINE_NAME = "line";
    private static final String TAG = "RouteFragment";

    boolean mInbound = true;
    public RecyclerView mList;
    static StopData[] mInStops, mOutStops;
    AnimatorSet moveLeft, moveRight, moveR2, moveL2;

	/**
	 * Returns a new instance of this fragment
     * sets the route stops and route name
	 */
	public static RouteFragment newInstance(StopData[] instops, StopData[] outstops, String title, int bgColor) {
		RouteFragment fragment = new RouteFragment();
		Bundle args = new Bundle();
        mInStops = instops;
        mOutStops = outstops;
        args.putString(LINE_NAME, title);
        args.putInt(TAG, bgColor);
		fragment.setArguments(args);
		return fragment;
	}

	public RouteFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.route_fragment, container, false);
        final Bundle args = getArguments();
        final TextView titleTV = (TextView)rootView.findViewById(R.id.mc_title);

		mList = (RecyclerView) rootView.findViewById(R.id.mc_routelist);

        if(mInStops == null && mOutStops == null) {
			mList.setVisibility(View.GONE);
            getActivity().findViewById(R.id.fab_in_out).setVisibility(View.GONE);
            Log.w(TAG, "no stops");
            titleTV.setText(args.getString(LINE_NAME));
            titleTV.setTextColor(args.getInt(TAG));
        } else {
            //rootView.findViewById(R.id.mc_map).setVisibility(View.VISIBLE);
            final CheckBox cb = (CheckBox) rootView.findViewById(R.id.mc_favorite);
            cb.setVisibility(View.VISIBLE);
            //read and set preference
            cb.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(
                    getArguments().getString(LINE_NAME), false));
            cb.setOnCheckedChangeListener(favListener);
            //defaulting to inbound
            final String routeNm = args.getString(LINE_NAME);
            if(Character.isDigit(routeNm.charAt(0))) {
                titleTV.setText(getString(R.string.bus_prefix) + args.getString(LINE_NAME));
            } else {
                titleTV.setText(args.getString(LINE_NAME));
            }

            titleTV.setTextColor(args.getInt(TAG));
            //TODO use this instead of titleTV
            //getActivity().setTitle(args.getString(LINE_NAME));
			mList.setVisibility(View.VISIBLE);
            if(mInStops == null || mOutStops == null) {
                //this is a one way route
                mList.setAdapter(new SimpleStopAdapter(
                    mInStops == null? mOutStops:mInStops, args.getInt(TAG)));
                getActivity().findViewById(R.id.fab_in_out).setVisibility(View.GONE);
            } else {
                mList.setAdapter(new SimpleStopAdapter(mInStops, args.getInt(TAG)));
                getActivity().findViewById(R.id.fab_in_out).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.fab_in_out).setOnClickListener(fabListener);
            }

            //TODO wire up inbound and outbound based on time/previous display
        }
        //Floating Action button switches the display between inbound and outbound

		return rootView;
	}

    View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(moveRight == null) {
                animationSetup(getView());
            }
            mInbound = !mInbound;
            if(mInbound) {
                ObjectAnimator.ofFloat(view, "rotation", 540f).start();
                //mItems = getArguments().getStringArray(IN_STOPS_LIST);
                ((FloatingActionButton)view).setImageResource(R.drawable.ic_menu_forward);
                ((TextView)getActivity().findViewById(R.id.mc_title)).setText(
                        getArguments().getString(LINE_NAME));
                moveRight.start();
            } else {
                ObjectAnimator.ofFloat(view, "rotation", -540f).start();
                //mItems = getArguments().getStringArray(OUT_STOPS_LIST);
                ((FloatingActionButton)view).setImageResource(R.drawable.ic_menu_back);
                ((TextView)getActivity().findViewById(R.id.mc_title)).setText(
                        getArguments().getString(LINE_NAME));
                moveLeft.start();
            }

        }
    };

    /*  The gesture detector does not play nicely with a scrolling list

    final GestureDetector gesture = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX, float velocityY) {
                    //Log.d(TAG,"on fling");
                    super.onFling(start, finish, velocityX, velocityY);
                    if (Math.abs(velocityX) < SWIPE_VELOCITY) {
                        return false;
                    }
                    if(start.getRawX() < finish.getRawX()) {
                        //swipe is going from left to right
                        setAndRunAnimation(true);
                    } else {
                        //swipe is from right to left
                        setAndRunAnimation(false);
                    }
                    return true;
                }
            });*/


    /**
     * these animations run when switching between inbound and outbound
     * the "moveRight" go along with the direction of the FAB
     * the moveLeft are also setup for gesture detectors, swiping changes the list
     */
    void animationSetup(final View screen) {

        final int width = screen.getWidth();
        Log.d(TAG, "setting up animations " + width);
        moveRight = new AnimatorSet();
        moveRight.play(ObjectAnimator.ofFloat(mList, "translationX", 0, 2 * width))
                .with(ObjectAnimator.ofFloat(mList, "alpha", 1f, 0f));
        moveR2 = new AnimatorSet();
        moveR2.play(ObjectAnimator.ofFloat(mList, "alpha", 0f, 1f))
                .with(ObjectAnimator.ofFloat(mList, "translationX", -width, 0));
        moveRight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                //move right, IN_STOPS_LIST
                mList.setAdapter(new SimpleStopAdapter(mInStops, getArguments().getInt(TAG)));
                moveR2.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });

        moveLeft = new AnimatorSet();
        moveLeft.play(ObjectAnimator.ofFloat(mList, "translationX", 0, -width))
                .with(ObjectAnimator.ofFloat(mList, "alpha", 1f, 0f));
        moveL2 = new AnimatorSet();
        moveL2.play(ObjectAnimator.ofFloat(mList, "translationX", width, 0))
                .with(ObjectAnimator.ofFloat(mList, "alpha", 0f, 1f));

        moveLeft.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                mList.setAdapter(new SimpleStopAdapter(mOutStops, getArguments().getInt(TAG)));
                moveL2.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
    }

    final CompoundButton.OnCheckedChangeListener favListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                    .putBoolean(getArguments().getString(LINE_NAME), b).commit();
            DBHelper.handleFavorite(getContext(), getArguments().getString(LINE_NAME), b);
            if(getActivity().findViewById(R.id.exp_favorite).getTag() != null) {
                getActivity().findViewById(R.id.exp_favorite).callOnClick();
                //problem with zero?
            }
        }
    };


}