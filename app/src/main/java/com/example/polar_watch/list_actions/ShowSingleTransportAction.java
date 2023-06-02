package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.ButtonsAdapter;
import com.example.polar_watch.ConfirmationActivity;
import com.example.polar_watch.DatabaseJSON;
import com.example.polar_watch.ShowSingleTransportActivity;
import com.example.polar_watch.TitleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowSingleTransportAction {

    private String TAG = "ShowSingleTransportAction";
    private Activity showSingleTransportActivity;
    private WearableRecyclerView wrv;
    private ArrayList<ButtonTuple> btnList;
    private String listTitle;
    private String type = "";
    private String label = "";
    private String engine = "";
    private double fuelPrice = 0;
    private double mpg = 0;
    private JSONObject jsonObject;
    private DatabaseJSON databaseJSON;

    private boolean isRedLabelAvailable = true;
    private boolean isGreenLabelAvailable = true;
    private boolean isYellowLabelAvailable = true;
    private int positionToDelete; // in JSON Array
    private String confirmationType = "The transport is successfully updated.";
    private String key = "";
    private String value = "";

    public ShowSingleTransportAction(Activity showSingleTransportActivity, WearableRecyclerView wrv) {
        this.showSingleTransportActivity = showSingleTransportActivity;
        this.wrv = wrv;
    }

    public void startForm() {
        // Step 1 initialize Database
        databaseJSON = new DatabaseJSON();
        jsonObject = databaseJSON.readJson(showSingleTransportActivity);

        // Step 2 initialize local attributes
        initializeAttributes();
        // Step 3 set list title
        setListTitle();

        // Step 4 set action buttons
        callNextFormInput(transportActionButtons());
    }

    public void finishForm() {

        // Start Confirmation Activity
        Intent intent = new Intent(showSingleTransportActivity, ConfirmationActivity.class);
        intent.putExtra("confirmationType", confirmationType);
        showSingleTransportActivity.startActivity(intent);

        // Store new value in db, if key and value are not empty
        if(!key.equals("") && !value.equals(""))
            updateValueInDB(key, value);

        // finish (close) showSingleTransportActivity
        showSingleTransportActivity.finish();
    }

    public ArrayList<ButtonTuple> transportActionButtons() {
        btnList = new ArrayList<>();

        if (isGreenLabelAvailable || isRedLabelAvailable || isYellowLabelAvailable) {
            ButtonTuple button = new ButtonTuple("Update label", v -> {
                listTitle = "Update label";
                callNextFormInput(updateLabelButtons());
            });
            btnList.add(button);
        }

        if (!engine.equals("")) {
            ButtonTuple button = new ButtonTuple("Update engine", v -> {
                listTitle = "Update engine";
                callNextFormInput(updateEngineButtons());
            });
            btnList.add(button);
        }

        if (fuelPrice != 0) {
            ButtonTuple button = new ButtonTuple("Update fuel price", v -> {
                listTitle = "Update fuel price";
                callNextFormInput(updateFuelPriceIntegerButtons());
            });
            btnList.add(button);
        }
        if(mpg != 0) {
            ButtonTuple button = new ButtonTuple("Update mpg", v -> {
                listTitle = "Update mpg";
                callNextFormInput(updateMPGIntegerButtons());
            });
            btnList.add(button);
        }

        ButtonTuple button = new ButtonTuple("Delete this transport", v -> {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("transports");
                jsonArray.remove(positionToDelete);

                databaseJSON = new DatabaseJSON();
                databaseJSON.update(showSingleTransportActivity, jsonObject);

                confirmationType = "The transport is successfully deleted.";
                finishForm();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        btnList.add(button);

        return btnList;
    }

    private ArrayList<ButtonTuple> updateLabelButtons() {

        btnList = new ArrayList<>();

        if (isRedLabelAvailable) {
            ButtonTuple button = labelTypeButton("Red");
            btnList.add(button);
        }
        if (isGreenLabelAvailable) {
            ButtonTuple button = labelTypeButton("Green");
            btnList.add(button);
        }
        if (isYellowLabelAvailable) {
            ButtonTuple button = labelTypeButton("Yellow");
            btnList.add(button);
        }


        return btnList;
    }

    public ButtonTuple labelTypeButton(String label) {
        return new ButtonTuple(label, v -> {
            key = "label";
            this.value = label;
            finishForm();
        });
    }

    public ArrayList<ButtonTuple> updateEngineButtons() {

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
            key = "engine";
            this.value = engineType;
            finishForm();
        });
    }

    // Integer: 1 2 3 4 5 6 7 8 9
    public ArrayList<ButtonTuple> updateFuelPriceIntegerButtons() {
        fuelPrice = 0; // reset fuel price
        return generateNumberButtons("fuel price", updatefuelPriceFDPButtons(), 1);
    }

    // FDP = First Decimal Place: 0.10 0.20 0.30 0.40 0.50 0.60 0.70 0.80 0.90
    public ArrayList<ButtonTuple> updatefuelPriceFDPButtons() {
        return generateNumberButtons("fuel price", updatefuelPriceSDPButtons(), 10);
    }

    // SDP = Second Decimal Place: 0.01 0.02 0.03 0.04 0.05 0.06 0.07 0.08 0.09
    public ArrayList<ButtonTuple> updatefuelPriceSDPButtons() {
        btnList = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {

            int finalI = i;
            ButtonTuple button = new ButtonTuple(String.valueOf((double) finalI / 100),
                    v -> {
                        fuelPrice += roundTwoDecimalPlace(((double) finalI / (double) 100));
                        Log.d(TAG, "Button fuelPrice Value: " + fuelPrice);
                        key = "fuelPrice";
                        value = String.valueOf(fuelPrice);
                        finishForm();
                    });

            btnList.add(button);
        }
        return btnList;
    }

    // Integer: 1 2 3 4 5 6 7 8 9
    public ArrayList<ButtonTuple> updateMPGIntegerButtons() {
        mpg = 0; // reset mpg
        return generateNumberButtons("mpg", updateMpgFDPButtons(), 1);
    }

    // FDP = First Decimal Place: 0.10 0.20 0.30 0.40 0.50 0.60 0.70 0.80 0.90
    public ArrayList<ButtonTuple> updateMpgFDPButtons() {
        return generateNumberButtons("mpg", updateMpgSDPButtons(), 10);
    }

    // SDP = Second Decimal Place: 0.01 0.02 0.03 0.04 0.05 0.06 0.07 0.08 0.09
    public ArrayList<ButtonTuple> updateMpgSDPButtons() {
        btnList = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {

            int finalI = i;
            ButtonTuple button = new ButtonTuple(String.valueOf((double) finalI / 100),
                    v -> {
                        mpg += roundTwoDecimalPlace(((double) finalI / (double) 100));
                        Log.d(TAG, "Button MPG Value: " + mpg);

                        key = "mpg";
                        value = String.valueOf(mpg);
                        finishForm();
                    });

            btnList.add(button);
        }
        return btnList;
    }


    public void updateListTitle(String valueToSet, double incrementValue) {

        // TODO Finish form ?
        if (incrementValue == 100 && valueToSet.equals("fuel price"))
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

    public void setListTitle() {
        listTitle = "Type: " + type + " " + "Label: " + label;
        if (!engine.equals("") && fuelPrice != 0 && mpg != 0)
            listTitle += " engine: " + engine + " " + "Fuel Price: " + fuelPrice + "MPG: " + mpg;
    }

    private void updateValueInDB(String key, String value) {
        try {
            if (!jsonObject.isNull("transports")) {
                JSONArray jsonArray = jsonObject.getJSONArray("transports");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJSONObject = jsonArray.getJSONObject(i);

                    if (tempJSONObject.get("type").equals(type) &&
                            tempJSONObject.get("label").equals(label)) {
                        tempJSONObject.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "updateValueInDB: updating problem encountered " + e.getMessage());
        }

        databaseJSON = new DatabaseJSON();
        databaseJSON.update(showSingleTransportActivity, jsonObject);
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

                    if (tempJSONObject.get("type").equals(type) &&
                            tempJSONObject.get("label").equals(label)) {
                        // position of element to delete
                        positionToDelete = i;

                        // if available then initialize engine, fuel price and mpg
                        if (!tempJSONObject.isNull("engine") &&
                                !tempJSONObject.isNull("fuelPrice") &&
                                !tempJSONObject.isNull("mpg")) {
                            engine = tempJSONObject.getString("engine");
                            fuelPrice = Double.parseDouble(tempJSONObject.getString("fuelPrice"));
                            mpg = Double.parseDouble(tempJSONObject.getString("mpg"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "initializeAttributes: checking problem encountered " + e.getMessage());
        }

    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void callNextFormInput(ArrayList<ButtonTuple> btnList) {
        TitleAdapter titleAdapter = new TitleAdapter(listTitle);
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(btnList);
        ConcatAdapter concatAdapter = new ConcatAdapter(titleAdapter, buttonsAdapter);
        wrv.setAdapter(concatAdapter);
    }

    public double roundTwoDecimalPlace(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
