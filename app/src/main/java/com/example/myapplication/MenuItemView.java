package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/8/12.
 */

public class MenuItemView extends View {
    private static final String TAG = "MenuItemView";
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
    private float mLineSize;
    private Paint mLinePaint;
    private int mLineStartMode;
    private int mLineEndMode;

    public MenuItemView(Context context) {
        super(context);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        mText = typeArray.getString(R.styleable.MenuItemView_text);
        mDrawLeft = typeArray.getResourceId(R.styleable.MenuItemView_drawLeft, R.mipmap.ic_launcher);
        mDrawRight = typeArray.getResourceId(R.styleable.MenuItemView_drawRight, R.mipmap.ic_launcher);


        mTextSize = (int) typeArray.getDimension(R.styleable.MenuItemView_textSize, 16);
        mTextColor = typeArray.getColor(R.styleable.MenuItemView_textColor, 0xFFFFFFFF);
        mTextPaddLeft = typeArray.getDimensionPixelSize(R.styleable.MenuItemView_textPaddLeft, 0);
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

        mLineColor = typeArray.getColor(R.styleable.MenuItemView_lineColor, 0xFFFFFFFF);
        mLineSize = typeArray.getDimensionPixelSize(R.styleable.MenuItemView_lineSize, 10);
        mLineStartMode = typeArray.getInt(R.styleable.MenuItemView_lineStart, 0);
        mLineEndMode = typeArray.getInt(R.styleable.MenuItemView_lineEnd, 0);
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);


    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1 这个也可以居中
//        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
//        float    offY = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
//        canvas.drawText(mText, -mRect.left + getPaddingLeft() + mleftBitmap.getWidth(),getHeight()/2+offY, mPaintText);
        // 2 这个 比较居中
        canvas.drawText(mText, -mRect.left + getPaddingLeft() + mleftBitmap.getWidth() + mTextPaddLeft, -mRect.top / 2 + getHeight() / 2, mPaintText);

    //    canvas.drawBitmap(mleftBitmap, getPaddingLeft(), getPaddingTop(), null);
        drawBitmap(canvas,mleftBitmap, getPaddingLeft(), getPaddingTop());
        //canvas.drawBitmap(mRightBitmap, getWidth() - getPaddingRight() - mRightBitmap.getWidth(), getPaddingTop(), null);
        drawBitmap(canvas,mRightBitmap, getWidth() - getPaddingRight() - mRightBitmap.getWidth(), getPaddingTop());
        drawLine(canvas);
    }
    /*---------------------------------
        * 绘制图片
        * @param       x屏幕上的x坐标
        * @param       y屏幕上的y坐标
        * @param       w要绘制的图片的宽度
        * @param       h要绘制的图片的高度
        * @param       bx图片上的x坐标
        * @param       by图片上的y坐标
        * http://www.cnblogs.com/error404/archive/2012/02/05/2339255.html
        * @return      null
        ------------------------------------*/

    /**
     *  1 图片  比 可画 的高度下
     *  2. 图片  比 可画的 高度大
     * @param canvas
     * @param bitmap
     * @param x
     * @param y
     */
    private  void drawBitmap(Canvas canvas, Bitmap bitmap, int x, int y)
    {
        int bx=0;
        int by=0;

        //x,y表示绘画的起点，
        Rect src = new Rect();// 图片
        Rect dst = new Rect();// 屏幕位置及尺寸

        //src 这个是表示绘画图片的大小
        src.left = bx;   //0,0
        src.top = by;
        src.right = bitmap.getWidth();  //bx + w;// mBitDestTop.getWidth();,这个是桌面图的宽度，
        src.bottom = bitmap.getHeight();// by + h;//mBitDestTop.getHeight()/2;// 这个是桌面图的高度的一半

        // 下面的 dst 是表示 绘画这个图片的位置
        dst.left = x;    //miDTX,//这个是可以改变的，也就是绘图的起点X位置
        dst.top = (getHeight() -getPaddingTop()-getPaddingBottom()-bitmap.getHeight())/2+ getPaddingTop() ;    //mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标
        dst.right = x+bitmap.getWidth();    //miDTX + mBitDestTop.getWidth();// 表示需绘画的图片的右上角
        dst.bottom = (getHeight()+bitmap.getHeight())/2  ;    // mBitQQ.getHeight() + mBitDestTop.getHeight();//表示需绘画的图片的右下角
        canvas.drawBitmap(bitmap, src, dst, null);//这个方法  第一个参数是图片原来的大小，第二个参数是 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置

        src = null;
        dst = null;

    }

    /**
     * <flag name="padd" value="0"/>
     <flag name="draw" value="1"/>
     <flag name="text" value="2"/>
     <flag name="view" value="3"/>
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        float left=getPaddingLeft();
        float right=getWidth() -getPaddingRight();

        switch (mLineStartMode) {
            case 2:
                left=getPaddingLeft() + mleftBitmap.getWidth() + mTextPaddLeft - mRect.left;
//                canvas.drawLine(getPaddingLeft() + mleftBitmap.getWidth() + mTextPaddLeft - mRect.left, getHeight() - mLineSize - getPaddingBottom(),
//                        getWidth() - getPaddingRight() - mRightBitmap.getWidth(), getHeight() - mLineSize - getPaddingBottom(), mLinePaint);
                break;
        }

        switch (mLineEndMode){

            case 1:
                break;
            case 3:
                right=getWidth()+getPaddingRight();
            break;
        }
        canvas.drawLine(left, getHeight() - getPaddingBottom(), right, getHeight()  - getPaddingBottom(), mLinePaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec); // 设置 大小
        int height = 0;
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        /**
         * 设置高度
         */
        int specMode = MeasureSpec.getMode(heightMeasureSpec); // 获取高 的模式 match_parent wrap_content
        /**
         * MeasureSpec.EXACTLY是精确尺寸，当我们将控件的layout_width或layout_height指定为具体数值时如andorid:layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
         * MeasureSpec.AT_MOST是最大尺寸，当控件的layout_width或layout_height指定为WRAP_CONTENT时，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
         * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，通过measure方法传入的模式。
         */
        if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
        {
            height = Math.max(height, View.MeasureSpec.getSize(heightMeasureSpec));
        } else {
            height = Math.max(height, mRect.height());
            height = Math.max(height, mleftBitmap.getHeight());
            height = Math.max(height, mRightBitmap.getHeight());
            height = height + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);

    }

    private void Log(String log) {
        Log.e(TAG, log);
    }
}
