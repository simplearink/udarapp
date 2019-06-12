package ru.simplearink.udarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import static ru.simplearink.udarapp.MainActivity.APP_PREFERENCES_EGE;
import static ru.simplearink.udarapp.MainActivity.APP_PREFERENCES_INSTRUCTIONS;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton backButton;
    private Switch egeSwitch;
    private Switch instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(oclBtnBack);

        egeSwitch = findViewById(R.id.egeswitch);
        egeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_EGE, egeSwitch.isChecked());
                editor.apply();
            }

        });

        instructions = findViewById(R.id.switch2);
        instructions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_INSTRUCTIONS, instructions.isChecked());
                editor.apply();
            }

        });

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        egeSwitch.setChecked(sharedPreferences.getBoolean(APP_PREFERENCES_EGE, false));
        instructions.setChecked(sharedPreferences.getBoolean(APP_PREFERENCES_INSTRUCTIONS, false));
    }

    View.OnClickListener oclBtnBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            SettingsActivity.this.finish();
        }
    };

    @Override
    public void onBackPressed() {
    }

}
