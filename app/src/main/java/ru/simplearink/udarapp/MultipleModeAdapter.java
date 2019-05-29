package ru.simplearink.udarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MultipleModeAdapter extends ArrayAdapter<String> {
    public MultipleModeAdapter(Context context, ArrayList<String> stats) {
        super(context, 0, stats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String question = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.multiple_mode_stats_word, parent, false);
        }
        // Lookup view for data population
        TextView word = convertView.findViewById(R.id.multiple_mode_stats_word);
        word.setText(question);
        word.setBackgroundColor(10091930);

        return convertView;
    }

}