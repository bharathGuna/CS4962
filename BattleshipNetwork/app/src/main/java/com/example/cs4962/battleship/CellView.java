package com.example.cs4962.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Bharath on 10/21/2015.
 */
public class CellView extends View
{
    public interface OnCellClickListener
    {
        void onCellClick(CellView cellView);
    }

    private OnCellClickListener onCellClickListener = null;
    private CellType type;
    private Point location;
    private boolean orientation;

    public void setOnCellClickListener(OnCellClickListener _onCellClickListener)
    {
        onCellClickListener = _onCellClickListener;
    }

    public CellView(Context context)
    {
        super(context);
        this.setBackgroundColor(Color.GREEN);
        type = CellType.NONE;
    }

    public void setType(CellType _type)
    {
        type = _type;
        invalidate();
    }

    public CellType getType()
    {
        return type;
    }
    public void setLocation(int x,int y)
    {
        location = new Point(x,y);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(onCellClickListener != null)
            onCellClickListener.onCellClick(this);
        return super.onTouchEvent(event);
    }

    public Point getLocation()
    {
        return location;
    }




    public OnCellClickListener getOnCellClickListener()
    {
        return onCellClickListener;
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

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        RectF rect = new RectF();
        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = getWidth() - getPaddingRight();
        rect.bottom = getHeight() - getPaddingBottom();


        switch(type)
        {
            case NONE:
                paint.setColor(Color.BLUE);
                break;
            case SHIP:
                paint.setColor(Color.GRAY);
                break;
            case HIT:
                paint.setColor(Color.RED);
                break;
            case MISS:
                paint.setColor(Color.WHITE);
                break;

        }

        canvas.drawRect(rect,paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth()*.2f);
        canvas.drawRect(rect,paint);

    }
}
