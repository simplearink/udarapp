package ru.simplearink.udarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsAdapter extends ArrayAdapter<SingleResultObject> {
    public StatsAdapter(Context context, ArrayList<SingleResultObject> stats) {
        super(context, 0, stats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SingleResultObject statsRes = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_stats_item_1, parent, false);
        }
        // Lookup view for data population
        TextView questionNum = convertView.findViewById(R.id.stats_list_background);
        TextView correctness = convertView.findViewById(R.id.correctnessField);
        // Populate the data into the template view using the data object
        questionNum.setText("Вопрос " + String.valueOf(statsRes.getIter()));

        String str;
        if (String.valueOf(statsRes.getUserAnswer()).equals(statsRes.getAnswer())) {
            str = "Верно";
        } else {
            str = "Неверно";
        }

        correctness.setText(str);
        // Return the completed view to render on screen
        return convertView;
    }

}