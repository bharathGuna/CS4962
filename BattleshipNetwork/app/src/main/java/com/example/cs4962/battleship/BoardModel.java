package com.example.cs4962.battleship;


import java.util.ArrayList;

/**
 * Created by Bharath on 10/22/2015.
 * Need to make sure all ships are placed correctly
 * Need to be able to return cells with hits
 */
public class BoardModel
{
    private CellModel[][] board;
    private ArrayList<String> sunkShips;


    public BoardModel(BattleshipNetwork.PlayerBoard[] playerBoard)
    {
        board =  new CellModel[10][10];
        sunkShips = new ArrayList<>();
        setGrid(playerBoard);

    }


    public void setGrid(BattleshipNetwork.PlayerBoard[] playerBoard)
    {
        for(int i = 0; i < 100; i++)
        {
            board[i%10][i/10] = new CellModel(playerBoard[i].status,playerBoard[i].xPos,playerBoard[i].yPos);
        }
    }

    public CellModel[][] getGrid()
    {
        return board;
    }
    public void setSunkShips(ArrayList<String> _sunkShips)
    {
        sunkShips = _sunkShips;
    }

    public ArrayList<String> getSunkShip()
    {
        return sunkShips;
    }


}
