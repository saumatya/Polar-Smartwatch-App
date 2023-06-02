package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.polar_watch.CreateTransportActivity;
import com.example.polar_watch.DatabaseJSON;
import com.example.polar_watch.ShowSingleTransportActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowAllTransportAction {

    private final Activity activity;
    private ArrayList<ButtonTuple> btnList;
    private static final String TAG = "ShowTransportAction";
    private static JSONObject data = new JSONObject();

    public ShowAllTransportAction(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ButtonTuple> initializeVehicleButtonList() {

        ButtonTuple addButton = new ButtonTuple("Add new transport",
                v -> startAnotherActivity(CreateTransportActivity.class));

        btnList = new ArrayList<>();
        btnList.add(addButton);


        // If necessary to remove transport json file
        // DatabaseJSON databaseJSON = new DatabaseJSON();
        // databaseJSON.deleteFile(activity);

        DatabaseJSON databaseJSON = new DatabaseJSON();
        JSONObject jsonObject = databaseJSON.readJson(activity);

        // TODO show already added transports
        try {
            if (!jsonObject.isNull("transports")) {
                JSONArray jsonArray = jsonObject.getJSONArray("transports");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                    if (!tempJSONObject.isNull("type") && !tempJSONObject.isNull("label")) {
                        String type = tempJSONObject.getString("type");
                        String label = tempJSONObject.getString("label");

                        ButtonTuple button = new ButtonTuple(type + " " + label,
                                v -> {
                                    Intent intent = new Intent(activity, ShowSingleTransportActivity.class);
                                    intent.putExtra("type", type);
                                    intent.putExtra("label", label);
                                    activity.startActivity(intent);
                                });
                        btnList.add(button);
                    }

                }
            }
        } catch (Exception e) {
            Log.e(TAG, "initializeVehicleButtonList: writing problem encountered " + e.getMessage());
        }

        // TODO click on already added transport and change it

        return btnList;
    }

    private void startAnotherActivity(Class<?> classType) {
        Intent intent = new Intent(activity, classType);
        activity.startActivity(intent);
    }

}
