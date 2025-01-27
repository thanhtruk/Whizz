package com.quizchic.whizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import services.BackgroundMusicService;

public class SettingActivity extends AppCompatActivity {
    public static Boolean isPlaying = false;

    public static Boolean isPlaySE =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ImageButton home = (ImageButton)findViewById(R.id.home);
        ImageButton user = (ImageButton) findViewById(R.id.user);
        ImageButton add = (ImageButton) findViewById(R.id.add);

        Switch sbg_switch = (Switch) findViewById(R.id.switch_sound1);

        Switch se_switch = (Switch) findViewById(R.id.switch_sound2);
        final SharedPreferences sharedPreferencesSbg = getPreferences(MODE_PRIVATE);
        sbg_switch.setChecked(sharedPreferencesSbg.getBoolean("isEnabled", false));
        final SharedPreferences sharedPreferencesSe = getPreferences(MODE_PRIVATE);
        se_switch.setChecked(sharedPreferencesSe.getBoolean("isEnabled", false));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomeActivity = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(toHomeActivity);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddActivity = new Intent(SettingActivity.this, AddQuestionActivity.class);
                startActivity(toAddActivity);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUserActivity = new Intent(SettingActivity.this,UserActivity.class);
                startActivity(toUserActivity);
            }
        });

        sbg_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferencesSbg.edit().putBoolean("isEnabled", isChecked).apply();
                if (isChecked) {
                   if (!isPlaying) {
                       startService(new Intent(SettingActivity.this, BackgroundMusicService.class));
                       isPlaying= true;
                    }
                } else {
                    if (isPlaying) {
                        stopService(new Intent(SettingActivity.this, BackgroundMusicService.class));
                        isPlaying = false;
                    }
                }
            }
        });
        se_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferencesSe.edit().putBoolean("isEnabled", isChecked).apply();
                if (isChecked) {
                    if (!isPlaySE) {
                        isPlaySE= true;
                    }
                } else {
                    if (isPlaySE) {
                        isPlaySE = false;
                    }
                }
            }
        });
    }
}