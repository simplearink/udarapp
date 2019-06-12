package ru.simplearink.udarapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static ru.simplearink.udarapp.ResultActivity.APP_STATS;


public class CheckerModeActivity extends AppCompatActivity {
    private TextView updateTextView;
    private View applicationView;
    private ApiConnection connection;
    private TextView mistakesCounter;
    private TextView rightCounter;
    private Button finishBtn;
    private TextView timerTextView;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    private CheckerGameController stats;
    private CheckerResultObject currentWordData;
    private ArrayList<CheckerResultObject> queue;
    private int usedFromQueue = 0;

    private boolean correctness;
    private boolean user;

    int counter = 0;
    int correctCounter = 0;
    double startTime = 0;
    double bestTime = 60000;
    double wholeTime = 0;
    int swipeCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_mode);
        queue = new ArrayList<>();
        makeQueue();

        MobileAds.initialize(this, this.getResources().getString(R.string.appID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.appInterstitalID));

        mInterstitialAd.setAdListener(mInterstitialAdListener);

        timerTextView = findViewById(R.id.time);

        finishBtn = findViewById(R.id.finishSingle);
        finishBtn.setOnClickListener(oclFinishBtn);

        updateTextView = findViewById(R.id.textView);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(updateTextView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        mistakesCounter = findViewById(R.id.counterChecker);
        rightCounter = findViewById(R.id.sameCounterChecker);

        applicationView = findViewById(R.id.window);
        applicationView.setOnTouchListener(otlSwipe);

        stats = new CheckerGameController();

        updateWord();
        timerTextView.setText("60");
        gameTimer.start();
        updateWord();
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

    CountDownTimer gameTimer = new CountDownTimer(60000, 100) {

        public void onTick(long millisUntilFinished) {
            timerTextView.setText(String.format("%.1f", (double) millisUntilFinished / 1000));
        }

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        public void onFinish() {
            timerTextView.setText("0");
            finishBtn.callOnClick();
        }
    };


    View.OnTouchListener otlSwipe = new View.OnTouchListener() {
        float startX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (currentWordData.getWord().equals(updateTextView.getText()) && swipeCounter == 0 && System.currentTimeMillis() - startTime > 150) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //первое касание
                    startX = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    float stopX = event.getX();
                    counter++;
                    swipeCounter++;
                    if (stopX > startX) {
                        if (correctness) {
                            correctCounter++;
                            manageBlinkEffect(true);
                        } else {
                            manageBlinkEffect(false);
                        }
                        user = true;
                    } else if (stopX < startX) {
                        if (!correctness) {
                            manageBlinkEffect(true);
                            correctCounter++;
                        } else {
                            manageBlinkEffect(false);
                        }
                        user = false;
                    }
                    currentWordData.setUserAnswer(user);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    }).start();
                }
            }
            return true;
        }
    };

    View.OnClickListener oclFinishBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.show();
            Intent result = new Intent(CheckerModeActivity.this, ResultActivity.class);
            SharedPreferences statistics = getSharedPreferences(APP_STATS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = statistics.edit();
            editor.putInt(ResultActivity.APP_STATS_RES_SIZE, stats.size());
            editor.apply();
            for (int i = 0; i < stats.size(); i++) {
                editor.putInt(ResultActivity.APP_STATS_RES_ID + i, stats.get(i).getWordId());
                editor.putString(ResultActivity.APP_STATS_RES_WORD + i, stats.get(i).getWord());
                editor.putString(ResultActivity.APP_STATS_RES_CORRECT + i, stats.get(i).getAnswer());
                editor.putString(ResultActivity.APP_STATS_RES_USERS + i, String.valueOf(stats.get(i).getUserAnswer()));
                editor.putString(ResultActivity.APP_STATS_COR_WORD + i, stats.get(i).getCorrectWord());
            }

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
            editor.putInt(ResultActivity.APP_MODE, 0);
            editor.apply();
            startActivity(result);
            CheckerModeActivity.this.finish();
        }
    };

    public void updateWord() {
        if (counter != 0) {
            double ansTime = System.currentTimeMillis() - startTime;
            if (user == correctness) {
                String r = String.valueOf(correctCounter);
                rightCounter.setText(r);
                if (bestTime > ansTime) {
                    bestTime = ansTime;
                }
            }
            wholeTime += ansTime;
            mistakesCounter.setText(String.valueOf(counter - correctCounter));
            stats.add(currentWordData);

            if (queue.size() > usedFromQueue) {
                getFromQueue();
            } else {
                ifQueueEmptyUpdate();
            }
        } else {
            ifQueueEmptyUpdate();
        }
        startTime = System.currentTimeMillis();
    }

    public boolean correct(String answer) {
        if (answer.equals("true"))
            return true;
        else return false;
    }

    private void manageBlinkEffect(boolean correctness) {
        ObjectAnimator anim;
        if (correctness) {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", Color.GREEN, Color.WHITE);
        } else {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", Color.RED, Color.WHITE);
        }
        anim.setDuration(500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.start();
    }

    private void makeQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (queue.size() < 100) {
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

                    connection = new ApiConnection(sharedPreferences.getBoolean(MainActivity.APP_PREFERENCES_EGE, false), 0, -1);
                    connection.execute();

                    String[][] res = null;
                    try {
                        res = connection.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    queue.add(new CheckerResultObject(0, Integer.parseInt(res[2][0]), res[0][0], res[1][0], true, res[3][0]));
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateWord();
        }
    };

    private void ifQueueEmptyUpdate() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        connection = new ApiConnection(sharedPreferences.getBoolean(MainActivity.APP_PREFERENCES_EGE, false), 0, -1);
        connection.execute();

        String[][] res = null;
        try {
            res = connection.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentWord = res[0][0];

        updateTextView.setText(currentWord);
        correctness = correct(res[1][0]);

        currentWordData = new CheckerResultObject(0, Integer.parseInt(res[2][0]), res[0][0], res[1][0], true, res[3][0]);
        swipeCounter = 0;
    }

    private void getFromQueue() {
        currentWordData = queue.get(usedFromQueue);
        usedFromQueue++;

        String currentWord = currentWordData.getWord();

        updateTextView.setText(currentWord);
        correctness = correct(currentWordData.getAnswer());
        if (usedFromQueue == 100) {
            queue = new ArrayList<>();
            makeQueue();
            usedFromQueue = 0;
        }
        swipeCounter = 0;
    }

    @Override
    public void onBackPressed() {
    }
}

