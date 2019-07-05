package com.phase.czq.signalpanel.plugs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.phase.czq.signalpanel.R;

public class Joystick extends View {

    private int maxRangeX = 100;
    private int maxRangeY = 100;
    private int backImg,forImg;
    private int axeX,axeY;
    private int lastAxeX,lastAxeY;
    private boolean autoCentral;
    private boolean locked = false;
    private Paint paint,fillpaint;
    private OnValueChange onValueChange = null;
    private String stickName = "";

    public interface OnValueChange{
        void onAxeXValueChange(int X);
        void onAxeYValueChange(int Y);
        void onAutoCentral();
    }

    public Joystick(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.design_default_color_primary));
        paint.setStyle(Paint.Style.STROKE);//设置内部为空心

        fillpaint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(getResources().getColor(R.color.design_default_color_primary));
        fillpaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public Joystick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Joystick);
        backImg = typedArray.getResourceId(R.styleable.Joystick_JoystickBackgroundImg,0);
        //forImg = typedArray.getString(R.styleable.Joystick_JoystickForwardImg);
        autoCentral = typedArray.getBoolean(R.styleable.Joystick_JoystickAutoCentrel,false);
        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("Joystick",  Integer.toString((int) event.getY()));
        if(!locked){
            if(axeX!=lastAxeX){
                onValueChange.onAxeXValueChange((axeX-getWidth()/2)*2*maxRangeX/getWidth());
            }
            if(axeY!=lastAxeY){
                onValueChange.onAxeYValueChange((axeY-getHeight()/2)*2*maxRangeY/getHeight());
            }
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                lastAxeX = axeX;
                lastAxeY = axeY;
                if(event.getX()>0&&event.getX()<getWidth()){
                    axeX = (int)event.getX();
                }
                if(event.getY()>0&&event.getY()<getHeight()){
                    axeY = (int)event.getY();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(locked){
                    performClick();
                }
            case MotionEvent.ACTION_CANCEL:
                if(autoCentral){
                    if(!locked){
                        onValueChange.onAutoCentral();
                    }
                    lastAxeX = axeX;
                    lastAxeY = axeY;
                    axeX=getWidth()/2;
                    axeY=getHeight()/2;
                }
                break;
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(0,0,getWidth(),getHeight());
        canvas.drawText(stickName,0,0,paint);
        canvas.drawRect(rect, paint);
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()/2,paint);

        canvas.drawCircle(axeX,axeY,20,fillpaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //调用者在布局文件中可能 wrap_content导致宽高不一致
        //确保是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    public void setAutoCentral(boolean enable){
        autoCentral = enable;
    }

    //锁定时，不会响应触摸移动滑块，而是启动onclick。
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public void setOnValueChange(OnValueChange onValueChange) {
        this.onValueChange = onValueChange;
    }
    public void setRange(int XRange,int YRange){
        maxRangeX = XRange;
        maxRangeY = YRange;
    }
    public void setStickName(String stickName) {
        this.stickName = stickName;
    }

}
