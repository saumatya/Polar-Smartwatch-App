package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.polar_watch.databinding.ActivityTravelHistoryBinding;

public class TravelHistoryActivity extends Activity {

    private ActivityTravelHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTravelHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}