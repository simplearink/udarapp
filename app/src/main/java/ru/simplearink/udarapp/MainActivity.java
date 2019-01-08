package ru.simplearink.udarapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button checkerModeButton;
    private Button chooseModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkerModeButton = findViewById(R.id.buttonSingleMode);
        checkerModeButton.setOnClickListener(oclBtnCheck);

        chooseModeButton = findViewById(R.id.buttonMultipleMode);
        chooseModeButton.setOnClickListener(oclBtnChoose);
    }

    View.OnClickListener oclBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener oclBtnChoose = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


}

