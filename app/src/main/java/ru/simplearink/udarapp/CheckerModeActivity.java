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
    private TextView resultTextView;
    private View applicationView;
    private ApiConnection connection;

    private boolean correctness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_mode);

        updateTextView = findViewById(R.id.textView);
        resultTextView = findViewById(R.id.resText);


        applicationView = findViewById(R.id.window);
        applicationView.setOnTouchListener(otlSwipe);

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
                                resultTextView.setText("Умница");
                                manageBlinkEffect(true);
                            } else {
                                resultTextView.setText("Лох");
                                manageBlinkEffect(false);
                            }
                        } else {
                            if (!correctness) {
                                resultTextView.setText("Умница");
                                manageBlinkEffect(true);
                            } else {
                                resultTextView.setText("Лох");
                                manageBlinkEffect(false);
                            }
                        }
                        updateWord();
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
        anim.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        anim.start();
    }
}
