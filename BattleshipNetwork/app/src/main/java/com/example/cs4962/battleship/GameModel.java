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

    public GameModel(BattleshipNetwork.Boards board)
    {
        player1 = new BoardModel(board.playerBoard);
        player2 = new BoardModel(board.opponentBoard);
        mode = GameStatus.PLAYING;
    }


    public GameStatus getMode()
    {
        return mode;
    }

    public BoardModel getPlayer1()
    {
        return player1;
    }


    public void setPlayer1(BattleshipNetwork.PlayerBoard[] _player1)
    {

        player1.setGrid(_player1);
    }

    public BoardModel getPlayer2()
    {
        return player2;
    }


    public void setPlayer2(BattleshipNetwork.PlayerBoard[] _player2)
    {

        player2.setGrid(_player2);
    }

    public void setMode(GameStatus _mode)
    {
        mode = _mode;
    }
}
