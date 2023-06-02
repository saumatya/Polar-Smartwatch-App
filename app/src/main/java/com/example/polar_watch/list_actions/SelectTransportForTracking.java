package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.polar_watch.CreateTransportActivity;
import com.example.polar_watch.DatabaseJSON;
import com.example.polar_watch.ShowSingleTransportActivity;
import com.example.polar_watch.StartTrackingActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectTransportForTracking {

    private final Activity activity;
    private ArrayList<ButtonTuple> btnList;
    private static final String TAG = "SelectTransportForTracking";
    private static JSONObject data = new JSONObject();

    public SelectTransportForTracking(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ButtonTuple> initializeVehicleButtonList() {
        btnList = new ArrayList<>();
        // If necessary to remove transport json file
        // DatabaseJSON databaseJSON = new DatabaseJSON();
        // databaseJSON.deleteFile(activity);

        DatabaseJSON databaseJSON = new DatabaseJSON();
        JSONObject jsonObject = databaseJSON.readJson(activity);

        ButtonTuple button = new ButtonTuple("Walking",
                v -> {
                    Intent intent = new Intent(activity, StartTrackingActivity.class);
                    intent.putExtra("type", "Walking");
                    activity.startActivity(intent);
                });
        btnList.add(button);

        try {
            if (!jsonObject.isNull("transports")) {
                JSONArray jsonArray = jsonObject.getJSONArray("transports");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                    if (!tempJSONObject.isNull("type") && !tempJSONObject.isNull("label")) {
                        String type = tempJSONObject.getString("type");
                        String label = tempJSONObject.getString("label");

                        Intent intent = new Intent(activity, StartTrackingActivity.class);
                        // if available then initialize engine, fuel price and mpg
                        if (!tempJSONObject.isNull("engine") &&
                                !tempJSONObject.isNull("fuelPrice") &&
                                !tempJSONObject.isNull("mpg")) {
                            String engine = tempJSONObject.getString("engine");
                            String fuelPrice = tempJSONObject.getString("fuelPrice");
                            String mpg = tempJSONObject.getString("mpg");
                            intent.putExtra("engine", engine);
                            intent.putExtra("fuelPrice", fuelPrice);
                            intent.putExtra("mpg", mpg);
                        }

                        intent.putExtra("type", type);
                        intent.putExtra("label", label);

                        button = new ButtonTuple(type + " " + label,
                                v -> {
                                    activity.startActivity(intent);
                                });
                        btnList.add(button);
                    }

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "initializeVehicleButtonList: writing problem encountered " + e.getMessage());
        }

        return btnList;
    }

    private void startAnotherActivity(Class<?> classType) {
        Intent intent = new Intent(activity, classType);
        activity.startActivity(intent);
    }

}
