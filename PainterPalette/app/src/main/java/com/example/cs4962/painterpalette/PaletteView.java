package com.example.cs4962.painterpalette;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bharath on 9/20/2015.
 */
public class PaletteView extends ViewGroup
{

    public interface OnColorChangedListener
    {
        void onColorChanged(PaletteView v);
    }

    public void setOnColorChangedListener( OnColorChangedListener onColorTouchedListener)
    {
        _onColorTouchedListener = onColorTouchedListener;
    }

    OnColorChangedListener _onColorTouchedListener = null;
    private boolean isDragging = false;
    private int maxColors = 10;
    public PaletteView(Context context)
    {
        super(context);
        setupPalette();
        setMinimumHeight(300);
        setMinimumWidth(300);
    }

    private void setupPalette()
    {
        addColor(Color.TRANSPARENT,true);
        addColor(Color.WHITE,false);
        addColor(Color.RED,false);
        addColor(Color.BLUE,false);
        addColor(Color.GREEN,false);
    }
    // Only allow PaintViews to be added to the PaletteView.
    @Override
    public void addView(View child)
    {
        if(child instanceof PaintView)
        {
            PaintView pv = (PaintView) child;
            super.addView(child);
            setActiveColor(pv.getColor());
        }


    }

    // Retrieve active color. If there is not one, return black.
    public int getActiveColor()
    {
        for(int viewIndex = 0; viewIndex < getChildCount(); viewIndex++)
        {
            PaintView paintView = (PaintView)getChildAt(viewIndex);

            if(paintView.isActive())
            {
                return paintView.getColor();
            }
        }

        return Color.BLACK;
    }

    // Set active color.
    public void setActiveColor(int c)
    {
        for(int viewIndex = 0; viewIndex < getChildCount(); viewIndex++)
        {
            PaintView paintView = (PaintView)getChildAt(viewIndex);

            if(paintView.getColor() == c)
                paintView.setActive(true);
            else
                paintView.setActive(false);
        }

        if(_onColorTouchedListener != null)
            _onColorTouchedListener.onColorChanged(this);
    }

    public int[] getColors()
    {
        int[] colors = new int[getChildCount()];

        for(int viewIndex = 0; viewIndex < getChildCount(); viewIndex++)
        {
            PaintView paintView = (PaintView)getChildAt(viewIndex);
            colors[viewIndex] = paintView.getColor();
        }

        return colors;
    }

    // Create a new paint splotch with that color.
    public void addColor(int color, boolean isDelete)
    {
        if(getChildCount() == maxColors)
        {
            buildAlertDialog();
            return;
        }
        for(int c : getColors())
        {
            if (c == color)
                return;
        }
        PaintView paintView;
        if(!isDelete)
        {
            paintView = new PaintView(getContext());
            paintView.setColor(color);
        }
        else
        {
            paintView = new PaintView(getContext(),true);
        }

        paintView.setOnColorTouchedListener(new PaintView.OnColorTouchedListener()
        {
            @Override
            public void onColorTouch(PaintView v)
            {
                v.setActive(true);
                setActiveColor(v.getColor());
                View.DragShadowBuilder splotchShadow = new View.DragShadowBuilder(v);
                v.startDrag(null, splotchShadow, v, 0);
            }
        });

        paintView.setOnDragListener(new View.OnDragListener()
        {

            @Override
            public boolean onDrag(View v, DragEvent event)
            {
                PaintView endView = (PaintView) v;
                switch (event.getAction())
                {
                    case DragEvent.ACTION_DRAG_STARTED:
                        isDragging = true;
                        return true;
                    case DragEvent.ACTION_DROP:
                        if (isDragging)
                        {
                            PaintView draggedView = (PaintView) event.getLocalState();
                            if (draggedView.equals(endView))
                                return true;
                            for (int i = 0; i < getChildCount(); i++)
                            {
                                PaintView view = (PaintView) getChildAt(i);
                                if (!draggedView.equals(view) && endView.equals(view) && !endView.getDelete() && !draggedView.getDelete())
                                {
                                    int newColorNum = mixColors((draggedView).getColor(), (view).getColor());
                                    addColor(newColorNum, false);
                                    break;
                                }
                                else
                                {

                                    if (endView.getDelete())
                                    {
                                        removeColor(draggedView.getColor());
                                        break;
                                    }
                                    else if (draggedView.getDelete())
                                    {
                                        removeColor(endView.getColor());
                                        break;
                                    }
                                }
                            }
                        }
                        isDragging = false;
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });


        addView(paintView);
        invalidate();
    }

    private int mixColors(int color1, int color2)
    {
        int color1R = (color1 & 0x00FF0000);
        int color1G = (color1 & 0x0000FF00);
        int color1B = (color1 & 0x000000FF);

        int color2R = (color2 & 0x00FF0000);
        int color2G = (color2 & 0x0000FF00);
        int color2B = (color2 & 0x000000FF);

        color1R = (color1R + color2R) / 2;
        color1G = (color1G + color2G) / 2;
        color1B = (color1B + color2B) / 2;

        return 0xFF000000 + color1R + color1G + color1B;
    }

    // Removes color from palette.
    public void removeColor(int color)
    {

        if(getChildCount() == 2)
        {
            removeAllViews();
            setupPalette();

        }
        for(int paintViewIndex = getChildCount() - 1; paintViewIndex >= 0; paintViewIndex--)
        {
            PaintView paintView = (PaintView)getChildAt(paintViewIndex);

            if(paintView.getColor() == color)
            {
                removeView(paintView);
            }
            if(paintView.isActive())
            {
                setActiveColor(getColors()[getColors().length-1]);
            }
        }

        if(_onColorTouchedListener != null)
        {
            _onColorTouchedListener.onColorChanged(this);
        }

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec,getSuggestedMinimumHeight());

        int childState = 0;
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
            child.measure(MeasureSpec.AT_MOST | (width/getChildCount()),MeasureSpec.AT_MOST|(height/getChildCount()));
            childState = combineMeasuredStates(childState,child.getMeasuredState());
        }
        setMeasuredDimension(
                resolveSizeAndState(width,widthMeasureSpec,childState),
                resolveSizeAndState(height,heightMeasureSpec,childState)
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childWidthMax = 0;
        int childHeightMax = 0;
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {

            PaintView child = (PaintView)getChildAt(childIndex);
            childWidthMax = Math.max(childWidthMax,child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax,child.getMeasuredHeight());
        }


        Rect layoutRect = new Rect();
        layoutRect.left = getPaddingLeft() + childWidthMax;
        layoutRect.top = getPaddingTop() + childHeightMax;
        layoutRect.right = getWidth() - getPaddingRight() - childWidthMax;
        layoutRect.bottom = getHeight() - getPaddingBottom() - childHeightMax;

        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {

            PaintView child = (PaintView)getChildAt(childIndex);
            Point centerPoint = new Point();
            double angle = ((double)childIndex/(double)getChildCount()) * Math.PI * 2.0f;

            centerPoint.x = (int) (layoutRect.centerX() + (layoutRect.width() / 2) * Math.cos(angle));
            centerPoint.y = (int) (layoutRect.centerY() + (layoutRect.height() / 2) * Math.sin(angle));


            Rect childR = new Rect();
            childR.left = centerPoint.x-childWidthMax;
            childR.top = centerPoint.y -childHeightMax;
            childR.right = centerPoint.x + childWidthMax;
            childR.bottom = centerPoint.y +childHeightMax;

            child.layout(childR.left,childR.top,childR.right,childR.bottom);
        }
    }

    private void buildAlertDialog()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.getContext());
        builder1.setMessage("Max Color Reached. You can only have 10. Please remove color to add another");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
