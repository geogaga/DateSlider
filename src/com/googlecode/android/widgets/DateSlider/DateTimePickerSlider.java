/*
 * Copyright (C) 2011 Daniel Berndt - Codeus Ltd  -  DateSlider
 * 
 * DateSlider which allows for an easy selection of a date containing a year scroller
 * thus allowing for greater time travels 
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

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

public class DateTimePickerSlider extends DateSlider {

	/**
	 * initialise the DateSlider
	 * 
	 * @param context
	 * @param l
	 * @param calendar calendar set with the date that should appear at start up
	 */
	public DateTimePickerSlider(Context context, OnDateSetListener l, Calendar calendar) {
        this(context, l, calendar, null, null);
	}

    public DateTimePickerSlider(Context context, OnDateSetListener l, Calendar calendar,
                                Calendar minDate, Calendar maxDate) {
        this(context, l, calendar, minDate, maxDate, 1);
    }

    public DateTimePickerSlider(Context context, OnDateSetListener l, Calendar calendar,
                                Calendar minDate, Calendar maxDate, int minInterval) {
        super(context, SliderController.instance(context).getParcel().getLayoutId("datetimepickerslider"), l, calendar, minDate, maxDate, minInterval);
    }

    @Override
    public DateSlider asEmbed(Bundle savedInstanceState) {
        mDialog = new FauxDialog(mContext);
        this.onCreate(savedInstanceState);
        findViewById(SliderController.instance().getParcel().getItemId("dateSliderOkButton")).setVisibility(View.INVISIBLE);
        findViewById(SliderController.instance().getParcel().getItemId("dateSliderCancelButton")).setVisibility(View.INVISIBLE);
        mDialog.show();
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImageButton flipButton = (ImageButton) findViewById(SliderController.instance().getParcel().getItemId("dateSliderFlipButton"));
        flipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout datePickerView = (LinearLayout)findViewById(SliderController.instance().getParcel().getItemId("datePickerView"));
                ViewSwitcher vs = (ViewSwitcher)findViewById(SliderController.instance().getParcel().getItemId("dateSliderFlipView"));
                vs.showNext();
                if(vs.getCurrentView() == datePickerView) {
                    flipButton.setImageResource(SliderController.instance().getParcel().getDrawableId("glclock"));
                }
                else {
                    flipButton.setImageResource(SliderController.instance().getParcel().getDrawableId("glcalendar"));
                }
            }
        });
    }

    @Override
    protected void setTitle() {
        if (mTitleText != null) {
            final Calendar c = getTime();
            mTitleText.setText(getContext().getString(SliderController.instance().getParcel().getIdentifier("dateSliderTitle", "string")) +
                    String.format(": %te. %tB %tY %tR", c, c, c, c));
        }
    }
}
