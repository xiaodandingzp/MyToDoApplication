package com.example.basesdkzp.baseapi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.basesdkzp.baseapi.util.DensityUtil;

/**
 *圆环进度条
 */

public class CircleProgressBar extends View {
    //画圆所在的距形区域
    RectF oval;
    Paint paint;
    int mProgressColor; //进度颜色
    int mColor; //底色
    int circleWidth;
    private int maxProgress = 100;
    private int progress = 0;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();
        paint = new Paint();
        mProgressColor = Color.rgb(0xff, 0x56, 0x00);
        mColor = Color.BLUE;
        circleWidth = DensityUtil.dip2px(context, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(mColor); // 设置画笔颜色
        canvas.drawColor(Color.TRANSPARENT); // 白色背景
        int progressStrokeWidth = circleWidth;
        paint.setStrokeWidth(progressStrokeWidth); //线宽
        paint.setStyle(Paint.Style.STROKE);

        oval.left = progressStrokeWidth / 2; // 左上角x
        oval.top = progressStrokeWidth / 2; // 左上角y
        oval.right = width - progressStrokeWidth / 2; // 左下角x
        oval.bottom = height - progressStrokeWidth / 2; // 右下角y

        canvas.drawArc(oval, 0, 360, false, paint); // 绘制白色圆圈，即进度条背景
        paint.setColor(mProgressColor);
        canvas.drawArc(oval, -90, -((float) progress / maxProgress) * 360, false, paint); // 绘制进度圆弧，这里是红色

        paint.setStyle(Paint.Style.FILL);
    }

    public void setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        invalidate();
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
        invalidate();
    }

    public void setBgColor(int color) {
        mColor = color;
        invalidate();
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }

    /**
     * 非ＵＩ线程调用
     */
    public void setProgressNotInUiThread(int progress) {
        this.progress = progress;
        this.postInvalidate();
    }
}
