package ru.simplearink.udarapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MultipleModeAdapter extends ArrayAdapter<IncorrectChoiceResultObject> {
    public MultipleModeAdapter(Context context, ArrayList<IncorrectChoiceResultObject> stats) {
        super(context, 0, stats);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        IncorrectChoiceResultObject question = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.multiple_mode_stats_word, parent, false);
        }
        // Lookup view for data population
        TextView word = convertView.findViewById(R.id.multiple_mode_stats_word);
        if (position == question.getAnswer()) {
            word.setBackgroundTintList(convertView.getResources().getColorStateList(R.color.correctAnswer));
        } else if (position == question.getUserAnswer() && !question.isUserAnswerCorrect()) {
            word.setBackgroundTintList(convertView.getResources().getColorStateList(R.color.wrongAnswer));
        } else {
            word.setBackgroundTintList(convertView.getResources().getColorStateList(R.color.colorAccent));
        }

        word.setText(question.getStatsWord());


        return convertView;
    }

}