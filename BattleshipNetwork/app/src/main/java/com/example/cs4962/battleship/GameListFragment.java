package com.example.cs4962.battleship;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Gravity;
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
    float textSize;
    Handler handler = new Handler();

    public static GameListFragment newInstance()
    {
        GameListFragment fragment = new GameListFragment();
        return fragment;
    }

    //Activity will implement this and then call Game Manager to open game
    public interface OnGameSelectedListener
    {
        void OnGameSelected(BattleshipNetwork.GameList gameName);
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
        loadList();
        //TODO: need to make this based of something else
        textSize = getResources().getDisplayMetrics().density*10f;
        //rootLayout.addView(listView);

        //rootLayout.addView(listView, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,0, 2));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (onGameSelectedListener != null)
                    onGameSelectedListener.OnGameSelected((BattleshipNetwork.GameList) getItem(position));
                listView.setAdapter(GameListFragment.this);
                handler.removeCallbacks(updateList);
            }
        });

        Button newGame = new Button(getActivity());
        newGame.setBackgroundColor(Color.RED);
        newGame.setText("New Game");
        newGame.setTextSize(textSize);
        newGame.setGravity(Gravity.CENTER);

        newGame.setOnClickListener(this);

        Button refreshList = new Button(getActivity());
        refreshList.setBackgroundColor(Color.RED);
        refreshList.setText("Refresh List");
        refreshList.setTextSize(textSize);
        refreshList.setGravity(Gravity.CENTER);

        refreshList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadList();
            }
        });

        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(listView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4));
        rootLayout.addView(newGame, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        rootLayout.addView(refreshList,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
      //  refreshList();
        return rootLayout;
    }

    private Runnable updateList = new Runnable()
    {
        @Override
        public void run()
        {

            loadList();
            refreshList();

        }
    };

    private void refreshList()
    {
        handler.postDelayed(updateList,500);
    }

    public void loadList()
    {

        AsyncTask<Void, Void, BattleshipNetwork.GameList[]> loadListTask = new AsyncTask<Void, Void, BattleshipNetwork.GameList[]>()
        {
            @Override
            protected BattleshipNetwork.GameList[] doInBackground(Void... params)
            {
                return BattleshipNetwork.getGameList();
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.GameList[] gameLists)
            {
                super.onPostExecute(gameLists);
                int index = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();

// notify dataset changed or re-assign adapter here
                BattleShipManager.getInstance().setGames(gameLists);
                listView.setAdapter(GameListFragment.this);
// restore the position of listview
                listView.setSelectionFromTop(index, top);



            }
        };

        loadListTask.execute();
    }

    private void newGame(final String playerName, final String gameName)
    {
        AsyncTask<Void,Void,BattleshipNetwork.MyGame> newGameTask = new AsyncTask<Void, Void, BattleshipNetwork.MyGame>()
        {
            @Override
            protected BattleshipNetwork.MyGame doInBackground(Void... params)
            {

                return BattleshipNetwork.createGame(playerName,gameName);
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.MyGame myGame)
            {
                super.onPostExecute(myGame);
                BattleShipManager.getInstance().newGame(myGame,playerName,gameName);
            }
        };
        newGameTask.execute();
    }

    @Override
    public void onClick(View v)
    {
        //TODO: Need to uncomment this code

        final AlertDialog.Builder inputWindow = new AlertDialog.Builder(v.getContext());

        inputWindow.setTitle("Start a new game");
        inputWindow.setMessage("Please enter information");

        final EditText playerName = new EditText(v.getContext());
        playerName.setHint("Player Name");
        final EditText gameName = new EditText(v.getContext());
        gameName.setHint("Game Name");
        LinearLayout multiInput = new LinearLayout(v.getContext());
        multiInput.setOrientation(LinearLayout.VERTICAL);
        multiInput.addView(playerName);
        multiInput.addView(gameName);
        inputWindow.setView(multiInput);

        inputWindow.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Editable pvalue = playerName.getText();
                Editable gvalue = gameName.getText();
                String pName = pvalue.toString();
                String gameName = gvalue.toString();

                if (gameName != null && !gameName.equals("")  && pName != null && !pName.equals("") )
                {
                    //BattleShipManager.getInstance().newGame(gameName); //TODO: Need to make this use my model
                    newGame(pName,gameName);
                    listView.setAdapter(GameListFragment.this);
                    handler.removeCallbacks(updateList);
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
        return BattleShipManager.getInstance().getGames().length;
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
        return BattleShipManager.getInstance().getGames()[position];
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
        BattleshipNetwork.GameList game = (BattleshipNetwork.GameList) getItem(position);
        TextView gameNameView = new TextView(getActivity());
        String content = "Game Name: " + game.name + "\n Game Status: " + game.status;
        gameNameView.setText(content);
        gameNameView.setTextSize(textSize);
        gameNameView.setTextColor(Color.BLACK);
        gameNameView.setGravity(Gravity.CENTER);
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
