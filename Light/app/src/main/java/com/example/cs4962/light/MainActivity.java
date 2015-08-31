package com.example.cs4962.light;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity
{
    private Switch _light_switch;
    private LinearLayout _lightlayout;
    private ImageView _lightImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*super.onCreate(savedInstanceState);
        LinearLayout mainLayout = new LinearLayout(this);
        ImageView lightImageView = new ImageView(this);
        lightImageView.setImageResource(R.drawable.off);
        mainLayout.addView(lightImageView);
        Switch lightSwitch = new Switch(this);
        mainLayout.addView(lightSwitch);
        setContentView(mainLayout);
        */
        super.onCreate(savedInstanceState);

        _lightlayout = new LinearLayout(this);
        _lightlayout.setOrientation(LinearLayout.VERTICAL);
       // _lightlayout.setBackgroundColor(Color.BLACK);


        _lightImageView = new ImageView(this);
        _lightImageView.setImageResource(R.drawable.off);
        //    _lightImageView.setBackgroundColor(Color.RED);
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
                } else
                {
                    _lightImageView.setImageResource(R.drawable.off);
                }
            }
        });


        setContentView(_lightlayout);
    }


}
