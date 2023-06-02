package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.polar_watch.databinding.ActivityInformationBinding;

public class InformationActivity extends Activity {

    private ActivityInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}