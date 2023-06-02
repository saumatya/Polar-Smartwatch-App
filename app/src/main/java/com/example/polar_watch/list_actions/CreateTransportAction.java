package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.ButtonsAdapter;
import com.example.polar_watch.ConfirmationActivity;
import com.example.polar_watch.DatabaseJSON;
import com.example.polar_watch.TitleAdapter;
import com.example.polar_watch.TransportJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateTransportAction {

    private String TAG = "CreateTransportAction";

    private WearableRecyclerView wrv;
    private final Activity createTransportActivity;
    private ArrayList<ButtonTuple> btnList;
    private String listTitle;


    private String type = "";
    private String engine = "";
    private double fuelPrice = 0;
    private double mpg = 0;
    private String label = "";

    private JSONObject jsonObject;
    private DatabaseJSON databaseJSON;
    private boolean isRedLabelAvailable = true;
    private boolean isGreenLabelAvailable = true;
    private boolean isYellowLabelAvailable = true;


    public CreateTransportAction(Activity createTransportActivity, WearableRecyclerView wrv) {
        this.createTransportActivity = createTransportActivity;
        this.wrv = wrv;
    }

    public void startForm() {
        // Step 1 initialize Database
        databaseJSON = new DatabaseJSON();
        jsonObject = databaseJSON.readJson(createTransportActivity);

        // Step 2 set title of the list and transport type buttons
        listTitle = "Select transport type";
        callNextFormInput(transportTypeButtons());
    }

    private void checkExistedLabels() {
        try {
            if (!jsonObject.isNull("transports")) {
                JSONArray jsonArray = jsonObject.getJSONArray("transports");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJSONObject = jsonArray.getJSONObject(i);

                    // update label
                    if (tempJSONObject.get("type").equals(type)) {
                        if (tempJSONObject.get("label").equals("Red"))
                            isRedLabelAvailable = false;
                        if (tempJSONObject.get("label").equals("Green"))
                            isGreenLabelAvailable = false;
                        if (tempJSONObject.get("label").equals("Yellow"))
                            isYellowLabelAvailable = false;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "checkExistedLabels: checking problem encountered " + e.getMessage());
        }
    }

    public void finishForm() {
        storeTransportIntoDB();

        // Start Confirmation Activity
        Intent intent = new Intent(createTransportActivity, ConfirmationActivity.class);
        createTransportActivity.startActivity(intent);

        // finish (close) CreateTransportActivity
        createTransportActivity.finish();
    }

    private void storeTransportIntoDB() {
        DatabaseJSON databaseJSON = new DatabaseJSON();

        TransportJSON transportJSON = new TransportJSON();
        transportJSON.setType(type);
        transportJSON.setLabel(label);
        transportJSON.setEngine(engine);
        transportJSON.setFuelPrice(fuelPrice);
        transportJSON.setMpg(mpg);

        databaseJSON.write(createTransportActivity, transportJSON);

        // TODO Error handling
        // It should not be possible to create transport with same type and label

    }

    // Form Input Step 1
    public ArrayList<ButtonTuple> transportTypeButtons() {

        ButtonTuple button1 = transportTypeButton("Bike");
        ButtonTuple button2 = transportTypeButton("E-Bike");
        ButtonTuple button3 = transportTypeButton("Motorcycle");
        ButtonTuple button4 = transportTypeButton("Car");

        btnList = new ArrayList<>();
        btnList.add(button1);
        btnList.add(button2);
        btnList.add(button3);
        btnList.add(button4);

        return btnList;
    }

    public ButtonTuple transportTypeButton(String transportType) {
        return new ButtonTuple(transportType, v -> {
            this.type = transportType;
            listTitle = "Select label";
            callNextFormInput(labelButtons());
        });
    }

    // Form Input Step 2
    public ArrayList<ButtonTuple> labelButtons() {

        checkExistedLabels();
        btnList = new ArrayList<>();

        if (isRedLabelAvailable) {
            ButtonTuple button = labelButton("Red");
            btnList.add(button);
        }

        if(isGreenLabelAvailable) {
            ButtonTuple button = labelButton("Green");
            btnList.add(button);

        }
        if(isYellowLabelAvailable) {
            ButtonTuple button = labelButton("Yellow");
            btnList.add(button);
        }

        return btnList;
    }

    public ButtonTuple labelButton(String label) {
        return new ButtonTuple(label, v -> {
            this.label = label;
            listTitle = "Select engine type";
            if (getNextInput() != null)
                callNextFormInput(getNextInput());
            else
                finishForm();
        });
    }

    public ArrayList<ButtonTuple> getNextInput() {
        if (type.equals("Car") || type.equals("Motorcycle"))
            return engineTypeButtons();
        else
            return null;
    }

    // Form Input Step 3
    public ArrayList<ButtonTuple> engineTypeButtons() {

        // TODO choose engine type
        ButtonTuple button1 = engineTypeButton("Gasoline");
        ButtonTuple button2 = engineTypeButton("Diesel");
        ButtonTuple button3 = engineTypeButton("Electric");

        btnList = new ArrayList<>();
        btnList.add(button1);
        btnList.add(button2);
        btnList.add(button3);

        return btnList;
    }

    public ButtonTuple engineTypeButton(String engineType) {
        return new ButtonTuple(engineType, v -> {
            this.engine = engineType;
            listTitle = "Select fuel price";
            callNextFormInput(fuelPriceIntegerButtons());
        });
    }

    // Form Input Step 4
    // Integer: 1 2 3 4 5 6 7 8 9
    public ArrayList<ButtonTuple> fuelPriceIntegerButtons() {
        return generateNumberButtons("fuel price", fuelPriceFDPButtons(),  1 );
    }

    // Form Input Step 5
    // FDP = First Decimal Place: 0.10 0.20 0.30 0.40 0.50 0.60 0.70 0.80 0.90
    public ArrayList<ButtonTuple> fuelPriceFDPButtons() {
        return generateNumberButtons("fuel price", fuelPriceSDPButtons(), 10);
    }

    // Form Input Step 6
    // SDP = Second Decimal Place: 0.01 0.02 0.03 0.04 0.05 0.06 0.07 0.08 0.09
    public ArrayList<ButtonTuple> fuelPriceSDPButtons() {
        return generateNumberButtons("fuel price", mpgIntegerButtons(), 100);
    }

    // Form Input Step 7
    // Integer: 1 2 3 4 5 6 7 8 9
    public ArrayList<ButtonTuple> mpgIntegerButtons() {
        return generateNumberButtons("mpg", mpgFDPButtons(), 1);
    }

    // Form Input Step 8
    // FDP = First Decimal Place: 0.10 0.20 0.30 0.40 0.50 0.60 0.70 0.80 0.90
    public ArrayList<ButtonTuple> mpgFDPButtons() {
        return generateNumberButtons("mpg", mpgSDPButtons(), 10);
    }

    // Form Input Step 9
    // SDP = Second Decimal Place: 0.01 0.02 0.03 0.04 0.05 0.06 0.07 0.08 0.09
    public ArrayList<ButtonTuple> mpgSDPButtons() {
        btnList = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {

            int finalI = i;
            ButtonTuple button = new ButtonTuple(String.valueOf((double) finalI / 100),
                    v -> {
                        mpg += roundTwoDecimalPlace(((double) finalI / (double) 100));
                        Log.d(TAG, "Button MPG Value: " + mpg);
                        finishForm();
                    });

            btnList.add(button);
        }
        return btnList;
    }

    public void updateListTitle(String valueToSet, double incrementValue) {

        if(incrementValue == 100 && valueToSet.equals("fuel price"))
            listTitle = "Select mpg"; // switch between fuel price and mpg
        else if (incrementValue == 100) // always for next question
            listTitle = "Select " + valueToSet;
        else if (incrementValue == 1)
            listTitle = "Select first decimal place for " + valueToSet;
        else if (incrementValue == 10)
            listTitle = "Select second decimal place for " + valueToSet;

    }

    public void updateValueToSet(String valueToSet, int index, double incrementValue) {

        if (valueToSet.equals("fuel price")) {
            fuelPrice += roundTwoDecimalPlace(((double) index / incrementValue));
            Log.d(TAG, "Button fuelPrice Value: " + fuelPrice);
        } else {
            mpg += roundTwoDecimalPlace(((double) index / incrementValue));
            Log.d(TAG, "Button MPG Value: " + mpg);
        }
    }

    public ArrayList<ButtonTuple> generateNumberButtons(
            String valueToSet,
            ArrayList<ButtonTuple> nextButtons,
            double incrementValue) {
        btnList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {

            int finalI = i;
            ButtonTuple button = new ButtonTuple(String.valueOf((double) finalI / incrementValue),
                    v -> {
                        updateListTitle(valueToSet, incrementValue);
                        updateValueToSet(valueToSet, finalI, incrementValue);
                        callNextFormInput(nextButtons);
                    });

            btnList.add(button);
        }
        return btnList;
    }

    public void callNextFormInput(ArrayList<ButtonTuple> btnList) {
        TitleAdapter titleAdapter = new TitleAdapter(listTitle);
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(btnList);
        ConcatAdapter concatAdapter = new ConcatAdapter(titleAdapter, buttonsAdapter);
        wrv.setAdapter(concatAdapter);
    }

    private void initializeAttributes() {
        try {
            if (!jsonObject.isNull("transports")) {
                JSONArray jsonArray = jsonObject.getJSONArray("transports");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJSONObject = jsonArray.getJSONObject(i);

                    // update label
                    if (tempJSONObject.get("type").equals(type)) {
                        if (tempJSONObject.get("label").equals("Red"))
                            isRedLabelAvailable = false;
                        if (tempJSONObject.get("label").equals("Green"))
                            isGreenLabelAvailable = false;
                        if (tempJSONObject.get("label").equals("Yellow"))
                            isYellowLabelAvailable = false;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "initializeAttributes: checking problem encountered " + e.getMessage());
        }

    }

    public double roundTwoDecimalPlace(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
