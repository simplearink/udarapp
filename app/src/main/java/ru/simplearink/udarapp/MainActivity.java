package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {
    private ImageButton settingsButton;
    private Button checkerModeButton;
    private Button chooseModeButton;
    private AdView mAdView;

    public static final String APP_PREFERENCES = "udarSettings";
    public static final String APP_PREFERENCES_EGE = "egeSwitch";
    public static final String APP_PREFERENCES_INSTRUCTIONS = "instructions";
    public static final String APP_PREFERENCES_FIRST_START = "firstStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, this.getResources().getString(R.string.appID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
            SharedPreferences AppSettings;
            AppSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

            Intent intent;

            if (AppSettings.getBoolean(APP_PREFERENCES_INSTRUCTIONS, false)) {
                intent = new Intent(MainActivity.this, InstructionsActivity.class);
            } else if (AppSettings.getBoolean(APP_PREFERENCES_FIRST_START, true)) {
                SharedPreferences.Editor editor = AppSettings.edit();
                editor.putBoolean(APP_PREFERENCES_FIRST_START, false);
                editor.apply();
                intent = new Intent(MainActivity.this, InstructionsActivity.class);
            } else {
                intent = new Intent(MainActivity.this, CheckerModeActivity.class);
            }

            startActivity(intent);
            MainActivity.this.finish();
        }
    };

    View.OnClickListener oclBtnChoose = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, IncorrectChoiceModeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };

    View.OnClickListener oclBtnSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };


}

