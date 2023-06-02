package com.example.polar_watch.list_actions;

import android.view.View;


public class ButtonTuple {

    public final String title;
    public final View.OnClickListener onClickListener;
    public String label;

    public ButtonTuple(String title, View.OnClickListener onClickListener) {
        this.title = title;
        this.onClickListener = onClickListener;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}