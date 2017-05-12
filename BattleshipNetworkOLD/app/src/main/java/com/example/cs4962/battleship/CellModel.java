package com.example.cs4962.battleship;



/**
 * Created by Bharath on 10/20/2015.
 */
public class CellModel
{

    private  CellType type;
    private Point location;

    public CellModel(CellType _type, Point xy)
    {
        type = _type;
        location = xy;
    }

    public Point getLocation()
    {
        return location;
    }
    public void setCellType(CellType _type)
    {
        switch (_type)
        {
            case EMPTY:
                type = CellType.EMPTY;
                break;
            case SHIP:
                type = CellType.SHIP;
                break;
            case MISS:
                type = CellType.MISS;
                break;
            case HIT:
                type = CellType.HIT;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean equals(Object o)
    {
        CellModel object = (CellModel)o;

       return this.getLocation().getX() == object.getLocation().getX() && this.getLocation().getY() == object.getLocation().getY();
    }

    public CellType getType()
    {
        return type;
    }
}
