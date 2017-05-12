package com.example.cs4962.battleship;

import android.app.Fragment;
import android.graphics.Color;
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
    int turns = 0;
    static final String  NAME = "NAME";
    static final String TURNS = "TURNS";
    private TextView currShips;
    private TextView oppShips;
    public static GameFragment newInstance(String name, int turn)
    {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putInt(TURNS,turn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootLayout = new LinearLayout(getActivity());

        if(getArguments() != null && getArguments().containsKey(NAME) && getArguments().containsKey(TURNS))
        {
            gameName = getArguments().getString(NAME);
            turns = getArguments().getInt(TURNS);
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
        currPlayer = covertModelToView(BattleShipManager.getInstance().getPlayer1Board(gameName),currShips);
        currPlayer.setBackgroundColor(Color.TRANSPARENT);
        currPlayerLayout.addView(currPlayer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4));
        //currPlayer.showShips();

        currPlayerLayout.addView(currShips, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));



        LinearLayout oppPlayerLayout = new LinearLayout(getActivity());
        oppShips = new TextView(getActivity());
        oppShips.setText("Player 2 \n Sunk Ships: ");
        oppShips.setBackgroundColor(Color.TRANSPARENT);
        oppShips.setTextSize(15f);
        oppShips.setTextColor(Color.BLACK);
        oppPlayer = covertModelToView(BattleShipManager.getInstance().getPlayer2Board(gameName), oppShips);
        oppPlayerLayout.setBackgroundColor(Color.TRANSPARENT);
        oppPlayer.setBackgroundColor(Color.TRANSPARENT);
        //oppPlayer.hideShips();
        oppPlayerLayout.addView(oppPlayer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4));

        oppPlayerLayout.addView(oppShips, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        oppPlayerLayout.setOrientation(LinearLayout.VERTICAL);

        setStatus(BattleShipManager.getInstance().getGameMode(gameName));
        passButton = new Button(getActivity());
        passButton.setText("Pass to Next Player");
        passButton.setEnabled(false);
        passButton.setOnClickListener(onClickListener());

        if(status == GameStatus.PLAYER1_TURN)
        {
            currPlayer.showShips();
            oppPlayer.hideShips();
            oppPlayer.setOnCellClickListener(this);
        }
        else if(status == GameStatus.PLAYER2_TURN)
        {
            currPlayer.hideShips();
            oppPlayer.showShips();
            currPlayer.setOnCellClickListener(this);
        }
        else if(status == GameStatus.PLAYER1_WON || status == GameStatus.PLAYER2_WON)
        {
            currPlayer.showShips();
            oppPlayer.showShips();
            passButton.setText("Go Back To Main Screen");
            passButton.setEnabled(true);
        }

        if(orientation == 1)
        {
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(currPlayerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,6 ));
            rootLayout.addView(passButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            rootLayout.addView(oppPlayerLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 6));
        }
        else if(orientation == 2)
        {
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(currPlayerLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));
            rootLayout.addView(passButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 3));
            rootLayout.addView(oppPlayerLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));
            //passButton.setTextAlignment();
        }

        return rootLayout;
    }


    private BoardGameView covertModelToView(BoardModel model, TextView sunk)
    {
        CellModel[][] cellModel = model.getBoard();
        ArrayList<ShipModel> ships = model.getPlacedShips();
        ArrayList<ShipModel> sunkShips = model.getSunkShip();

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
        for(ShipModel ship : ships)
            view.addShip(ship.getLocations());

        view.setBoard(convert);

        for(ShipModel ship : sunkShips)
            updateText(sunk,ship);

        return view;
    }
    public void setGameName(String _gameName)
    {
        gameName = _gameName;
    }

    public void setStatus(GameStatus _status)
    {
        status = _status;
        currPlayer.setMode(_status);
        oppPlayer.setMode(_status);
    }

    public void updateText(GameStatus status,ShipModel model)
    {
        String player1 = currShips.getText().toString();
        String player2 = oppShips.getText().toString();
        if(status == GameStatus.PLAYER1_TURN)
            oppShips.setText(player2 + model.getType().toString() + " ");
        else if(status == GameStatus.PLAYER2_TURN)
            currShips.setText(player1 + model.getType().toString() + " ");
    }

    public void updateText(TextView update, ShipModel model)
    {
        String text = update.getText().toString();
        update.setText(text + model.getType() + " ");
    }
    @Override
    public void onCellClick(Point piece)
    {

        if(BattleShipManager.getInstance().isHit(gameName,status,piece))
        {
            oppPlayer.setHit(piece, true);
            ShipModel sunk = BattleShipManager.getInstance().isSunk(gameName,status,piece);
            if(sunk != null)
                updateText(status,sunk);

        }
        else
            oppPlayer.setHit(piece,false);

        passButton.setEnabled(true);
        currPlayer.hideShips();
        oppPlayer.setOnCellClickListener(null);

        turns++;
        if(turns >= 17*2)
        {
            if(BattleShipManager.getInstance().isGameOver(gameName, status))
            {

                status = BattleShipManager.getInstance().getGameMode(gameName);
            }
        }


        if(status == GameStatus.PLAYER1_TURN)
            BattleShipManager.getInstance().setGameMode(gameName,GameStatus.PLAYER2_TURN);
        else if(status == GameStatus.PLAYER2_TURN)
            BattleShipManager.getInstance().setGameMode(gameName,GameStatus.PLAYER1_TURN);
        status = BattleShipManager.getInstance().getGameMode(gameName);



    }

    public Button.OnClickListener onClickListener()
    {
        Button.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!passButton.getText().toString().equals("Go Back To Main Screen"))
                {
                    //Swapping views
                    BoardGameView current = currPlayer;
                    currPlayer = oppPlayer;
                    oppPlayer = current;


                    currPlayer.showShips();
                    currPlayer.setOnCellClickListener(null);
                    oppPlayer.hideShips();
                    oppPlayer.setOnCellClickListener(GameFragment.this);
                    passButton.setEnabled(false);
                }
                else
                {
                    BattleShipManager.getInstance().gameOver(gameName,status);
                }
            }
        };

        return listener;
    }
}
