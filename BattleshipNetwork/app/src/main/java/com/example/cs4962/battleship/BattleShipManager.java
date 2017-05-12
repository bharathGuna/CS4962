package com.example.cs4962.battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Bharath on 10/23/2015.
 */

public class BattleShipManager
{


    //Interface for changing Fragments
    //TODO: need to get all possible ship types to deployfragment
    public interface OnNewGameListener
    {
        void onNewGameListener(BattleshipNetwork.MyGame name);
    }

    public interface OnJoinGameListener
    {
        void onJoinListener(BattleshipNetwork.MyGame name);
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

    public void setOnJoinGameLister(OnJoinGameListener _onJoinGameLister)
    {
        onJoinGameListener = _onJoinGameLister;
    }

    private static BattleShipManager instance;

    private OnJoinGameListener onJoinGameListener = null;
    private OnGameOverListener onGameOverListener = null;
    private OnNewGameListener onNewGameListener = null;
    private BattleshipNetwork.GameList[] games;
    private ArrayList<BattleshipNetwork.MyGame > myGames;
    private ArrayList<String> playerNames;
    private ArrayList<String> gameNames;
    private ArrayList<GameModel> gameModels;
    private String playerName = "";
    private BattleshipNetwork.MyGame currentGame;

    public BattleshipNetwork.GameDetail getCurrentGameDetails()
    {
        return currentGameDetails;
    }

    public void setCurrentGameDetails(BattleshipNetwork.GameDetail _currentGameDetails)
    {
       currentGameDetails = _currentGameDetails;
    }

    private BattleshipNetwork.GameDetail currentGameDetails;
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
        games = new BattleshipNetwork.GameList[0];
        myGames = new ArrayList<>();
        playerNames = new ArrayList<>();
        gameNames = new ArrayList<>();
    }

    public void newGame(BattleshipNetwork.MyGame game,String playerName,String gameName)
    {
        myGames.add(game);
        playerNames.add(playerName);
        setPlayerName(playerName);
        setCurrentGame(game);
        gameNames.add(gameName);
        if (onNewGameListener != null)
        {
            onNewGameListener.onNewGameListener(game);
        }

    }

    public void joinGame(BattleshipNetwork.MyGame game,String playerName)
    {
        myGames.add(game);
        playerNames.add(playerName);
        setPlayerName(playerName);
        setCurrentGame(game);
        if (onJoinGameListener != null)
        {
            onJoinGameListener.onJoinListener(game);
        }
    }

    public void addGameName(String name)
    {
        gameNames.add(name);
    }
    public void createGameModel(BattleshipNetwork.Boards currentGame)
    {
        GameModel gm = new GameModel(currentGame);
        gameModels.add(gm);
    }

    public void updateGameModel(BattleshipNetwork.Boards currentGame)
    {
         int gameIndex = myGames.indexOf(getCurrentGame());
         try
         {
             GameModel gm = gameModels.get(gameIndex);
             gm.setPlayer1(currentGame.playerBoard);
             gm.setPlayer2(currentGame.opponentBoard);
         }
         catch (IndexOutOfBoundsException e)
         {
             createGameModel(currentGame);
         }

    }

    public String getGameName()
    {
        if(!gameNames.isEmpty())
        return gameNames.get(findIndex());
        return "Loading...";
    }
    public int findIndex()
    {
        return myGames.indexOf(getCurrentGame());
    }
    /*
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



    public GameStatus getGameMode(String name)
    {
        return gameModels.get(games.indexOf(name)).getMode();
    }

    public void setGameMode(String name, GameStatus mode)
    {
        gameModels.get(games.indexOf(name)).setMode(mode);
    }

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
    public boolean myGame(BattleshipNetwork.GameList gl)
    {
        String gameId = gl.id;
        for(BattleshipNetwork.MyGame mg : myGames)
        {
            if(mg.gameId.equals(gameId))
                return true;
        }
        return false;
    }

    public BattleshipNetwork.MyGame getMyGame(BattleshipNetwork.GameList gl)
    {
        String gameId = gl.id;
        for(BattleshipNetwork.MyGame mg : myGames)
        {
            if(mg.gameId.equals(gameId))
                return mg;
        }
       return null;
    }

    public BoardModel getPlayer1Board()
    {
            return gameModels.get(findIndex()).getPlayer1();
    }

    public BoardModel getPlayer2Board()
    {
        return gameModels.get(findIndex()).getPlayer2();
    }

    public void setGames(BattleshipNetwork.GameList[] gamesList)
    {
        Arrays.sort(gamesList,new BattleshipNetwork().new GameListComparator());
        games = gamesList;
    }

    public BattleshipNetwork.GameList[] getGames()
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

    public ArrayList<BattleshipNetwork.MyGame> getMyGames()
    {
        return myGames;
    }

    public void setMyGames(ArrayList<BattleshipNetwork.MyGame> mygames)
    {
        myGames = mygames;

    }
    public void setCurrentGame(BattleshipNetwork.MyGame  game)
    {
        currentGame = game;
    }

    public BattleshipNetwork.MyGame getCurrentGame()
    {
        return currentGame;
    }

}
