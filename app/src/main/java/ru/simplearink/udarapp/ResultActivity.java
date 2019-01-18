package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends Activity {
    private Button backButton;
    private TextView text;
    private String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        backButton = findViewById(R.id.toMainButton);
        backButton.setOnClickListener(oclBackMain);

        text = findViewById(R.id.textView2);
        
        Intent intent = getIntent();
        int size = intent.getIntExtra("size", 0);
        for (int i = 0; i < size; i++) {
            SingleResultObject res = (SingleResultObject) intent.getSerializableExtra("stats" + i);
            str+=(res.getWordId() + " " + res.getWord() +
                    " " + res.getAnswer() + " " + res.getUserAnswer());
        }

        text.setText(str);
    }

    View.OnClickListener oclBackMain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent backToMain = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(backToMain);
        }
    };
}
