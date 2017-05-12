package com.example.cs4962.annimations;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bharath on 10/3/2015.
 */
public class TransformView extends ViewGroup
{
    public TransformView(Context context)
    {
        super(context);
        this.setBackgroundColor(Color.BLACK);
        View view = new View(context);
        view.setBackgroundColor(Color.GRAY);
        addView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        PointF _touchPoint = new PointF(event.getX(),event.getY());

        View view = getChildCount() == 0? null : getChildAt(0);
        if(view != null)
        {
            view.setX(_touchPoint.x);
            view.setY(_touchPoint.y);
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childSize = (int)(Math.min(getWidth(),getHeight()) * 0.2);
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View childView = getChildAt(childIndex);
            Rect childRect = new Rect();
            childRect.left = getWidth()/2 -childSize/2;
            childRect.top = getHeight()/2 -childSize/2;
            childRect.right = getWidth()/2 +childSize/2;
            childRect.bottom = getHeight()/2 +childSize/2;


            childView.layout(childRect.left,childRect.top,childRect.right,childRect.bottom);
        }
    }
}
