package com.lenovo.smarttraffic.LifeAssistant_14;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.Gson.Left_Gson;
import com.lenovo.smarttraffic.Gson.Surr_Gson;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LifeAssistantActivity extends Activity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private String URL_1 = "GetWeather.do";//天气情况
    private String URL_2 = "GetAllSense.do";//天气详情
    private RecyclerView mRecycler_life;
    private LifeAdapter mAdapter;
    private Handler mHandler = new Handler();
    private String TAG = "LifeAssistantActivity";
    private String[] temperature = new String[6];
    private String[] WData = new String[6];
    private String[] type = new String[6];
    private TextView mNumber_2, mNumber_1;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private static RadioButton[] mRadioButton = new RadioButton[4];
    private static int[] RadioButtonId = {R.id.but_1, R.id.but_2, R.id.but_3, R.id.but_4};
    private List<View> ViewPages = new ArrayList<>();
    private int mNumber;
    /*private Thread */
    private XAxis mXAxis;
    private BarChart mBarChart;
    private String[] XString = {"03", "06", "09", "12", "15", "28", "21", "24", "27", "30",
            "33", "36", "39", "42", "45", "48", "51", "54", "57", "60"};
    private ArrayList<BarEntry> mEntries = new ArrayList<BarEntry>();
    private String[] namevalues = {"昨天", "今天", "明天", "周五", "周六", "周日"};
    private String[] names = {"紫外线指数", "感冒指数", "穿衣指数", "运动指数", "空气污染指数"};
    private ArrayList<Integer> List_pm = new ArrayList<>();
    private ArrayList<Integer> List_co2 = new ArrayList<>();
    private ArrayList<Integer> List_humidity = new ArrayList<>();//湿度
    private ArrayList<Integer> List_Relative_humidity = new ArrayList<>();//相对湿度


    //LineChart
    private LineChart LineChart;
    private XAxis lXAxis;
    private YAxis lLifeYAxis;
    private YAxis lRightYAxis;
    private Legend lLegend;
    private LimitLine llimitLine;
    private LinearLayout mLeft_root;
    private Thread mThread, mGetThread, BindThread;
    private Map mMap1;
    private SQLiteDatabase db;
    Gson gson = new Gson();

    //存放4个图标数据
    private List<Integer>[] Data = new ArrayList[6];
    private List<Integer>[] tempData = new ArrayList[6];
    private boolean flag = true;
    private List<String> temp_times = new ArrayList<>();
    private List<String> times = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
        startService(new Intent(this, LifeAssistantActivityService.class));//查询数据，将数据放入数据库
        InitView();
        SetRecy();
        BindData();
        //InitChart(LineChart);
        SetData_1();
    }

    private void InitView() {
        setContentView(R.layout.activity_life_assistant);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mNumber_2 = findViewById(R.id.number_2);
        mNumber_1 = findViewById(R.id.number_1);
       /* mBarChart = findViewById(R.id.Left_BarChart);
        mLeft_root = findViewById(R.id.left_root);*/

        LineChart = findViewById(R.id.MpChart_1);

        mViewPager = findViewById(R.id.VP_left);
        mRadioGroup = findViewById(R.id.RG_left);
        for (int i = 0; i < RadioButtonId.length; i++) {
            mRadioButton[i] = findViewById(RadioButtonId[i]);
            ViewPages.add(LayoutInflater.from(this).inflate(R.layout.life_barchart, null));
        }
        mViewPager.setAdapter(new Life_PagerAdapter(this, ViewPages));
        mViewPager.addOnPageChangeListener(this);

        for (int i = 0; i < Data.length; i++) {
            Data[i] = new ArrayList<>();
            tempData[i] = new ArrayList<>();
        }

        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioButton[0].setChecked(true);

        GetData();//将数据库的数据查询出来后面待用
    }

    private void SetRecy() {
        mRecycler_life = findViewById(R.id.recy_life_2);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        linear.setOrientation(LinearLayout.HORIZONTAL);//设置布局方向
        mAdapter = new LifeAdapter(this, temperature);
        mRecycler_life.setLayoutManager(linear);
        mRecycler_life.addItemDecoration(new MyDecoration());
        mRecycler_life.setAdapter(mAdapter);
    }

    //设置左侧天气数据
    private void BindData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                map.put("UserName", "user1");
                try {
                    String Data_1 = NetorkTools.SendPostRequest(URL_1, map);
                    JSONObject jsonObject = new JSONObject(Data_1);
                    JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                    Gson gson_1 = new Gson();
                    Left_Gson left_gson = gson_1.fromJson(Data_1, Left_Gson.class);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Log.e(TAG, "temperature[" + i + "]" + left_gson.getROWS_DETAIL().get(i).getTemperature());
                        //Log.e(TAG, "type[" + i + "]" + left_gson.getROWS_DETAIL().get(i).getType());
                        //Log.e(TAG, "WData[" + i + "]" + left_gson.getROWS_DETAIL().get(i).getWData());
                        temperature[i] = left_gson.getROWS_DETAIL().get(i).getTemperature();
                        type[i] = left_gson.getROWS_DETAIL().get(i).getType();
                        WData[i] = left_gson.getROWS_DETAIL().get(i).getWData();
                    }

                    String Data_2 = NetorkTools.SendPostRequest(URL_2, map);
                    Gson gson_2 = new Gson();
                    Surr_Gson surr_gson = gson_2.fromJson(Data_2, Surr_Gson.class);
                    mNumber = surr_gson.getTemperature();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mNumber_2.setText(temperature[0]);
                        mNumber_1.setText(mNumber + "");
                    }
                });
            }
        }).start();
    }

    //线图_1
    private void SetData_1() {
        LineChart.setDragEnabled(true);
        LineChart.setScaleEnabled(false);

        /*Description description = new Description();
        description.setText(null);*/
        LineChart.setDescription(null);

        lXAxis = LineChart.getXAxis();
        lXAxis.setDrawGridLines(false);//绘制网格线
        lXAxis.setDrawLabels(true);//绘制该轴标签
        lXAxis.setTextColor(Color.RED);// 设置轴标签的颜色。
        lXAxis.setTextSize(11f);//设置轴标签的文字大小。
        //*lXAxis.setLabelCount(2);*//
        ArrayList<String> xValues = new ArrayList<>();//Arrays.asList(name)
        /*for (int i = 0; i < name.length; i++) {
            xValues.add(i,name[i]);
        }*/
        lXAxis.setPosition(XAxis.XAxisPosition.TOP);

        ArrayList<Entry> yValues_1 = new ArrayList<>();
        yValues_1.add(new Entry(0, 14));
        yValues_1.add(new Entry(1, 15));
        yValues_1.add(new Entry(2, 16));
        yValues_1.add(new Entry(3, 17));
        yValues_1.add(new Entry(4, 16));
        yValues_1.add(new Entry(5, 16));

        LineDataSet set1 = new LineDataSet(yValues_1, "");
        set1.setFillAlpha(1030);
        set1.setLineWidth(4);
        set1.setValueTextSize(18);
        set1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                return value + "";
            }
        });

        ArrayList<Entry> yValues_2 = new ArrayList<>();
        yValues_2.add(new Entry(0, 22));
        yValues_2.add(new Entry(1, 24));
        yValues_2.add(new Entry(2, 25));
        yValues_2.add(new Entry(3, 25));
        yValues_2.add(new Entry(4, 25));
        yValues_2.add(new Entry(5, 22));

        LineDataSet set2 = new LineDataSet(yValues_2, "");
        set2.setFillAlpha(85);
        set2.setLineWidth(4);
        set2.setValueTextSize(18);
        set2.setColor(Color.RED);
        set2.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n = (int) value;
                return n + "";
            }
        });

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);

        LineChart.setData(data);

        //设置左边Y轴显示
        LineChart.getAxisRight().setEnabled(false);
        /*LineChart.getAxisLeft().setEnabled(false);
        LineChart.getXAxis().setEnabled(true);*/
        LineChart.getAxisLeft().setEnabled(true);
        LineChart.getAxisLeft().setTextColor(Color.TRANSPARENT);

        //设置X轴标题
        mXAxis = LineChart.getXAxis();
        mXAxis.setValueFormatter(new MyXAxisValueFormatter(namevalues));
        mXAxis.setGranularity(1);
        mXAxis.setTextColor(Color.BLUE);
        mXAxis.setTextSize(10);


    }

    private void showBarChart(int position) {

        if (BindThread != null && BindThread.isAlive()) {
            BindThread.interrupt();
            SystemClock.sleep(50);
        }
        mBarChart = ViewPages.get(position).findViewById(R.id.Left_BarChart);
        /*TextView title = ViewPages.get(position).findViewById(R.id.T_title);
        title.setText(names[position]);
        title.setTextColor(Color.BLACK);
*/
        mBarChart.invalidate();//

        BindThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<BarEntry> entry = new ArrayList<>();
                while (true) {
                    if (flag) {
                        synchronized (this) {
                            try {
                                long start = System.currentTimeMillis();
                                entry.clear();
                                for (int i = 0; i < Data[position].size(); i++) {
                                    entry.add(new BarEntry(i, Data[position].get(i)));
                                    //Log.d(TAG, "entry1"+entry.size());
                                }
                                /*Log.d(TAG, "entry2"+entry.size());
                                Log.d(TAG, "Data[" + position+"]"+entry.size());
                                原本有数据则只更新数据，不重新创建视图，减少渲染时间*/
                                if (mBarChart.getBarData() != null && mBarChart.getBarData().getDataSets().size() > 0) {
                                    for (IBarDataSet set : mBarChart.getBarData().getDataSets()) {
                                        BarDataSet data = (BarDataSet) set;
                                        data.setValues(entry);
                                        //Log.d(TAG, "if 走了");
                                        //Log.d(TAG, "entry长度 " + entry.size());
                                    }
                                } else {
                                    //Log.d(TAG, "else 走了");
                                    //Log.d(TAG, "entry长度 " + entry.size());
                                    BarDataSet barDataSet = new BarDataSet(entry, "");
                                    barDataSet.setColor(Color.GRAY);
                                    BarData data = new BarData(barDataSet);
                                    data.setBarWidth(1);
                                    mBarChart.setTouchEnabled(true);//是否可以触摸
                                    mBarChart.setScaleEnabled(true);//是否可以拖拽
                                    mBarChart.setDragEnabled(false);//是否可以缩小
                                    mBarChart.setDescription(null);
                                    mBarChart.setData(data);

                                    XAxis xAxis = mBarChart.getXAxis();
                                    xAxis.setEnabled(false);

                                    mBarChart.setDrawBarShadow(false);
                                    mBarChart.setDrawValueAboveBar(true);
                                    mBarChart.setMaxVisibleValueCount(50);
                                    mBarChart.setPinchZoom(false);
                                    mBarChart.setDrawGridBackground(true);
                                    mBarChart.getAxisLeft().setEnabled(true);
                                    mBarChart.getAxisRight().setEnabled(false);
                                }
                                mBarChart.getData().notifyDataChanged();//更新坐标轴数据,
                                mBarChart.notifyDataSetChanged();//更新图表数据
                                mHandler.post(() -> mBarChart.invalidate());
                                long end = System.currentTimeMillis();
                                flag = false;
                                if (end - start < 3000) {
                                    Thread.sleep(3000 - (end - start));
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                }
            }
        });
        BindThread.start();
    }

    private void GetData() {
        mGetThread = new Thread(() -> {

            while (true) {
                if (!flag) {
                    //Log.d(TAG, "判断走了");
                    synchronized (this) {
                        try {
                            Long start = System.currentTimeMillis();
                            for (int i = 0; i < tempData.length; i++) {
                                tempData[i].clear();
                            }
                            temp_times.clear();
                            Cursor cursor = db.query("Weather", null, null, null, null, null, "id");
                            if (cursor.moveToFirst()) {
                                do {
                                    for (int j = 1; j < 6; j++) {
                                        tempData[j - 1].add(cursor.getInt(j));
                                        //Log.d(TAG, "数据 tempData["+(j-1)+"] " + tempData[j - 1].toString());
                                        Log.d(TAG, "tempData["+(j-1)+"]" +tempData[j-1].size());
                                    }
                                    temp_times.add(cursor.getString(6));
                                } while (cursor.moveToNext());
                            }
                            for (int i = 0; i < Data.length; i++) {
                                //Log.e(TAG, "Data长度" + Data.length);
                                Log.d(TAG, "清除前 Data数据"+Data[i].toString());
                                Data[i].clear();
                                //Log.e(TAG, "Data是否清除" +Data[i].toString() );
                                Data[i].addAll(tempData[i]);
                                Log.d(TAG, "清除后 Data数据" + Data[i].toString() );
                            }

                            long end = System.currentTimeMillis();
                            flag = true;
                            if ((end - start) < 3000) {
                                Thread.sleep(3000 - (end - start));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mGetThread.start();
    }


    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(20, 10, 20, 10);
        }
    }

    //ViewPager.OnPageChangeListener 方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mRadioButton[position].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //RadioGroup.OnCheckedChangeListener 方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.but_1:
                showBarChart(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.but_2:
                showBarChart(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.but_3:
                showBarChart(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.but_4:
                showBarChart(3);
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }


    //X轴标题栏设置
    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }
}