package com.lenovo.smarttraffic.ViolationHome;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Violation_Result_2 extends AppCompatActivity {
    private static String CodeUrl = "GetPeccancyType.do";
    private ViolationAdapter adapter_left,adapter_right;
    private RecyclerView left_recycler,right_recycler;
    private SQLiteDatabase db;
    private String FirstNumber;
    private String TAG = "Violation_Result_2";
    private List<Map> AllCodeInfo = new ArrayList<>();
    private List<Map> LeftInfo = new ArrayList<>();
    private List<Map> RightInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
        QueryData();
    }
    private void InitView() {
        setContentView(R.layout.activity_violation__result_2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        left_recycler = findViewById(R.id.left_root);
        right_recycler = findViewById(R.id.right_root);

        FirstNumber = getIntent().getExtras().getString("carnumber");
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
    }

    private void InitData() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        left_recycler.setLayoutManager(manager);
        adapter_left = new ViolationAdapter(this,false,LeftInfo);

        right_recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter_right = new ViolationAdapter(this,true,RightInfo);

        left_recycler.setAdapter(adapter_left);
        right_recycler.setAdapter(adapter_right);
        left_recycler.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        right_recycler.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    private void QueryData() {
        new Thread(()->{
            try{
                AllCodeInfo.clear();
                String data = NetorkTools.SendPostRequest(CodeUrl, new HashMap());
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject info = jsonArray.getJSONObject(i);
                    Map code = new HashMap();
                    code.put("pcode",info.get("pcode"));
                    code.put("premarks",info.get("premarks"));
                    code.put("pmoney",info.get("pmoney"));
                    code.put("pscore",info.get("pscore"));
                    AllCodeInfo.add(code);
                }
                /*按车牌号分组查到所有车牌名*/
                Cursor cursor = db.query("Violation",null,null,null,"carnumber",null,null);
                if (cursor.moveToFirst()){
                    /*将车牌存到number里待用*/
                    List<String> number = new ArrayList<>();
                    do {
                        number.add(cursor.getString(1));
                    }while (cursor.moveToFirst());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

}
