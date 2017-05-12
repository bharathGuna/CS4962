package com.example.cs4962.battleship;

import java.util.ArrayList;

/**
 * Created by Bharath on 10/20/2015.
 * Needs to know if ship is sunk
 * What ship it is
 * Valid ship
 */

public class ShipModel implements Comparable<ShipModel>
{
    private int length;
    private boolean orientation;
    private ShipType type;
    private ArrayList<CellModel> shipPieces;


    public ShipModel()
    {
        type = ShipType.NONE;
    }

    public void setShipPieces(ArrayList<CellModel> _shipPieces)
    {
        if (validShipBuilt(_shipPieces.size(), _shipPieces))
            shipPieces = (ArrayList<CellModel>)_shipPieces.clone();
    }

    public ArrayList<CellModel> getShipPieces()
    {
        return shipPieces;
    }

    public ShipType getType()
    {
        return type;
    }

    public boolean validShipBuilt(int _length, ArrayList<CellModel> _shipPieces)
    {
        //proper length recieved
        //check pieces are adjacent and in one direction
        type = getShipForLength(_length);
        if (!type.equals(ShipType.NONE))
        {
            length = _length;
            if (checkPosition((ArrayList<CellModel>)_shipPieces.clone()))
            {
                shipPieces = (ArrayList<CellModel>)_shipPieces.clone();
                for (CellModel cellModel : shipPieces)
                    cellModel.setCellType(CellType.SHIP);
                return true;
            }
        }
        return false;
    }

    private boolean checkPosition(ArrayList<CellModel> _shipPieces)
    {
        int prevX = -1;
        int prevY = -1;
        int currX;
        int currY;
        boolean horizontal = false;
        int hCount = 0;
        int vCount = 0;
        int aCount = 0;
        for (CellModel piece : _shipPieces)
        {
            currX = piece.getLocation().getX();
            currY = piece.getLocation().getY();

            if (prevX == -1 && prevY == -1)
            {
                prevX = piece.getLocation().getX();
                prevY = piece.getLocation().getY();
                continue;
            }


            if (currX == prevX && currY == prevY + 1)
            {
                horizontal = false;
                vCount++;
                aCount++;
            }
            else if (currY == prevY && currX == prevX + 1)
            {
                horizontal = true;
                hCount++;
                aCount++;
            }
            else
            {
                aCount--;
            }


            prevX = currX;
            prevY = currY;


        }

        if (aCount != _shipPieces.size()-1 || vCount > 0 && hCount > 0)
            return false;

        orientation = horizontal;
        return true;


    }

    public boolean isHit(CellModel model)
    {
        if(contains(model))
        {
           int index = shipPieces.indexOf(model);
           shipPieces.get(index).setCellType(CellType.HIT);
            return true;
        }

        return false;
    }

    public boolean contains(CellModel model)
    {
        for(CellModel cm : shipPieces)
        {
            if(cm.equals(model))
            {
                return true;
            }
        }
        return false;


    }

    public boolean isSunk()
    {
        int sunk = 0;
        for (CellModel cellModel : shipPieces)
        {
            if (cellModel.getType().equals(CellType.HIT))
            {
                sunk++;
            }
        }
        return sunk == length;
    }

    public int getShipLength(ShipType ship)
    {
        int length = 0;
        switch (ship)
        {
            case DESTROYER:
                length = 2;
                break;
            case CRUISER:
                length = 3;
                break;
            case BATTLESHIP:
                length = 4;
                break;
            case AIRCRAFTCARRIER:
                length = 5;
                break;
        }
        return length;
    }


    public ShipType getShipForLength(int length)
    {

        ShipType type = ShipType.NONE;

        switch (length)
        {
            case 2:
                type = ShipType.DESTROYER;
                break;
            case 3:
                type = ShipType.CRUISER;
                break;
            case 4:
                type = ShipType.BATTLESHIP;
                break;
            case 5:
                type = ShipType.AIRCRAFTCARRIER;
                break;
            default:
                break;
        }

        return type;
    }

    @Override
    public int compareTo(ShipModel another)
    {
        return Integer.compare(this.length, another.length);
    }

    public ArrayList<Point> getLocations()
    {
        ArrayList<Point> temp = new ArrayList<>();
        for (CellModel cm : shipPieces)
        {
            temp.add(cm.getLocation());
        }
        return temp;
    }
}
