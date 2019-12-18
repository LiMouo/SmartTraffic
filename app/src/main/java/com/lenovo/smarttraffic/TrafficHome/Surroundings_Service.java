package com.lenovo.smarttraffic.TrafficHome;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.Gson.Surr_Gson;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Surroundings_Service extends Service {
    private String URL = "GetAllSense.do";
    private String TAG = "TrafficActivity_SQLIT";
    private Thread mThread;
    private Handler mHandler = new Handler();
    private SQLiteDatabase db;
    public Surroundings_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
        GetData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mThread != null && mThread.isAlive()){
            mThread.interrupt();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void GetData() {
        Log.e(TAG, "服务器启动: ");
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    long startTime = System.currentTimeMillis();
                    Gson gson = new Gson();
                    Map map = new HashMap();
                    map.put("UserName", "user1");
                    try {
                        String Data = NetorkTools.SendPostRequest(URL,map);
                        Surr_Gson GSON = gson.fromJson(Data,Surr_Gson.class);
                        ContentValues values = new ContentValues();
                        values.put("pm2_5", GSON.get_$Pm25272());
                        values.put("co2", GSON.getCo2());
                        values.put("LightIntensity", GSON.getLightIntensity());
                        values.put("humidity", GSON.getHumidity());
                        values.put("temperature", GSON.getTemperature());
                        values.put("date", getDate());
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                long count = db.insert( "Environment", null, values);
                                if (count > 20){
                                    db.execSQL("delete from Environment where id = (select id from Environment limit 1)");
                                }
                            }
                        });
                        try {
                        long endTime = System.currentTimeMillis();
                        if ((endTime - startTime) < 30000){
                                Thread.sleep(30000  - (endTime - startTime));
                                int s = (int) endTime;
                            Log.e(TAG, "时间 "+String.valueOf(s));
                            }
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mThread.start();
    }
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String str = format.format(date);
        return str;
    }
}
