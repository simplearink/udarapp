package ru.simplearink.udarapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static ru.simplearink.udarapp.ResultActivity.APP_MODE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_ANS_CORRECT;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_COR_WORD;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_QUEST_SIZE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_CORRECT;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_CORRECT_POSITION;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_ID;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_SIZE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_USERS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_WORD;

public class StatisticsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CheckerStatsAdapter checkerAdapter;
    private IncorrectChoiceStatsAdapter choiceAdapter;
    private MultipleModeAdapter multipleAdapter;
    private CorrectWordExtractor connection;

    private int mode;
    private Dialog pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        backButton = findViewById(R.id.statBack);
        backButton.setOnClickListener(oclBack);

        SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
        mode = shared.getInt(APP_MODE,-1);

        if (mode == 0) {
            ArrayList<CheckerResultObject> arrayOfAnswers = new ArrayList<>();
            checkerAdapter = new CheckerStatsAdapter(this, arrayOfAnswers);
            ListView listView = findViewById(R.id.singleStatsList);
            listView.setOnItemClickListener(oclItem);
            listView.setAdapter(checkerAdapter);

            int statsSize = shared.getInt(APP_STATS_RES_SIZE, 0);
            for (int i = 0; i < statsSize; i++) {
                int id = shared.getInt(APP_STATS_RES_ID + i, 0);
                String word = shared.getString(APP_STATS_RES_WORD + i, null);
                String cor = shared.getString(APP_STATS_RES_CORRECT + i, null);
                String users = shared.getString(APP_STATS_RES_USERS + i, null);
                arrayOfAnswers.add(new CheckerResultObject(i + 1, id, word, cor, Boolean.valueOf(users)));
            }
        } else if (mode == 1) {
            ArrayList<IncorrectChoiceResultObject> array = new ArrayList<>();
            choiceAdapter = new IncorrectChoiceStatsAdapter(this, array);
            ListView listView = findViewById(R.id.singleStatsList);
            listView.setOnItemClickListener(oclItem);
            listView.setAdapter(choiceAdapter);

            int statsSize = shared.getInt(APP_STATS_RES_SIZE, 0);
            for (int i = 0; i < statsSize; i++) {
                ArrayList<String> ids = new ArrayList<>();
                ArrayList<String> words = new ArrayList<>();
                int questionSize = shared.getInt(APP_STATS_QUEST_SIZE + i, -1);
                for (int j = 0; j < questionSize; j++) {
                    String id = shared.getString(APP_STATS_RES_ID + "[" + i + "][" + j + "]", null);
                    ids.add(id);
                    String word = shared.getString(APP_STATS_RES_WORD + "[" + i + "][" + j + "]", null);
                    words.add(word);
                }
                int cor = shared.getInt(APP_STATS_RES_CORRECT_POSITION + i, -1);
                int users = shared.getInt(APP_STATS_RES_USERS + i, -1);

//                System.out.println(cor + " vs " + users);

                Boolean userCorrectness = shared.getBoolean(APP_STATS_ANS_CORRECT + i, false);
                array.add(new IncorrectChoiceResultObject(questionSize, ids, words, cor, users, userCorrectness));
            }
        }

    }

    View.OnClickListener oclBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent back = new Intent(StatisticsActivity.this, ResultActivity.class);
            startActivity(back);
            StatisticsActivity.this.finish();
        }
    };

    AdapterView.OnItemClickListener oclItem = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
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
                editor.apply();

                showStats(position, mode);

//                System.out.println("Word = " + word + "; Correct word = " + res + "; Correct answer = " + cor + "; User's answer = " + users);
            } else if (mode == 1) {
                showStats(position, mode);
            }
        }
    };



    protected void showStats(int position, int appMode) {
        SharedPreferences shared = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);

        LayoutInflater inflater;
        View layout;
//        final PopupWindow pw;

        if (appMode == 0) {
            String word = shared.getString(APP_STATS_RES_WORD + position, null);
            Boolean cor = Boolean.valueOf(shared.getString(APP_STATS_RES_CORRECT + position, null));
            Boolean users = Boolean.valueOf(shared.getString(APP_STATS_RES_USERS + position, null));
            String corWord = shared.getString(APP_STATS_COR_WORD + position, null);

            inflater = (LayoutInflater) StatisticsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.checker_activity_word_info, null);

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

            float dip = 240f;
            Resources r = getResources();
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.getDisplayMetrics()
            );

        } else {
            inflater = (LayoutInflater) StatisticsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.choice_activity_word_info, null);
            ArrayList<IncorrectChoiceResultObject> statWords = new ArrayList<>();

            multipleAdapter = new MultipleModeAdapter(this, statWords);
            ListView statListView = layout.findViewById(R.id.statsChoiceList);
            statListView.setAdapter(multipleAdapter);

            int size = shared.getInt(APP_STATS_QUEST_SIZE + position, -1);

            int correctPos = shared.getInt(APP_STATS_RES_CORRECT_POSITION + position, -1);
            int userAnsPos = shared.getInt(APP_STATS_RES_USERS + position, -1);
            for (int i = 0; i < size; i++) {
                String word = shared.getString(APP_STATS_RES_WORD + "[" + position + "][" + i + "]", null);
                statWords.add(new IncorrectChoiceResultObject(word, correctPos, userAnsPos));
            }
        }

        int height = StatisticsActivity.this.getResources().getDisplayMetrics().heightPixels;
        int width = StatisticsActivity.this.getResources().getDisplayMetrics().widthPixels;

        pw = new Dialog(this) {
            public boolean dispatchTouchEvent(MotionEvent event)
            {
                pw.dismiss();
                return false;
            }
        };

        pw.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        pw.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setContentView(layout);

        Window window = pw.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setWindowAnimations(R.style.UpDialogAnimation);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        wlp.y = 30;
        wlp.width = width - 60;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        pw.setCanceledOnTouchOutside(true);
        pw.show();

    }
}