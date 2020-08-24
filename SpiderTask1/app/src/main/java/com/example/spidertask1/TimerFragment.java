package com.example.spidertask1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class TimerFragment extends Fragment {

    private EditText inputTime;
    private TextView countDown;
    private ImageView setTime;
    private ImageView startPause;
    private ImageView reset;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long startTimeInMS;
    private long timeLeftInMS;
    private long endTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_timer, container, false);

        inputTime = v.findViewById(R.id.editText_timer_enter);
        countDown = v.findViewById(R.id.textView_timer_countDown);
        setTime = v.findViewById(R.id.imageView_timer_done);
        startPause = v.findViewById(R.id.imageView_timer_start);
        reset = v.findViewById(R.id.imageView_timer_reset);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputTime.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(getContext(), "Enter a time", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                inputTime.setText("");
            }
        });

        startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        return v;
    }

    private void setTime(long milliseconds) {
        startTimeInMS = milliseconds;
        resetTimer();
    }
    
    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMS;
        countDownTimer = new CountDownTimer(timeLeftInMS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMS = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                updateWatchInterface();
            }
        }.start();
        timerRunning = true;
        updateWatchInterface();
    }
    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        updateWatchInterface();
    }
    private void resetTimer() {
        timeLeftInMS = startTimeInMS;
        updateCountDownText();
        updateWatchInterface();
    }
    private void updateCountDownText() {
        int hours = (int) (timeLeftInMS / 1000) / 3600;
        int minutes = (int) ((timeLeftInMS / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMS / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        countDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (timerRunning) {
            inputTime.setVisibility(View.INVISIBLE);
            setTime.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.INVISIBLE);
            startPause.setImageResource(R.drawable.ic_pause_circle);
        } else {
            inputTime.setVisibility(View.VISIBLE);
            setTime.setVisibility(View.VISIBLE);
            startPause.setImageResource(R.drawable.ic_play_circle);
            if (timeLeftInMS < 1000) {
                startPause.setVisibility(View.INVISIBLE);
            } else {
                startPause.setVisibility(View.VISIBLE);
            }
            if (timeLeftInMS < startTimeInMS) {
                reset.setVisibility(View.VISIBLE);
            } else {
                reset.setVisibility(View.INVISIBLE);
            }
        }
    }
   
    
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", startTimeInMS);
        editor.putLong("millisLeft", timeLeftInMS);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTime);
        editor.apply();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        startTimeInMS = prefs.getLong("startTimeInMillis", 600000);
        timeLeftInMS = prefs.getLong("millisLeft", startTimeInMS);
        timerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateWatchInterface();
        if (timerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMS = endTime - System.currentTimeMillis();
            if (timeLeftInMS < 0) {
                timeLeftInMS = 0;
                timerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
    
}
