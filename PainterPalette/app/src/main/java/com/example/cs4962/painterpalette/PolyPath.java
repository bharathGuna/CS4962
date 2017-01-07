package com.example.cs4962.painterpalette;

import android.graphics.PointF;
import java.util.ArrayList;


/**
 * Created by Bharath on 10/5/2015.
 */
public class PolyPath
{
        private int _color;
        private int _scaleX;
        private int _scaleY;
       // private Path _path;
        private ArrayList<PointF> _pathPoints;


        public PolyPath(int color)
        {
            _color = color;
            //_path = new Path();
            _pathPoints = new ArrayList<PointF>();
        }

        public void addPoint( float scaleX, float scaleY)
        {
            PointF scaledPoint = new PointF();
            /*if(_path.isEmpty())
            {
                _path.moveTo(x,y);
            }
            else
            {
                _path.lineTo(x,y);
            }
*/
            scaledPoint.x = scaleX;
            scaledPoint.y = scaleY;
            _pathPoints.add(scaledPoint);
        }

   /*     public Path getPath()
        {
            return _path;
        }
*/
        public int getColor()
        {
            return _color;
        }

    public ArrayList<PointF> getPathPoints()
    {
        return _pathPoints;
    }

    public void setPathPoints(ArrayList<PointF> pp)
    {
        _pathPoints = (ArrayList<PointF>)pp.clone();
    }
}
