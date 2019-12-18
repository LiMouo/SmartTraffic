package com.lenovo.smarttraffic.TrafficHome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


public class ViewTraffic extends View {
    public ViewTraffic(Context context) {
        super(context);
    }
    /*                              畅通       缓行      一般拥堵 中度拥堵    严重拥堵*/
    private String[] setColor = {"#6ab82e", "#ece93a", "#f49b25", "#e33532", "#b01e23"};
    private int[] count = new int[7];


    public ViewTraffic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        /*setShow(); *//*启动查询道路状态线程*/
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
        //第二个X是下面的点的长度,第二个Y是下面的点的长度
        //第一个X是上面的开始距离，第一个Y是
        canvas.drawLine(290, 500, 290, 120, paint1);

        //联想路
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.parseColor(setColor[count[1]]));
        paint2.setStrokeWidth(26);
        canvas.drawLine(570, 500, 570, 120, paint2);

        //幸福路
        Paint paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setColor(Color.parseColor(setColor[count[2]]));
        paint3.setStyle(Paint.Style.FILL);
        paint3.setStrokeWidth(26);
        canvas.drawLine(290, 960, 290, 580, paint3);

        //医院路
        Paint paint4 = new Paint();
        paint4.setStrokeWidth(26);
        paint4.setStyle(Paint.Style.FILL);
        paint4.setColor(Color.parseColor(setColor[count[3]]));
        paint4.setAntiAlias(true);
        canvas.drawLine(570, 960, 570, 580, paint4);


        /*环城快速路*/
        Paint paint5 = new Paint();
        paint5.setAntiAlias(true);
        paint5.setStyle(Paint.Style.FILL);
        paint5.setColor(Color.parseColor(setColor[count[4]]));
        paint5.setStrokeWidth(64);
        //1.上线左点 2.上线右点 3.左线上边的点 4.左线下边的点 5.下线左边的点 6.下线右边的点
        canvas.drawLines(new float[]{105, 75, 1000, 75, 70, 40, 70, 1040, 105, 1000, 990, 1000}, paint5);
        //RectF 画布
        RectF rectF2 = new RectF(135, 27, 90, 80);
        canvas.drawArc(rectF2, 180, 90, true, paint5);
        RectF rectF3 = new RectF(35, 553, 90, 607);
        canvas.drawArc(rectF3, 90, 90, true, paint5);


        /*环城高速*/
        Paint paint6 = new Paint();
        paint6.setAntiAlias(true);
        paint6.setColor(Color.parseColor(setColor[count[5]]));
        paint6.setStyle(Paint.Style.FILL);
        paint6.setStrokeWidth(64);
        /*new float[]{685, 50, 685, 580, 625, 40, 672, 40, 625, 594, 672, 593}*/
        canvas.drawLine(1025,45,1025,1040, paint6);
        RectF rectF4 = new RectF(645, 27, 698, 80);
        canvas.drawArc(rectF4, 270, 90, true, paint6);
        RectF rectF5 = new RectF(644, 553, 698, 606);
        canvas.drawArc(rectF5, 360, 90, true, paint6);

        /*停车场*/
        Paint paint7 = new Paint();
        paint7.setAntiAlias(true);
        paint7.setStyle(Paint.Style.FILL);
        paint7.setColor(Color.parseColor(setColor[count[6]]));
        paint7.setStrokeWidth(140);
        canvas.drawLine(790, 660, 790, 410, paint7);
        ShowText(canvas, wordSize);

        Paint paint8 = new Paint();
        paint8.setAntiAlias(true);
        paint8.setStyle(Paint.Style.FILL);
        paint8.setColor(Color.parseColor("#ffffff"));
        paint8.setStrokeWidth(80);
        canvas.drawLine(100, 540, 585, 540, paint8);
        ShowText(canvas, wordSize);
    }

    void ShowText(Canvas canvas, Paint paint1){
        canvas.drawText("环 城 快 速 路", 540, 80, paint1);
        canvas.drawText("环", 60, 200, paint1);
        canvas.drawText("城", 60, 230, paint1);
        canvas.drawText("快", 60, 260, paint1);
        canvas.drawText("速", 60, 290, paint1);
        canvas.drawText("路", 60, 320, paint1);
        canvas.drawText("环", 60, 600, paint1);
        canvas.drawText("城", 60, 630, paint1);
        canvas.drawText("快", 60, 660, paint1);
        canvas.drawText("速", 60, 690, paint1);
        canvas.drawText("路", 60, 720, paint1);
        canvas.drawText("环 城 快 速 路", 280, 600, paint1);

        canvas.drawText("环", 1020, 200, paint1);
        canvas.drawText("城", 1020, 230, paint1);
        canvas.drawText("高", 1020, 260, paint1);
        canvas.drawText("速", 1020, 290, paint1);
        canvas.drawText("环", 1020, 600, paint1);
        canvas.drawText("城", 1020, 630, paint1);
        canvas.drawText("高", 1020, 660, paint1);
        canvas.drawText("速", 1020, 690, paint1);

        canvas.drawText("学", 283, 150, paint1);
        canvas.drawText("院", 283, 180, paint1);
        canvas.drawText("路", 283, 210, paint1);

        canvas.drawText("联", 563, 150, paint1);
        canvas.drawText("想", 563, 180, paint1);
        canvas.drawText("路", 563, 210, paint1);

        canvas.drawText("幸", 283, 630, paint1);
        canvas.drawText("福", 283, 650, paint1);
        canvas.drawText("路", 283, 670, paint1);

        canvas.drawText("医", 563, 630, paint1);
        canvas.drawText("院", 563, 650, paint1);
        canvas.drawText("路", 563, 670, paint1);

        canvas.drawText("停", 783, 500, paint1);
        canvas.drawText("车", 783, 520, paint1);
        canvas.drawText("场", 783, 540, paint1);
    }
    @Override
    public void invalidate() {
        super.invalidate();
    }
}
