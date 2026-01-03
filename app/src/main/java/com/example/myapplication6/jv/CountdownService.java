package com.example.myapplication6.jv;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CountdownService extends Service {

    public static final String ACTION_TICK = "com.example.myapplication6.TICK";
    public static final String ACTION_FINISH = "com.example.myapplication6.FINISH";

    private CountDownTimer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        timer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Intent i = new Intent(ACTION_TICK);
                i.putExtra("seconds", millisUntilFinished / 1000);
                sendBroadcast(i);
            }

            @Override
            public void onFinish() {
                sendBroadcast(new Intent(ACTION_FINISH));
                stopSelf();
            }

        }.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}