package ru.simplearink.udarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StatisticsActivity extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        backButton = findViewById(R.id.statBack);
        backButton.setOnClickListener(oclBack);

        
    }

    View.OnClickListener oclBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent back = new Intent(StatisticsActivity.this, ResultActivity.class);
            startActivity(back);
        }
    };
}
