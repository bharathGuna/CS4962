package com.example.cs4962.paint;

import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PaintActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);
        if(Configuration.ORIENTATION_PORTRAIT == 1)
        layout.setOrientation(LinearLayout.VERTICAL);
        else
            layout.setOrientation(LinearLayout.HORIZONTAL);
        PaintView paintView = new PaintView(this);
        PaintView pv2 = new PaintView(this);
        pv2.setColor(Color.GREEN);
        //paintView.setColor(Color.RED);
        setContentView(layout);
        layout.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        layout.addView(pv2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        if (layout.getChildCount() == 2)
        {
            layout.setBackgroundColor(Color.YELLOW);
        }

        paintView.setOnColorTouchedListener(new PaintView.OnColorTouchedListener()
        {
            @Override
            public void onColorTouch(PaintView pv)
            {
                pv.setActive(true);
                layout.setBackgroundColor(pv.getColor());
            }

        });

        pv2.setOnColorTouchedListener(new PaintView.OnColorTouchedListener()
        {
            @Override
            public void onColorTouch(PaintView pv)
            {
                pv.setActive(true);
                layout.setBackgroundColor(pv.getColor());
            }

        });

    }







}
