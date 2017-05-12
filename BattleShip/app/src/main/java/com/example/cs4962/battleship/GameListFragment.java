package com.example.cs4962.battleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Bharath on 10/23/2015.
 */
public class GameListFragment extends Fragment implements ListAdapter, View.OnClickListener
{
    LinearLayout rootLayout;
    ListView listView;
    public static GameListFragment newInstance()
    {
        GameListFragment fragment = new GameListFragment();
        return fragment;
    }

    //Activity will implement this and then call Game Manager to open game
    public interface OnGameSelectedListener
    {
        void OnGameSelected(String gameName);
    }

    OnGameSelectedListener onGameSelectedListener = null;
    public void setOnGameSelectedListener(OnGameSelectedListener _onGameSelectedListener)
    {
        onGameSelectedListener = _onGameSelectedListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootLayout = new LinearLayout(getActivity());
        //1 is Vertical and 2 Horizontal
        int orientation = getActivity().getResources().getConfiguration().orientation;
        listView = new ListView(getActivity());
        listView.setBackgroundColor(Color.TRANSPARENT);
        listView.setAdapter(this);
        //rootLayout.addView(listView);

        //rootLayout.addView(listView, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,0, 2));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(onGameSelectedListener != null)
                    onGameSelectedListener.OnGameSelected((String) getItem(position));
                listView.setAdapter(GameListFragment.this);
            }
        });

        Button newGame = new Button(getActivity());
        newGame.setBackgroundColor(Color.RED);
        newGame.setText("New Game");

        newGame.setOnClickListener(this);


            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(listView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
            rootLayout.addView(newGame, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));



        return rootLayout;
    }


    @Override
    public void onClick(View v)
    {
        //TODO: Need to uncomment this code

        final AlertDialog.Builder inputWindow = new AlertDialog.Builder(v.getContext());

        inputWindow.setTitle("Start a new game");
        inputWindow.setMessage("Please enter a game name:");

        final EditText input = new EditText(v.getContext());
        inputWindow.setView(input);
        inputWindow.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Editable value = input.getText();
                String gameName = value.toString();
                if (gameName != null && !gameName.equals("") && !BattleShipManager.getInstance().getGameNames().contains(gameName))
                {
                    BattleShipManager.getInstance().newGame(gameName); //TODO: Need to make this use my model
                    listView.setAdapter(GameListFragment.this);
                }
            }
        });

        inputWindow.show();

    }

    @Override
    public boolean isEmpty()
    {
        return getCount() > 0;
    }


    @Override
    public int getCount()
    {
        return BattleShipManager.getInstance().getGameNames().size();
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
        return BattleShipManager.getInstance().getGameNames().get(position);
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


    //View GameName - Progress - Missile Fired by each Player
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String name = (String)getItem(position);
        int missleFired1, missleFired2 = 0;
        String status =  ""+BattleShipManager.getInstance().getGameMode(name);
        String content = "";
        TextView gameNameView = new TextView(getActivity());
        int padding = (int)(8.0f * getResources().getDisplayMetrics().density);
        gameNameView.setPadding(padding,padding,padding,padding);
        missleFired1 = BattleShipManager.getInstance().getPlayer2Board(name).getMisslesFired();
        missleFired2 = BattleShipManager.getInstance().getPlayer1Board(name).getMisslesFired();
        content = name +"\t"+ "Player 1 : Missiles Fired :" +missleFired1 + " Player 2: Missiles Fired : " + missleFired2 +" Status : " + status;
        gameNameView.setText(content);
        gameNameView.setTextSize(15f);
        gameNameView.setTextColor(Color.BLACK);
        return gameNameView;
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
