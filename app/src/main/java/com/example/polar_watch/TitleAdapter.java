package com.example.polar_watch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import java.util.ArrayList;

public class TitleAdapter extends WearableRecyclerView.Adapter<TitleAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_adapter_id);
        }
    }

    private final ArrayList<String> titleList = new ArrayList<>();

    public TitleAdapter(String title) { titleList.add(title); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View transportView = inflater.inflate(R.layout.item_title_of_list, parent, false);

        return new ViewHolder(transportView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String stringItem = titleList.get(position);
        TextView title = holder.title;
        title.setText(stringItem);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }
}
