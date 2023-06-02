package com.example.polar_watch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseJSON {

    private String TAG = "DatabaseJSON";

//    public DatabaseJSON(String TAG) {
//        this.TAG = TAG;
//    }

    public void update(Activity activity, JSONObject jsonObject) {
        try {

            String jsonString = jsonObject.toString(4);
            File file = new File(activity.getFilesDir(), "data.json");
            Log.d(TAG, "write: " + activity.getFilesDir());
            Log.d(TAG, "saved JSON: " + jsonObject.toString(4));
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(jsonString);
            bufferedWriter.close();
        } catch (JSONException | IOException e) {
            Log.e(TAG, "update: updating problem encountered " + e.getMessage());
        }
    }

    public void write(Activity activity, TransportJSON transportJSON) {
        try {
            JSONObject finalOutput = new JSONObject();
            JSONObject existedJson = readJson(activity);

            JSONArray jsonArray;
            if (existedJson.isNull("transports"))
                jsonArray = new JSONArray();
            else
                jsonArray = existedJson.getJSONArray("transports");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", transportJSON.getType());
            jsonObject.put("label", transportJSON.getLabel());
            jsonObject.put("engine", transportJSON.getEngine());
            jsonObject.put("fuelPrice", transportJSON.getFuelPrice());
            jsonObject.put("mpg", transportJSON.getMpg());
            jsonArray.put(jsonObject);
            finalOutput.put("transports", jsonArray);

            String jsonString = finalOutput.toString(4);
            File file = new File(activity.getFilesDir(), "data.json");
            Log.d(TAG, "write: " + activity.getFilesDir());
            Log.d(TAG, "saved JSON: " + finalOutput.toString(4));
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(jsonString);
            bufferedWriter.close();
        } catch (JSONException | IOException e) {
            Log.e(TAG, "write: writing problem encountered " + e.getMessage());
        }
    }

    public JSONObject readJson(Activity activity) {

        String buffer = "", jsonString = "";

        try {
            File file = new File(activity.getFilesDir(), "data.json");

            if (file.exists()) {

                FileReader fileReader = new FileReader(file);

                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while ((buffer = bufferedReader.readLine()) != null) {
                    jsonString += buffer;
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "readJson: " + e.getMessage());
        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(jsonString);
            Log.d(TAG, "readJson: retrieved " + jsonObject.length() + " Datafields");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public boolean deleteFile(Activity activity) {
        File file = new File(activity.getFilesDir(), "data.json");
        boolean result = file.delete();

        if (result) {
            Log.d(TAG, "File Removed");
        }
        return result;
    }
}
