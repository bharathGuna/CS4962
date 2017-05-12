package com.example.cs4962.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bharath on 9/17/2015.
 */
public class PaletteView extends ViewGroup
{
    public PaletteView(Context context)
    {
        super(context);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float childSize = Math.min(getWidth(),getHeight())/ getChildCount() * 2.0f;
        RectF layoutOvalRect = new RectF();
        layoutOvalRect.left = getPaddingLeft() + childSize *0.5f;
        layoutOvalRect.top = getPaddingTop() + childSize * 0.5f;
        layoutOvalRect.right = getWidth()  - getPaddingRight() -  childSize *0.5f;
        layoutOvalRect.bottom = getHeight() - getPaddingBottom() -   childSize * 0.5f;

        canvas.drawOval(layoutOvalRect, new Paint());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        int childState = 0;
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
           child.measure(MeasureSpec.AT_MOST| (width/getChildCount()), MeasureSpec.AT_MOST | (height/getChildCount()));
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            childState = combineMeasuredStates(childState,child.getMeasuredState());
        }
        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState)
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childWidthMax = 0;
        int childHeightMax = 0;
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
            childWidthMax = Math.max(childWidthMax,child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax,child.getMeasuredHeight());
        }
      //  float childSize = Math.min(getWidth(),getHeight())/ getChildCount() * 2.0f;
        RectF layoutOvalRect = new RectF();
       /* layoutOvalRect.left = getPaddingLeft() + childSize *0.5f;
        layoutOvalRect.top = getPaddingTop() + childSize * 0.5f;
        layoutOvalRect.right = getWidth()  - getPaddingRight() -  childSize *0.5f;
        layoutOvalRect.bottom = getHeight() - getPaddingBottom() -   childSize * 0.5f;
*/
        layoutOvalRect.left = getPaddingLeft() + childWidthMax *0.5f;
        layoutOvalRect.top = getPaddingTop() + childWidthMax * 0.5f;
        layoutOvalRect.right = getWidth()  - getPaddingRight() -  childWidthMax *0.5f;
        layoutOvalRect.bottom = getHeight() - getPaddingBottom() -   childHeightMax * 0.5f;

        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View childView = getChildAt(childIndex);
            float childTheta = (float)childIndex/(float)getChildCount()*2.0f * (float)Math.PI;

            PointF childCenter = new PointF();

            childCenter.x = layoutOvalRect.centerX()+layoutOvalRect.width() * 0.5f * (float)Math.cos(childTheta);
            childCenter.y = layoutOvalRect.centerY()+layoutOvalRect.height() * 0.5f * (float)Math.sin(childTheta);

           // childView.getMeasuredWidth();

            Rect childRect = new Rect();
            childRect.left = (int)(childCenter.x - childWidthMax * 0.5f);
            childRect.top = (int) (childCenter.y - childHeightMax * 0.5f);
            childRect.right = (int)(childCenter.x + childWidthMax * 0.5f);
            childRect.bottom = (int) (childCenter.y + childHeightMax * 0.5f);

            childView.layout(childRect.left,childRect.top,childRect.right,childRect.bottom);
        }
    }
}
