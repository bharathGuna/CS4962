package com.example.cs4962.battleship;

import java.util.ArrayList;

/**
 * Created by Bharath on 10/20/2015.
 */
public class GameModel
{
    private BoardModel player1;
    private BoardModel player2;
    private GameStatus mode;

    public GameModel()
    {
        player1 = new BoardModel();
        player2 = new BoardModel();
        mode = GameStatus.DEPOLOYMENT_PLAYER1;
    }


    //working on the deployment part
    public ArrayList<ShipModel> placeShip(ArrayList<Point> points)
    {
        if(mode == GameStatus.DEPOLOYMENT_PLAYER1)
        {
            if(player1.placeShip(points))
            {
                return player1.getPlacedShips();
            }
        }
        else if(mode == GameStatus.DEPOLOYMENT_PLAYER2)
        {
            if(player2.placeShip(points))
            {
                return player2.getPlacedShips();
            }
        }
        return new ArrayList<ShipModel>();
    }


    public GameStatus getMode()
    {
        return mode;
    }

    public BoardModel getPlayer1()
    {
        return player1;
    }


    public void setPlayer1(BoardModel player1)
    {
        this.player1 = player1;
    }

    public BoardModel getPlayer2()
    {
        return player2;
    }


    public void setPlayer2(BoardModel _player2)
    {
        player2 = _player2;
    }

    public void setMode(GameStatus _mode)
    {
        mode = _mode;
    }
}
