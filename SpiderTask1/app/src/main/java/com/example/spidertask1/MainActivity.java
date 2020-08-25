package com.example.spidertask1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    AlarmFragment alarmFragment;
    TimerFragment timerFragment;
    StopWatchFragment stopWatchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmFragment = new AlarmFragment();
        timerFragment = new TimerFragment();
        stopWatchFragment = new StopWatchFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, alarmFragment, "alarm").commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFrag = null;

            switch (item.getItemId()) {
                case R.id.alarm:
                    selectedFrag = alarmFragment;
                    break;
                case R.id.stopwatch:
                    selectedFrag = stopWatchFragment;
                    break;
                case R.id.timer:
                    selectedFrag = timerFragment;
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, selectedFrag).commit();

            return true;
        }
    };

}
