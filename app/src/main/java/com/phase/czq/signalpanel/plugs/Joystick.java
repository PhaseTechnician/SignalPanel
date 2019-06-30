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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.phase.czq.signalpanel.R;

public class Joystick extends View {

    private int backImg,forImg;
    private int axeX,axeY;
    private boolean autoCentral;
    private Paint paint,fillpaint;

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
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(0,0,getWidth(),getHeight());
        canvas.drawRect(rect, paint);
        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,canvas.getWidth()/2,fillpaint);

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
}
