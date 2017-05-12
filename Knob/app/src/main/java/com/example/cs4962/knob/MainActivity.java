package com.example.cs4962.knob;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(rootLayout);
        final Knob knobView = new Knob(this);
        final Knob kv = new Knob(this);
        knobView.setPadding(20, 20, 20, 20);
        knobView.setBackgroundColor(Color.RED);
        rootLayout.addView(knobView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        rootLayout.addView(kv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
       // rootLayout.addView(knobView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        final TextView knobValueLabel = new TextView(this);
        knobValueLabel.setBackgroundColor(Color.YELLOW);
        knobValueLabel.setText("" + knobView.getValue());
      //  rootLayout.addView(knobValueLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));

        knobView.setOnValueChangedListener(new Knob.OnValueChangedListener()
        {
            public void onValueChanged(double value)
            {
                Log.i("Knob", "knob changed to value " + value);
                knobValueLabel.setText("" + knobView.getValue());

            }
        });

        final ValueAnimator knobAnimator = new ValueAnimator();
        knobAnimator.setFloatValues(0.0f, 1.0f);
        knobAnimator.setDuration(10000);
        knobAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (Float) knobAnimator.getAnimatedValue();
                knobView.setValue(value);
            }
        });
       // knobAnimator.start();

    }

}
