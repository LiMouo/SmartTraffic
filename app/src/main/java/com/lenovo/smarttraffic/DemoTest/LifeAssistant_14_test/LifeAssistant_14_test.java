package com.lenovo.smarttraffic.DemoTest.LifeAssistant_14_test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.lenovo.smarttraffic.LifeAssistant_14.LifeAssistantActivityService;
import com.lenovo.smarttraffic.LifeAssistant_14.Life_PagerAdapter;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.TolssHome.MySqLiteOpenHelper;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class LifeAssistant_14_test extends AppCompatActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener{
    private RadioButton[] mRadioButtons = new RadioButton[4];
    private static int[] RadioButtonId = {R.id.but_1, R.id.but_2, R.id.but_3, R.id.but_4};
    private List<View> ViewPages = new ArrayList<>();
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private List<Integer>[] Data = new ArrayList[4];
    private List<Integer>[] tempData = new ArrayList[4];
    private Thread GetDataThread,QueryThread;
    private Handler mHandler = new Handler();
    private List<String> temp_times = new ArrayList<>();
    private List<String> times = new ArrayList<>();
    private SQLiteDatabase db;
    private String TAG = "LifeAssistantActivity_Service";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_assistant);
        startService(new Intent(this, LifeAssistantActivityService.class));
        InitView();
        GetData();
    }

    private void InitView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        db = MySqLiteOpenHelper.getInstance(this).getWritableDatabase();
        mViewPager = findViewById(R.id.VP_left);
        mRadioGroup = findViewById(R.id.RG_left);
        for (int i = 0; i < RadioButtonId.length; i++) {
            mRadioButtons[i] = findViewById(RadioButtonId[i]);
            ViewPages.add(LayoutInflater.from(this).inflate(R.layout.life_barchart, null));
        }
        mViewPager.setAdapter(new Life_PagerAdapter(this, ViewPages));
        mViewPager.addOnPageChangeListener(this);

        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioButtons[0].setChecked(true);

        for (int i = 0; i < Data.length; i++) {
            Data[i] = new ArrayList<>();
            tempData[i] = new ArrayList<>();
        }

    }

    private void GetData() {
        GetDataThread = new Thread(()->{
            while (true){
                for (List<Integer> tempDatum : tempData) {
                    tempDatum.clear();
                }
                temp_times.clear();
                Cursor cursor = db.query("Environment",null,null,null,null,null,"id");
                if (cursor.moveToFirst()){
                    do{
                        for (int i = 1; i < 4; i++) {
                            tempData[i-1].add(cursor.getInt(i));
                            Log.d(TAG, "tempData" + tempData[i-1]);
                        }
                        temp_times.add(cursor.getString(6));
                    }while (cursor.moveToNext());
                }
            }
        });
    }

    private void showBarChart(int position) {
        TextView title = ViewPages.get(position).findViewById(R.id.T_title);//获得当前页面的Title
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
        switch (checkedId) {
            case R.id.but_1:
                showBarChart(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.but_2:
                //showBarChart(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.but_3:
                //showBarChart(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.but_4:
                //showBarChart(3);
                mViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

}
