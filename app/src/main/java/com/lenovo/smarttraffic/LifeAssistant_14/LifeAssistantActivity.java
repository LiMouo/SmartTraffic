package com.lenovo.smarttraffic.LifeAssistant_14;

import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.Gson.Left_Gson;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LifeAssistantActivity extends AppCompatActivity {

    private String URL = "GetWeather.do";
    private RecyclerView mRecycler_life;
    private LifeAdapter mAdapter;
    private Handler mHandler = new Handler();
    private String TAG = "LifeAssistantActivity";
    private String[] temperature = new String[6];
    private String[] WData = new String[6];
    private String[] type = new String[6];
    private TextView mNumber_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        SetRecy();
        BindData();
    }

    private void BindData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                map.put("UserName","user1");
                try {
                    String Data = NetorkTools.SendPostRequest(URL,map);
                    JSONObject jsonObject = new JSONObject(Data);
                    JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                    Gson gson = new Gson();
                    Left_Gson left_gson = gson.fromJson(Data,Left_Gson.class);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        /*String temperature ="";
                        String WData = "";
                        String type ="";*/
                        /*Log.e(TAG, "温度 " + left_gson.getWCurrent() );
                        Log.e(TAG, "Data" + Data.toString());
                        Log.e(TAG, "Temperature: " + left_gson.getROWS_DETAIL().get(i).getTemperature() );
                        Log.e(TAG, "getWData: " + left_gson.getROWS_DETAIL().get(i).getWData());
                        Log.e(TAG, "getType: " + left_gson.getROWS_DETAIL().get(i).getType());*/
                        temperature[i] = left_gson.getROWS_DETAIL().get(i).getTemperature();
                        type[i] = left_gson.getROWS_DETAIL().get(i).getType();
                        WData[i] = left_gson.getROWS_DETAIL().get(i).getWData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mNumber_2.setText(temperature[0]);
                    }
                });
            }
        }).start();
    }

    private void SetRecy() {
        mRecycler_life = findViewById(R.id.recy_life_2);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        linear.setOrientation(LinearLayout.HORIZONTAL);//设置布局方向
        mAdapter = new LifeAdapter(this,temperature);
        mRecycler_life.setLayoutManager(linear);
        mRecycler_life.addItemDecoration(new MyDecoration());
        mRecycler_life.setAdapter(mAdapter);
    }

    private void InitView() {
        setContentView(R.layout.activity_life_assistant);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mNumber_2 = findViewById(R.id.number_2);
    }

    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(20,10,20,10);
        }
    }
}
