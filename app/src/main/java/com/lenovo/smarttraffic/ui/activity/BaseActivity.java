package com.lenovo.smarttraffic.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.lenovo.smarttraffic.InitApp;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public abstract class BaseActivity extends SupportActivity{

    public static String Ip, Port, UrlHead, user = "user1";
    //初始化标识
    public static boolean isFirst = true;
    private static SharedPreferences sp;
    //汽车余额阈值设置
    public static int Trip_money = 0;
    public static String username;
    private static SharedPreferences.Editor editor;
    private static final String TAG = "BaseActivity";
    private Unbinder unbind;
    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbind = ButterKnife.bind(this);
        InitApp.getInstance().addActivity(this);

        if (isFirst){
            sp = getSharedPreferences("MyAppCompatActivity", MODE_PRIVATE);
            editor = sp.edit();
            Ip = sp.getString("Ip", "127.0.0.1");
            Port = sp.getString("Port", "8088");
            Port = sp.getString("Port", "8088");
            UrlHead = "http://" + Ip + ":" + Port + "/transportservice/action/";
            username = sp.getString("username", "");
            SharedPreferences s = getSharedPreferences("UserInfo",MODE_PRIVATE);
            Trip_money = s.getInt("TripMoney",0);
            isFirst = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressedSupport();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressedSupport() {
        //fragment逐个退出
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            super.onBackPressedSupport();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InitApp.getInstance().removeActivity(this);
        unbind.unbind();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    protected abstract int getLayout();

    protected void SetPort(String ip, String port) {
            Ip = ip;
            Port = port;
            UrlHead = "http://" + Ip + ":" + Port + "/transportservice/action/";
            editor.putString("Ip", Ip);
            editor.putString("Port", Port);
            editor.apply();
    }
}
