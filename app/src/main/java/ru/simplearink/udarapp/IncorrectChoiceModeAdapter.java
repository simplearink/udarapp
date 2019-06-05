package ru.simplearink.udarapp;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IncorrectChoiceModeAdapter extends ArrayAdapter<String> {
    public IncorrectChoiceModeAdapter(Context context, ArrayList<String> stats) {
        super(context, 0, stats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String question = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.multiple_mode_word, parent, false);
        }
        // Lookup view for data population
        TextView word = convertView.findViewById(R.id.multiple_mode_word);

        if (question.length() >= 18) {
            word.setTextSize(29);
        } else if (question.length() >= 15) {
            word.setTextSize(33);
        } else {
            word.setTextSize(40);
        }
        word.setText(question);

        return convertView;
    }

}