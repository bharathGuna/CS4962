package com.example.cs4962.art;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Bharath on 10/21/2015.
 */
public class ArtFragment extends Fragment
{
    public static final String ART_RESOURCE_KEY = "ART_RESOURCE_KEY";

    public static ArtFragment newInstance(int artResourceId)
    {
        ArtFragment fragment = new ArtFragment();
        Bundle args = new Bundle();
        args.putInt(ART_RESOURCE_KEY,artResourceId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int artDrawable = R.drawable.one;

        if(getArguments() != null && getArguments().containsKey(ART_RESOURCE_KEY))
                artDrawable = getArguments().getInt(ART_RESOURCE_KEY);

        ImageView rootView = new ImageView(getActivity());
        rootView.setBackgroundColor(Color.YELLOW);
        rootView.setImageResource(artDrawable);
        return rootView;
    }

    public void setArtResource(int artResource)
    {
        ImageView view = (ImageView)getView();
        view.setImageResource(artResource);
    }

    public int getArtResource()
    {
        return 0; //TODO;
    }
}
