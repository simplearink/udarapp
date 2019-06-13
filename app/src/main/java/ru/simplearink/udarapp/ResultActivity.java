package ru.simplearink.udarapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    public static final String APP_STATS = "udarStats";
    public static final String APP_STATS_MISTAKES = "mistakes";
    public static final String APP_STATS_WHOLE = "whole";
    public static final String APP_STATS_CORRECT = "correct";
    public static final String APP_STATS_BEST = "bestTime";
    public static final String APP_STATS_AVG = "avgTime";

    public static final String APP_STATS_RES_SIZE = "size";
    public static final String APP_STATS_RES_ID = "wordID";
    public static final String APP_STATS_RES_WORD = "word";
    public static final String APP_STATS_RES_CORRECT = "correctAns";
    public static final String APP_STATS_RES_USERS = "usersAns";
    public static final String APP_STATS_COR_WORD = "corWord";
    public static final String APP_STATS_QUEST_SIZE = "questionSize";
    public static final String APP_STATS_ANS_CORRECT = "isAnswerCorrect";
    public static final String APP_STATS_RES_CORRECT_POSITION = "correctAnswerOnPosition";

    public static final String APP_MODE = "mode";

    SharedPreferences statsPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        statsPreferences = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);

        Button backButton = findViewById(R.id.toMainButton);
        backButton.setOnClickListener(oclBackMain);

        Button statsButton = findViewById(R.id.statsSingle);
        statsButton.setOnClickListener(oclStats);

        TextView mistakesText = findViewById(R.id.mistakesCounter);
        TextView correctText = findViewById(R.id.correctCounter);
        TextView wholeCount = findViewById(R.id.wholeSetSize);
        TextView bestTime = findViewById(R.id.bestTime);
        TextView avgTime = findViewById(R.id.avgTime);

        SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);

        int mistakes = shared.getInt(APP_STATS_MISTAKES, 0);
        mistakesText.setText(String.valueOf(mistakes));

        int correct = shared.getInt(APP_STATS_CORRECT, 0);
        correctText.setText(String.valueOf(correct));

        int whole = shared.getInt(APP_STATS_WHOLE, 0);
        wholeCount.setText(String.valueOf(whole));

        String bestT = shared.getString(APP_STATS_BEST, "0.0");
        bestTime.setText(bestT);

        String avgT = shared.getString(APP_STATS_AVG, "0.0");
        avgTime.setText(avgT);
    }

    View.OnClickListener oclBackMain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.clear();
            editor.apply();
            Intent backToMain = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(backToMain);
            ResultActivity.this.finish();
        }
    };

    View.OnClickListener oclStats = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toStats = new Intent(ResultActivity.this, StatisticsActivity.class);
            startActivity(toStats);

        }
    };

    @Override
    public void onBackPressed() {
    }
}
