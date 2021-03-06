package com.lenovo.smarttraffic.ViolationHome;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private List<List<Map>> AllRightInfo = new ArrayList<>();
    private List<Map> LeftInfo = new ArrayList<>();
    private List<Map> RightInfo = new ArrayList<>();
    private int ClickItem = 0;
    private Handler handler = new Handler();


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

        /*左边每一项*/
        adapter_right.setOnItemClick((v,position) ->{
            left_recycler.getViewTreeObserver().addOnDrawListener(()->{//为RV添加onDraw监听事
                for (int i = 0; i < manager.getItemCount(); i++) {//遍历所有item项
                    View view = manager.findViewByPosition(i);//findViewByPosition 找到所有item的position项
                    if (view != null){
                        view.findViewById(R.id.L_root).setBackground(getDrawable(R.drawable.violation_result_item_board));//为所有item设置边框加白背景色
                        v.setBackground(getDrawable(R.drawable.violation_result_item_background));//为点击的item设置边框加灰背景色
                    }
                }
            });
            ClickItem = position;
            RightInfo.clear();
            RightInfo.addAll(AllRightInfo.get(position));
            adapter_right.notifyDataSetChanged();
        });

        //减号图标
        adapter_right.setOnReduceClick(position -> {
            if (ClickItem == position){
                String number = LeftInfo.get(position).get("carnumber").toString();
                db.delete("Violation","carnumber='"+number+"'",null);
                LeftInfo.remove(position);
                AllRightInfo.remove(position);
                adapter_left.RemoveItem(position);
                if(LeftInfo.size() <= 0){
                    Toast.makeText(this,"已无历史记录,请重新查询!",Toast.LENGTH_LONG).show();
                    finish();
                }
            }else {
                Toast.makeText(this, "请先选择数据", Toast.LENGTH_SHORT).show();
            }
        });

        /*加号图标*/
        adapter_left.setOnPlusClick(() -> {
            Intent intent = new Intent(this,ViolationActivity.class).putExtras(new Bundle());
            startActivityForResult(intent,1);
            overridePendingTransition(R.anim.gradual_in,R.anim.gradual_out);
        });

        /*右边列表项点击*/
        adapter_right.setOnItemClick((v,position)-> Toast.makeText(this, "？？？？", Toast.LENGTH_SHORT).show());


        left_recycler.setAdapter(adapter_left);
        right_recycler.setAdapter(adapter_right);
        left_recycler.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        right_recycler.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    FirstNumber = data.getExtras().getString("carnumber");
                    QueryData();
                }else {
                    handler.postDelayed(()->NetorkTools.Toast(this,"取消查询",false),400);
                }
                break;
        }
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
                    }while (cursor.moveToNext());
                    /*设置第一个显示为查询的车牌、用了 remove 下面查询的时候就不会重复*/
                    setChildInfo(number.remove(number.indexOf(FirstNumber)));
                    /*后面接着显示为查询过的车牌*/
                    for (int j = 0; j < number.size(); j++) {
                        setChildInfo(number.get(j));
                    }
                }
                handler.post(()->{
                    adapter_left.notifyDataSetChanged();
                    adapter_right.notifyDataSetChanged();
                });
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
        }).start();
    }

    private void setChildInfo(String number){
        Cursor single = db.query("Violation",null,"carnumber='"+number+"'", null,null,null,null);
        if(single.moveToFirst()){
            List<Map> list = new ArrayList<>();
            String carnumber = number;
            int count= single.getCount() ;
            int reduce = 0;
            int money = 0;
            do {
                Map info = new HashMap();
                info.put("car_number",single.getString(1));
                info.put("road",single.getString(3));
                info.put("time",single.getString(4));
                for (int i = 0; i < AllCodeInfo.size(); i++) {
                    if(AllCodeInfo.get(i).get("pcode").equals(single.getString(2))){
                        info.put("info",AllCodeInfo.get(i).get("premarks"));
                        info.put("money",AllCodeInfo.get(i).get("pmoney"));
                        info.put("reduce",AllCodeInfo.get(i).get("pscore"));
                        money += Integer.parseInt(AllCodeInfo.get(i).get("pmoney").toString());
                        reduce += Integer.parseInt(AllCodeInfo.get(i).get("pscore").toString());
                        break;
                    }
                }
                list.add(info);
            }while (single.moveToNext());
            Map map = new HashMap();
            map.put("carnumber",carnumber);
            map.put("count",count);
            map.put("reduce",reduce);
            map.put("money",money);
            LeftInfo.add(map);
            AllRightInfo.add(list);
        }
        Log.w(TAG, "setChildInfo: "+LeftInfo.size() );
    }
}
