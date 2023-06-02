package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.databinding.ActivityTemplateBinding;
import com.example.polar_watch.list_actions.ShowAllTransportAction;

public class TransportListActivity extends Activity {

    private ActivityTemplateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        TitleAdapter titleAdapter = new TitleAdapter("Transport list");
        ShowAllTransportAction showAllTransportAction = new ShowAllTransportAction(this);
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(showAllTransportAction.initializeVehicleButtonList());
        ConcatAdapter concatAdapter = new ConcatAdapter(titleAdapter, buttonsAdapter);

        WearableRecyclerView wrv = binding.templateRecycleViewId;
        wrv.setLayoutManager(new WearableLinearLayoutManager(this));
        wrv.setAdapter(concatAdapter);
    }
}