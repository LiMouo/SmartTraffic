package com.lenovo.smarttraffic.DemoTest.PieChartHome;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lenovo.smarttraffic.R;

import java.util.ArrayList;

public class PieChartDemoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private PieChart mPieChart;
    private RadioButton[] mRadioButtons = new RadioButton[7];
    private int[] mButtonId = {R.id.bnt_1,R.id.bnt_2,R.id.bnt_3,R.id.bnt_4,R.id.bnt_5,R.id.bnt_6,R.id.bnt_7};
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_daemo);
        InitView();
        //showPieChart();
    }



    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mPieChart = findViewById(R.id.PieChart);
        mViewPager = findViewById(R.id.VP_analysis);
        for (int i = 0; i < mRadioButtons.length; i++) {

        }
    }

    private void showPieChart() {
        //设置使用百分比值
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5,10,5,5);
        //设置摩擦系数
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置实心
        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> YValue = new ArrayList<>();
        YValue.add(new PieEntry(34f,"春"));
        YValue.add(new PieEntry(23f,"夏"));
        YValue.add(new PieEntry(14f,"秋"));
        YValue.add(new PieEntry(35f,"东"));
        YValue.add(new PieEntry(40f,"无"));
        YValue.add(new PieEntry(23f,"感冒"));
        //出现动画设置
        mPieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet set1 = new PieDataSet(YValue,"饼状图");
        set1.setColors(ColorTemplate.JOYFUL_COLORS);
        //设置dp中的饼图切片之间的剩余空间
        set1.setSliceSpace(1f);
        set1.setSelectionShift(5f);

        PieData data = new PieData(set1);
        data.setValueTextSize(18);
        data.setValueTextColor(Color.YELLOW);

        mPieChart.setData(data);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.bnt_1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_3:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_4:
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_5:
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_6:
                Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bnt_7:
                Toast.makeText(this, "7", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
