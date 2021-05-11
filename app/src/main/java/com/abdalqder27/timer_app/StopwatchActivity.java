package com.abdalqder27.timer_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.String.format;

public class StopwatchActivity extends AppCompatActivity {
    private int millisecond = 0;
    private boolean running;
    private boolean wasRunning;
    @BindView(R2.id.onStart)
    Button startButton;
    @BindView(R2.id.onStop)
    Button stopButton;
    @BindView(R2.id.timerText)
    TextView textViewTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            millisecond = Integer.parseInt(savedInstanceState.getString("seconds"));
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        click();
        runTimer();
    }

    private void click() {
        startButton.setOnClickListener(v -> {
            if (running) {

                running = false;
                startButton.setText("Resume");
                startButton.setBackgroundColor(getResources().getColor(R.color.green));

            } else {
                running = true;
                startButton.setText("Pause");
                startButton.setBackgroundColor(getResources().getColor(R.color.red));


            }
            stopButton.setVisibility(View.VISIBLE);

        });
        stopButton.setOnClickListener(v -> {
            running = false;
            millisecond = 0;
            stopButton.setVisibility(View.GONE);
            startButton.setText("Start");
            startButton.setBackgroundColor(getResources().getColor(R.color.green));

        });
    }

    private void runTimer() {
        Handler handler = new Handler();
        handler.post(() -> {
            int second = millisecond / 100;
            int hours = second / 3600;
            int minutes = (second % (60 * 60)) / 60;
            int secs = second % 60;

            String time = format("%d: %1d%1d: %1d%1d : %1d%1d", hours, minutes / 10, minutes % 10, secs / 10, secs % 10, (millisecond % 100) / 10, millisecond % 10);
            textViewTimer.setText(time);
            if (running) millisecond++;
            handler.postDelayed(this::runTimer, 1);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("second", String.valueOf(millisecond));
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        wasRunning = running;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }
}