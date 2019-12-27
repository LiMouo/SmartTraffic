package com.lenovo.smarttraffic.LifeAssistant_14;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.Gson.Surr_Gson;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LifeAssistantActivityService extends Service {

    private Map mMap1;
    private Thread mThread;
    private Handler mHandler = new Handler();
    private String URL_2 = "GetAllSense.do";//天气详情
    private Gson gson = new Gson();
    private SQLiteDatabase db;
    private String TAG = "LifeAssistantActivity";

    public LifeAssistantActivityService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Query();
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
        Log.d(TAG, "服务器启动");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mThread.isAlive()){
            mThread.interrupt();
            SystemClock.sleep(50);
        }
    }

    public void Query() {
        mMap1 = new HashMap();
        mMap1.put("UserName", "user1");
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    while (true) {
                        String data = NetorkTools.SendPostRequest(URL_2, mMap1);
                        ContentValues values = new ContentValues();
                        Surr_Gson surr_gson = gson.fromJson(data, Surr_Gson.class);
                        values.put("temperature", surr_gson.getTemperature());
                        values.put("humidity", surr_gson.getTemperature());
                        values.put("LightIntensity", surr_gson.getTemperature());
                        values.put("co2", surr_gson.getTemperature());
                        values.put("pm2_5", surr_gson.getTemperature());
                        values.put("date", getDate());
                        Long count = db.insert("Weather", null, values);
                        Log.e(TAG, "count " + count);
                        if (count > 20) {
                            db.execSQL("delete from Weather where id = (select id from Weather limit 1)");
                        }
                        long end = System.currentTimeMillis();
                        if ((end - start) < 3000) {
                            Thread.sleep(3000 - (end - start));
                            Log.e(TAG, "线程休眠");
                        } else {
                            Log.e(TAG, "运行时间" + (end - start));
                        }
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }
    private static String getDate() {
        Date date = new Date();
        return date.toString();
    }
}
