package com.googlecode.android.widgets.DateSlider.labeler;

import java.util.List;

abstract public class AbstractLabelerModel {

    abstract public List<String> getModel();

    public int size() {
        return getModel().size();
    }

    public String get(int idx) {
        return getModel().get(idx);
    }
}
