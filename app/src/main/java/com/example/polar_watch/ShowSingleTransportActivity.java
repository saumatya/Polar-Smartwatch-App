package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;

import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.databinding.ActivityTemplateBinding;
import com.example.polar_watch.list_actions.CreateTransportAction;
import com.example.polar_watch.list_actions.ShowSingleTransportAction;

public class ShowSingleTransportActivity extends Activity {

    private ActivityTemplateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WearableRecyclerView wrv = binding.templateRecycleViewId;
        wrv.setLayoutManager(new WearableLinearLayoutManager(this));

        ShowSingleTransportAction showSingleTransportAction = new ShowSingleTransportAction(this, wrv);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("type");
            String label = extras.getString("label");
            showSingleTransportAction.setType(type);
            showSingleTransportAction.setLabel(label);
        }

        showSingleTransportAction.startForm();
    }
}