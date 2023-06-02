package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;

import com.example.polar_watch.ConfirmationActivity;

import java.util.ArrayList;

public class ShowWinnersOfTheDay {
    private final Activity activity;
    private ArrayList<ButtonTuple> btnList;
    private static final String TAG = "ShowWinnersOfTheDay";

    public ShowWinnersOfTheDay(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ButtonTuple> initializeButtonList() {
        btnList = new ArrayList<>();


        Intent intent = new Intent(activity, ConfirmationActivity.class);
        String confirmationType = "InformationAboutChallenger";
        intent.putExtra("confirmationType", confirmationType);
        ButtonTuple button = new ButtonTuple("1 Thomas Schneider",
                v -> {
                    activity.startActivity(intent);
                });
        btnList.add(button);

        ButtonTuple button2 = new ButtonTuple("2 John Smith",
                v -> {
                    activity.startActivity(intent);
                });
        btnList.add(button2);

        ButtonTuple button3 = new ButtonTuple("3 Elisabeth Meyer",
                v -> {
                    activity.startActivity(intent);
                });
        btnList.add(button3);

        ButtonTuple button4 = new ButtonTuple("Show my position",
                v -> {
                    activity.startActivity(intent);
                });
        btnList.add(button4);


        return btnList;
    }

    private void startAnotherActivity(Class<?> classType) {
        Intent intent = new Intent(activity, classType);
        activity.startActivity(intent);
    }
}
