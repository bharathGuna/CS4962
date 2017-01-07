package com.example.cs4962.painterpalette;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Bharath on 9/20/2015.
 */
public class PaintArea extends View
{

    private PolyPath _currentPath;
    private ArrayList<PolyPath> allPaths;
    private Paint _drawPaint, _canvasPaint;
    private int _paintColor;
    private Canvas _canvas;
    private Bitmap _canvasBitmap;
    private boolean _paintMode;
    private float _percent = 0.0f;
    private boolean _resume;
    public PaintArea(Context context)
    {
        super(context);
        setUpDrawing();
        _resume = false;
        _paintMode = true;
    }

    private void setUpDrawing()
    {

        _drawPaint = new Paint();
        _drawPaint.setColor(_paintColor);
        _drawPaint.setAntiAlias(true);
        _drawPaint.setStrokeWidth(20);
        _drawPaint.setStyle(Paint.Style.STROKE);
        _drawPaint.setStrokeJoin(Paint.Join.ROUND);
        _drawPaint.setStrokeCap(Paint.Cap.ROUND);
        _canvasPaint = new Paint(Paint.DITHER_FLAG);
        allPaths = new ArrayList<PolyPath>();
    }




    @Override
    protected void onDraw(Canvas canvas) {

        Path path = new Path();
        if(_paintMode)
        {

            int activeColor = _paintColor;
            for (PolyPath pp : allPaths)
            {
                setPaintColor(pp.getColor());
                for(int i = 0; i < pp.getPathPoints().size(); i++)
                {
                    PointF point = pp.getPathPoints().get(i);
                    if (i == 0)
                    {
                        path.moveTo(point.x*getWidth(),point.y*getHeight());
                    }
                    else
                        path.lineTo(point.x*getWidth(),point.y*getHeight());
                }

                canvas.drawPath(path, _drawPaint);
                path.reset();
            }

            setPaintColor(activeColor);
            if (_currentPath != null)
            {
                for(int i = 0; i < _currentPath.getPathPoints().size(); i++)
                {
                    PointF point = _currentPath.getPathPoints().get(i);
                    if (i == 0)
                    {
                        path.moveTo(point.x*getWidth(),point.y*getHeight());
                    }
                    else
                        path.lineTo(point.x*getWidth(),point.y*getHeight());
                }
                canvas.drawPath(path, _drawPaint);
            }
        }
        else
        {
            int numberOfPoints;
            if(_resume)
            {
                numberOfPoints = numberOfPoints();
                _paintMode = true;
                _resume = false;
            }
            else
                numberOfPoints =(int) (numberOfPoints() * _percent);

            int pointCount = 0;
            PointF point = null;
            for(PolyPath pp : allPaths)
            {
                if(pointCount == numberOfPoints)
                    break;
                ArrayList<PointF>  temp = pp.getPathPoints();



                for(int index=0; index < temp.size() && pointCount < numberOfPoints; index++, pointCount++ )
                {
                    point = temp.get(index);
                    if(index == 0)
                        path.moveTo(point.x*getWidth(),point.y*getHeight());
                    else
                        path.lineTo(point.x*getWidth(),point.y*getHeight());
                }

                Paint pPaint = new Paint();
                pPaint.setColor(pp.getColor());
                pPaint.setAntiAlias(true);
                pPaint.setStrokeWidth(20);
                pPaint.setStyle(Paint.Style.STROKE);
                pPaint.setStrokeJoin(Paint.Join.ROUND);
                pPaint.setStrokeCap(Paint.Cap.ROUND);
                canvas.drawPath(path,pPaint);
                path.reset();
            }

        }
    }

    public void setPaintColor(int color)
    {
        _paintColor = color;
        _drawPaint.setColor(_paintColor);
    }

    public void clear()
    {
        allPaths.clear();
        invalidate();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(_paintMode)
        {
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    _currentPath = new PolyPath(_paintColor);
                    _currentPath.addPoint(touchX/getWidth(), touchY/getHeight());
                    break;
                case MotionEvent.ACTION_MOVE:
                    _currentPath.addPoint(touchX / getWidth(), touchY / getHeight());
                    break;
                case MotionEvent.ACTION_UP:
                    allPaths.add(_currentPath);
                    _currentPath = null;
                    break;
                default:
                    return false;
            }

            invalidate();
        }
        return true;
    }

    public int getColor()
    {
        return _paintColor;
    }

    public void setPaintMode(boolean mode)
    {
        _paintMode = mode;
        invalidate();
    }
    public int numberOfPoints()
    {
        int count = 0;
        for(PolyPath pp : allPaths)
        {
           count+= pp.getPathPoints().size();
        }
        return count;
    }


    public void setPercent(float percent)
    {
        _percent = percent;
        invalidate();
    }

    public float getPercent()
    {
        return _percent;
    }

    public ArrayList<PolyPath> getAllPaths()
    {
        return allPaths;
    }

    public void setAllPaths(ArrayList<PolyPath> pp)
    {
        allPaths = (ArrayList<PolyPath>)pp.clone();
        _resume = true;
        setPaintMode(false);
    }

}
