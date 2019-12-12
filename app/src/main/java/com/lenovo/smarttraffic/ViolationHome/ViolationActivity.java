package com.lenovo.smarttraffic.ViolationHome;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;
import com.lenovo.smarttraffic.ui.activity.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViolationActivity extends AppCompatActivity {
    private static String AllInfoUrl = "GetAllCarPeccancy.do";
    private Button btn_query;
    private EditText e_number;
    private List<JSONObject> AllInfo = new ArrayList<>();
    private Thread thread;
    private Handler handler = new Handler();
    private List<JSONObject> QueryInfo = new ArrayList<>();
    private String TAG = "ViolationActivity";
    private ProgressDialog waitDialog;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
        QueryData();
    }

    private void InitView() {
        setContentView(R.layout.activity_violation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btn_query = findViewById(R.id.btn_query);
        e_number = findViewById(R.id.e_number);
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
    }

    private void InitData() {
        btn_query.setOnClickListener(view -> {
            btn_query.setEnabled(false);
            new Thread(() -> {
                try {
                    QueryInfo.clear();
                    String number = e_number.getText().toString();
                    Log.e(TAG, "number 的数据是 ：" + number);
                    boolean isNull = true;
                    for (int i = 0; i < AllInfo.size(); i++) {
                        /*Log.e(TAG, i+"AllInfo.size()" + AllInfo.size() );*/
                        if (AllInfo.get(i).getString("carnumber").equals("鲁" + number)) {
                            /*Log.e(TAG, "AllInfo.get("+i+").getString(carNumber)"+AllInfo.get(i).getString("carnumber")
                                    +"  " + number );*/
                            QueryInfo.add(AllInfo.get(i));
                            Log.e(TAG, "AllInfo.get("+i+")" + AllInfo.get(i) );
                            isNull = false;
                        }
                    }
                    if (isNull) {
                        if (number.equals("")) {
                            handler.post(() -> Toast.makeText(this, "请输入查询车牌号", Toast.LENGTH_SHORT).show());
                        } else {
                            handler.post(() -> Toast.makeText(this, "没有查询到" + number + "车的违章数据！", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        handler.post(() -> {
                            waitDialog = NetorkTools.WaitDialog(this, "正在查询");
                            waitDialog.show();
                        });
                    Cursor cursor = db.query("Violation", new String[]{"carnumber"}, "carnumber == '鲁" + number + "'", null, null, null, null);
                    if (!(cursor.getCount() > 0)) {
                        for (int i = 0; i < QueryInfo.size(); i++) {
                            ContentValues values = new ContentValues();
                            JSONObject data = QueryInfo.get(i);
                            values.put("carnumber", data.getString("carnumber"));
                            values.put("pcode", data.getString("pcode"));
                            values.put("paddr", data.getString("paddr"));
                            values.put("pdatetime", data.getString("pdatetime"));
                            db.insert("Violation", null, values);
                        }
                    }
                        handler.post(()->{
                            waitDialog.dismiss();
                            Log.e(TAG, "检查"+"运行" );
                            Bundle b = getIntent().getExtras();
                            Log.e(TAG, "Bundle  b :" + b );
                            if (b==null){
                                Intent intent = new Intent(this,Violation_Result_3.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("carnumber","鲁"+number);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("carnumber","鲁"+number);
                                intent.putExtras(bundle);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.post(()->btn_query.setEnabled(true));
            }).start();
        });
    }


    private void QueryData() {
        thread = new Thread(() -> {
            while (true) {
                try {
                    String Data = NetorkTools.SendPostRequest(AllInfoUrl, new HashMap());
                    JSONObject json = new JSONObject(Data);
                    JSONArray jsonArray = json.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AllInfo.add(object);
                        /*Log.e(TAG,  i+ "AllInfo 数据" + AllInfo );*/
                    }
                    break;
                } catch (IOException e) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
