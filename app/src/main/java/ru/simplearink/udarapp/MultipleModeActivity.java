package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MultipleModeActivity extends Activity {

    private Button finishGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_mode);

        finishGame = findViewById(R.id.finishMultiple);
        finishGame.setOnClickListener(finishGameocl);
    }

    View.OnClickListener finishGameocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent result = new Intent(MultipleModeActivity.this, ResultActivity.class);
            startActivity(result);
        }
    };


}
