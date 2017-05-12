package com.example.cs4962.palettemaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Bharath on 8/31/2015.
 */
public class Knob extends View
{

    public Knob(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public interface OnValueChangedListener
    {
        public void onValueChanged(double value);
    }

    OnValueChangedListener _onValueChangedListener = null;

    public OnValueChangedListener getonValueChangedListener()
    {
        return _onValueChangedListener;
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener)
    {
        this._onValueChangedListener = onValueChangedListener;
    }

    private double _value = 0.25;
    private double _minValue = 0.0;
    private double _maxValue = 1.0;
    private RectF _knobRect = new RectF();



    public double getValue()
    {
        return _value;
    }
    public void setValue(double value)
    {
        _value = value;
        invalidate();
    }

    public double getMinValue()
    {
        return _minValue;
    }

    public void setMinValue(double minValue)
    {
        _minValue = minValue;
        invalidate();
    }

    public double getMaxValue()
    {
        return _maxValue;
    }

    public void setMaxValue(double maxValue)
    {
        _maxValue = maxValue;
        invalidate();
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle parcel =  new Bundle();
        Parcelable superClassParcel = super.onSaveInstanceState();
        parcel.putParcelable("superClassData", superClassParcel);
        parcel.putDouble("value", _value);
        return parcel;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(state == null || !(state instanceof Bundle))
            return;
        Bundle parcel = (Bundle)state;
        Parcelable superClass = parcel.getParcelable("superClassData");
        super.onRestoreInstanceState(superClass);
        Double value = parcel.getDouble("value", 1.0);
        setValue(value);
    }

    @Override
    public  boolean onTouchEvent(MotionEvent event)
    {
        PointF knobTouchPoint = new PointF(event.getX()- _knobRect.centerX(),event.getY()-_knobRect.centerY());
        double knobAngle = Math.atan2(knobTouchPoint.y, knobTouchPoint.x);
        double knobValue = ((knobAngle/(-2.0 * Math.PI)) * (_maxValue-_minValue) + _minValue);
        setValue(knobValue);
        if(_onValueChangedListener != null)
            _onValueChangedListener.onValueChanged(_value);
        return true; //TODO: Radius check
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth = width  ;
        int measureHeight = height ;
        int desiredSize = (int) (getResources().getDisplayMetrics().density * 160 * 1.0);

        if(widthMod == MeasureSpec.AT_MOST && heightMod == MeasureSpec.AT_MOST)
        {
            measureWidth = Math.min(Math.min(width,height),desiredSize);
            measureHeight = measureWidth;
        }

        if(widthMod == MeasureSpec.AT_MOST && heightMod == MeasureSpec.EXACTLY)
        {
            if(height > desiredSize)
            {
                measureHeight = height;
                measureWidth = measureHeight;
            }
            else
            {
                measureWidth = Math.min(width, height);
                measureHeight = height;

            }
        }
        if(widthMod == MeasureSpec.EXACTLY && heightMod == MeasureSpec.AT_MOST)
        {
            if(width > desiredSize)
            {
                measureHeight = width;
                measureWidth = width;
            }
            else
            {
                measureWidth = width;
                measureHeight = Math.min(width, height);
            }
        }

        if(measureWidth < desiredSize )
        {
            measureWidth |= MEASURED_STATE_TOO_SMALL;
        }

        if(measureHeight < desiredSize)
            measureHeight |= MEASURED_STATE_TOO_SMALL;


        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(Color.GRAY);


        _knobRect.left = getPaddingLeft();
        _knobRect.top = getPaddingTop();
        _knobRect.right = getWidth() - getPaddingRight();
        _knobRect.bottom = getHeight() - getPaddingBottom();

        if(_knobRect.width() < _knobRect.height())
        {
            _knobRect.bottom -= _knobRect.height() - _knobRect.width();
        }
        else
            _knobRect.right -= _knobRect.width() - _knobRect.height();

        canvas.drawOval(_knobRect, knobPaint);

        Paint nibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nibPaint.setColor(Color.WHITE);
        nibPaint.setStrokeWidth(10*getResources().getDisplayMetrics().density);

        float nibAngle = (float) ((_value - _minValue)/(_maxValue-_minValue)* -2.0 * Math.PI);
        float knobRadius = _knobRect.width() * 0.5f;
        PointF nibEndPoint = new PointF();
        nibEndPoint.x = _knobRect.centerX() + knobRadius * (float)Math.cos(nibAngle);
        nibEndPoint.y = _knobRect.centerX() + knobRadius * (float)Math.sin(nibAngle);
        PointF nibStartPoint = new PointF();
        nibStartPoint.x = _knobRect.centerX() + knobRadius *0.8f * (float)Math.cos(nibAngle);
        nibStartPoint.y = _knobRect.centerX() + knobRadius * 0.8f * (float)Math.sin(nibAngle);

        canvas.drawLine(nibStartPoint.x, nibStartPoint.y, nibEndPoint.x, nibEndPoint.y, nibPaint);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        p.setTextSize(50.f);
        canvas.drawText(""+_value, _knobRect.centerX(), _knobRect.centerY(),p);
    }

}
