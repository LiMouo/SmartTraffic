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

public class Violation_Result_3 extends AppCompatActivity {

    private static String CodeUrl = "GetPeccancyType.do";
    private List<Map> RightInfo = new ArrayList<>();
    private List<Map> LeftInfo = new ArrayList<>();
    private List<Map> AllCodeInfo = new ArrayList<>();
    private List<List<Map>> AllRightInfo = new ArrayList<>();
    private ViolationAdapter_2 adapter_left, adapter_right;
    private RecyclerView left_View, right_View;
    private SQLiteDatabase db;
    private Handler handler = new Handler();
    private String FirstNumber;
    private int ClickItem = 0;
    private String TAG = "Violation_Result_2";

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
        FirstNumber = getIntent().getExtras().getString("carnumber");
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
        left_View = findViewById(R.id.left_root);
        right_View = findViewById(R.id.right_root);
    }

    private void InitData() {

        LinearLayoutManager left_manager = new LinearLayoutManager(this);
        left_View.setLayoutManager(left_manager);
        adapter_left = new ViolationAdapter_2(this, false, LeftInfo);

        right_View.setLayoutManager(new LinearLayoutManager(this));
        adapter_right = new ViolationAdapter_2(this, true, RightInfo);

        /*左边每一项*/
        adapter_left.setOnItemClick((v, position) -> {
            left_View.getViewTreeObserver().addOnDrawListener(() -> {//为RV添加onDraw监听事件
                //getItemCount() 返回绑定到父RecyclerView的适配器中的项目数。
                for (int i = 1; i < left_manager.getItemCount(); i++) {//遍历所有item项
                    View view = left_manager.findViewByPosition(i);////findViewByPosition 找到所有item的position项
                    if (view != null) {
                        //为所有item设置边框加白背景色
                        view.findViewById(R.id.L_root).setBackground(getDrawable(R.drawable.violation_result_item_board));
                    }
                    //为点击的item设置边框加灰背景色
                    v.setBackground(getDrawable(R.drawable.violation_result_item_background));
                }
            });
            ClickItem = position;
            RightInfo.clear();
            RightInfo.addAll(AllRightInfo.get(position));
            Log.e(TAG, "渲染数据 4 " + RightInfo);
            adapter_right.notifyDataSetChanged();
        });

        adapter_left.setOnPlusClick(() -> {
            Intent intent = new Intent(this, ViolationActivity.class).putExtras(new Bundle());
            startActivityForResult(intent, 1);
            /*未写弹出动画*/
        });

        adapter_left.setOnReduceClick(position ->
                {
                    if (ClickItem == position) {
                        String number = LeftInfo.get(position).get("carnumber").toString();
                        db.delete("Violation", "carnumber='" + number + "'", null);
                        LeftInfo.remove(position);
                        AllRightInfo.remove(position);
                        adapter_left.RemoveItem(position);
                        if (LeftInfo.size() <= 0) {
                            Toast.makeText(this, "已无历史记录,请重新查询!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "先选中数据好吗", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        /*右边列表项点击*//*
        adapter_right.setOnItemClick((v,position)-> startActivity(new Intent(this,Violation_CaptureActivity.class)));*/

        left_View.setAdapter(adapter_left);
        right_View.setAdapter(adapter_right);
    }

    private void QueryData() {
        new Thread(() -> {
            try {
                AllCodeInfo.clear();
                AllRightInfo.clear();
                LeftInfo.clear();
                /*找到所有违章代码存到 AllCodeInfo 集合里*/
                String Data = NetorkTools.SendPostRequest(CodeUrl, new HashMap());
                JSONObject jsonObject = new JSONObject(Data);
                JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject info = jsonArray.getJSONObject(i);
                    Map code = new HashMap();
                    code.put("pcode", info.get("pcode"));
                    code.put("premarks", info.get("premarks"));
                    code.put("pmoney", info.get("pmoney"));
                    code.put("pscore", info.get("pscore"));
                    AllCodeInfo.add(code);
                }
                Cursor cursor = db.query("Violation", null, null, null, "carnumber", null, null);
                if (cursor.moveToNext()) {
                    List<String> number = new ArrayList<>();
                    do {
                        number.add(cursor.getString(1));
                    } while (cursor.moveToNext());
                    /*设置第一个显示为查询的车牌、用了 remove 下面查询的时候就不会重复*/
                    setChildInfo(number.remove(number.indexOf(FirstNumber)));
                    /*后面接着显示为查询过的车牌*/
                    for (int j = 0; j < number.size(); j++) {
                        setChildInfo(number.get(j));
                    }
                }
                handler.post(() -> {
                    adapter_left.notifyDataSetChanged();
                    adapter_right.notifyDataSetChanged();
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setChildInfo(String number) {
        Cursor single = db.query("Violation", null, "carnumber='" + number + "'", null, null, null, null);
        if (single.moveToFirst()) {
            List<Map> list = new ArrayList<>();
            String carnumber = number;
            int count = single.getCount();
            int reduce = 0;
            int money = 0;
            do {
                Map info = new HashMap();
                info.put("car_number", single.getString(1));
                info.put("road", single.getString(3));
                info.put("time", single.getString(4));
                for (int i = 0; i < AllCodeInfo.size(); i++) {
                    if (AllCodeInfo.get(i).get("pcode").equals(single.getString(2))) {
                        info.put("info", AllCodeInfo.get(i).get("premarks"));
                        info.put("money", AllCodeInfo.get(i).get("pmoney"));
                        info.put("reduce", AllCodeInfo.get(i).get("pscore"));
                        money += Integer.parseInt(AllCodeInfo.get(i).get("pmoney").toString());
                        reduce += Integer.parseInt(AllCodeInfo.get(i).get("pscore").toString());
                        break;
                    }
                }
                list.add(info);
            } while (single.moveToNext());
            Map map = new HashMap();
            map.put("carnumber", carnumber);
            map.put("count", count);
            map.put("reduce", reduce);//扣分
            map.put("money", money);
            LeftInfo.add(map);
            AllRightInfo.add(list);
            Log.e(TAG, "leftInfo" + LeftInfo);
            Log.e(TAG, "AllRightInfo" + AllRightInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    FirstNumber = data.getExtras().getString("carnumber");
                    QueryData();
                } else {
                    handler.postDelayed(() -> NetorkTools.Toast(this, "取消查询", false), 400);
                }
                break;
        }
    }
}
