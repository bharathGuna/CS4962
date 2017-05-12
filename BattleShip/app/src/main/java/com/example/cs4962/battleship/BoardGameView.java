package com.example.cs4962.battleship;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Bharath on 10/20/2015.
 */
public class BoardGameView extends ViewGroup implements CellView.OnCellClickListener
{

    //Interfaces
    //DeploymentMode
    //check if ship is sucessfully built and placed
    public interface OnBuildShipListener
    {
        void onValidShip(ArrayList<Point> pieces);
    }

    public interface OnCellClickListener
    {
        void onCellClick(Point piece);
    }

    private OnCellClickListener onCellClickListener = null;
    private OnBuildShipListener onBuildShipListener = null;
    private CellView[][] board;
    private ArrayList<Point> selected = new ArrayList<>();
    private GameStatus mode;
    private ArrayList<ArrayList<Point>> shipLocations;
    private int col, row;

    public void setOnBuildShipListener(OnBuildShipListener _onBuildShipListener)
    {
        onBuildShipListener = _onBuildShipListener;
    }

    public void setOnCellClickListener(OnCellClickListener _onCellClickListener)
    {
        onCellClickListener = _onCellClickListener;
    }

    public BoardGameView(Context context)
    {
        super(context);


        this.setBackgroundColor(Color.GRAY);
        // this.addView(new Button(context));
        this.setColumns(10);

        this.setRow(10);
        board = new CellView[10][10];
        for (int i = 0; i < 100; i++)
        {
            CellView temp = new CellView(context);
            temp.setLocation(i % 10, i / 10);
            temp.setOnCellClickListener(this);
            board[i % 10][i / 10] = temp;
            this.addView(temp);//Button(context));

        }
        shipLocations = new ArrayList<>();

    }

    public void setBoard(CellView[][] _board)
    {
        int x, y;
        for (int i = 0; i < 100; i++)
        {
            x = i % 10;
            y = i / 10;
            board[x][y].setType(_board[x][y].getType());

        }
    }


    public void addShip(ArrayList<Point> pieces)
    {
        if(!shipLocations.contains(pieces))
        {
            for (Point p : pieces)
            {
                board[p.getX()][p.getY()].setType(CellType.SHIP);
            }
            shipLocations.add(pieces);
            selected.clear();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        //super.onLayout(changed, left, top, right, bottom);
        final int columns = this.getColumnCount();//edit this if you need different grid
        final int rows = this.getRowCount();

        int children = getChildCount();

        int width = getWidth();
        int height = getHeight();


        int viewWidth = width / columns;
        int viewHeight = height / rows;

        if (viewWidth > viewHeight)
            viewWidth = viewHeight;
        else
            viewHeight = viewWidth;

        int rowIndex = 0;
        int columnIndex = 0;

        for (int i = 0; i < children; i++)
        {
            getChildAt(i).layout(viewWidth * columnIndex, viewHeight * rowIndex, viewWidth * columnIndex + viewWidth, viewHeight * rowIndex + viewHeight);
            columnIndex++;
            if (columnIndex == columns)
            {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        int childState = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);

            final int columns = this.getColumnCount();//edit this if you need different grid
            final int rows = this.getRowCount();


            int viewWidth = width / columns;
            int viewHeight = height / rows;

            if (viewWidth > viewHeight)
                viewWidth = viewHeight;
            else
                viewHeight = viewWidth;

            child.measure(MeasureSpec.AT_MOST | viewWidth, MeasureSpec.AT_MOST | viewHeight);
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState)
        );
    }

    public void setColumns(int _col)
    {
        col = _col;
    }

    public void setRow(int _row)
    {
        row = _row;
    }

    public int getColumnCount()
    {
        return col;
    }

    public int getRowCount()
    {
        return row;
    }

    public GameStatus getMode()
    {
        return mode;
    }

    public void setMode(GameStatus _mode)
    {
        mode = _mode;
    }

    public void clearSelection()
    {
        for (Point p : selected)
        {
            if (board[p.getX()][p.getY()].getType() == CellType.SELECTED)
                board[p.getX()][p.getY()].setType(CellType.EMPTY);
        }
        selected.clear();
    }


    public void hideShips()
    {
        for (ArrayList<Point> ship : shipLocations)
        {
            for (Point piece : ship)
                if(board[piece.getX()][piece.getY()].getType() != CellType.HIT)
                    board[piece.getX()][piece.getY()].setType(CellType.EMPTY);
        }
    }

    public void showShips()
    {
        for (ArrayList<Point> ship : shipLocations)
        {
            for (Point piece : ship)
                if(board[piece.getX()][piece.getY()].getType() != CellType.HIT)
                    board[piece.getX()][piece.getY()].setType(CellType.SHIP);
        }
    }


    public void setHit(Point piece, boolean hit)
    {
        CellView temp = board[piece.getX()][piece.getY()];
        if(hit)
            temp.setType(CellType.HIT);
        else
            temp.setType(CellType.MISS);
    }

    public ArrayList<ArrayList<Point>> getShipLocations()
    {
        return shipLocations;
    }

    public CellView[][] getBoard()
    {
        return board;
    }

    public void onCellClick(CellView cellView)
    {

        //Deployment mode
        if (mode == GameStatus.DEPOLOYMENT_PLAYER1 || mode == GameStatus.DEPOLOYMENT_PLAYER2)
            if (cellView.getType() != CellType.SHIP && selected.size() < 5 && shipLocations.size() < 5)
            {
                if (!selected.contains(cellView.getLocation()))
                {
                    selected.add(cellView.getLocation());
                    cellView.setType(CellType.SELECTED);
                    if (onBuildShipListener != null)
                    {
                        Collections.sort(selected);
                        onBuildShipListener.onValidShip(selected);
                    }

                }
                else
                {
                    selected.remove(cellView.getLocation());
                    cellView.setType(CellType.EMPTY);
                }
            }
            else
            {
                clearSelection();
            }
        else if (mode == GameStatus.PLAYER1_TURN || mode == GameStatus.PLAYER2_TURN)
        {
            if(cellView.getType() != CellType.HIT && cellView.getType() != CellType.MISS)
            {

                if (onCellClickListener != null)
                    onCellClickListener.onCellClick(cellView.getLocation());
            }
            else
            {
                Toast toast = Toast.makeText(getContext(),"Missile has all ready been fired here",Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }
}


