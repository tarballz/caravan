package edu.cmps121.app;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static final private String LOG_TAG = "test2017app1";

    private int seconds = 0;

    private CountDownTimer timer = null;

    private static final int ONE_SECOND_IN_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayTime();
    }

    public void onClickPlus(View v) {
        seconds += 60;
        displayTime();
    }

    public void onClickMinus(View v) {
        seconds -= 60;
        displayTime();
    }

    public void onClickStop(View v) {
        cancelTimer();
        displayTime();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void onClickStart(View v) {
        if (seconds == 0)
            cancelTimer();
        if (timer == null) {
            timer = new CountDownTimer(seconds * ONE_SECOND_IN_MILLIS, ONE_SECOND_IN_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        Log.d(LOG_TAG, "Tick at " + millisUntilFinished);
                        timer.wait(1000);

                        seconds -= 1;
                        displayTime();

                        if (seconds == 0)
                            onFinish();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFinish() {
                    Log.d(LOG_TAG, "Timer reached 0:00");
                    cancelTimer();
                }
            };
            timer.start();
        }
    }

    private void displayTime() {
        Log.d(LOG_TAG, "Displaying time " + seconds);
        TextView v = (TextView) findViewById(R.id.display);
        int m = seconds / 60;
        int s = seconds % 60;
        v.setText(String.format("%d:%02d", m, s));
        // Manages the buttons.
        Button stopButton = (Button) findViewById(R.id.stop_button);
        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setEnabled(timer == null && seconds > 0);
        stopButton.setEnabled(timer != null && seconds > 0);
    }
}
