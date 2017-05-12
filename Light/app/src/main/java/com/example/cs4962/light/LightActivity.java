package com.example.cs4962.light;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

public class LightActivity extends AppCompatActivity
{
    private Switch _light_switch;
    private LinearLayout _lightlayout;
    private ImageView _lightImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _lightlayout = new LinearLayout(this);
        _lightlayout.setOrientation(LinearLayout.VERTICAL);

        _lightImageView = new ImageView(this);
        _lightImageView.setImageResource(R.drawable.off);

        _lightlayout.addView(_lightImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,9));

        _light_switch = new Switch(this);
        LinearLayout.LayoutParams lightSwitchLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0,1);
        lightSwitchLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        _lightlayout.addView(_light_switch, lightSwitchLayoutParams);

        _light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    _lightImageView.setImageResource(R.drawable.on);
                    _lightlayout.setBackgroundColor(Color.BLACK);
                } else
                {
                    _lightImageView.setImageResource(R.drawable.off);
                    _lightlayout.setBackgroundColor(Color.WHITE);
                }
            }
        });

        setContentView(_lightlayout);
    }


}
