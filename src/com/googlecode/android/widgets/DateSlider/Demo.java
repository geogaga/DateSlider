/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 *
 * This is a small demo application which demonstrates the use of the
 * dateSelector
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

import java.security.KeyStore;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.android.widgets.DateSlider.labeler.AbstractLabelerModel;
import com.googlecode.android.widgets.DateSlider.labeler.TimeLabeler;

/**
 * Small Demo activity which demonstrates the use of the DateSlideSelector
 *
 * @author Daniel Berndt - Codeus Ltd
 *
 */
public class Demo extends Activity {

static final int DEFAULTDATESELECTOR_ID = 0;
static final int DEFAULTDATESELECTOR_WITHLIMIT_ID = 6;
static final int ALTERNATIVEDATESELECTOR_ID = 1;
static final int CUSTOMDATESELECTOR_ID = 2;
static final int MONTHYEARDATESELECTOR_ID = 3;
static final int TIMESELECTOR_ID = 4;
static final int TIMESELECTOR_WITHLIMIT_ID = 7;
static final int DATETIMESELECTOR_ID = 5;
static final int DATETIMEPICKERSELECTOR_ID = 8;
static final int ENUMERATIONSELECTOR_ID = 9;

    private TextView dateText;
    private DateSlider mEnumSlider;
    private DateSlider mBogusSlider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // load and initialise the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dateText = (TextView) this.findViewById(R.id.selectedDateLabel);
        Button defaultButton = (Button) this.findViewById(R.id.defaultDateSelectButton);
        // set up a listener for when the button is pressed
        defaultButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(DEFAULTDATESELECTOR_ID);
            }
        });
        
        Button defaultLimitButton = (Button) this.findViewById(R.id.defaultDateLimitSelectButton);
        // set up a listener for when the button is pressed
        defaultLimitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(DEFAULTDATESELECTOR_WITHLIMIT_ID);
            }
        });

        Button alternativeButton = (Button) this.findViewById(R.id.alternativeDateSelectButton);
        // set up a listener for when the button is pressed
        alternativeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(ALTERNATIVEDATESELECTOR_ID);
            }
        });

        Button customButton = (Button) this.findViewById(R.id.customDateSelectButton);
        // set up a listener for when the button is pressed
        customButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(CUSTOMDATESELECTOR_ID);
            }
        });

        Button monthYearButton = (Button) this.findViewById(R.id.monthYearDateSelectButton);
        // set up a listener for when the button is pressed
        monthYearButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(MONTHYEARDATESELECTOR_ID);
            }
        });

        /*
        Button timeButton = (Button) this.findViewById(R.id.timeSelectButton);
        // set up a listener for when the button is pressed
        timeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(TIMESELECTOR_ID);
            }
        });
        */
        
        Button timeLimitButton = (Button) this.findViewById(R.id.timeLimitSelectButton);
        // set up a listener for when the button is pressed
        timeLimitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(TIMESELECTOR_WITHLIMIT_ID);
            }
        });

        Button dateTimeButton = (Button) this.findViewById(R.id.dateTimeSelectButton);
        // set up a listener for when the button is pressed
        dateTimeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(DATETIMESELECTOR_ID);
            }
        });

        Button timeButton = (Button) this.findViewById(R.id.dateTimePickerSelectButton);
        // set up a listener for when the button is pressed
        timeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(DATETIMEPICKERSELECTOR_ID);
            }
        });

        Button enumerationButton = (Button) this.findViewById(R.id.enumerationSelectButton);
        // set up a listener for when the button is pressed
        enumerationButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                // call the internal showDialog method using the predefined ID
                showDialog(ENUMERATIONSELECTOR_ID);
            }
        });

        LinearLayout additionalFields = (LinearLayout) this.findViewById(R.id.additionalFields);
        TextView tv1 = new TextView(this);
        tv1.setText("At exactly this time...");
        additionalFields.addView(tv1);
        mBogusSlider = new DateTimePickerSlider(this, mFinerDateTimeSetListener, Calendar.getInstance()).asEmbed(savedInstanceState == null ? null : savedInstanceState.getBundle("test_2"));
        additionalFields.addView(mBogusSlider);
        TextView tv2 = new TextView(this);
        tv2.setText("Remind me:");
        additionalFields.addView(tv2);
        mEnumSlider = new EnumerationSlider(
                this,
                new EnumActionModel(),
                mEnumerationSetListener,
                "enumeratedactionsslider").asEmbed(savedInstanceState == null ? null : savedInstanceState.getBundle("test_1"));
        additionalFields.addView(mEnumSlider);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle("test_1", mEnumSlider.onSaveInstanceState());
        savedInstanceState.putBundle("test_2", mBogusSlider.onSaveInstanceState());
    }

    // define the listener which is called once a user selected the date.
    private DateSlider.OnDateSetListener mDateSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                dateText.setText(String.format("The chosen date:%n%te. %tB %tY", selectedDate, selectedDate, selectedDate));
            }
    };

    private DateSlider.OnDateSetListener mMonthYearSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                dateText.setText(String.format("The chosen date:%n%tB %tY", selectedDate, selectedDate));
            }
    };

    private DateSlider.OnDateSetListener mTimeSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                dateText.setText(String.format("The chosen time:%n%tR", selectedDate));
            }
    };

    private DateSlider.OnDateSetListener mDateTimeSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                int minute = selectedDate.get(Calendar.MINUTE) /
                        TimeLabeler.MINUTEINTERVAL*TimeLabeler.MINUTEINTERVAL;
                dateText.setText(String.format("The chosen date and time:%n%te. %tB %tY%n%tH:%02d",
                        selectedDate, selectedDate, selectedDate, selectedDate, minute));
            }
    };

    private DateSlider.OnDateSetListener mFinerDateTimeSetListener =
        new DateSlider.OnDateSetListener() {
            public void onDateSet(DateSlider view, Calendar selectedDate) {
                // update the dateText view with the corresponding date
                dateText.setText(String.format("The chosen date and time:%n%te. %tB %tY%n%tH:%tM",
                        selectedDate, selectedDate, selectedDate, selectedDate, selectedDate));
            }
    };

    private DateSlider.OnEnumSetListener mEnumerationSetListener =
        new DateSlider.OnEnumSetListener() {
            public void onEnumSet(DateSlider view, int idx) {
                // update the dateText view with the corresponding date
                dateText.setText(String.format("You selected: %s", new EnumModel().get(idx)));
            }

            public void onDateSet(DateSlider view, Calendar selectedDate) {
                throw new IllegalArgumentException("This is an enumeration, not a date. Need a model.");
            }
        };

    @Override
    protected Dialog onCreateDialog(int id) {
        // this method is called after invoking 'showDialog' for the first time
        // here we initiate the corresponding DateSlideSelector and return the dialog to its caller
        final Calendar c = Calendar.getInstance();
        
        switch (id) {
        case DEFAULTDATESELECTOR_ID:
            return new DefaultDateSlider(this,mDateSetListener,c).asDialog();
        case DEFAULTDATESELECTOR_WITHLIMIT_ID:
        	final Calendar maxTime = Calendar.getInstance();
        	maxTime.add(Calendar.DAY_OF_MONTH, 14);
            return new DefaultDateSlider(this,mDateSetListener,c,c,maxTime).asDialog();
        case ALTERNATIVEDATESELECTOR_ID:
            return new AlternativeDateSlider(this,mDateSetListener,c,c,null).asDialog();
        case CUSTOMDATESELECTOR_ID:
            return new CustomDateSlider(this,mDateSetListener,c).asDialog();
        case MONTHYEARDATESELECTOR_ID:
            return new MonthYearDateSlider(this,mMonthYearSetListener,c).asDialog();
        case TIMESELECTOR_ID:
            return new TimeSlider(this,mTimeSetListener,c,15).asDialog();
        case TIMESELECTOR_WITHLIMIT_ID:
        	final Calendar minTime = Calendar.getInstance();
        	minTime.add(Calendar.HOUR, -2);
            return new TimeSlider(this,mTimeSetListener,c,minTime,c,5).asDialog();
        case DATETIMESELECTOR_ID:
            return new DateTimeSlider(this,mDateTimeSetListener,c).asDialog();
        case DATETIMEPICKERSELECTOR_ID:
            return new DateTimePickerSlider(this,mFinerDateTimeSetListener,c).asDialog();
        case ENUMERATIONSELECTOR_ID:
            return new EnumerationSlider(
                    this,
                    new EnumModel(),
                    mEnumerationSetListener).asDialog();
        }
        return null;
    }

    static public class EnumModel extends AbstractLabelerModel {
        protected List<String> mModel = Arrays.asList("Orange", "Red", "Blue", "Yellow", "Green", "Maroon", "Gray", "Lightgray", "Magenta", "Pink", "Fuschia", "Mauve", "Cyan");
        public List<String> getModel() {
            return mModel;
        }
    }

    static public class EnumActionModel extends AbstractLabelerModel {
        protected List<String> mModel = Arrays.asList("Vacation", "Holiday", "Anniversary", "Birthday", "School", "Wedding", "Taxes");
        public List<String> getModel() {
            return mModel;
        }
    }
    
}