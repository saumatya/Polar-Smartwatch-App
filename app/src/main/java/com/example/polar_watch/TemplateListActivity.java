package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.polar_watch.databinding.ActivityTemplateBinding;

public class TemplateListActivity extends Activity {

    private ActivityTemplateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}