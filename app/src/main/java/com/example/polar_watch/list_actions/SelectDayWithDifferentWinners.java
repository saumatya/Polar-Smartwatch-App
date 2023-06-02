package com.example.polar_watch.list_actions;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.polar_watch.ShowWinnersOfTheDayActivity;
import com.example.polar_watch.StartTrackingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SelectDayWithDifferentWinners {
    private final Activity activity;
    private ArrayList<ButtonTuple> btnList;
    private static final String TAG = "ShowDaysWithDifferentWinners";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public SelectDayWithDifferentWinners(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ButtonTuple> initializeButtonList() {
        btnList = new ArrayList<>();

        // initialize firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Date date = new java.util.Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        String actualMonth = monthFormat.format(date);
        Log.d(TAG, "MONTH " + actualMonth);

        db.collectionGroup("finaldb").whereEqualTo("month",
                "5").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "Query succeeded.");
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String email = documentSnapshot.getString("Email");
//                    Double coveredDistance = Double.parseDouble(documentSnapshot.getString("coveredDistance"));
//                    Double savedCarbonEmission = Double.parseDouble(documentSnapshot.getString("savedCarbonEmission"));
//                    int day = Integer.parseInt(documentSnapshot.getString("day"));
                 //   Log.d(TAG, "Data: " + email + " "  + coveredDistance + " "  + savedCarbonEmission + " " + day);
                    Log.d(TAG, "Data " + email);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting documents: ", e);
            }
        });

        Intent intent = new Intent(activity, ShowWinnersOfTheDayActivity.class);
        ButtonTuple button = new ButtonTuple("4 May",
                v -> {
                    intent.putExtra("date", "4 May");
                    activity.startActivity(intent);
                });
        btnList.add(button);

        ButtonTuple button2 = new ButtonTuple("5 May",
                v -> {
                    intent.putExtra("date", "5 May");
                    activity.startActivity(intent);
                });
        btnList.add(button2);

        return btnList;
    }

    private void startAnotherActivity(Class<?> classType) {
        Intent intent = new Intent(activity, classType);
        activity.startActivity(intent);
    }
}
