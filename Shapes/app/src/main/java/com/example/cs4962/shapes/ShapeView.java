package com.example.cs4962.shapes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Bharath on 11/7/2015.
 */
public class ShapeView extends View
{

    int sideCount = 5;
    int shapeColor = Color.GREEN;

    public ShapeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        int sideCount = attributes.getInt(R.styleable.ShapeView_sideCount,3);
        int shapeColor = attributes.getInt(R.styleable.ShapeView_shapeColor,Color.YELLOW);
        attributes.recycle();

        setShapeColor(shapeColor);
        setSideCount(sideCount);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Path path = new Path();
        for(int sideIndex = 0; sideIndex < sideCount; sideIndex++)
        {
            float angle = (float)(Math.PI * 2 / (float) sideCount * (float) sideIndex);
            float x = getWidth() * ((float)Math.cos(angle) + 1) *.5f;
            float y = getHeight() * ((float)Math.sin(angle) + 1) *.5f;

            if(sideIndex == 0)
            {
                path.moveTo(x,y);
            }
            else
                path.lineTo(x,y);

        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(shapeColor);
        canvas.drawPath(path,paint);
    }

    public int getSideCount()
    {
        return sideCount;
    }

    public void setSideCount(int _sideCount)
    {
        sideCount = _sideCount;
        invalidate();
        requestLayout();
    }
    public int getShapeColor()
    {
        return shapeColor;
    }

    public void setShapeColor(int _shapeColor)
    {
        shapeColor = _shapeColor;
        invalidate();
    }
}
