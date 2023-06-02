package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.databinding.ActivityTemplateBinding;
import com.example.polar_watch.list_actions.SelectDayWithDifferentWinners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectDayWithDifferentWinnersActivity extends Activity {

    private ActivityTemplateBinding binding;
    private String TAG = "SelectDayWithDifferentWinnersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TitleAdapter titleAdapter = new TitleAdapter("Select Day with winners");
        SelectDayWithDifferentWinners selectDayWithDifferentWinners = new SelectDayWithDifferentWinners(this);
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(selectDayWithDifferentWinners.initializeButtonList());
        ConcatAdapter concatAdapter = new ConcatAdapter(titleAdapter, buttonsAdapter);

        WearableRecyclerView wrv = binding.templateRecycleViewId;
        wrv.setLayoutManager(new WearableLinearLayoutManager(this));
        wrv.setAdapter(concatAdapter);
    }
}