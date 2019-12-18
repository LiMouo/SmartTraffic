package com.lenovo.smarttraffic.TrafficHome;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.Gson.Surr_Gson;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrafficActivity extends AppCompatActivity implements View.OnClickListener{

    private static  String TAG = "TrafficActivity";
    private String URL = "GetAllSense.do";
    private TextView mTime_day,mTime_week,mNumber_tem,mNumber_pm25,mNumber_hum;
    private String mTime_d,mTime_w;
    private SQLiteDatabase db;
    private Handler mHandler = new Handler();
    private ImageButton mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        /*BindData();*/
        dateTime();
        startService(new Intent(this,Surroundings_Service.class));
        BindData();
    }


    /*private void BindData() {
        Map map = new HashMap();
        Cursor cursor = db.query("Environment",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            map.put("pm25",cursor.getInt());
            map.put("co2",gson.getCo2());
            map.put("LightIntensity", gson.getLightIntensity());
            map.put("humidity", gson.getHumidity());
            map.put("temperature", gson.getTemperature());
        }
        
    }*/

    private void dateTime() {
        mTime_day = findViewById(R.id.T_time);
        mTime_week = findViewById(R.id.T_week);
        Date date = new Date();
        SimpleDateFormat dateday = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateweek = new SimpleDateFormat("EEEE");
        mTime_d = dateday.format(date);
        mTime_w = dateweek.format(date);
        mTime_day.setText(mTime_d);
        mTime_week.setText(mTime_w);
        mNumber_pm25 = findViewById(R.id.t_number_pm25);
        mNumber_tem = findViewById(R.id.T_number_tem);
        mNumber_hum = findViewById(R.id.t_number_hum);

        mRefresh = findViewById(R.id.refresh);
        mRefresh.setOnClickListener(this);
    }

    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_traffic);
    }
    private void BindData() {
        /*db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
        Cursor cursor = db.query("Environment",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            int wendu = cursor.getColumnIndex("temperature");
            int shidu = cursor.getColumnIndex("humidity");
            int pm25 = cursor.getColumnIndex("pm2_5");
            Log.e(TAG, "wendu " + wendu );
            Log.e(TAG, "shidu " + shidu );
            Log.e(TAG, "pm25 " + pm25 );
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mNumber_pm25.setText(pm25+"");
                    mNumber_tem.setText(wendu+"");
                    mNumber_hum.setText(shidu+"");
                }
            });
        }
    }*/
    new Thread(new Runnable() {
        @Override
        public void run() {
            Map map = new HashMap();
            map.put("UserName","user1");
            Gson gson = new Gson();
            try {
                String Data = NetorkTools.SendPostRequest(URL,map);
                Surr_Gson GSON = gson.fromJson(Data,Surr_Gson.class);

                int pm25 = GSON.get_$Pm25272();
                int tem = GSON.getTemperature();
                int hum = GSON.getHumidity();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mNumber_pm25.setText(pm25 + "");
                        mNumber_tem.setText(tem+"");
                        mNumber_hum.setText(hum+"");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                BindData();
                Toast.makeText(this, "加油", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
