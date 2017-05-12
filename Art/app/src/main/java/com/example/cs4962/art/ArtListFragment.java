package com.example.cs4962.art;

import android.app.Activity;
import android.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Bharath on 10/21/2015.
 */
public class ArtListFragment extends Fragment implements ListAdapter
{
    public static int[] ART = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four};

    public static ArtListFragment newInstance()
    {
        ArtListFragment fragment = new ArtListFragment();
        fragment.setArguments(new Bundle());
        return  fragment;
    }

    public interface OnArtSelectedListener
    {
        void OnArtSelected(int artResourceId);
    }

    OnArtSelectedListener _onArtSelectedListener = null;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            _onArtSelectedListener = (OnArtSelectedListener)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() +" need to support this lisener");
        }
    }

    @Override
    public void onDetach()
    {
        _onArtSelectedListener = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        ListView rootView = new ListView(getActivity());
        rootView.setBackgroundColor(Color.GREEN);
        rootView.setAdapter(this);

        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                _onArtSelectedListener.OnArtSelected((int)getItem(position));
            }
        });
        return rootView;
    }


    @Override
    public boolean isEmpty()
    {
        return getCount() > 0;
    }


    @Override
    public int getCount()
    {
        return ART.length;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public Object getItem(int position)
    {
        return ART[position];
    }


    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int artResourceId = (int)getItem(position);
        TextView artTitleView = new TextView(getActivity());
        int padding = (int)(8.0f * getResources().getDisplayMetrics().density);
        artTitleView.setPadding(padding,padding,padding,padding);
        artTitleView.setText(artResourceId);
        return artTitleView;
    }



    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
    }


    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }


    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }


}
