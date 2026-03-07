package com.example.myapplication6;

import android.os.CountDownTimer;
import android.os.SystemClock;

public class GameTimer {

    // -------- Singleton --------
    private static GameTimer instance;

    public static synchronized GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

    private GameTimer() {}

    // -------- Timer Core (CountDownTimer) --------
    private static final long TICK_INTERVAL = 1000;

    private CountDownTimer countDownTimer;

    private long endTimeMillis;     // absolute end time (for pause precision)
    private long timeLeftMillis;    // cached remaining time
    private boolean isRunning = false;

    // -------- Listener --------
    public interface TimerListener {
        void onTick(long millisLeft);
        void onFinish();
    }

    private TimerListener listener;

    public void setListener(TimerListener listener) {
        this.listener = listener;
    }

    // -------- Public API --------
    public boolean isRunning() {
        return isRunning;
    }

    public void start(long durationMillis) {
        if (isRunning) return;

        stopInternal(); // safety
        timeLeftMillis = durationMillis;
        endTimeMillis = SystemClock.elapsedRealtime() + durationMillis;
        isRunning = true;

        countDownTimer = new CountDownTimer(durationMillis, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                if (listener != null) listener.onTick(timeLeftMillis);
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                isRunning = false;
                countDownTimer = null;
                if (listener != null) listener.onFinish();
            }
        }.start();
    }

    public void pause() {
        if (!isRunning) return;

        // try to be precise using endTimeMillis
        long now = SystemClock.elapsedRealtime();
        timeLeftMillis = endTimeMillis - now;
        if (timeLeftMillis < 0) timeLeftMillis = 0;

        stopInternal();
    }

    public void resume() {
        if (isRunning || timeLeftMillis <= 0) return;

        long durationMillis = timeLeftMillis;
        endTimeMillis = SystemClock.elapsedRealtime() + durationMillis;
        isRunning = true;

        countDownTimer = new CountDownTimer(durationMillis, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                if (listener != null) listener.onTick(timeLeftMillis);
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                isRunning = false;
                countDownTimer = null;
                if (listener != null) listener.onFinish();
            }
        }.start();
    }

    public void stop() {
        timeLeftMillis = 0;
        stopInternal();
    }

    // (שם לא משהו אבל משאיר כדי שלא ישבור לך קריאות קיימות)
    public long getElaspeTime() {
        if (isRunning) {
            long now = SystemClock.elapsedRealtime();
            timeLeftMillis = endTimeMillis - now;
            if (timeLeftMillis < 0) timeLeftMillis = 0;
        }
        return timeLeftMillis;
    }

    public long getTimeLeftMillis() {
        if (isRunning) {
            long now = SystemClock.elapsedRealtime();
            timeLeftMillis = endTimeMillis - now;
            if (timeLeftMillis < 0) timeLeftMillis = 0;
        }
        return timeLeftMillis;
    }

    // -------- Internal --------
    private void stopInternal() {
        isRunning = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    // ===== Countdown API (CountDownTimer) =====
    public interface CountdownTick {
        void onTick(long millisLeft);
    }

    public interface CountdownFinish {
        void onFinish();
    }

    public void startCountdown(long durationMillis, CountdownTick tick, CountdownFinish finish) {
        stopInternal();

        timeLeftMillis = durationMillis;
        endTimeMillis = SystemClock.elapsedRealtime() + durationMillis;
        isRunning = true;

        countDownTimer = new CountDownTimer(durationMillis, TICK_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                tick.onTick(timeLeftMillis);
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                isRunning = false;
                countDownTimer = null;
                finish.onFinish();
            }
        }.start();
    }

    // ===== Stopwatch =====
    // CountDownTimer לא מתאים לסטופר (סופר למטה). לכן נשאיר סטופר על בסיס SystemClock בלי "טיימר" רץ.
    private boolean stopwatchRunning = false;
    private long stopwatchStartMillis;
    private long stopwatchAccumulatedMillis;

    public void startStopwatch() {
        stopwatchRunning = true;
        stopwatchStartMillis = SystemClock.elapsedRealtime();
        stopwatchAccumulatedMillis = 0;
    }

    public long stopStopwatch() {
        if (!stopwatchRunning) return stopwatchAccumulatedMillis;
        stopwatchAccumulatedMillis = SystemClock.elapsedRealtime() - stopwatchStartMillis;
        stopwatchRunning = false;
        return stopwatchAccumulatedMillis;
    }
}