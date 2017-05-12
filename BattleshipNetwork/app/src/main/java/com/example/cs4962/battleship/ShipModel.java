package com.example.cs4962.battleship;

import java.util.ArrayList;

/**
 * Created by Bharath on 10/20/2015.
 * Needs to know if ship is sunk
 * What ship it is
 * Valid ship
 */

public class ShipModel
{
    private int length;
    private boolean orientation;
    private ShipType type;
    private ArrayList<CellModel> shipPieces;


    public ShipModel()
    {
        type = ShipType.NONE;
    }


    public ArrayList<CellModel> getShipPieces()
    {
        return shipPieces;
    }

    public ShipType getType()
    {
        return type;
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

}
