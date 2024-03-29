package com.example.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Bharath on 9/15/2015.
 */
public class PaintView extends View
{

    public interface OnColorTouchedListener
    {
        void onColorTouch(PaintView pv);
    }

    OnColorTouchedListener _onColorTouchedListener = null;
    public void setOnColorTouchedListener(OnColorTouchedListener onColorTouchedListener)
    {
        _onColorTouchedListener = onColorTouchedListener;
    }

    private RectF _contentRect;
    private int _color = Color.BLACK;
    private boolean _active = false;
    private float _radius;

    public PaintView(Context context)
    {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        float circleCenterX = _contentRect.centerX();
        float circleCenterY = _contentRect.centerY();

        float distance = (float)Math.sqrt((circleCenterX - x)*(circleCenterX-x)+(circleCenterY-y)*(circleCenterY-y));

        if(distance < _radius)
        if(_onColorTouchedListener != null)
            _onColorTouchedListener.onColorTouch(this);

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if(widthMode == MeasureSpec.AT_MOST)
            width = widthSpec;
        if(heightMode == MeasureSpec.AT_MOST)
            height = heightSpec;

        if(widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
            height = width;
        }
        if(heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
            width = height;
        }

        if(width > height && widthMode != MeasureSpec.EXACTLY)
            width = height;
        if(height > width && heightMode != MeasureSpec.EXACTLY)
            height = width;

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL : 0),
                resolveSizeAndState(height, widthMeasureSpec, height < getSuggestedMinimumHeight() ? MEASURED_STATE_TOO_SMALL : 0));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(_color);

        Path path = new Path();

      //  int pointCount = 30;

        _contentRect = new RectF();
        _contentRect.left = getPaddingLeft();
        _contentRect.top = getPaddingTop();
        _contentRect.right = getWidth() - getPaddingRight();
        _contentRect.bottom = getHeight() - getPaddingBottom();
        paint.setColor(0xFF000000|(int)(Math.random()*0x00FFFFFF));
        canvas.drawRect(_contentRect, paint);
        paint.setColor(_color);

    //   PointF center = new PointF(_contentRect.centerX(),_contentRect.centerY());

  //      _radius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);

/*

        int pointIndex = 0;
        while(pointIndex < pointCount)
        {

            _radius += (float)(Math.random() - 0.5f) *2.0f *0.05f * _radius;
            PointF point = new PointF();

            point.x = center.x + _radius *
                    (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
            point.y = center.y + _radius *
                    (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

            if(isPointInShape(_contentRect,point.x,point.y))
            {
                if (pointIndex == 0)
                    path.moveTo(point.x, point.y);
                else
                    path.lineTo(point.x, point.y);
                pointIndex++;
            }
        }
*/

        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());
        float maxRadius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
        float minRadius = 0.25f * maxRadius;
        _radius = minRadius + (maxRadius - minRadius) * 0.75f;
        int pointCount = 50;
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex += 3) {

            PointF control1 = new PointF();
            float control1Radius = _radius + (float) (Math.random() - 0.5f) * 2.0f * (maxRadius - _radius);
            control1.x = center.x + control1Radius * (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
            control1.y = center.y + control1Radius * (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

            PointF control2 = new PointF();
            float control2Radius = _radius + (float) (Math.random() - 0.5f) * 2.0f * (maxRadius - _radius);
            control2.x = center.x + control2Radius * (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
            control2.y = center.y + control2Radius * (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

            if (pointIndex == 0)
                path.moveTo((control1.x + control2.x) / 2, (control1.y + control2.y) / 2);
            else
                path.lineTo((control1.x + control2.x) / 2, (control1.y + control2.y) / 2);
        }

        /*
        float maxRadius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
        float minRadius = 0.25f * maxRadius;
        _radius = minRadius + (maxRadius - minRadius) * 0.50f;

        for (int pointIndex = 0; pointIndex < pointCount; pointIndex += 3) {

            PointF control1 = new PointF();
            float control1Radius = _radius + (float) (Math.random() - 0.5f) * 2.0f * maxRadius;
            control1.x = center.x + control1Radius * (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
            control1.y = center.y + control1Radius * (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

            PointF control2 = new PointF();
            float control2Radius = _radius + (float) (Math.random() - 0.5f) * 2.0f * minRadius;
            control2.x = center.x + control2Radius * (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
            control2.y = center.y + control2Radius * (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

            PointF point = new PointF();
            point.x = center.x + _radius * (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
            point.y = center.y + _radius * (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

            if (pointIndex == 0)
                path.moveTo(point.x, point.y);
            else

                path.rCubicTo(control1.x,control1.y,control2.x,control2.y,point.x, point.y);
        }

        */
        path.close();
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPath(path, paint);
        if(_active)
        {
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8.0f);
            canvas.drawPath(path,paint);
        }
    }

    private boolean isPointInShape(RectF rect,float x, float y)
    {
        boolean inX = false;
        boolean inY = false;
        float diff = 0.0f;
        if (x > rect.left && x < rect.right)
            inX = true;
        if (y < rect.bottom && y > rect.top)
            inY = true;

        return (inX && inY);
    }

    public int getColor()
    {
        return _color;
    }

    public void setColor(int color)
    {
        _color = color;
        invalidate();
    }

    public void setActive(boolean active)
    {
        _active = active;
        invalidate();
    }

}
