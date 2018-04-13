package com.luan.dms_management.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by luan.nt on 8/18/2017.
 */

public class TimerWork extends Service {
    private Handler handler;
    private int time = 0;
    public final static String MY_ACTION = "TimeWork";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time = time + 1;
                Intent intent = new Intent(MY_ACTION);
                intent.putExtra("MESSAGE", time);
                sendBroadcast(intent);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
