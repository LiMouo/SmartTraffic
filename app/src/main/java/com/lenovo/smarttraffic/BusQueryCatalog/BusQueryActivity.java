package com.lenovo.smarttraffic.BusQueryCatalog;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import android.widget.ExpandableListView;


import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BusQueryActivity extends AppCompatActivity {

    private ExpandableListView EL_main;
    private static String[] GroupNames = {"中医院站"," 联想大厦"};
    private List<Map>[]ChildData = new LinkedList[2];
    private String TAG = "BusQueryActivity";
    private BusQueryAdapter adapter;
    private Thread thread;
    private Handler handler = new Handler();
    private static String PlatformUrl = "GetBusStationInfo.do";//站台信息
    private static String CapacityUrl = "GetBusCapacity.do";//车载容量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }

    private void InitView() {
        setContentView(R.layout.activity_bus_query);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        EL_main = findViewById(R.id.EL_main);
        Toolbar toolbar = findViewById(R.id.bus_toolbar);
        toolbar.setTitle("公交车查询");
        for (int i = 0; i <ChildData.length ; i++) {
            ChildData[i] = new LinkedList<>();
        }
    }

    private void InitData() {
        adapter = new BusQueryAdapter(this,GroupNames,ChildData);
        EL_main.setAdapter(adapter);
        thread = new Thread(() -> {
            while (true){
                try {
                    for (int i = 0; i < 2; i++) {
                        Map PlatformMap= new HashMap();
                        Map CapacityMap = new HashMap();
                        //车站号
                        PlatformMap.put("BusStationId",i+1);
                        //巴士号
                        CapacityMap.put("BusId",i+1);
                        String PlatformData = NetorkTools.SendPostRequest(PlatformUrl,PlatformMap);
                        Log.e(TAG, "PlatformData 数据 " + PlatformData);
                        JSONObject Platform = new JSONObject(PlatformData);
                        Log.e(TAG, "Platform 数据 " + PlatformData);
                        JSONArray distance = Platform.getJSONArray("ROWS_DETAIL");
                        List<Map> list = new LinkedList<>();
                        for(int j =0;j<distance.length();j++){
                            JSONObject info = distance.getJSONObject(j);
                            int BusId = info.getInt("BusId")-1;
                            Log.e(TAG, "BusId 数据: " + BusId );
                            String CapacityData = NetorkTools.SendPostRequest(CapacityUrl,CapacityMap);
                            JSONObject Capacity = new JSONObject(CapacityData);
                            Map map = new HashMap();
                            map.put("person",Capacity.getInt("BusCapacity"));
                            map.put("distance",info.getInt("Distance") /10);
                            map.put("carId",BusId+1);
                            map.put("time",info.getInt("Distance")/10/334);
                            list.add(map);
                        }
                        ChildData[i].clear();
                        ChildData[i] = list;
                    }
                    for (int i = 0; i < ChildData.length; i++) {
                        Collections.sort(ChildData[i], (o1, o2) -> {
                            if(Integer.parseInt(o1.get("distance").toString())>Integer.parseInt(o2.get("distance").toString())) return 1;
                            else return -1;
                        });
                    }
                    handler.post(() -> adapter.notifyDataSetChanged());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onDestroy() {
        if(thread.isAlive()){
            thread.interrupt();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
