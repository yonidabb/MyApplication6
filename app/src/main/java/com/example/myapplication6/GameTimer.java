package com.example.myapplication6;

import android.os.Handler;
import android.os.Looper;
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

    // -------- Timer Core --------
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    private long endTimeMillis;     // absolute end time
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

        endTimeMillis = SystemClock.elapsedRealtime() + durationMillis;
        isRunning = true;

        runnable = new Runnable() {
            @Override
            public void run() {
                long now = SystemClock.elapsedRealtime();
                timeLeftMillis = endTimeMillis - now;

                if (timeLeftMillis <= 0) {
                    timeLeftMillis = 0;
                    stopInternal();
                    if (listener != null) listener.onFinish();
                    return;
                }

                if (listener != null) listener.onTick(timeLeftMillis);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    public void pause() {
        if (!isRunning) return;

        timeLeftMillis = endTimeMillis - SystemClock.elapsedRealtime();
        stopInternal();
    }

    public void resume() {
        if (isRunning || timeLeftMillis <= 0) return;

        endTimeMillis = SystemClock.elapsedRealtime() + timeLeftMillis;
        isRunning = true;
        handler.post(runnable);
    }

    public void stop() {
        timeLeftMillis = 0;
        stopInternal();
    }

    public long getElaspeTime() {
        if (isRunning) {
            timeLeftMillis = endTimeMillis - SystemClock.elapsedRealtime();
            if (timeLeftMillis < 0) timeLeftMillis = 0;
        }
        return timeLeftMillis;
    }
    public long getTimeLeftMillis() {
        if (isRunning) {
            timeLeftMillis = endTimeMillis - SystemClock.elapsedRealtime();
            if (timeLeftMillis < 0) timeLeftMillis = 0;
        }
        return timeLeftMillis;
    }

    // -------- Internal --------
    private void stopInternal() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }
    // ===== Countdown API =====
    public interface CountdownTick {
        void onTick(long millisLeft);
    }

    public interface CountdownFinish {
        void onFinish();
    }

    public void startCountdown(
            long durationMillis,
            CountdownTick tick,
            CountdownFinish finish
    ) {
        stopInternal();

        endTimeMillis = SystemClock.elapsedRealtime() + durationMillis;
        isRunning = true;

        runnable = new Runnable() {
            @Override
            public void run() {
                long now = SystemClock.elapsedRealtime();
                timeLeftMillis = endTimeMillis - now;

                if (timeLeftMillis <= 0) {
                    isRunning = false;
                    handler.removeCallbacks(this);
                    finish.onFinish();
                    return;
                }

                tick.onTick(timeLeftMillis);
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }
    // ===== Stopwatch =====
    public void startStopwatch() {
        stopInternal();
        isRunning = true;
        endTimeMillis = SystemClock.elapsedRealtime();

        runnable = new Runnable() {
            @Override
            public void run() {
                timeLeftMillis = SystemClock.elapsedRealtime() - endTimeMillis;
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    public long stopStopwatch() {
        stopInternal();
        return timeLeftMillis;
    }
}