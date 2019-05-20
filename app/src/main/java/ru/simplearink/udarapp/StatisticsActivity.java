package ru.simplearink.udarapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import static ru.simplearink.udarapp.ResultActivity.APP_STATS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_CORRECT;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_ID;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_SIZE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_USERS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_WORD;

public class StatisticsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private StatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        backButton = findViewById(R.id.statBack);
        backButton.setOnClickListener(oclBack);

        ArrayList<SingleResultObject> arrayOfAnswers = new ArrayList<>();
        adapter = new StatsAdapter(this, arrayOfAnswers);
        ListView listView = findViewById(R.id.singleStatsList);
        listView.setAdapter(adapter);

        SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
        int statsSize = shared.getInt(APP_STATS_RES_SIZE, 0);
        for (int i = 0; i < statsSize; i++) {
            int id = shared.getInt(APP_STATS_RES_ID + i, 0);
            String word = shared.getString(APP_STATS_RES_WORD + i, null);
            String cor = shared.getString(APP_STATS_RES_CORRECT + i, null);
            String users = shared.getString(APP_STATS_RES_USERS + i, null);
            arrayOfAnswers.add(new SingleResultObject(i + 1, id, word, cor, Boolean.valueOf(users)));
        }
    }

    View.OnClickListener oclBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adapter.clear();
            Intent back = new Intent(StatisticsActivity.this, ResultActivity.class);
            startActivity(back);
        }
    };
}