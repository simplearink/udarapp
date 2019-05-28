package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity {
    private ImageButton settingsButton;
    private Button checkerModeButton;
    private Button chooseModeButton;

    public static final String APP_PREFERENCES = "udarSettings";
    public static final String APP_PREFERENCES_EGE = "egeSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences AppSettings;
        AppSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(oclBtnSettings);

        checkerModeButton = findViewById(R.id.buttonSingleMode);
        checkerModeButton.setOnClickListener(oclBtnCheck);

        chooseModeButton = findViewById(R.id.buttonMultipleMode);
        chooseModeButton.setOnClickListener(oclBtnChoose);
    }

    View.OnClickListener oclBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, CheckerModeActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener oclBtnChoose = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, IncorrectChoiceModeActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener oclBtnSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    };


}

