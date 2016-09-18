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
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public TextView label;

    public double timerValue;
    public MediaPlayer media;
    public int git = R.raw.git;
    public int rah = R.raw.rah;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x = 0, last_y = 0, last_z = 0;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        gitStart();

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_SHOW_UI);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                while (speed > SHAKE_THRESHOLD) {
                    rahStart();
                    curTime = System.currentTimeMillis();
                    if ((curTime - lastUpdate) > 100) {
                        speed = Math.abs(last_x + last_y + last_z - sensorEvent.values[0] - sensorEvent.values[1] - sensorEvent.values[2]) / diffTime * 10000;
                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                onSensorChanged(senAccelerometer);
            }
            public void onFinish() {
                label.setText("Finished!");
            }
        }.start();
    }
}

