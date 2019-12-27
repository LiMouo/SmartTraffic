package com.lenovo.smarttraffic.DataAnalysis_15;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lenovo.smarttraffic.LifeAssistant_14.Life_PagerAdapter;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.NetorkTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.platform.Platform;

public class DataAnalysisActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private PieChart mPieChart[] = new PieChart[2];
    private RadioButton[] mRadioButtons = new RadioButton[7];
    private int[] mButtonId = {R.id.bnt_1, R.id.bnt_2, R.id.bnt_3, R.id.bnt_4, R.id.bnt_5, R.id.bnt_6, R.id.bnt_7};
    private ViewPager mViewPager;
    private List<View> ViewPages = new ArrayList<>();
    private RadioGroup mRadioGroup;
    private String[] TitleName = {"平台上有违章车辆和没有违章车辆的占比统计", "有无“重复违章记录的车辆”的占比统计",
            "违章车辆的违章次数占比分布图统计", "年龄群体车辆违章的占比统计",
            "平台上男性和女性有无车辆违章的占比统计", "每日时段内车辆违章的占比统计", "排名前十位的交通违法行为的占比统计"};
    private List<View> view = new ArrayList<>();
    private String TAG = "DataAnalysisActivity";
    private BarChart mBarChart[] = new BarChart[5];

    private final String URL = "GetCarInfo.do";     /*所有用户车辆*/
    private final String URL_1 = "GetAllCarPeccancy.do";//所有违章数据
    private final String URL_2 = "GetPeccancyType.do";//违章代码
    private List<JSONObject> CarMessage = new ArrayList<>();           /*用户所有车辆信息*/
    private List<JSONObject> ViolationMessage = new ArrayList<>();     /*所有车辆违章信息*/
    private List<JSONObject> AllPCode = new ArrayList<>();       /*违章代码*/
    private List<JSONObject> YesViolationCar = new ArrayList<>();/*用户名下违章过的车辆 即: 真实车辆 无黑车*/
    private List<JSONObject> NoViolationCar = new ArrayList<>();       /*用户名未违章过的车辆*/

    private Handler handler = new Handler();
    private Thread QueryThread, OneThread, TwoThread;
    private TextView mTitle;
    private Boolean isTrue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        InitView();
        Query();
    }

    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewPager = findViewById(R.id.VP_analysis);
        mRadioGroup = findViewById(R.id.RG_analysis);
        mTitle = findViewById(R.id.title_analysis);
        for (int i = 0; i < mRadioButtons.length; i++) {
            if (i < 2) {
                mRadioButtons[i] = findViewById(mButtonId[i]);
                ViewPages.add(LayoutInflater.from(this).inflate(R.layout.dataanalysis_piechart, null));
                mPieChart[i] = ViewPages.get(i).findViewById(R.id.PieChart);
            } else {
                mRadioButtons[i] = findViewById(mButtonId[i]);
                ViewPages.add(LayoutInflater.from(this).inflate(R.layout.dataanalysis_barchart, null));
                mBarChart[i - 2] = ViewPages.get(i).findViewById(R.id.BarChart);
            }
        }
        mViewPager.setAdapter(new DataAnalysisAdapter(this, ViewPages));
        mViewPager.addOnPageChangeListener(this);

        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioButtons[0].setChecked(true);
    }

    private void showPieChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //mPieChart = findViewById(R.id.PieChart);

            }
        }).start();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mRadioButtons[position].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //RGB 变化事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.bnt_1:
                mViewPager.setCurrentItem(0);
                mTitle.setText(TitleName[0]);
                break;
            case R.id.bnt_2:
                mViewPager.setCurrentItem(1);
                mTitle.setText(TitleName[1]);
                break;
            case R.id.bnt_3:
                mViewPager.setCurrentItem(2);
                mTitle.setText(TitleName[2]);
                break;
            case R.id.bnt_4:
                mViewPager.setCurrentItem(3);
                mTitle.setText(TitleName[3]);
                break;
            case R.id.bnt_5:
                mViewPager.setCurrentItem(4);
                mTitle.setText(TitleName[4]);
                break;
            case R.id.bnt_6:
                mViewPager.setCurrentItem(5);
                mTitle.setText(TitleName[5]);
                break;
            case R.id.bnt_7:
                mViewPager.setCurrentItem(6);
                mTitle.setText(TitleName[6]);
                break;
            default:
                break;
        }
    }

    private void setPieChart(PieChart pieChart, ArrayList<PieEntry> pieEntries) {//, ArrayList<PieEntry> pieEntry
        pieChart.setTouchEnabled(true);             /*禁止触摸*/
        pieChart.setHighlightPerTapEnabled(true);   /*图表不可放大*/
        pieChart.setDrawCenterText(false);           /*不显示中间文字*/
        pieChart.setCenterText("This FUCK PieChart");/*中心显示文字 上面为false 则不显示*/
        pieChart.setRotationEnabled(false);          /*不可手动旋转图表*/
        pieChart.setUsePercentValues(true);          /*设置百分比显示 PieEntry 百分比*/
        pieChart.setDrawHoleEnabled(false);          /*默认是false 饼图 true 是圆环*/
        pieChart.invalidate();
        pieChart.notifyDataSetChanged();

        //设置使用百分比值
        pieChart.setUsePercentValues(true);
        /*不显示描述*/
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        //设置摩擦系数
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置实心
        pieChart.setDrawHoleEnabled(false);/*设置中间是否空心 不为空心 进行 块间距设置可能会不相等*/
        pieChart.setHoleRadius(0f);                  /*设置大圆里面的无色圆的半径*/
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        /*--------------------------------设置 Item 文本属性 PieEntry label---------------------------*/
        pieChart.setDrawEntryLabels(false);          /*设置pieChart 是否图饼上百分比显示 标签是否存在*/
        pieChart.setEntryLabelTextSize(10f);         /*Item 字体大小*/
        pieChart.setEntryLabelColor(Color.RED);      /* 字体颜色*/


        //出现动画设置
        //pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        /*-------------------------设置数据--------------------------*/
        PieDataSet set1 = new PieDataSet(pieEntries, null);
        set1.setValueTextSize(10f);         //字体大小
        set1.setValueTextColor(Color.RED);  //字体颜色
        set1.setValueTypeface(Typeface.DEFAULT_BOLD); //字体样式
        /*----------------------设置Values在图表外显示--------------------*/
        set1.setValueLinePart1Length(0.7f);      /*前半段长度*/
        set1.setValueLinePart2Length(0.7f);      /*后半段长度*/
        set1.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); /*value 值外面显示*/
        set1.setValueLineColor(Color.BLACK);     /*外面线显示得颜色*/

        /*-----------------------------设置底部描述-----------------------------*/
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setFormSize(14f);                       /*设置底部描述的图形大小*/
        legend.setFormLineWidth(9f);                   /*设置底部描述的图形线宽*/
        legend.setForm(Legend.LegendForm.LINE);        /*设置底部描述未线性*/

        set1.setColors(ColorTemplate.JOYFUL_COLORS);
        //设置dp中的饼图切片之间的剩余空间
        set1.setSliceSpace(1f);
        set1.setSelectionShift(5f);

        PieData data = new PieData(set1);
        data.setValueTextSize(18);
        data.setValueTextColor(Color.RED);
        //设置百分比
        data.setDrawValues(true);
        data.setValueFormatter(new IValueFormatter() {
            DecimalFormat format = new DecimalFormat("#.00");

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (isTrue) {
                    isTrue = false;
                    return pieEntries.get(1).getLabel() + ": " + format.format(value) + "%";
                } else {
                    isTrue = true;
                    return pieEntries.get(0).getLabel() + ": " + format.format(value) + "%";
                }

            }
        });
        pieChart.setData(data);
    }

    private void Query() {
        QueryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = new HashMap();
                map.put("UserName", "user1");
                try {
                    String Data = NetorkTools.SendPostRequest(URL, map);
                    JSONObject object = new JSONObject(Data);
                    JSONArray array = object.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            CarMessage.add(array.getJSONObject(i));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    /*----------------------------取得所有车辆违章数据--------------------------------*/

                    String DataNOAll = NetorkTools.SendPostRequest(URL_1, map);
                    JSONObject object1 = new JSONObject(DataNOAll);
                    JSONArray array1 = object1.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < array1.length(); i++) {
                        ViolationMessage.add(array1.getJSONObject(i));
                    }

                    String DataNO = NetorkTools.SendPostRequest(URL_2, map);
                    JSONObject object2 = new JSONObject(DataNO);
                    JSONArray array2 = object2.getJSONArray("ROWS_DETAIL");
                    for (int i = 0; i < array2.length(); i++) {
                        AllPCode.add(array2.getJSONObject(i));
                    }

                    /*--------------------------取得真实违章车辆 无黑车-------------------------------*/
                    for (JSONObject tempCar : CarMessage) {
                        String temp_1 = tempCar.getString("carnumber");
                        for (JSONObject tempViolation : ViolationMessage) {
                            if (temp_1.equals(tempViolation.getString("carnumber"))) {
                                YesViolationCar.add(tempViolation);
                            }
                        }
                    }
                    handler.post(() -> {
                        one();
                        Two();
                        Three(mBarChart[0]);
                        Four(mBarChart[1]);
                        //Five(mBarChart[2]);
                    });

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        QueryThread.start();
    }

    /*15题第一个问实现*/
    private void one() {
        /*------------------------------------违章过的车辆----------------------------------*/
        OneThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int YesViolation = 0;
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                /*------------------------------------违章过的车辆----------------------------------*/
                try {
                    for (JSONObject tempCar : CarMessage) {
                        String temp = tempCar.getString("carnumber");
                        for (JSONObject tempViolation : YesViolationCar) {
                            if (temp.equals(tempViolation.getString("carnumber"))) {
                                YesViolation++;
                                break;
                            }
                        }
                    }
                    int NoViolation = CarMessage.size() - YesViolation;

                    pieEntries.add(new PieEntry(YesViolation, "有违章"));
                    pieEntries.add(new PieEntry(NoViolation, "无违章"));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setPieChart(mPieChart[0], pieEntries);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        OneThread.start();
    }

    /*15题第二个问实现*/
    private void Two() {
        /*------------------------------------从复违章过的车辆----------------------------------*/
        TwoThread = new Thread(new Runnable() {
            int YesViolationRepeat = 0;
            int NoViolationRepeat = 0;
            ArrayList<PieEntry> pieEntries = new ArrayList<>();

            @Override
            public void run() {
                try {
                    for (JSONObject tempCar : CarMessage) {
                        int count = 0;
                        String temp = tempCar.getString("carnumber");
                        for (JSONObject tempViolation : YesViolationCar) {
                            if (temp.equals(tempViolation.getString("carnumber"))) {
                                count++;
                            }
                        }
                        if (count > 1) {
                            YesViolationRepeat++;
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pieEntries.add(new PieEntry(getCount(0, 2, true), "无重复违章"));
                            pieEntries.add(new PieEntry(YesViolationRepeat, "有重复违章"));
                            setPieChart(mPieChart[1], pieEntries);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        TwoThread.start();
    }

    /*15题三个问实现*/
    private void Three(BarChart barChart) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setBarChart(barChart);
                barChart.getLegend().setEnabled(false);
                float level_1 = getCount(0, 3, true),
                        level_2 = getCount(2, 6, true),
                        level_3 = getCount(0, 5, false);
                int[] colors = {Color.GREEN, Color.BLUE, Color.RED};
                float max = level_1 + level_2 + level_3;
                List<BarEntry> entryList = new ArrayList<>();
                /*-----------------------------------数据-------------------------------*/
                entryList.add(new BarEntry(0,(level_1 / max) * 100,"1——2条违章记录"));
                entryList.add(new BarEntry(1,level_2 == 0? 0:(level_2/max)*100,"3——5条违章记录"));
                entryList.add(new BarEntry(2,(level_3 / max) * 100 ,"5条以上违章记录"));

                /*-----------------------------------设置 X轴-------------------------------*/
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false); /*不显示边框线*/
                xAxis.setTextSize(10f);         /*文本显示大小*/
                xAxis.setLabelCount(3);          /*最大Label 显示3 */
                xAxis.setGranularity(1f);       /*防止放大后标签错乱*/
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return entryList.get((int)value).getData().toString();
                    }
                });
                /*-----------------------------------设置 Y左边轴-------------------------------*/
                YAxis LeftYAxis = barChart.getAxisLeft();
                LeftYAxis.setAxisMinimum(0);/*最小从0 开始*/
                LeftYAxis.setAxisMaximum(100);     /*最大值显示100*/
                LeftYAxis.setEnabled(false);       /*不显示*/
                /*-----------------------------------设置 Y右边轴-------------------------------*/
                YAxis RightYAxis = barChart.getAxisRight();
                RightYAxis.setAxisMinimum(0);
                RightYAxis.setAxisMaximum(100);
                RightYAxis.setTextSize(10f);
                RightYAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        DecimalFormat format = new DecimalFormat("#.00");
                        if ((int)value==0){
                            return "0.00%";
                        }else {
                            return format.format(value) + "%";
                        }
                    }
                });

                BarDataSet dataSet = new BarDataSet(entryList,"");
                dataSet.setColors(colors);           /*设置柱形显颜色*/
                dataSet.setValueTextColor(Color.RED);/*设置顶端文本颜色*/
                dataSet.setValueTextSize(10f);       /*设置顶端文字大小*/
                dataSet.setDrawValues(true);         /*显示顶端数值*/
                dataSet.setValueFormatter(new IValueFormatter() {
                    DecimalFormat format = new DecimalFormat("#.00");
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        if (value == 0){
                            return "0.00%";
                        }else {
                            return format.format(value) + "%";
                        }
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BarData barData = new BarData(dataSet);
                        barData.setBarWidth(0.4f);    /*柱状显示宽度40%*/
                        barChart.setData(barData);
                    }
                });
            }
        }).start();
    }
    /*15题四个问实现*/
    private void Four(BarChart barChart) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setBarChart(barChart);
                ArrayList<BarEntry> barEntries = setDataFour();
                Legend legend = barChart.getLegend();
                legend.setForm(Legend.LegendForm.SQUARE);
                legend.setFormSize(14f);     // 图形大小
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                legend.setDrawInside(true);
                /*-----------------------------------设置X轴-------------------------------*/
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setLabelCount(5);             /*最大Label 显示3 */
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return barEntries.get((int) value).getData().toString();
                    }
                });
                /*---------------------------------设置Y轴 左边-------------------------------*/
                YAxis leftYAxis = barChart.getAxisLeft();
                leftYAxis.setDrawGridLines(true);
                leftYAxis.setEnabled(true);
                leftYAxis.setTextSize(12f);
                leftYAxis.setAxisMinimum(0);
                leftYAxis.setAxisMaximum(120);
                /*---------------------------------设置Y轴 右边-------------------------------*/
                YAxis RightYAxis = barChart.getAxisRight();
                RightYAxis.setEnabled(false);
                /*-----------------------------------设置数据--------------------------------*/
                int[] colors = {Color.parseColor("#CDDC39"), Color.parseColor("#FF9800")};

                BarDataSet barDataSet = new BarDataSet(barEntries, null);
                barDataSet.setColors(colors);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(12f);
                barDataSet.setFormLineWidth(1f);
                barDataSet.setStackLabels(new String[]{"无违章", "有违章"}); /*设置Stack Label*/

                barDataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        DecimalFormat format = new DecimalFormat("##.0");
                        if (isTrue) {
                            isTrue = false;
                            return "";
                        } else {
                            isTrue = true;
                            return value == 0 ? "0.0%" : format.format(value) + "%";
                        }
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BarData barData = new BarData(barDataSet);
                        barData.setBarWidth(0.3f);
                        setDataFour();
                        barChart.setData(barData);
                    }
                });
            }
        }).start();
    }

    //第五题
    private void Five(BarChart barChart) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置水平柱状图
     */
    public void setBarChart(BarChart barChart) {
        barChart.setLogEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setScaleEnabled(false); //是否允许xy 轴缩放
        barChart.setDrawBarShadow(false);              /*背景阴影*/
    }

    private int getCount(int x, int y, boolean Is) {
        int tempInt = 0;
        try {
            for (JSONObject temp : CarMessage) {
                int count = 0;
                String tm = temp.getString("carnumber");
                for (JSONObject temp1 : YesViolationCar) {
                    String tm1 = temp1.getString("carnumber");
                    if (tm.equals(tm1)) {
                        count++;
                    }
                }

                if (Is) {
                    if (count > x && count < y) {
                        tempInt++;
                    }
                } else {
                    if (count > y) {
                        tempInt++;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempInt;
    }

    private ArrayList<BarEntry> setDataFour() {
        float All_90 = 0, All_80 = 0, All_70 = 0, All_60 = 0, All_50 = 0;
        float All_Yes_90 = 0, All_Yes_80 = 0, All_Yes_70 = 0, All_Yes_60 = 0, All_Yes_50 = 0;

        for(JSONObject temp : CarMessage){
            try {
                int intTemp = Integer.valueOf(temp.getString("pcardid").substring(8,9));
                switch (intTemp){
                    case 9:
                        All_90++;
                        for (JSONObject e : YesViolationCar) {
                            if (temp.getString("carnumber").equals(e.getString("carnumber"))){
                                All_Yes_90++;
                                break;
                            }
                        }
                        break;
                    case 8:
                        All_80++;
                        for (JSONObject e : YesViolationCar) {
                            if (temp.getString("carnumber").equals(e.getString("carnumber"))){
                                All_Yes_80++;
                                break;
                            }
                        }
                        break;
                    case 7:
                        All_70++;
                        for (JSONObject e : YesViolationCar) {
                            if (temp.getString("carnumber").equals(e.getString("carnumber"))){
                                All_Yes_70++;
                                break;
                            }
                        }
                        break;
                    case 6:
                        All_60++;
                        for (JSONObject e : YesViolationCar) {
                            if (temp.getString("carnumber").equals(e.getString("carnumber"))){
                                All_Yes_60++;
                                break;
                            }
                        }
                        break;
                    case 5:
                        All_50++;
                        for (JSONObject e : YesViolationCar) {
                            if (temp.getString("carnumber").equals(e.getString("carnumber"))){
                                All_Yes_50++;
                                break;
                            }
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, new float[]{All_90 == 0 ? 0 : (All_Yes_90 / All_90) * 100, All_90 == 0 ? 0 : ((All_90 - All_Yes_90) / All_90) * 100}, "90后"));
        barEntries.add(new BarEntry(1, new float[]{All_80 == 0 ? 0 : (All_Yes_80 / All_80) * 100, All_80 == 0 ? 0 : ((All_80 - All_Yes_80) / All_80) * 100}, "80后"));
        barEntries.add(new BarEntry(2, new float[]{All_70 == 0 ? 0 : (All_Yes_70 / All_70) * 100, All_70 == 0 ? 0 : ((All_70 - All_Yes_70) / All_70) * 100}, "70后"));
        barEntries.add(new BarEntry(3, new float[]{All_60 == 0 ? 0 : (All_Yes_60 / All_60) * 100, All_60 == 0 ? 0 : ((All_60 - All_Yes_60) / All_60) * 100}, "60后"));
        barEntries.add(new BarEntry(4, new float[]{All_50 == 0 ? 0 : (All_Yes_50 / All_50) * 100, All_50 == 0 ? 0 : ((All_50 - All_Yes_50) / All_50) * 100}, "50后"));
        return barEntries;
    }
}
