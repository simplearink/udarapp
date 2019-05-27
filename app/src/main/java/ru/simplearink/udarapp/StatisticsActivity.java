package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static ru.simplearink.udarapp.ResultActivity.APP_MODE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_COR_WORD;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_CORRECT;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_ID;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_SIZE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_USERS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_WORD;

public class StatisticsActivity extends Activity {

    private ImageButton backButton;
    private StatsAdapter adapter;
    private CorrectWordExtractor connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        backButton = findViewById(R.id.statBack);
        backButton.setOnClickListener(oclBack);

        ArrayList<SingleResultObject> arrayOfAnswers = new ArrayList<>();
        adapter = new StatsAdapter(this, arrayOfAnswers);
        ListView listView = findViewById(R.id.singleStatsList);
        listView.setOnItemClickListener(oclItem);
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
            Intent back = new Intent(StatisticsActivity.this, ResultActivity.class);
            startActivity(back);
        }
    };

    AdapterView.OnItemClickListener oclItem = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
            int mode = shared.getInt(APP_MODE, -1);
            if (mode == 0) {
                String word = shared.getString(APP_STATS_RES_WORD + position, null);
                String cor = shared.getString(APP_STATS_RES_CORRECT + position, null);
                String users = shared.getString(APP_STATS_RES_USERS + position, null);

                connection = new CorrectWordExtractor(shared.getInt(APP_STATS_RES_ID + position, 0));
                connection.execute();

                String res = null;
                try {
                    res = connection.get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = shared.edit();
                editor.putString(APP_STATS_COR_WORD + position, res);
                editor.commit();

                showStats(position);

                System.out.println("Word = " + word + "; Correct word = " + res + "; Correct answer = " + cor + "; User's answer = " + users);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void showStats(int position) {
        SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);

        int mode = shared.getInt(APP_MODE, -1);
        LayoutInflater inflater;
        View layout;


        if (mode == 0) {
            String word = shared.getString(APP_STATS_RES_WORD + position, null);
            Boolean cor = Boolean.valueOf(shared.getString(APP_STATS_RES_CORRECT + position, null));
            Boolean users = Boolean.valueOf(shared.getString(APP_STATS_RES_USERS + position, null));
            String corWord = shared.getString(APP_STATS_COR_WORD + position, null);

            inflater = (LayoutInflater) StatisticsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.activity_word_info, null);

            ((TextView) layout.findViewById(R.id.questionWord)).setText(word);
            ((TextView) layout.findViewById(R.id.correctQuestionWord)).setText(corWord);

            Context contextInstance = getApplicationContext();

            if (cor) {
                (layout.findViewById(R.id.correctAnsColor)).setBackgroundTintList(contextInstance.getResources().getColorStateList(R.color.correctAnswer));
            } else {
                (layout.findViewById(R.id.correctAnsColor)).setBackgroundTintList(contextInstance.getResources().getColorStateList(R.color.wrongAnswer));
            }

            if (users) {
                (layout.findViewById(R.id.userAnsColor)).setBackgroundTintList(contextInstance.getResources().getColorStateList(R.color.correctAnswer));
            } else {
                (layout.findViewById(R.id.userAnsColor)).setBackgroundTintList(contextInstance.getResources().getColorStateList(R.color.wrongAnswer));
            }

            System.out.println(cor);
            System.out.println(users);
        } else {
            String word = shared.getString(APP_STATS_RES_WORD + position, null);
            Boolean cor = Boolean.valueOf(shared.getString(APP_STATS_RES_CORRECT + position, null));
            Boolean users = Boolean.valueOf(shared.getString(APP_STATS_RES_USERS + position, null));
            String corWord = shared.getString(APP_STATS_COR_WORD + position, null);

            inflater = (LayoutInflater) StatisticsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.activity_word_info, null);

        }


        int height = StatisticsActivity.this.getResources().getDisplayMetrics().heightPixels;
        int width = StatisticsActivity.this.getResources().getDisplayMetrics().widthPixels;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, width, height);

        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);
        // display the pop-up in the center
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }
}