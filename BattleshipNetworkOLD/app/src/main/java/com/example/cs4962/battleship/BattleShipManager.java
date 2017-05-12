package com.example.cs4962.battleship;

import java.util.ArrayList;

/**
 * Created by Bharath on 10/23/2015.
 */

public class BattleShipManager
{


    //Interface for changing Fragments
    //TODO: need to get all possible ship types to deployfragment
    public interface OnNewGameListener
    {
        void onNewGameListener(String name);
    }

    public interface OnGameOverListener
    {
        void OnGameOverListener(String name);
    }


    public void setOnNewGameListener(OnNewGameListener _onNewGameListener)
    {
        onNewGameListener = _onNewGameListener;
    }

    public void setOnGameOverListener(OnGameOverListener _onGameOverListener)
    {
        onGameOverListener = _onGameOverListener;
    }

    private static BattleShipManager instance;


    private OnGameOverListener onGameOverListener = null;
    private OnNewGameListener onNewGameListener = null;
    private String playerName;
    private BattleShipNetwork.GameList[] games;
    private ArrayList<GameModel> gameModels;
    private String currentGame;

    /*
    Singleton get instance reference
    */
    public static BattleShipManager getInstance()
    {
        if (instance == null)
        {
            instance = new BattleShipManager();
        }
        return instance;
    }

    private BattleShipManager()
    {
        // games = new HashMap<String, GameModel>();
        gameModels = new ArrayList<>();
        //int [] t = new int[3];
       games = new BattleShipNetwork.GameList[0];// new ArrayList<>();
    }

    /*
    public void newGame(String name)
    {
        if (!games.contains(name))
        {
            games.add(name);
            gameModels.add(new GameModel());

            setGameMode(name, GameStatus.DEPOLOYMENT_PLAYER1);
            currentGame = name;
            if (onNewGameListener != null)
            {
                onNewGameListener.onNewGameListener(name);
            }
        }
    }

    public ArrayList<ShipModel> placeShip(String gameName, ArrayList<Point> points)
    {
        if (games.contains(gameName))
        {
            return gameModels.get(games.indexOf(gameName)).placeShip(points);
            //games.indexOf(gameName);
            //return games.get(gameName).placeShip(points);
        }

        return new ArrayList<ShipModel>();
    }

    public boolean isHit(String gameName, GameStatus status, Point piece)
    {
        if (games.contains(gameName))
        {
            BoardModel board = new BoardModel();
            if (status == GameStatus.PLAYER1_TURN)
                board = getPlayer2Board(gameName);
            else if (status == GameStatus.PLAYER2_TURN)
                board = getPlayer1Board(gameName);

            return board.isHit(piece);
        }
        return false;
    }

    public ShipModel isSunk(String gameName, GameStatus status, Point sunk)
    {
        if (games.contains(gameName))
        {
            BoardModel board = new BoardModel();
            if (status == GameStatus.PLAYER1_TURN)
                board = getPlayer2Board(gameName);
            else if (status == GameStatus.PLAYER2_TURN)
                board = getPlayer1Board(gameName);

            return board.isSunk(sunk);
        }
        return null;
    }

    public boolean isGameOver(String gameName, GameStatus status)
    {
        if (games.contains(gameName))
        {
            BoardModel board = new BoardModel();
            if (status == GameStatus.PLAYER1_TURN)
                board = getPlayer2Board(gameName);
            else if (status == GameStatus.PLAYER2_TURN)
                board = getPlayer1Board(gameName);
            if(board.getSunkShip().size() == 5)
            {
                gameOver(gameName,status);
                return true;
            }
        }
        return false;
    }

    public void gameOver(String name, GameStatus status)
    {
        if (status == GameStatus.PLAYER1_TURN)
        {
            status = GameStatus.PLAYER1_WON;
            setGameMode(name, status);
        }
        else if (status == GameStatus.PLAYER2_TURN)
        {
            status = GameStatus.PLAYER2_WON;
            setGameMode(name, status);
        }

        if (onGameOverListener != null)
            onGameOverListener.OnGameOverListener(name);
    }

    public BoardModel getPlayer1Board(String gameName)
    {
        if (games.contains(gameName))
        {
            return gameModels.get(games.indexOf(gameName)).getPlayer1();
        }

        return null;
    }

    public BoardModel getPlayer2Board(String gameName)
    {
        if (games.contains(gameName))
        {
            return gameModels.get(games.indexOf(gameName)).getPlayer2();
        }
        return null;
    }

    public GameStatus getGameMode(String name)
    {
        return gameModels.get(games.indexOf(name)).getMode();
    }

    public void setGameMode(String name, GameStatus mode)
    {
        gameModels.get(games.indexOf(name)).setMode(mode);
    }
*/
    public void setGames(BattleShipNetwork.GameList[] gamesList)
    {
       games = gamesList;

    }

    public BattleShipNetwork.GameList[] getGameList()
    {

        return games;
    }

    public void setPlayerName(String name)
    {
        playerName = name;
    }

    public String getPlayerName()
    {
        return playerName;
    }
/*
    public ArrayList<GameModel> getGameModels()
    {

        return gameModels;
    }

    public void setGameModels(ArrayList<GameModel> _gameModels)
    {
        gameModels = _gameModels;
    }

    public void setCurrentGame(String game)
    {
        currentGame = game;
    }

    public String getCurrentGame()
    {
        return currentGame;
    }
*/
}
