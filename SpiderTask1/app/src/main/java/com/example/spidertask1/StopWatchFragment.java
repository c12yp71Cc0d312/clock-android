package com.example.spidertask1;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StopWatchFragment extends Fragment {

    private Chronometer chronometer;
    private long pauseOffsetTime;
    private boolean running;

    private ImageView start, stop, reset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_stopwatch, container, false);

        start = v.findViewById(R.id.imageView_startStopWatch);
        stop = v.findViewById(R.id.imageView_pauseStopWatch);
        reset = v.findViewById(R.id.imageView_resetStopWatch);

        chronometer = v.findViewById(R.id.chronometer_stopWatch);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffsetTime);
                    chronometer.start();
                    running = true;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    chronometer.stop();
                    pauseOffsetTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffsetTime = 0;
            }
        });

        return v;
    }
}
