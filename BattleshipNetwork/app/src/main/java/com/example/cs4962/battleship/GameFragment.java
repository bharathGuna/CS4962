package com.example.cs4962.battleship;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Bharath on 10/25/2015.
 */
public class GameFragment extends Fragment implements BoardGameView.OnCellClickListener
{
    private String gameName;
    private GameStatus status;
    private LinearLayout rootLayout;
    private BoardGameView currPlayer;
    private BoardGameView oppPlayer;
    private Button passButton;
    boolean turns;
    static final String  NAME = "NAME";
    static final String TURNS = "TURNS";
    private TextView currShips;
    private TextView oppShips;


    public static GameFragment newInstance(boolean turn)
    {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putBoolean(TURNS, turn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootLayout = new LinearLayout(getActivity());

        if(getArguments() != null && getArguments().containsKey(TURNS))
        {
            turns = getArguments().getBoolean(TURNS);
        }
        //1 is Vertical and 2 Horizontal

        int orientation = getActivity().getResources().getConfiguration().orientation;
        LinearLayout currPlayerLayout = new LinearLayout(getActivity());
        currPlayerLayout.setBackgroundColor(Color.TRANSPARENT);
        currPlayerLayout.setOrientation(LinearLayout.VERTICAL);

        currShips = new TextView(getActivity());
        currShips.setText("Player 1 \n Sunk Ships: ");
        currShips.setTextSize(15f);
        currShips.setBackgroundColor(Color.TRANSPARENT);
        currShips.setTextColor(Color.BLACK);
        currPlayer = covertModelToView(BattleShipManager.getInstance().getPlayer1Board(),currShips);
        currPlayer.setBackgroundColor(Color.TRANSPARENT);
        currPlayerLayout.addView(currPlayer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4));
        currPlayer.showShips();

        currPlayerLayout.addView(currShips, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));



        LinearLayout oppPlayerLayout = new LinearLayout(getActivity());
        oppShips = new TextView(getActivity());
        oppShips.setText("Player 2 \n Sunk Ships: ");
        oppShips.setBackgroundColor(Color.TRANSPARENT);
        oppShips.setTextSize(15f);
        oppShips.setTextColor(Color.BLACK);
        oppPlayer = covertModelToView(BattleShipManager.getInstance().getPlayer2Board(), oppShips);
        oppPlayerLayout.setBackgroundColor(Color.TRANSPARENT);
        oppPlayer.setBackgroundColor(Color.TRANSPARENT);
        oppPlayer.hideShips();
        oppPlayerLayout.addView(oppPlayer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4));

        oppPlayerLayout.addView(oppShips, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        oppPlayerLayout.setOrientation(LinearLayout.VERTICAL);

        if(turns)
            oppPlayer.setOnCellClickListener(this);

        if(orientation == 1)
        {
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(currPlayerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,6 ));
            rootLayout.addView(oppPlayerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 6));
        }
        else if(orientation == 2)
        {
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(currPlayerLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));
            rootLayout.addView(oppPlayerLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));

        }

        return rootLayout;
    }


    private BoardGameView covertModelToView(BoardModel model, TextView sunk)
    {
        CellModel[][] cellModel = model.getGrid();

        CellView[][] convert = new CellView[cellModel.length][cellModel[0].length];
        CellView temp;
        BoardGameView view = new BoardGameView(getActivity());
        for(int i = 0; i < cellModel.length; i++)
        {
            for(int j = 0; j < cellModel[i].length; j++)
            {
                temp = new CellView(getActivity());
                temp.setLocation(i,j);
                temp.setType(cellModel[i][j].getType());
                convert[i][j] = temp;
            }
        }
        view.setBoard(convert);

        return view;
    }

    public void updateText(TextView update, ShipType model)
    {
        String text = update.getText().toString();
        update.setText(text + model + " ");
    }

    @Override
    public void onCellClick(Point piece)
    {
        //need to make a guess
        makeGuess(piece);

    }

    private void makeGuess(final Point piece)
    {
        AsyncTask<Void,Void,BattleshipNetwork.Guess> guessTask = new AsyncTask<Void, Void, BattleshipNetwork.Guess>()
        {
            @Override
            protected BattleshipNetwork.Guess doInBackground(Void... params)
            {
                BattleshipNetwork.MyGame current = BattleShipManager.getInstance().getCurrentGame();
                return BattleshipNetwork.makeGuess(current.gameId,current.playerId,piece);
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.Guess guess)
            {
                super.onPostExecute(guess);
                oppPlayer.setHit(piece,guess.hit);
                if(guess.shipSunk != 0)
                {
                    ShipModel sm = new ShipModel();
                    updateText(oppShips,sm.getShipForLength(guess.shipSunk));

                }
            }
        };

        guessTask.execute();
        oppPlayer.setOnCellClickListener(null);

    }

}

