package com.googlecode.android.widgets.DateSlider;

import android.content.Context;
import com.commonsware.cwac.parcel.ParcelHelper;

public class SliderController {

    static private SliderController instance_;
    private Context mContext = null;
    private ParcelHelper mParcel = null;

    private SliderController(Context context) {
        mContext = context;
    }

    static public SliderController instance(Context context) {
        if(null == instance_) {
            instance_ = new SliderController(context);
        }
        return instance_;
    }

    static public SliderController instance() {
        if(null == instance_) {
            throw new Error("Sorry, SliderController instance should already exist.");
        }
        return instance_;
    }

    public ParcelHelper getParcel() {
        if(null == mParcel) {
            mParcel = new ParcelHelper("dateslider", mContext);
        }
        return mParcel;
    }
}
