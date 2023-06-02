package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;

import com.example.polar_watch.InformationActivity;
import com.example.polar_watch.SelectDayWithDifferentWinnersActivity;
import com.example.polar_watch.SelectTransportForTrackingActivity;
import com.example.polar_watch.TransportListActivity;

import java.util.ArrayList;

public class ShowMenuAction {

    private final Activity activity;
    private ArrayList<ButtonTuple> btnList;


    public ShowMenuAction(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ButtonTuple> initializeMainMenuButtonList() {

        ButtonTuple item1 = new ButtonTuple("Start Tracking",
                v -> startAnotherActivity(SelectTransportForTrackingActivity.class));

        ButtonTuple item2 = new ButtonTuple("Vehicle List",
                v -> startAnotherActivity(TransportListActivity.class));

        ButtonTuple item3 = new ButtonTuple("Save The World Challenge",
                v -> startAnotherActivity(SelectDayWithDifferentWinnersActivity.class));

        ButtonTuple item4 = new ButtonTuple("General Information",
                v -> startAnotherActivity(InformationActivity.class));

        btnList = new ArrayList<>();
        btnList.add(item1);
        btnList.add(item2);
        btnList.add(item3);
        btnList.add(item4);

        return btnList;
    }

    private void startAnotherActivity(Class<?> classType) {
        Intent intent = new Intent(activity, classType);
        activity.startActivity(intent);
    }
}
