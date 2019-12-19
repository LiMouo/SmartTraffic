package com.lenovo.smarttraffic.LifeAssistant_14;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.lenovo.smarttraffic.Gson.Left_Gson;
import com.lenovo.smarttraffic.Gson.Surr_Gson;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LifeAssistantActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,RadioGroup.OnCheckedChangeListener{

    private String URL_1 = "GetWeather.do";//天气情况
    private String URL_2 = "GetAllSense.do";//天气详情
    private RecyclerView mRecycler_life;
    private LifeAdapter mAdapter;
    private Handler mHandler = new Handler();
    private String TAG = "LifeAssistantActivity";
    private String[] temperature = new String[6];
    private String[] WData = new String[6];
    private String[] type = new String[6];
    private TextView mNumber_2,mNumber_1;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private static RadioButton[] mTextButton = new RadioButton[6];
    // private static int[] TextButtonId = {R.id.but_1,R.id.but_2,R.id.but_3,R.id.but_4,R.id.but_5,R.id.but_6,};
    private List<View> ViewPages = new ArrayList<>();
    private int mNumber;
    /*private Thread */
    private XAxis mXAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        SetRecy();
        BindData();
    }

    //设置左侧天气数据
    private void BindData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                map.put("UserName","user1");
                try {
                    String Data_1 = NetorkTools.SendPostRequest(URL_1,map);
                    JSONObject jsonObject = new JSONObject(Data_1);
                    JSONArray jsonArray = jsonObject.getJSONArray("ROWS_DETAIL");
                    Gson gson_1 = new Gson();
                    Left_Gson left_gson = gson_1.fromJson(Data_1,Left_Gson.class);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e(TAG, "temperature[" + i +"]" + left_gson.getROWS_DETAIL().get(i).getTemperature());
                        Log.e(TAG, "type[" + i +"]" +left_gson.getROWS_DETAIL().get(i).getType());
                        Log.e(TAG, "WData[" + i +"]" + left_gson.getROWS_DETAIL().get(i).getWData());
                        temperature[i] = left_gson.getROWS_DETAIL().get(i).getTemperature();
                        type[i] = left_gson.getROWS_DETAIL().get(i).getType();
                        WData[i] = left_gson.getROWS_DETAIL().get(i).getWData();
                    }

                    String Data_2 = NetorkTools.SendPostRequest(URL_2,map);
                    Gson gson_2 = new Gson();
                    Surr_Gson surr_gson = gson_2.fromJson(Data_2,Surr_Gson.class);
                    mNumber = surr_gson.getTemperature();
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
                        mNumber_1.setText(mNumber+"");
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
        mNumber_1 = findViewById(R.id.number_1);


        /*mViewPager = findViewById(R.id.v_pager_life_1);
        mRadioGroup = findViewById(R.id.RG_left);
        for (int i = 0; i < mTextButton.length; i++) {
            mTextButton[i] = findViewById(TextButtonId[i]);
            ViewPages.add(LayoutInflater.from(this).inflate(R.layout.life_mp,null));
        }

        mViewPager.setAdapter(new Life_PagerAdapter(this,ViewPages));
        mViewPager.addOnPageChangeListener(this);

        mRadioGroup.setOnCheckedChangeListener(this);
        mTextButton[1].setChecked(true);*/
    }

    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(20,10,20,10);
        }
    }

    //ViewPager.OnPageChangeListener 方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTextButton[position].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //RadioGroup.OnCheckedChangeListener 方法
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /*switch (checkedId){
        }*/
    }

    //线图_1
    private void SetData_1(int position){

    }
    //线图_2
    private void SetData_2(int position){
        BarChart barChart = findViewById(R.id.MpChart_1);

        //1.基本设置
        mXAxis = barChart.getXAxis();
        mXAxis.setDrawAxisLine(true);
        mXAxis.setDrawGridLines(false);

        barChart.setDrawGridBackground(false);//是否显示表格颜色
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.setTouchEnabled(false);//设置可否触摸
        barChart.setDragEnabled(true);//是否可以拖拽
        barChart.setScaleEnabled(true);//是否可以缩放

        //2.y轴和比例尺

        /*barChart.setDescription();  //数据描述*/
        //setEnabled 设置启用
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        Legend legend = barChart.getLegend();//隐藏比例尺
        legend.setEnabled(false);

        //3. X轴数据和显示位置
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add("第一季度");
        xValues.add("第二季度");
        xValues.add("第三季度");
        xValues.add("第四季度");

        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//数据位于底部

        //4、Y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<>();
        //new BarEntry(20,0) 前面代表数据，后面代表柱状图位置
        yValues.add(new BarEntry(20,0));
        yValues.add(new BarEntry(18,1));
        yValues.add(new BarEntry(4,2));
        yValues.add(new BarEntry(7,3));



        //5.设置显示的数字为整数
        BarDataSet barDataSet = new BarDataSet(yValues,"");
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int n =(int) value;
                return n + "";
            }
        });
        //6.设置柱状图的颜色
        barDataSet.setColors(new int[]{Color.rgb(104, 202, 37), Color.rgb(192, 32, 32),
                Color.rgb(34, 129, 197), Color.rgb(175, 175, 175)});


        //7.显示，柱状图的宽度和动画效果
        BarData barData = new BarData(xValues,barDataSet);
    }
}
