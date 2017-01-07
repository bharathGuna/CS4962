package com.example.cs4962.painterpalette;

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
    private boolean isDelete = false;
    public PaintView(Context context)
    {
        super(context);
        int preferredSize = (int)(getResources().getDisplayMetrics().density * 40f);
        setMinimumWidth(preferredSize);
        setMinimumHeight(preferredSize);
    }

    public PaintView(Context context, boolean delete)
    {
        this(context);
        isDelete = delete;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        float circleCenterX = _contentRect.centerX();
        float circleCenterY = _contentRect.centerY();

        float distance = (float)Math.sqrt((circleCenterX - x)*(circleCenterX-x)+(circleCenterY-y)*(circleCenterY-y));

        if(distance <= _radius )
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

        int width = getSuggestedMinimumWidth() ;
        int height = getSuggestedMinimumHeight();

        if(widthMode == MeasureSpec.AT_MOST)
        {
            if(width >=widthSpec)
                width = widthSpec;
        }
        if(heightMode == MeasureSpec.AT_MOST)
        {
            if(height >= heightSpec)
                height = heightSpec;
        }

        if(widthMode == MeasureSpec.EXACTLY)
        {
            if(width >= widthSpec)
            {
                width = widthSpec;
                height = width;
            }
        }
        if(heightMode == MeasureSpec.EXACTLY)
        {
            if(height >= widthSpec)
            {
                height = heightSpec;
                width = height;
            }
        }

        if(width > height  && widthMode != MeasureSpec.EXACTLY)
            width = height;
        if(height > width && heightMode != MeasureSpec.EXACTLY)
            height = width;



        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL : 0),
                resolveSizeAndState(height, heightMeasureSpec, height < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL : 0)
        );
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(_color);

        Path path = new Path();

        int pointCount = 50;

        _contentRect = new RectF();
        _contentRect.left = getPaddingLeft();
        _contentRect.top = getPaddingTop();
        _contentRect.right = getWidth() - getPaddingRight();
        _contentRect.bottom = getHeight() - getPaddingBottom();



        PointF center = new PointF(_contentRect.centerX(), _contentRect.centerY());
        float maxRadius = Math.min(_contentRect.width() * 0.5f, _contentRect.height() * 0.5f);
        float minRadius = 0.25f * maxRadius;
        _radius = minRadius + (maxRadius - minRadius) * 0.5f;

        if(!isDelete)
        {
            for (int pointIndex = 0; pointIndex < pointCount; pointIndex++)
            {

                _radius += (float) (Math.random() - 0.5f) * 2.0f * 0.05f * _radius;
                PointF point = new PointF();

                point.x = center.x + _radius *
                        (float) Math.cos(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);
                point.y = center.y + _radius *
                        (float) Math.sin(((double) pointIndex / (double) pointCount) * 2.0 * Math.PI);

                if (isPointInShape(_contentRect, point.x, point.y))
                {
                    if (pointIndex == 0)
                        path.moveTo(point.x, point.y);
                    else
                        path.lineTo(point.x, point.y);
                }
            }


            path.close();
            canvas.drawPath(path, paint);
            if (_active)
            {
                paint.setColor(Color.YELLOW);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8.0f);
                canvas.drawPath(path, paint);
            }
        }
        else
        {

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8.0f);

            paint.setColor(Color.RED);
            float modRad = ((2*_radius)/(float)Math.sqrt(2))/2.0f;
            canvas.drawLine(_contentRect.centerX() - modRad, _contentRect.centerY() + modRad,
                    _contentRect.centerX() + modRad, _contentRect.centerY() - modRad, paint);
            canvas.drawLine(_contentRect.centerX() - modRad, _contentRect.centerY() - modRad,
                    _contentRect.centerX() + modRad, _contentRect.centerY() + modRad, paint);

            paint.setColor(Color.BLACK);
            canvas.drawCircle(_contentRect.centerX(), _contentRect.centerY(), _radius, paint);
            paint.setColor(Color.TRANSPARENT);
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

    public boolean isActive()
    {
        return _active;
    }

    public boolean getDelete()
    {
        return isDelete;
    }

}