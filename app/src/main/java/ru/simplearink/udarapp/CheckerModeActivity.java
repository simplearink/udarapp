package ru.simplearink.udarapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class CheckerModeActivity extends Activity {
    private TextView updateTextView;
    private TextView resultTextView;
    private Button updateButton;
    private View applicationView;
    private ApiConnection connection;

    private boolean correctness;
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_mode);

        updateTextView = findViewById(R.id.textView);
        resultTextView = findViewById(R.id.resText);


        updateButton = findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(oclBtnStart);

        applicationView = findViewById(R.id.window);
        applicationView.setOnTouchListener(otlSwipe);

    }

    View.OnClickListener oclBtnStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            updateWord();
            clicked = true;
        }
    };

    View.OnTouchListener otlSwipe = new View.OnTouchListener() {
        float startX;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (clicked) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: //первое касание
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP: //отпускание
                        float stopX = event.getX();
                        if (stopX > startX) {
                            if (correctness) {
                                resultTextView.setText("Умница");
                            } else {
                                resultTextView.setText("Лох");
                            }
                        } else {
                            if (!correctness) {
                                resultTextView.setText("Умница");
                            } else {
                                resultTextView.setText("Лох");
                            }
                        }
                        updateWord();
                        break;
                }
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
}
