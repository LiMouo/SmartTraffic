package com.lenovo.smarttraffic.TrafficHome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import java.lang.reflect.Type;

public class ViewText extends View {

    /*                              畅通       缓行      一般拥堵 中度拥堵    严重拥堵*/
    private String[] setColor = {"#6ab82e", "#ece93a", "#f49b25", "#e33532", "#b01e23"};
    private int[] count = new int[7];

    public ViewText(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //防锯齿 setAntiAlias
        //设置字体 setTypeface
        //设置样式 setStyle 1.填充内容 Paint.Style.FILL  2.描边 Paint.Style.STROKE 3.Paint.Style.FILL_AND_STROKE 描边+填充
        //设置颜色 setColor
        //设置画笔宽度 setStrokeWidth

        //黑色字体
        Paint wordSize = new Paint();
        wordSize.setAntiAlias(true);
        wordSize.setTextSize(18);
        wordSize.setTypeface(Typeface.MONOSPACE);

        //学院路
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.parseColor(setColor[count[0]]));
        paint1.setStrokeWidth(26);
        canvas.drawLine(210, 290, 210, 55, paint1);

        //联想路
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.parseColor(setColor[count[1]]));
        paint2.setStrokeWidth(26);
        canvas.drawLine(370, 290, 370, 55, paint2);

        //幸福路
        Paint paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setColor(Color.parseColor(setColor[count[2]]));
        paint3.setStyle(Paint.Style.FILL);
        paint3.setStrokeWidth(26);
        canvas.drawLine(210, 350, 210, 580, paint3);

        //医院路
        Paint paint4 = new Paint();
        paint4.setStrokeWidth(26);
        paint4.setStyle(Paint.Style.FILL);
        paint4.setColor(Color.parseColor(setColor[count[3]]));
        paint4.setAntiAlias(true);
        canvas.drawLine(370, 350, 370, 580, paint4);

    }
}
