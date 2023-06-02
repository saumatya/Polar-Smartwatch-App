package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.databinding.ActivityShowWinnersOfTheDayBinding;
import com.example.polar_watch.databinding.ActivityTemplateBinding;
import com.example.polar_watch.list_actions.SelectDayWithDifferentWinners;
import com.example.polar_watch.list_actions.ShowWinnersOfTheDay;

public class ShowWinnersOfTheDayActivity extends Activity {

    private ActivityTemplateBinding binding;
    private String TAG = "ShowWinnersOfTheDayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TitleAdapter titleAdapter = new TitleAdapter("The winners of this day");
        ShowWinnersOfTheDay showWinnersOfTheDay = new ShowWinnersOfTheDay(this);
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(showWinnersOfTheDay.initializeButtonList());
        ConcatAdapter concatAdapter = new ConcatAdapter(titleAdapter, buttonsAdapter);

        WearableRecyclerView wrv = binding.templateRecycleViewId;
        wrv.setLayoutManager(new WearableLinearLayoutManager(this));
        wrv.setAdapter(concatAdapter);
    }
}