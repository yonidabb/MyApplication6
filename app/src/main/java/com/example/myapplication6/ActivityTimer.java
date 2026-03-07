package com.example.myapplication6;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import com.example.myapplication6.jv.AfterLoginActivity;

import androidx.fragment.app.Fragment;

import com.example.myapplication6.jv.NextActivity;
import com.example.myapplication6.jv.ScoresActivity;
import com.example.myapplication6.jv.TimerFragment;

public class ActivityTimer {

    private TimerFragment fragment;
    private boolean gameFinished = false;

    public void setFragment(TimerFragment fragment) {
        this.fragment = fragment;
    }

    private static ActivityTimer instance;

    public static synchronized ActivityTimer getInstance() {
        if (instance == null) {
            instance = new ActivityTimer();
        }
        return instance;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private boolean isRunning = false;


    private ActivityTimer() {}

    private CountDownTimer timer;
    private long timeLeftMillis = 5 * 60 * 1000; // 5 דקות
    private long startedAt;

    public long getTimeLeftMillis() {
        return timeLeftMillis;
    }

    public long getGameTime() {
        return SystemClock.elapsedRealtime()-startedAt;
    }

    public void startTimer() {
        startedAt = SystemClock.elapsedRealtime();
        isRunning=true;

        timer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                if (fragment != null) {
                    fragment.updateUI();
                }
            }
            public void finishGame() {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                isRunning = false;
            }

            @Override
            public void onFinish() {
                if (gameFinished) return;

                isRunning = false;

                if (fragment != null && fragment.isAdded() && fragment.getActivity() != null) {
                    Intent intent = new Intent(fragment.getActivity(), AfterLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    fragment.startActivity(intent);
                    fragment.getActivity().finish();
                }
            }
        }.start();
    }

    public void finishGame() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        isRunning = false;
    }
}
