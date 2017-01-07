package com.example.cs4962.painterpalette;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Bharath on 10/4/2015.
 */
public class PaletteActivity extends Activity
{
    public static String ACTIVE_COLOR = "active_color";
    public static int ACTIVE_COLOR_REQUEST_CODE = 13;
    private PaletteView palette;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout palLayout = new LinearLayout(this);
        palLayout.setOrientation(LinearLayout.VERTICAL);
        palLayout.setBackgroundResource(R.drawable.palettebackground);
        Button returnButton = new Button(this);
        returnButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        returnButton.setText("Return to Painting");
       // returnButton.setBackgroundColor(Color.DKGRAY);

        returnButton.setTextColor(Color.WHITE);

         palette = new PaletteView(this);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent colorIntent = new Intent();
                colorIntent.putExtra(ACTIVE_COLOR, palette.getActiveColor());
                setResult(ACTIVE_COLOR_REQUEST_CODE, colorIntent);
                finish();
            }
        });

        Intent receiveActiveColor = getIntent();
        int aColor = receiveActiveColor.getIntExtra(ACTIVE_COLOR, -1);

        if(aColor == -1)
            Log.i("Receiving color", "Oops, -1.");
        else {
        //    palette.addColor(aColor,false);
            palette.setActiveColor(aColor);
        }

        palLayout.addView(palette,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        palLayout.addView(returnButton);

        setContentView(palLayout);
    }
}

