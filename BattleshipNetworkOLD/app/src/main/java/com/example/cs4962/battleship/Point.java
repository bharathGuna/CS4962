package com.example.cs4962.battleship;

import java.util.Comparator;

/**
 * Created by Bharath on 10/23/2015.
 */
public class Point implements Comparable<Point>
{
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }






    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(Point another)
    {
        if(x == another.x)
            return Integer.compare(y,another.y);
        else
            return Integer.compare(x,another.x);
    }
}
