package com.lenovo.smarttraffic.TolssHome;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lenovo.smarttraffic.ui.activity.BaseActivity;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetorkTools {
    private static Gson gson = new Gson();
    private static boolean Toasting = false;
    private static Toast toast;
    /**
     * 发送一个Post请求
     * @param url  目标url
     * @param map  参数
     * @return 返回的数据
     * @throws
     */
    public static String SendPostRequest(String url, Map map) throws IOException {
        map.put("UserName", BaseActivity.user);
        String parms = gson.toJson(map);
        RequestBody  requestBody = RequestBody.create( MediaType.parse("application/json; charset=utf-8"),parms);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(BaseActivity.UrlHead+url).post(requestBody).build();
        Response response =okHttpClient.newCall(request).execute();
        String Data = response.body().string();
        response.close();
        return Data;
    }

    /**
     * 创建等待对话框
     * @param context  上下文
     * @param msg  显示的提示消息
     * @return   对话框对象
     */
    public static ProgressDialog WaitDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("请稍等");
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    /**
     *杜绝Toast重复弹出
     **/
    public static void Toast(Context context, String msg, boolean LongTime) {
        if (Toasting)
            toast.cancel();
        toast = Toast.makeText(context, null, LongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
        Toasting = true;
    }
}
