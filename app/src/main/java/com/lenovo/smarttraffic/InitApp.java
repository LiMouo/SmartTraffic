package com.lenovo.smarttraffic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 * 分包机制
 */
public class InitApp extends MultiDexApplication {

    private static Handler mainHandler;
//    private static Context AppContext;
    private static InitApp instance;
    private Set<Activity> allActivities;
    private String TAG = "InitApp";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static synchronized InitApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        AppContext = this;
        instance = this;
        mainHandler = new Handler();

    }

//    public static Context getContext(){
//        return AppContext;
//    }
    public static Handler getHandler(){
        return mainHandler;
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    /*preferences = getSharedPreferences("MyAppCompatActivity", MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putInt("isImage",1);
                    editor.apply();*/
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());//退出程序
        System.exit(0);//结束虚拟机
    }

}
