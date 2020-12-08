package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private long START_TIME_IN_MILLIS ;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mAssign;
    private CountDownTimer mCountDownTimer;
    private MediaPlayer mp;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mAssign = findViewById(R.id.assign);
        mp= MediaPlayer.create(getApplicationContext(),R.raw.tribe);

        mAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i,j;
                try {
                    EditText minutes = findViewById(R.id.time);
                    i = Integer.parseInt(minutes.getText().toString());
                } catch (Exception E){
                    i=0;
                }
                try {
                EditText seconds = findViewById(R.id.seconds);
                j= Integer.parseInt(seconds.getText().toString());
                } catch (Exception E){
                    j=0;
                }
                i=i*60;
                i=i+j;
                if(i!=0) {
                    START_TIME_IN_MILLIS = i * 1000;
                    int minute = (int) (i) / 60;
                    int second = (int) (i) % 60;
                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minute, second);
                    mTextViewCountDown.setText(timeLeftFormatted);
                    mButtonStartPause.setVisibility(View.VISIBLE);
                }
                if(mTimerRunning){
                    mCountDownTimer.cancel();
                    START_TIME_IN_MILLIS+=1;
                    startTimer();
                }
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }
    private void startTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                if(!mp.isPlaying()) {
                    mp.start();
                }
                Toast.makeText(getApplicationContext(), "Timer done ", Toast.LENGTH_LONG).show();
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer() {
        if(mp.isPlaying()){
            mp.seekTo(0);
            mp.pause();
        }
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
}