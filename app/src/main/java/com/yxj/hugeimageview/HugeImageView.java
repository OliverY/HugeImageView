package com.yxj.hugeimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author:  Yxj
 * Time:    2019/5/16 上午8:21
 * -----------------------------------------
 * Description:
 */
public class HugeImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {


    private InputStream is;
    private Rect rect;
    private int mWidth;
    private int mHeight;
    private float scale;
    private BitmapFactory.Options options;
    private Matrix matrix;
    private BitmapRegionDecoder bitmapRegionDecoder;
    private Bitmap bitmap;
    private int imgWidth;
    private int imgHeight;
    private GestureDetector gestureDetector;
    private Scroller scroller;

    public HugeImageView(Context context) {
        this(context,null);
    }

    public HugeImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HugeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rect = new Rect();

        setOnTouchListener(this);
        gestureDetector = new GestureDetector(context, this);
        scroller = new Scroller(context);
    }

    public void setImageStream(InputStream is){
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null, options);
        imgWidth = options.outWidth;
        imgHeight = options.outHeight;

        options.inMutable = true;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        try {
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(is,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(bitmapRegionDecoder != null){

            options.inBitmap = bitmap;
            bitmap = bitmapRegionDecoder.decodeRegion(rect,options);

            matrix = new Matrix();
            matrix.setScale(scale,scale);
            canvas.drawBitmap(bitmap,matrix,null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        rect.top = 0;
        rect.left = 0;
        rect.right = imgWidth;
        scale = mWidth*1.0f/imgWidth;
        rect.bottom = (int) (mHeight/ scale);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if(!scroller.isFinished()){
            scroller.forceFinished(true);
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        rect.offset(0, (int) distanceY);
        if(rect.top <= 0){
            rect.top = 0;
            rect.bottom = (int) (rect.top + 1.0f*mHeight/scale);
        }else if(rect.bottom >= imgHeight){
            rect.bottom = imgHeight;
            rect.top = (int) (rect.bottom - mHeight*1.0f/scale);
        }

        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        scroller.fling(0,rect.top,0, (int) -velocityY,0,0,0, (int) (imgHeight - mHeight/scale));
        return false;
    }

    @Override
    public void computeScroll() {
        if(scroller.isFinished()){
            return;
        }
        if(scroller.computeScrollOffset()){
            rect.top=scroller.getCurrY();
            rect.bottom=rect.top+(int)(mHeight/scale);
            invalidate();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
