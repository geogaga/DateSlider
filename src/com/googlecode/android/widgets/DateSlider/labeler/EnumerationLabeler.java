package com.googlecode.android.widgets.DateSlider.labeler;

import com.googlecode.android.widgets.DateSlider.TimeObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EnumerationLabeler extends Labeler {
    private final String mFormatString;
    private AbstractLabelerModel mModel;

    public EnumerationLabeler(String formatString) {
        this(formatString, null);
    }

    public EnumerationLabeler(String formatString, AbstractLabelerModel model) {
        super(90, 60);
        mFormatString = formatString;
        mModel = model;
    }

    @Override
    public TimeObject add(long time, int val) {
        return timeObjectfromCalendar(Util.addYears(time, val));
    }

    @Override
    protected TimeObject timeObjectfromCalendar(Calendar c) {
        int year = c.get(Calendar.YEAR);
        c.set(year, 0, 1, 0, 0, 0);
        long startTime = c.getTimeInMillis();
        c.set(year, 11, 31, 23, 59, 59);
        c.set(Calendar.MILLISECOND, 999);
        long endTime = c.getTimeInMillis();
        int idx = year - Calendar.getInstance().get(Calendar.YEAR);
        if(mModel == null || idx < 0 || idx >= mModel.size()) {
            return new TimeObject("", startTime, endTime);
        }
        return new TimeObject(mModel.get(idx), startTime, endTime);
    }
}
