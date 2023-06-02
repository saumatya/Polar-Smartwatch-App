package com.example.polar_watch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.example.polar_watch.list_actions.ButtonTuple;

import java.util.ArrayList;

public class ButtonsAdapter extends WearableRecyclerView.Adapter<ButtonsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.item_main_menu_btn_id);
        }
    }

    private final ArrayList<ButtonTuple> buttonList;

    public ButtonsAdapter(ArrayList<ButtonTuple> buttonList) {
        this.buttonList = buttonList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View transportView = inflater.inflate(R.layout.item_action_button_of_list, parent, false);

        return new ViewHolder(transportView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String buttonTitle = buttonList.get(position).title;
        View.OnClickListener buttonClickListener = buttonList.get(position).onClickListener;

        Button button = holder.button;
        button.setEnabled(true);
        button.setText(buttonTitle);
        button.setOnClickListener(buttonClickListener);
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }

}
