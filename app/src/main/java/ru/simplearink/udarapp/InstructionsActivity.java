package ru.simplearink.udarapp;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class InstructionsActivity extends AppCompatActivity {
    private TextView correctDot;
    private TextView incorrectDot;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        correctDot = findViewById(R.id.dotCorrect);
        incorrectDot = findViewById(R.id.dotIncorrect);

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(okButton);

        Animation right = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        right.setFillAfter(true);
        correctDot.startAnimation(right);

        Animation left = AnimationUtils.loadAnimation(this, R.anim.left_animation);
        left.setFillAfter(true);
        incorrectDot.startAnimation(left);
    }

    View.OnClickListener okButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(InstructionsActivity.this, CheckerModeActivity.class);
            startActivity(intent);
            InstructionsActivity.this.finish();
        }
    };

    @Override
    public void onBackPressed() {
    }
}
