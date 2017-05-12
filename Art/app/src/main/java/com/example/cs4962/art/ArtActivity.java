package com.example.cs4962.art;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ArtActivity extends Activity implements ArtListFragment.OnArtSelectedListener
{

    public static final String ART_LIST_FRAGMENT_TAG = "ART_LIST_FRAGMENT_TAG";
    public static final String ART_FRAGMENT_TAG = "ART_FRAGMENT_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(mainLayout);

        FrameLayout masterFrameLayout = new FrameLayout(this);
        masterFrameLayout.setId(10);
        mainLayout.addView(masterFrameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
        FrameLayout detailFrameLayout = new FrameLayout(this);
        detailFrameLayout.setId(11);
        mainLayout.addView(detailFrameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,2));

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ArtListFragment artListFragment = (ArtListFragment)getFragmentManager().findFragmentByTag(ART_LIST_FRAGMENT_TAG);

        if(artListFragment == null)
        {
            artListFragment = ArtListFragment.newInstance();
            transaction.add(masterFrameLayout.getId(), artListFragment ,ART_LIST_FRAGMENT_TAG); //TODO: use newInstance

        }
        ArtFragment artFragment = (ArtFragment)getFragmentManager().findFragmentByTag(ART_FRAGMENT_TAG);
        if(artFragment == null)
        {
            artFragment = ArtFragment.newInstance(R.drawable.one);
            transaction.add(detailFrameLayout.getId(), artFragment,ART_FRAGMENT_TAG);

        }


        transaction.commit();
    }


    @Override
    public void OnArtSelected(int artResourceId)
    {
        ArtFragment artFragment = (ArtFragment)getFragmentManager().findFragmentByTag(ART_FRAGMENT_TAG);
        artFragment.setArtResource(artResourceId);
    }
}
