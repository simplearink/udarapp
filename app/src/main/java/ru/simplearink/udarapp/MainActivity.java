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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, this.getResources().getString(R.string.appID));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


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

