package com.cshum.productiivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Vibrator;
public class MainActivity extends AppCompatActivity {
    public TextView label;

    public double timerValue;
    public MediaPlayer media;
    public int git = R.raw.git;
    public int rah = R.raw.rah;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gitStart();

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_SHOW_UI);
    }


    public void gitStart() {
        media = MediaPlayer.create(this, git);
        media.start();
    }

    public void rahStart() {
        media = MediaPlayer.create(this, rah);
        media.start();
    }

    public void timerStart() {

    }

    public void startTimer(View view) {
        label = (TextView) findViewById(R.id.labelTxt);
        EditText time = (EditText) findViewById(R.id.editTxt);

        label.setText(time.getText().toString());
        timerValue = Double.parseDouble(time.getText().toString());

        new CountDownTimer(Long.parseLong(time.getText().toString())*60000, 1000) {
            public void onTick(long millisUntilFinished) {
                label.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                label.setText("Finished!");
                rahStart();
            }
        }.start();
    }
}

