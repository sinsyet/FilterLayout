package com.example.filterlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by sin on 2017/7/17.
 */

public class TriangleView extends View {
    private static final String TAG = "TriangleView";

    private int mColor;
    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    private Path path;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(dp2px(3));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw: ");


        path.reset();
        path.moveTo(0, mHeight);
        path.lineTo(mWidth / 2, 0);
        path.lineTo(mWidth, mHeight);
        path.close();

        canvas.drawPath(path, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Log.e(TAG, "onSizeChanged: ");
    }

    public void setTriangleColor(int color) {
        mColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    private int dp2px(float dp) {
        // 获取屏幕分辨率
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // 将值转换为设备独立像素转换为
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics) + 0.5f);
    }
}
