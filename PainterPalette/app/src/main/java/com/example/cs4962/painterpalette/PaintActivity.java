package com.example.cs4962.painterpalette;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Bharath on 9/20/2015.
 * Change palette button to a paint view
 */
public class PaintActivity extends AppCompatActivity implements ValueAnimator.AnimatorUpdateListener
{
    LinearLayout layout;
    LinearLayout menuLayout;
    Button _goToPalette;  // true
    Button _clear;
    //PaintView _goToPalette;
    Button _switchMode; // false
    Button _play;
    SeekBar _timeBar;
    boolean _menuMode = true;
    PaintArea _paintArea;
    ValueAnimator animator;
    String file;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final PaletteView colorLayout = new PaletteView(this);
        animator = new ValueAnimator();
        animator.addUpdateListener(this);
        _paintArea = new PaintArea(this);
        _paintArea.setBackgroundColor(Color.WHITE);
        _paintArea.setPaintColor(Color.RED);


        layout.addView(_paintArea, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 9));
        menuLayout = new LinearLayout(this);
        menuLayout.setBackgroundColor(Color.DKGRAY);
        createMenuButtons();
        addClickListeners();
        _goToPalette.setBackgroundColor(Color.RED);
        layout.addView(menuLayout);
        setContentView(layout);
        buildAlertDialog(this);
        file = "test3.txt";


    }


    @Override
    protected void onPause()
    {
        super.onPause();
        ArrayList<PolyPath> polyPaths = _paintArea.getAllPaths();
        Gson gson = new Gson();
        String jsonPolyPaths = gson.toJson(polyPaths);
        try
        {
            File drawingFile = new File(getFilesDir(), file);
            FileWriter fileWriter = new FileWriter(drawingFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(jsonPolyPaths);
            writer.close();
        } catch (Exception e)
        {
            Log.e("Persistence", "Error saving drawing file" + e.getMessage());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            File drawingFile = new File(getFilesDir(), file);
            FileReader fileReader = new FileReader(drawingFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String polyLineJason = reader.readLine();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<PolyPath>>(){}.getType();
            ArrayList<PolyPath> polyPaths = gson.fromJson(polyLineJason, collectionType);
            _paintArea.setAllPaths(polyPaths);
            reader.close();
        } catch (Exception e)
        {
            Log.e("Persistence", "Error opening drawing file" + e.getMessage());

        }
    }

    private void buildAlertDialog(Context c)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(c);
        builder1.setTitle("How to Paint");
        builder1.setMessage("Start moving your finger!!!" +
                "You can view the palette by clicking the back button. " +
                "Click it again and comeback to the canvas");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        _paintArea.setPaintColor(data.getIntExtra(PaletteActivity.ACTIVE_COLOR, -1));
        _goToPalette.setBackgroundColor(_paintArea.getColor());

        if (_paintArea.getColor() == Color.WHITE)
            _goToPalette.setTextColor(Color.BLACK);
        else
            _goToPalette.setTextColor(Color.WHITE);

    }

    public void createMenuButtons()
    {
        _goToPalette = new Button(this);
        _goToPalette.setText("Color Palette");
        _goToPalette.setTextColor(Color.WHITE);

        _clear = new Button(this);
        _clear.setText("Clear");
        _clear.setTextColor(Color.BLACK);

        _switchMode = new Button(this);
        _switchMode.setText("Watch Mode");
        _switchMode.setTextColor(Color.WHITE);

        _play = new Button(this);
        _play.setText("Play");
        _play.setTextColor(Color.WHITE);

        _timeBar = new SeekBar(this);
        // _timeBar.setMax(100);


        //_playing = false;

        _switchMode.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _goToPalette.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _play.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _clear.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        _timeBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        menuLayout.addView(_goToPalette, 0);
        menuLayout.addView(_switchMode, 1);
        menuLayout.addView(_play, 2);
        menuLayout.addView(_timeBar, 3);
        menuLayout.addView(_clear,4);
        changeMenu();
    }


    private void changeMenu()
    {
        //true means only toPalette and WatchMode displayed
        if (_menuMode)
        {
            menuLayout.getChildAt(0).setVisibility(View.VISIBLE);
            menuLayout.getChildAt(1).setVisibility(View.VISIBLE);
            menuLayout.getChildAt(2).setVisibility(View.GONE);
            menuLayout.getChildAt(3).setVisibility(View.GONE);
            menuLayout.getChildAt(4).setVisibility(View.VISIBLE);
            _switchMode.setText("Watch Mode");

        }
        else
        {
            menuLayout.getChildAt(0).setVisibility(View.GONE);
            menuLayout.getChildAt(1).setVisibility(View.VISIBLE);
            _switchMode.setText("Paint Mode");
            menuLayout.getChildAt(2).setVisibility(View.VISIBLE);
            menuLayout.getChildAt(3).setVisibility(View.VISIBLE);
            menuLayout.getChildAt(4).setVisibility(View.GONE);
            _timeBar.setProgress((int) (_paintArea.getPercent() * 100f));
        }
        _paintArea.setPaintMode(_menuMode);
        _menuMode = !_menuMode;
    }

    private void addClickListeners()
    {
        _goToPalette.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent activeColorDetail = new Intent();
                activeColorDetail.putExtra(PaletteActivity.ACTIVE_COLOR, _paintArea.getColor());
                activeColorDetail.setClass(PaintActivity.this, PaletteActivity.class);
                startActivityForResult(activeColorDetail, PaletteActivity.ACTIVE_COLOR_REQUEST_CODE);
            }
        });

        _switchMode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeMenu();
                _paintArea.setPaintMode(!_menuMode);
                _paintArea.setPercent(0.0f);

            }
        });

        _clear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _paintArea.clear();
            }
        });
        _play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!_paintArea.getAllPaths().isEmpty())
                {

                    if (_play.getText() == "Play")
                    {
                        if ((int) (_paintArea.getPercent() * 100f) == 100)
                        {
                            _paintArea.setPercent(0.0f);
                        }
                        animator.setIntValues((int) (_paintArea.getPercent() * 100f), 100);
                        animator.setDuration((long) (10000 - _paintArea.getPercent() * 10000f));
                        animator.start();
                        _play.setText("Pause");
                    }
                    else if (_play.getText() == "Pause")
                    {
                        animator.cancel();
                        _play.setText("Play");
                    }
                }
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setTitle("Nothing to Animate");
                    builder1.setMessage("Please draw something then click on play");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                    _switchMode.performClick();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        _timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                float percent = (float) progress / 100f;
                _paintArea.setPercent(percent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation)
    {
        int value = (Integer) animation.getAnimatedValue();
        _timeBar.setProgress(value);
        if (value == 100)
        {
            _play.setText("Play");
        }
    }
}