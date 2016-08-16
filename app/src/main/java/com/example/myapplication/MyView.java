package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/8/12.
 */

public class MyView extends View {
    // 基本属性
    private String mText;
    private int mDrawLeft;
    private int mDrawRight;

    // 文字画笔
    private Paint mPaintText;
    private int mTextSize;
    private int mTextColor;
    private Rect mRect; //  用于计算 text 的宽高
    private float mTextPaddLeft;

    // 左右 图片
    private Bitmap mRightBitmap;
    private Bitmap mleftBitmap;

    private int mLineColor;
    private  float mLineSize;
    private  Paint mLinePaint;
    private  int mLineStartMode;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        mText = typeArray.getString(R.styleable.MyView_text);
        mDrawLeft = typeArray.getResourceId(R.styleable.MyView_drawLeft, R.mipmap.ic_launcher);
        mDrawRight = typeArray.getResourceId(R.styleable.MyView_drawRight, R.mipmap.ic_launcher);


        mTextSize = (int) typeArray.getDimension(R.styleable.MyView_textSize, 16);
        mTextColor = typeArray.getColor(R.styleable.MyView_textColor, 0xFFFFFFFF);
        mTextPaddLeft=typeArray.getDimensionPixelSize(R.styleable.MyView_textPaddLeft,0);
        // 设置字体
        mPaintText = new Paint();
        mPaintText.setColor(mTextColor);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.LEFT);
        mRect = new Rect();
        mPaintText.getTextBounds(mText, 0, mText.length(), mRect);

        mRightBitmap = BitmapFactory.decodeResource(getResources(), mDrawRight);
        mleftBitmap = BitmapFactory.decodeResource(getResources(), mDrawLeft);

        mLineColor=typeArray.getColor(R.styleable.MyView_lineColor,0xFFFFFFFF);
        mLineSize=typeArray.getDimensionPixelSize(R.styleable.MyView_lineSize,10);
        mLineStartMode=typeArray.getInt(R.styleable.MyView_lineStart,0);
        mLinePaint=new Paint();
        mLinePaint.setColor(mLineColor);


    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Log.e("111", "mRect : " + mRect.left + "  mRect : " + mRect.top);

        //1 这个也可以剧中
//        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
//        float    offY = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
//        canvas.drawText(mText, -mRect.left + getPaddingLeft() + mleftBitmap.getWidth(),getHeight()/2+offY, mPaintText);
        // 2 这个 比较居中
      canvas.drawText(mText, -mRect.left + getPaddingLeft() + mleftBitmap.getWidth() + mTextPaddLeft,-mRect.top/2+ getHeight()/2, mPaintText);

//        Log(getPaddingLeft() + " getPaddingLeft() ");
//        Log(getTop() + " getTop() ");
        canvas.drawBitmap(mRightBitmap, getWidth() - getPaddingRight() - mRightBitmap.getWidth(), getPaddingTop(), null);
        canvas.drawBitmap(mleftBitmap, getPaddingLeft(), getPaddingTop(), null);



        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        switch (mLineStartMode){
            case 0:
                canvas.drawLine(getPaddingLeft(),getHeight()-mLineSize-getPaddingBottom(),getWidth()-getPaddingRight(),getHeight()-mLineSize-getPaddingBottom(),mLinePaint);
                break;
            case 2:

                canvas.drawLine(getPaddingLeft()+mleftBitmap.getWidth()+mTextPaddLeft-mRect.left, getHeight()-mLineSize-getPaddingBottom(),
                        getWidth()-getPaddingRight()- mRightBitmap.getWidth(),   getHeight()-mLineSize-getPaddingBottom(),mLinePaint
                        );
            break;
        }


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); // 设置 大小
        int height = 0;
        int width = View.MeasureSpec.getSize(widthMeasureSpec);

        //     Log.e("11111111111111","height : "+ height+ "  heightMeasureSpec : "+heightMeasureSpec +" text :"+ mRect.height() +"   left :" + mleftBitmap.getHeight() + "  right : "+ mRightBitmap.getHeight());

        /**
         * 设置高度
         */
        int specMode = MeasureSpec.getMode(heightMeasureSpec); // 获取高 的模式 match_parent wrap_content
        height = Math.max(height, mRect.height());
        height = Math.max(height, mleftBitmap.getHeight());
        height = Math.max(height, mRightBitmap.getHeight());
        height=height+getPaddingTop()+ getPaddingBottom();
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            height = Math.max(height, View.MeasureSpec.getSize(heightMeasureSpec));
        }


        setMeasuredDimension(width, height);

    }

    private void Log(String log) {
        Log.e("1111111", log);
    }
}
