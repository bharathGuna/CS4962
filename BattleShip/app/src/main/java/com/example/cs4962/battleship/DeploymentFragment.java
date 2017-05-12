package com.example.cs4962.battleship;

import android.app.Fragment;
import android.app.backup.BackupManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Bharath on 10/23/2015.
 */
public class DeploymentFragment extends Fragment
{

    //Inteface for passing and starting game
    public interface OnDoneButtonClickListener
    {
        void onDoneButtonClick(String name);
    }

    public interface OnPassButtonClickListener
    {
        void onPassButtonClick(String name);
    }


    private OnDoneButtonClickListener onDoneButtonClick = null;
    private OnPassButtonClickListener onPassButtonClick = null;
    private BoardGameView board;
    private Button done;
    private String name;
    private TextView title;
    private int shipsBuilt = 0;
    private static final String NAME = "NAME";
    private static final String SHIPBUILT = "SHIPBUILT";
    private Bundle args;

    public void setOnDoneButtonClick(OnDoneButtonClickListener _onDoneButtonClick)
    {
        onDoneButtonClick = _onDoneButtonClick;
    }

    public void setOnPassButtonClick(OnPassButtonClickListener _onPassButtonClick)
    {
       onPassButtonClick = _onPassButtonClick;
    }

    public static DeploymentFragment newInstance(String name,int shipBuilt)
    {
        DeploymentFragment fragment = new DeploymentFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putInt(SHIPBUILT,shipBuilt);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if(getArguments() != null && getArguments().containsKey(NAME) && getArguments().containsKey(SHIPBUILT))
        {
            name = getArguments().getString(NAME);
            shipsBuilt = getArguments().getInt(SHIPBUILT);
        }

        LinearLayout rootLayout = new LinearLayout(getActivity());
        rootLayout.setBackgroundColor(Color.rgb(00, 69, 94));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        title = new TextView(getActivity());
        title.setBackgroundColor(Color.TRANSPARENT);
           String turn = "";
        GameStatus mode = BattleShipManager.getInstance().getGameMode(name);
            if( mode == GameStatus.DEPOLOYMENT_PLAYER1)
               turn = "Player 1";
           else if(mode == GameStatus.DEPOLOYMENT_PLAYER2)
               turn = "Player 2";
            title.setTextSize(30f);
            title.setText("GAME: " + name.toUpperCase() + "   \t\t  " + turn.toUpperCase());

        rootLayout.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        BoardModel player = null;
        if(mode == GameStatus.DEPOLOYMENT_PLAYER1)
        {
            player = BattleShipManager.getInstance().getPlayer1Board(name);
        }
        else if(mode == GameStatus.DEPOLOYMENT_PLAYER2)
        {
            player = BattleShipManager.getInstance().getPlayer2Board(name);
        }
        if(player == null)
        {
            board = new BoardGameView(getActivity());
        }
        else
        {
            board = convertModelToView(player);
        }
        board.setMode(mode);
        board.setBackgroundColor(Color.TRANSPARENT);
        rootLayout.addView(board, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4));


        board.setOnBuildShipListener(new BoardGameView.OnBuildShipListener()
        {
            @Override
            public void onValidShip(ArrayList<Point> pieces)
            {
                ArrayList<ShipModel> temp = BattleShipManager.getInstance().placeShip(getGameName(), pieces);
                if (temp.size() > 0)
                {
                    Collections.sort(temp);
                    for (ShipModel sm : temp)
                    {
                        board.addShip(sm.getLocations());
                    }
                    shipsBuilt++;
                }
                if (shipsBuilt == 5)
                {
                    done.setEnabled(true);
                }
            }
        });


        done = new Button(getActivity());
        done.setBackgroundColor(Color.TRANSPARENT);
        if(shipsBuilt == 5)
        done.setEnabled(true);
        else
            done.setEnabled(false);
        String text = "";
        if( mode == GameStatus.DEPOLOYMENT_PLAYER1)
            text = "Pass To Player 2";
        else if(mode == GameStatus.DEPOLOYMENT_PLAYER2)
            text = "Pass To Player 1";
        done.setText(text);
        done.setTextSize(30f);
        rootLayout.addView(done, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    if(done.getText() == "Pass To Player 2")
                    {
                        if(onPassButtonClick != null)
                        onPassButtonClick.onPassButtonClick(getGameName());
                    }
                    else
                    {
                        if(onDoneButtonClick != null)
                         onDoneButtonClick.onDoneButtonClick(getGameName());
                    }

            }
        });
        return rootLayout;
    }

    public void setGameName(String _name)
    {
        name = _name;
    }

    public String getGameName()
    {
        return name;
    }

    public BoardGameView convertModelToView(BoardModel model)
    {
        CellModel[][] cellModel = model.getBoard();
        ArrayList<ShipModel> shipsModel =  model.getPlacedShips();
        BoardGameView boardView = new BoardGameView(getActivity());
        CellView view[][] = new CellView[10][10];
        for (int i = 0; i < 100; i++)
        {
            CellView temp = new CellView(getActivity());
            temp.setLocation(i % 10, i / 10);
            temp.setOnCellClickListener(boardView);
            view[i % 10][i / 10] = temp;
            temp.setType(cellModel[i % 10][i / 10].getType());
        }

        boardView.setBoard(view);

        for (ShipModel sm : shipsModel)
        {
            boardView.addShip(sm.getLocations());
        }


        return boardView;

    }
}
