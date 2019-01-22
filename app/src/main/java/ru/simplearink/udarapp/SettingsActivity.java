package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends Activity {
    private Button backButton;
    private Switch egeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(oclBtnBack);

        egeSwitch = findViewById(R.id.egeSwitch);
        egeSwitch.setOnClickListener(oclEgeSwitch);

    }

    View.OnClickListener oclBtnBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener oclEgeSwitch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };


}