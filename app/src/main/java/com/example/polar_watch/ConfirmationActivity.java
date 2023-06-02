package com.example.polar_watch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.polar_watch.databinding.ActivityConfirmationBinding;

public class ConfirmationActivity extends Activity {

    private TextView mTextView;
    private ActivityConfirmationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String confirmationType = extras.getString("confirmationType");
            binding.confirmationTitleId.setText(confirmationType);
        }

        Button button = binding.confirmationButtonId;

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        });
    }
}