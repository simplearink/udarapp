package ru.simplearink.udarapp;

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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private ImageButton settingsButton;
    private Button checkerModeButton;
    private Button chooseModeButton;
    private AdView mAdView;

    public static final String APP_PREFERENCES = "udarSettings";
    public static final String APP_PREFERENCES_EGE = "egeSwitch";
    public static final String APP_PREFERENCES_INSTRUCTIONS = "instructions";
    public static final String APP_PREFERENCES_FIRST_START = "firstStart";

    private CheckerResultObject checkerMode;
    private IncorrectChoiceResultObject incorrectChoiceMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        downloadChecker();
        downloadIncorrect();

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
            intent.putExtra("checker", checkerMode);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };

    View.OnClickListener oclBtnChoose = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, IncorrectChoiceModeActivity.class);
            intent.putExtra("choose", incorrectChoiceMode);
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

    @Override
    public void onBackPressed() {
    }

    public void downloadFirst() {
        downloadChecker();
        downloadIncorrect();
    }

    private void downloadChecker() {
        final SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiConnection checkerConn = new ApiConnection(sharedPreferences.getBoolean(MainActivity.APP_PREFERENCES_EGE, false), 0, 1);
                checkerConn.execute();

                String[][] res = null;
                try {
                    res = checkerConn.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                checkerMode = new CheckerResultObject(0, Integer.parseInt(res[2][0]), res[0][0], res[1][0], true, res[3][0]);
            }
        }).start();

    }

    public void downloadIncorrect() {
        final SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiConnection choiceConn = new ApiConnection(sharedPreferences.getBoolean(MainActivity.APP_PREFERENCES_EGE, false), 1, 3);
                choiceConn.execute();

                String[][] choice = null;
                try {
                    choice = choiceConn.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int correctPosition = -1;

                String correctWordID = choice[1][0];
                ArrayList<String> words = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    if (choice[2][i].equals(correctWordID)) {
                        correctPosition = i;
                    }
                    ids.add(choice[2][i]);
                    words.add(choice[0][i].substring(1, choice[0][i].length() - 1));
                }

                incorrectChoiceMode = new IncorrectChoiceResultObject(3, ids, words, correctPosition, -1, false);
            }
        }).start();
    }
}

