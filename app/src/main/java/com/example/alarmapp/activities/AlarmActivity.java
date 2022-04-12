package com.example.alarmapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alarmapp.Alarm;
import com.example.alarmapp.R;
import com.google.gson.Gson;

import java.util.Objects;

public class AlarmActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    TextView tvMessage;
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        tvMessage = findViewById(R.id.tvMessage);

        // phát chuông báo thức
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.start();

        //lấy ra đối tượng báo thức lưu trong SharedPreferences
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = mPrefs.getString("alarm", "");
        Alarm alarm = gson.fromJson(json, Alarm.class);
        //set text tvMessage là thuộc tính getMessage trong alarm
        tvMessage.setText(alarm.getMessage());

        //sự kiện cho nút tắt báo thức: dừng nhạc và kết thúc activity
        findViewById(R.id.btTurnOffAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}