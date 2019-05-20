package ru.simplearink.udarapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class CheckerModeActivity extends Activity {
    private TextView updateTextView;
    private View applicationView;
    private ApiConnection connection;
    private TextView mistakesCounter;
    private TextView rightCounter;
    private Button finishBtn;
    private TextView timerTextView;

    private SingleGameController stats;
    private SingleResultObject currentWordData;

    private boolean correctness;
    private boolean user;

    int counter = 0;
    int correctCounter = 0;
    double startTime = 0;
    double bestTime = 60000;
    double wholeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_mode);

        timerTextView = findViewById(R.id.time);

        finishBtn = findViewById(R.id.finishSingle);
        finishBtn.setOnClickListener(oclFinishBtn);

        updateTextView = findViewById(R.id.textView);
        mistakesCounter = findViewById(R.id.counterChecker);
        rightCounter = findViewById(R.id.sameCounterChecker);

        applicationView = findViewById(R.id.window);
        applicationView.setOnTouchListener(otlSwipe);

        stats = new SingleGameController();

        updateWord();
        timerTextView.setText("60");
        gameTimer.start();
    }

    CountDownTimer gameTimer = new CountDownTimer(60000, 100) {

        public void onTick(long millisUntilFinished) {
            timerTextView.setText(String.valueOf((double) millisUntilFinished / 1000));
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
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: //первое касание
                    startX = event.getX();
                    counter++;
                    break;
                case MotionEvent.ACTION_UP: //отпускание
                    float stopX = event.getX();
                    if (stopX > startX) {
                        if (correctness) {
                            manageBlinkEffect(true);
                        } else {
                            manageBlinkEffect(false);
                        }
                        user = true;
                    } else {
                        if (!correctness) {
                            manageBlinkEffect(true);
                        } else {
                            manageBlinkEffect(false);
                        }
                        user = false;
                    }
                    if (user == correctness) {
                        String r = String.valueOf(++correctCounter);
                        rightCounter.setText(String.valueOf(r));
                        double ansTime = System.currentTimeMillis() - startTime;
                        if (bestTime > ansTime) {
                            bestTime = ansTime;
                        }
                        wholeTime += ansTime;
                    }
                    mistakesCounter.setText(String.valueOf(counter - correctCounter));
                    currentWordData.setUserAnswer(user);
                    stats.add(currentWordData);
                    updateWord();
                    break;
            }
            return true;
        }
    };

    View.OnClickListener oclFinishBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent result = new Intent(CheckerModeActivity.this, ResultActivity.class);
            result.putExtra("size", stats.size());
            for (int i = 0; i < stats.size(); i++) {
                System.out.println(stats.get(i).getWordId() + " " + stats.get(i).getWord() +
                        " " + stats.get(i).getAnswer() + " " + stats.get(i).getUserAnswer());
                result.putExtra("stats" + i, stats.get(i));
            }
            /*result.putExtra("bestTime", bestTime);
            result.putExtra("mistakes", counter - correctCounter);
            result.putExtra("correct", correctCounter);
            result.putExtra("avgTime", wholeTime / correctCounter);*/

            SharedPreferences stats = getSharedPreferences(ResultActivity.APP_STATS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = stats.edit();
            editor.putInt(ResultActivity.APP_STATS_MISTAKES, counter - correctCounter);
            editor.putInt(ResultActivity.APP_STATS_CORRECT, correctCounter);
            editor.putInt(ResultActivity.APP_STATS_WHOLE, counter);
            editor.putString(ResultActivity.APP_STATS_BEST, String.format("%.1f", bestTime / 1000));
            editor.putString(ResultActivity.APP_STATS_AVG, String.format("%.1f", (wholeTime / correctCounter) / 1000));
            editor.apply();
            startActivity(result);
        }
    };

    public void updateWord() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

        connection = new ApiConnection(sharedPreferences.getBoolean(MainActivity.APP_PREFERENCES_EGE, false), 0);
        connection.execute();

        String[] res = null;
        try {
            res = connection.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        updateTextView.setText(res[0]);
        correctness = correct(res[1]);

        currentWordData = new SingleResultObject(Integer.parseInt(res[2]), res[0], res[1], true);
        startTime = System.currentTimeMillis();
    }

    public boolean correct(String answer) {
        if (answer.equals("true"))
            return true;
        else return false;
    }

    private void manageBlinkEffect(boolean correctness) {
        ObjectAnimator anim;
        //ObjectAnimator anim2;
        if (correctness) {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", Color.GREEN, Color.WHITE);
            //anim2 = ObjectAnimator.ofInt(updateTextView,
            //"background", Color.GREEN, R.color.colorPrimaryDark);
        } else {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", Color.RED, Color.WHITE);
            //anim2 = ObjectAnimator.ofInt(updateTextView,
            //"background", Color.RED, R.color.colorPrimaryDark);
        }
        anim.setDuration(700);
        // anim2.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        //anim2.setEvaluator(new ArgbEvaluator());
        anim.start();
        //anim2.start();
    }
}
