package ru.simplearink.udarapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class CheckerModeActivity extends Activity {
    private TextView updateTextView;
    private View applicationView;
    private ApiConnection connection;
    private SingleGameController stats;

    private boolean correctness;
    private boolean user;

    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_mode);

        updateTextView = findViewById(R.id.textView);


        applicationView = findViewById(R.id.window);
        applicationView.setOnTouchListener(otlSwipe);

        stats = new SingleGameController();
        updateWord();
    }

    View.OnTouchListener otlSwipe = new View.OnTouchListener() {
        float startX;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: //первое касание
                        startX = event.getX();
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
                        stats.getLast().setUserAnswer(user);
                        updateWord();
                        counter++;
                        if (counter == 10) {
                            for (int i = 0; i < stats.size(); i++) {
                                System.out.println(stats.get(i).getWordId() + " " + stats.get(i).getWord() +
                                        " " + stats.get(i).getAnswer() + " " + stats.get(i).getUserAnswer());
                            }
                            counter = 0;
                        }
                        break;
                }
            return true;
        }
    };

    public void updateWord() {

        connection = new ApiConnection();
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

        stats.add(new SingleResultObject(Integer.parseInt(res[2]), res[0], res[1], true));
    }

    public boolean correct(String answer) {
        if (answer.equals("true"))
            return true;
        else return false;
    }

    private void manageBlinkEffect(boolean correctness) {
        ObjectAnimator anim;
        ObjectAnimator anim2;
        if (correctness) {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", Color.GREEN, Color.WHITE);
            anim2 = ObjectAnimator.ofInt(updateTextView,
                    "backgroundColor", Color.GREEN, R.color.colorPrimaryDark);
        } else {
            anim = ObjectAnimator.ofInt(applicationView,
                    "backgroundColor", Color.RED, Color.WHITE);
            anim2 = ObjectAnimator.ofInt(updateTextView,
                    "backgroundColor", Color.RED, R.color.colorPrimaryDark);
        }
        anim.setDuration(700);
        anim2.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        anim2.setEvaluator(new ArgbEvaluator());
        anim.start();
        anim2.start();
    }
}
