/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 *
 * Class for setting up the dialog and initialsing the underlying
 * ScrollLayouts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.android.widgets.DateSlider;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.android.widgets.DateSlider.SliderContainer.OnTimeChangeListener;
import com.googlecode.android.widgets.DateSlider.labeler.AbstractLabelerModel;

/**
 * A Dialog subclass that hosts a SliderContainer and a couple of buttons,
 * displays the current time in the header, and notifies an observer
 * when the user selectes a time.
 */
public class DateSlider extends LinearLayout {

//	private static String TAG = "DATESLIDER";

    protected OnDateSetListener onDateSetListener;
    protected Calendar mInitialTime, minTime, maxTime;
    protected int mLayoutID;
    protected TextView mTitleText;
    protected SliderContainer mContainer;
    protected int minuteInterval;
    protected Context mContext;
    protected Dialog mDialog = null;
    protected Handler scrollHandler = new Handler();
    private Runnable lastScrollRunnable;


    public DateSlider(Context context, int layoutID, OnDateSetListener l, Calendar initialTime) {
    	this(context,layoutID,l,initialTime, null, null, 1);
    }
    
    public DateSlider(Context context, int layoutID, OnDateSetListener l, Calendar initialTime, int minInterval) {
    	this(context,layoutID,l,initialTime, null, null, minInterval);
    }
    
    public DateSlider(Context context, int layoutID, OnDateSetListener l,
            Calendar initialTime, Calendar minTime, Calendar maxTime) {
    	this(context,layoutID,l,initialTime, minTime, maxTime, 1);
    }
    
    public DateSlider(Context context, int layoutID, OnDateSetListener l,
            Calendar initialTime, Calendar minTime, Calendar maxTime, int minInterval) {
        super(context);
        SliderController.instance(context);
        mContext = context;
        this.onDateSetListener = l;
        this.minTime = minTime; this.maxTime = maxTime;
        mInitialTime = Calendar.getInstance(initialTime.getTimeZone());
        mInitialTime.setTimeInMillis(initialTime.getTimeInMillis());
        mLayoutID = layoutID;
        this.minuteInterval = minInterval;
        if (minInterval>1) {
        	int minutes = mInitialTime.get(Calendar.MINUTE);
    		int diff = ((minutes+minuteInterval/2)/minuteInterval)*minuteInterval - minutes;
    		mInitialTime.add(Calendar.MINUTE, diff);
        }
    }

    public Dialog asDialog() {
        mDialog = new Dialog(mContext) {
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //this.getWindow().getDecorView().
                DateSlider.this.onCreate(savedInstanceState);
                // Note: if I set a new title component for this dialog, only the dialog will be able
                // to access its title text.
                // Corollary: if not in a dialog, we will not access our whole app title. Yay!
                final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
                this.setContentView(DateSlider.this);
                if(customTitleSupported) {
                    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, SliderController.instance().getParcel().getLayoutId("dialogtitle"));
                }
                mTitleText = (TextView)findViewById(SliderController.instance().getParcel().getItemId("dateSliderTitleText"));
            }

            @Override
            public Bundle onSaveInstanceState() {
                Bundle savedInstanceState = super.onSaveInstanceState();
                if (savedInstanceState==null) savedInstanceState = new Bundle();
                savedInstanceState.putSerializable("time", getTime());
                return savedInstanceState;
            }
        };
        return mDialog;
    }


    public DateSlider asEmbed(Bundle savedInstanceState) {
        mDialog = new FauxDialog(mContext);
        this.onCreate(savedInstanceState);
        findViewById(SliderController.instance().getParcel().getItemId("dateSliderButLayout")).setVisibility(View.GONE);
        mDialog.show();
        return this;
    }

    public void dismiss() {
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void setContentView(int id) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addView(inflater.inflate(id, null));
    }

    /**
     * Set up the dialog with all the views and their listeners
     */
    public void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState!=null) {
            Calendar c = (Calendar)savedInstanceState.getSerializable("time");
            if (c != null) {
                mInitialTime = c;
            }
        }

        setContentView(mLayoutID);

        mTitleText = (TextView) this.findViewById(SliderController.instance().getParcel().getItemId("dateSliderTitleText"));
        mContainer = (SliderContainer) this.findViewById(SliderController.instance().getParcel().getItemId("dateSliderContainer"));

        mContainer.setOnTimeChangeListener(onTimeChangeListener);
        mContainer.setMinuteInterval(minuteInterval);
        mContainer.setTime(mInitialTime);
        if (minTime!=null) mContainer.setMinTime(minTime);
        if (maxTime!=null) mContainer.setMaxTime(maxTime);

        Button okButton = (Button) findViewById(SliderController.instance().getParcel().getItemId("dateSliderOkButton"));
        okButton.setOnClickListener(okButtonClickListener);

        Button cancelButton = (Button) findViewById(SliderController.instance().getParcel().getItemId("dateSliderCancelButton"));
        cancelButton.setOnClickListener(cancelButtonClickListener);
    }
    
    public void setTime(Calendar c) {
        mContainer.setTime(c);
    }
    
    /**
     * Scrolls the time to the provided target using an animation
     * @param target
     * @param durationInMillis duration of the scroll animation
     * @param linearMovement if true the scrolling will have a constant speed, if false the animation
     * will slow down at the end
     */
    public void scrollToTime(Calendar target, long durationInMillis, final boolean linearMovement) {
    	final Calendar ca = Calendar.getInstance();
    	final long startTime=System.currentTimeMillis();
    	final long endTime=startTime+durationInMillis;
    	final long startMillis = getTime().getTimeInMillis();
    	final long diff = target.getTimeInMillis()-startMillis;
    	if (lastScrollRunnable!=null) scrollHandler.removeCallbacks(lastScrollRunnable);
    	lastScrollRunnable = new Runnable() {
			@Override
			public void run() {
				long currTime = System.currentTimeMillis();
				double fraction = 1-(endTime-currTime)/(double)(endTime-startTime);
				if (!linearMovement) fraction = Math.pow(fraction,0.2);
				if (fraction>1) fraction=1;
				ca.setTimeInMillis(startMillis+(long)(diff*fraction));
				setTime(ca);
				// if not complete yet, call again in 20 milliseconds
				if (fraction<1) scrollHandler.postDelayed(this, 20); 
			}
		};
    	scrollHandler.post(lastScrollRunnable);
    }

    private android.view.View.OnClickListener okButtonClickListener = new android.view.View.OnClickListener() {
        public void onClick(View v) {
            if (onDateSetListener!=null) {
                if(onDateSetListener instanceof OnEnumSetListener) {
                    ((OnEnumSetListener) onDateSetListener).onEnumSet(
                            DateSlider.this,
                            getTime().get(Calendar.YEAR) - Calendar.getInstance().get(Calendar.YEAR));
                }
                else {
                    onDateSetListener.onDateSet(DateSlider.this, getTime());
                }
            }
            dismiss();
        }
    };

    private android.view.View.OnClickListener cancelButtonClickListener = new android.view.View.OnClickListener() {
        public void onClick(View v) {
            dismiss();
        }
    };

    private OnTimeChangeListener onTimeChangeListener = new OnTimeChangeListener() {

        public void onTimeChange(Calendar time) {
            setTitle();
        }
    };

    public Bundle onSaveInstanceState() {
        return mDialog.onSaveInstanceState();
    }

    public long getSelectedId() {
        Calendar cal = mContainer.getTime();
        if(cal == null)
            return 0L;
        return cal.getTimeInMillis();
    }

    public String getSelectedText() {
        Calendar cal = mContainer.getTime();
        if(cal == null)
            return null;
        return DateFormat.getInstance().format(cal.getTime());
    }

    /**
     * @return The currently displayed time
     */
    protected Calendar getTime() {
        return mContainer.getTime();
    }

    /**
     * This method sets the title of the dialog
     */
    protected void setTitle() {
        if (mTitleText != null) {
            final Calendar c = getTime();
            mTitleText.setText(getContext().getString(SliderController.instance().getParcel().getIdentifier("dateSliderTitle", "string")) +
                    String.format(": %te. %tB %tY", c, c, c));
        }
    }

    /**
     * Defines the interface which defines the methods of the OnDateSetListener
     */
    public interface OnDateSetListener {
        /**
         * this method is called when a date was selected by the user
         * @param view			the caller of the method
         *
         */
        public void onDateSet(DateSlider view, Calendar selectedDate);
    }
    public interface OnEnumSetListener extends OnDateSetListener {
        /**
         * this method is called when a date was selected by the user
         * @param view			the caller of the method
         *
         */
        public void onEnumSet(DateSlider view, int idx);
    }

    class FauxDialog extends Dialog {
        public FauxDialog(Context context) {
            super(context, SliderController.instance().getParcel().getIdentifier("InvisibleDialog", "style"));

            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setGravity(Gravity.BOTTOM);
        }

        @Override
        public Bundle onSaveInstanceState() {
            Bundle savedInstanceState = super.onSaveInstanceState();
            if (savedInstanceState==null) savedInstanceState = new Bundle();
            savedInstanceState.putSerializable("time", getTime());
            return savedInstanceState;
        }
    }
}
