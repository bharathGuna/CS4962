package com.example.cs4962.battleship;



/**
 * Created by Bharath on 10/20/2015.
 */
public class CellModel
{

    private CellType type;
    private Point location;

    public CellModel(CellType _type, int x , int y)
    {
        type = _type;
        location = new Point(x,y);
    }

    public Point getLocation()
    {
        return location;
    }
    public void setCellType(CellType _type)
    {
        switch (_type)
        {
            case NONE:
                type = CellType.NONE;
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
