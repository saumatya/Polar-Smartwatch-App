package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.databinding.ActivityTemplateBinding;
import com.example.polar_watch.list_actions.CreateTransportAction;

public class CreateTransportActivity extends Activity {

    private ActivityTemplateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WearableRecyclerView wrv = binding.templateRecycleViewId;
        wrv.setLayoutManager(new WearableLinearLayoutManager(this));

        CreateTransportAction createTransportAction = new CreateTransportAction(this, wrv);
        createTransportAction.startForm();
    }
}


