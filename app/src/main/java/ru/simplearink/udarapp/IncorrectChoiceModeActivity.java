package ru.simplearink.udarapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static ru.simplearink.udarapp.ResultActivity.APP_STATS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_ANS_CORRECT;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_QUEST_SIZE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_CORRECT;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_CORRECT_POSITION;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_ID;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_SIZE;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_USERS;
import static ru.simplearink.udarapp.ResultActivity.APP_STATS_RES_WORD;

public class IncorrectChoiceModeActivity extends AppCompatActivity {

    private ApiConnection connection;
    private IncorrectChoiceGameController currentData;
    IncorrectChoiceResultObject currentQuestion;
    private IncorrectChoiceModeAdapter adapter;
    String correctWordID;
    ArrayList<String> words;
    ArrayList<String> ids;

    private Button finishGame;
    private TextView level;
    private TextView inARow;
    private View applicationView;
    private AdView mAdView;

    int currentLevel = 3;
    int inARowNow = 0;
    int counter = 0;
    int correctCounter = 0;

    double startTime = 0;
    double bestTime = 60000;
    double wholeTime = 0;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_mode);

        IncorrectChoiceResultObject firstObj = (IncorrectChoiceResultObject) getIntent().getSerializableExtra("choose");

        MobileAds.initialize(this, this.getResources().getString(R.string.appID));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.appInterstitalID));

        mInterstitialAd.setAdListener(mInterstitialAdListener);

        finishGame = findViewById(R.id.finishMultiple);
        finishGame.setOnClickListener(finishGameocl);

        level = findViewById(R.id.level);
        inARow = findViewById(R.id.inARow);

        applicationView = findViewById(R.id.windowMultiple);

        currentData = new IncorrectChoiceGameController();

        if (firstObj != null) {
            putAsFirst(firstObj);
        } else {
            updateWord(3);
        }
    }

    AdListener mInterstitialAdListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            mInterstitialAd.show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    };

    View.OnClickListener finishGameocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.show();
            Intent result = new Intent(IncorrectChoiceModeActivity.this, ResultActivity.class);

            SharedPreferences statistics = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = statistics.edit();
            editor.putInt(APP_STATS_RES_SIZE, counter);
            editor.apply();

            for (int i = 0; i < counter; i++) {
                IncorrectChoiceResultObject obj = currentData.get(i);
                int objSize = obj.getSize();
                editor.putInt(APP_STATS_QUEST_SIZE + i, objSize);
                editor.putString(APP_STATS_RES_CORRECT + i, correctWordID);
                for (int j = 0; j < objSize; j++) {
                    editor.putString(APP_STATS_RES_WORD + "[" + i + "][" + j + "]", obj.getWord(j));
                    editor.putString(APP_STATS_RES_ID + "[" + i + "][" + j + "]", obj.getWordID(j));
                }
                editor.putInt(APP_STATS_RES_CORRECT_POSITION + i, obj.getAnswer());
                editor.putInt(APP_STATS_RES_USERS + i, obj.getUserAnswer());
                editor.putBoolean(APP_STATS_ANS_CORRECT + i, obj.isUserAnswerCorrect());
            }
            editor.apply();

            editor.putInt(ResultActivity.APP_STATS_MISTAKES, counter - correctCounter);
            editor.putInt(ResultActivity.APP_STATS_CORRECT, correctCounter);
            editor.putInt(ResultActivity.APP_STATS_WHOLE, counter);
            double avg;
            if (correctCounter == 0 || counter == 0) {
                avg = 0.0;
                bestTime = 0.0;
            } else {
                avg = wholeTime / counter / 1000;
                bestTime = bestTime / 1000;
            }
            editor.putString(ResultActivity.APP_STATS_BEST, String.format("%.1f", bestTime));
            editor.putString(ResultActivity.APP_STATS_AVG, String.format("%.1f", avg));
            editor.putInt(ResultActivity.APP_MODE, 1);
            editor.apply();

            startActivity(result);
            IncorrectChoiceModeActivity.this.finish();
        }
    };

    AdapterView.OnItemClickListener oclItem = new AdapterView.OnItemClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            boolean correctness = isCorrect(ids.get(position), correctWordID);
            double ansTime = System.currentTimeMillis() - startTime;
            wholeTime += ansTime;
            if (correctness) {
                manageBlinkEffect(true);
                inARowNow++;
                counter++;
                correctCounter++;
                if (ansTime < bestTime) {
                    bestTime = ansTime;
                }
            } else {
                manageBlinkEffect(false);
                counter++;
                inARowNow = 0;
            }
            currentQuestion.setCorrectness(correctness);
            currentQuestion.setUserAnswer(position);

            if (inARowNow == currentLevel) {
                inARowNow = 0;
                currentLevel++;
            }
            level.setText(String.valueOf(currentLevel));
            inARow.setText(String.valueOf(inARowNow));
            updateWord(currentLevel);
        }
    };

    public void updateWord(int level) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        connection = new ApiConnection(sharedPreferences.getBoolean(MainActivity.APP_PREFERENCES_EGE, false), 1, level);
        connection.execute();

        String[][] res = null;
        try {
            res = connection.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int correctPosition = -1;

        correctWordID = res[1][0];
        words = new ArrayList<>();
        ids = new ArrayList<>();

        for (int i = 0; i < level; i++) {
            if (res[2][i].equals(correctWordID)) {
                correctPosition = i;
            }
            ids.add(res[2][i]);
            words.add(res[0][i].substring(1, res[0][i].length() - 1));
        }

        adapter = new IncorrectChoiceModeAdapter(this, words);
        ListView listView = findViewById(R.id.choiceModeWords);
        listView.setOnItemClickListener(oclItem);
        listView.setAdapter(adapter);

        currentQuestion = new IncorrectChoiceResultObject(currentLevel, ids, words, correctPosition, -1, false);
        currentData.add(currentQuestion);
        startTime = System.currentTimeMillis();
    }

    protected boolean isCorrect(String id1, String id2) {
        if (id1.equals(id2)) {
            return true;
        } else {
            return false;
        }
    }

    private void manageBlinkEffect(boolean correctness) {
        ObjectAnimator anim;
        if (correctness) {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", getResources().getColor(R.color.correctAnswer), Color.WHITE);
        } else {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", getResources().getColor(R.color.wrongAnswer), Color.WHITE);
        }
        anim.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        anim.start();
    }

    public void putAsFirst(IncorrectChoiceResultObject obj) {
        int correctPosition = obj.getAnswer();
        correctWordID = obj.getWordID(correctPosition);
        words = obj.getWords();
        ids = obj.getWordsIDs();

        adapter = new IncorrectChoiceModeAdapter(this, words);
        ListView listView = findViewById(R.id.choiceModeWords);
        listView.setOnItemClickListener(oclItem);
        listView.setAdapter(adapter);

        currentQuestion = obj;
        currentData.add(currentQuestion);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onBackPressed() {
    }
}