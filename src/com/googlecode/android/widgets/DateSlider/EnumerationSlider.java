package com.googlecode.android.widgets.DateSlider;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.googlecode.android.widgets.DateSlider.labeler.AbstractLabelerModel;

import java.util.Calendar;
import java.util.List;

/*
 * A very basic class for holding arbitrary strings representing non-date items
 * So basic, in fact, that at this point it takes a List object as its data source.
 * No model handling of any kind.
 */
public class EnumerationSlider extends DateSlider {

    private List<String> mEnumeratedValues;
    private int mStartYear;

    /**
     * initialise the EnumerationSlider
     *
     * @param context
     * @param l
     */
    public EnumerationSlider(Context context, AbstractLabelerModel model, OnDateSetListener l, String layoutName) {
        super(context, SliderController.instance(context).getParcel().getLayoutId(layoutName),
                l, Calendar.getInstance(),
                null, null, 1);

        minTime = Calendar.getInstance();
        maxTime = Calendar.getInstance();
        maxTime.set(Calendar.YEAR, maxTime.get(Calendar.YEAR) + model.size() - 1);
        mInitialTime = Calendar.getInstance();
        mStartYear   = mInitialTime.get(Calendar.YEAR);

        mEnumeratedValues = model.getModel();
    }

    /**
     * initialise the EnumerationSlider
     *
     * @param context
     * @param l
     */
    public EnumerationSlider(Context context, AbstractLabelerModel model, OnDateSetListener l) {
        this(context, model, l, "enumerationslider");
    }

    /**
     * define our own title of the dialog
     */
    @Override
    protected void setTitle() {
        if (mTitleText != null) {
            int idx = getTime().get(Calendar.YEAR) - mStartYear;
            mTitleText.setText(getContext().getString(SliderController.instance().getParcel().getIdentifier("dateSliderTitle", "string")) +
                    String.format(": %s", mEnumeratedValues.get(idx)));
        }
    }
}
