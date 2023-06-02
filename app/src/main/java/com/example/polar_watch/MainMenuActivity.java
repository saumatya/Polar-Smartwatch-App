package com.example.polar_watch;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;


import com.example.polar_watch.databinding.ActivityTemplateBinding;
import com.example.polar_watch.list_actions.ShowMenuAction;

public class MainMenuActivity extends Activity {

    private ActivityTemplateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Concat Adapter (First Adapter Titel) Second Adapter (List)
        // https://developer.android.com/reference/androidx/recyclerview/widget/ConcatAdapter
        TitleAdapter titleAdapter = new TitleAdapter("Save The World Challenge");
        ShowMenuAction showMenuAction = new ShowMenuAction(this);
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(showMenuAction.initializeMainMenuButtonList());
        ConcatAdapter concatAdapter = new ConcatAdapter(titleAdapter, buttonsAdapter);

        WearableRecyclerView wrv = binding.templateRecycleViewId;
        wrv.setLayoutManager(new WearableLinearLayoutManager(this));
        wrv.setAdapter(concatAdapter);
    }
}