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
    private ArrayList<ShipType> availableShips;
    private ArrayList<ShipModel> ships;
    private ArrayList<ShipModel> sunkShips;
    private int misslesFired = 0;

    public BoardModel()
    {
        board =  new CellModel[10][10];
        availableShips = new ArrayList<ShipType>();
        availableShips.add(ShipType.AIRCRAFTCARRIER);
        availableShips.add(ShipType.BATTLESHIP);
        availableShips.add(ShipType.CRUISER);
        availableShips.add(ShipType.CRUISER);
        availableShips.add(ShipType.DESTROYER);
        ships = new ArrayList<>();
        sunkShips = new ArrayList<>();
        initializeGrid();

    }

    private void initializeGrid()
    {
        for(int i = 0; i < board.length; i++)
            for(int j = 0; j < board[i].length; j++)
            {
                board[i][j] = new CellModel(CellType.EMPTY, new Point(i, j));
            }
    }

    public boolean placeShip(ArrayList<Point> points)
    {
        ArrayList<CellModel> ship = new ArrayList<>();
        for(int i = 0; i < points.size(); i++)
        {
            Point p = points.get(i);
            board[p.getX()][p.getY()].setCellType(CellType.SHIP);
            ship.add(board[p.getX()][p.getY()]);
        }
        ShipModel temp = new ShipModel();

        if(temp.validShipBuilt(ship.size(),ship))
        {
            ShipType type = temp.getType();
            if(availableShips.contains(type))
            {
                availableShips.remove(type);
                ships.add(temp);
                return true;
            }
        }
        for(int i = 0; i < points.size(); i++)
        {
            Point p = points.get(i);
            board[p.getX()][p.getY()].setCellType(CellType.EMPTY);
        }
        return false;
    }

    public boolean isHit(Point hit)
    {
       misslesFired ++;
       CellModel piece = board[hit.getX()][hit.getY()];
       for(ShipModel ship : ships)
       {
           if(ship.isHit(piece))
           {
               piece.setCellType(CellType.HIT);
               return true;
           }
       }
        piece.setCellType(CellType.MISS);
        return false;
    }

    public ShipModel isSunk(Point sunk)
    {
        CellModel piece = board[sunk.getX()][sunk.getY()];
        ShipModel temp = new ShipModel();
        for(ShipModel ship : ships)
        {
            if(ship.contains(piece))
            {
                temp = ship;
                break;
            }
        }

        if(temp.isSunk())
        {
            ships.remove(temp);
            sunkShips.add(temp);
            return temp;
        }
        return null;
    }
    
    
    public int getMisslesFired()
    {
        return misslesFired;
    }
    


    public void setSunkShips(ArrayList<ShipModel> _sunkShips)
    {
        sunkShips = _sunkShips;
    }

    public ArrayList<ShipModel> getSunkShip()
    {
        return sunkShips;
    }

    public void setAvailableShips( ArrayList<ShipType> types)
    {
        availableShips = types;
    }

    public ArrayList<ShipType> getAvailableShips()
    {
        return availableShips;
    }

    public void setBoard(CellModel[][] _board)
    {
        board = _board;
    }
    public CellModel[][] getBoard()
    {
        return board;
    }
    public ArrayList<ShipModel> getPlacedShips()
    {
        return ships;
    }
    public void setPlacedShips(ArrayList<ShipModel> _ships)
    {
        ships = _ships;
    }


    public void setMisslesFired(int misslesFired)
    {
        this.misslesFired = misslesFired;
    }
}
