package com.example.cs4962.battleship;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Bharath on 11/8/2015.
 */
public class BattleShipNetwork
{
    public static final String BASE_URL = "http://battleship.pixio.com";

    public enum GameStatus
    {
        DONE, PLAYING, WAITING
    }

    public enum GridSquareStatus
    {
        HIT, MISS, SHIP, NONE
    }

    public class GameList
    {
        public String id;
        public String name;
        public GameStatus status;
    }

    public static class GameModel
    {
        public String id;
        public String name;
        public String player1;
        public String player2;
        public String winner;
        public int missilesLaunched;
    }

    public class Boards
    {
        public PlayerBoard[] playerBoard;
        public OpponentBoard[] opponentBoard;
    }

    public class PlayerBoard
    {
        public int xPos;
        public int yPos;
        public GridSquareStatus status;
    }

    public class OpponentBoard
    {
        public int xPos;
        public int yPos;
        public GridSquareStatus status;
    }

    public class Guess
    {
        public boolean hit;
        public int shipSunk;
    }

    public class Turn
    {
        public boolean isYourTurn;
        public String winner;
    }

    public class CreateGame
    {
        public String playerId;
        public String gameId;
    }

    public class JoinGame
    {
        public String playerId;
    }

    public static GameList[] getGameList()
    {
        URL gameListUrl = null;
        HttpURLConnection gameListConnection = null;
        Scanner gameListScanner = null;
        GameList[] games = null;
        try
        {
            gameListUrl = new URL("http://battleship.pixio.com/api/games");
            gameListConnection = (HttpURLConnection) gameListUrl.openConnection();
            gameListScanner = new Scanner(gameListConnection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            while (gameListScanner.hasNextLine())
            {
                stringBuilder.append(gameListScanner.nextLine());
            }
            String gameListString = stringBuilder.toString();

            Gson gson = new Gson();
            games = gson.fromJson(gameListString, GameList[].class);
            Log.i("Battleship", "Got back gameList with count: " + games.length);


        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            gameListConnection.disconnect();
        }
        return games;
    }
}
