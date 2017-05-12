package com.example.cs4962.battleship;

import android.media.JetPlayer;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Bharath on 11/8/2015.
 */
public class BattleshipNetwork
{
    public static final String BASE_URL = "http://battleship.pixio.com";


    public class GameListComparator implements Comparator<GameList>
    {

        @Override
        public int compare(GameList lhs, GameList rhs)
        {

                //games that you are playing are first
                //Waiting games
                //Everything else


                ArrayList<MyGame> myGames = BattleShipManager.getInstance().getMyGames();

                if(!myGames.isEmpty())
                {
                    for (MyGame mg : myGames)
                    {

                        if(mg.gameId == lhs.id && rhs.id == mg.gameId)
                        {
                            return 0;
                        }
                        else if (mg.gameId == rhs.id )
                        {
                            return -1;
                        }
                        else if(lhs.id == mg.gameId)
                        {
                            return 0;
                        }
                    }
                }

                return Integer.compare(rhs.getValue(), lhs.getValue());
        }
    }
    public class GameList
    {
        public String id; //gameId
        public String name; //gameName
        public GameStatus status;

        /*
         return < 0 this < another
         return 0 this == another
         return > 0 this > another
         */

        public int getValue()
        {
            switch (status)
            {
                case WAITING:
                    return 2;
                case PLAYING:
                    return 1;
                case DONE:
                    return 0;
            }
            return 0;
        }


    }

    public static class GameDetail
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
        public PlayerBoard[] opponentBoard;
    }


    public class PlayerBoard
    {
        public int xPos;
        public int yPos;
        public CellType status;
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

    public class MyGame
    {
        public String playerId;
        public String gameId;
    }

    public class JoinGame
    {
        public String playerId;
    }


    public static GameDetail getGameDetails(String gameId)
    {
        URL gameListUrl = null;
        HttpURLConnection gameDetailConnection = null;
        Scanner gameDetailScanner = null;
        GameDetail games = null;
        try
        {
            gameListUrl = new URL(BASE_URL+"/api/games/"+gameId );
            gameDetailConnection = (HttpURLConnection) gameListUrl.openConnection();
            gameDetailScanner = new Scanner(gameDetailConnection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            while (gameDetailScanner.hasNextLine())
            {
                stringBuilder.append(gameDetailScanner.nextLine());
            }
            String gameListString = stringBuilder.toString();

            Gson gson = new Gson();
            games = gson.fromJson(gameListString, GameDetail.class);
           // Log.i("Battleship", "Got back gameList with count: " + games.length);


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
            gameDetailConnection.disconnect();
        }
        return games;
    }
    public static GameList[] getGameList()
    {
        URL gameListUrl = null;
        HttpURLConnection gameListConnection = null;
        Scanner gameListScanner = null;
        GameList[] games = null;
        try
        {
            gameListUrl = new URL(BASE_URL+"/api/games");
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


    public static MyGame createGame(String playerName, String gameName)
    {
        URL gameCreateUrl;
        HttpURLConnection gameCreateConnection = null;
        Scanner createGameScanner;
        Map<String,String> names = new HashMap();
        names.put("gameName",gameName);
        names.put("playerName",playerName);
        MyGame newGame = null;
        Gson gson = new Gson();
        try
        {
            gameCreateUrl = new URL(BASE_URL+"/api/games");
            gameCreateConnection = (HttpURLConnection) gameCreateUrl.openConnection();
            gameCreateConnection.setRequestMethod("POST");
            //JSONObject payload = new JSONObject()
            gameCreateConnection.addRequestProperty("Content-Type", "application/json");
           // String payload ="{ \"gameName\"  : \"STRING\", \"playerName\": \"STRING\" }"; //"{\"playerName\" : me\", \"gameName\" : \"someGame\" }";
            JSONObject jsonObject = new JSONObject(names);
            gameCreateConnection.setDoOutput(true);
            OutputStreamWriter outputStream = new OutputStreamWriter(gameCreateConnection.getOutputStream());
            outputStream.write(jsonObject.toString());
            outputStream.flush();
            outputStream.close();

            if(gameCreateConnection.getResponseCode() == 200)
            {
                createGameScanner = new Scanner(gameCreateConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                while (createGameScanner.hasNextLine())
                {
                    stringBuilder.append(createGameScanner.nextLine());
                }
                String createGameString = stringBuilder.toString();
                newGame = gson.fromJson(createGameString,MyGame.class);
            }
            Log.i("Battleship", "Added game with response code: " + newGame.playerId);

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
            gameCreateConnection.disconnect();
        }
        return newGame;
    }

    public static MyGame joinGame(String gameID,String playerName)
    {
        URL gameCreateUrl;
        HttpURLConnection joinGameConnection = null;
        Scanner createGameScanner;
        Map<String,String> names = new HashMap();
        names.put("playerName",playerName);
        MyGame newGame = null;
        JoinGame game = null;
        Gson gson = new Gson();
        try
        {
            gameCreateUrl = new URL(BASE_URL+"/api/games/" + gameID+"/join");
            joinGameConnection = (HttpURLConnection) gameCreateUrl.openConnection();
            joinGameConnection.setRequestMethod("POST");
            //JSONObject payload = new JSONObject()
            joinGameConnection.addRequestProperty("Content-Type", "application/json");
            // String payload ="{ \"gameName\"  : \"STRING\", \"playerName\": \"STRING\" }"; //"{\"playerName\" : me\", \"gameName\" : \"someGame\" }";
            JSONObject jsonObject = new JSONObject(names);
            joinGameConnection.setDoOutput(true);
            OutputStreamWriter outputStream = new OutputStreamWriter(joinGameConnection.getOutputStream());
            outputStream.write(jsonObject.toString());
            outputStream.flush();
            outputStream.close();

            if(joinGameConnection.getResponseCode() == 200)
            {
                createGameScanner = new Scanner(joinGameConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                while (createGameScanner.hasNextLine())
                {
                    stringBuilder.append(createGameScanner.nextLine());
                }
                String createGameString = stringBuilder.toString();
                game = gson.fromJson(createGameString,JoinGame.class);
            }

            names.clear();
            names.put("playerId", game.playerId);
            names.put("gameId", gameID);
            jsonObject = new JSONObject(names);
            newGame = gson.fromJson(jsonObject.toString(),MyGame.class);

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
            joinGameConnection.disconnect();
        }
        return newGame;
    }

    public static Turn isTurn(String gameID,String playerId)
    {
        URL turnUrl;
        HttpURLConnection turnConnection = null;
        Scanner createGameScanner;
        Map<String,String> names = new HashMap();
        names.put("playerId",playerId);
        Turn turn = null;
        Gson gson = new Gson();
        try
        {
            turnUrl = new URL(BASE_URL+"/api/games/" + gameID+"/status");
            turnConnection = (HttpURLConnection) turnUrl.openConnection();
            turnConnection.setRequestMethod("POST");
            turnConnection.addRequestProperty("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject(names);
            turnConnection.setDoOutput(true);
            OutputStreamWriter outputStream = new OutputStreamWriter(turnConnection.getOutputStream());
            outputStream.write(jsonObject.toString());
            outputStream.flush();
            outputStream.close();

            if(turnConnection.getResponseCode() == 200)
            {
                createGameScanner = new Scanner(turnConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                while (createGameScanner.hasNextLine())
                {
                    stringBuilder.append(createGameScanner.nextLine());
                }
                String createGameString = stringBuilder.toString();
                turn = gson.fromJson(createGameString,Turn.class);
            }


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
            turnConnection.disconnect();
        }
        return turn;
    }

    public static Boards getBoard(String gameID,String playerId)
    {
        URL turnUrl;
        HttpURLConnection turnConnection = null;
        Scanner createGameScanner;
        Map<String,String> names = new HashMap();
        names.put("playerId",playerId);
        Boards boards = null;
        Gson gson = new Gson();
        try
        {
            turnUrl = new URL(BASE_URL+"/api/games/" + gameID+"/board");
            turnConnection = (HttpURLConnection) turnUrl.openConnection();
            turnConnection.setRequestMethod("POST");
            turnConnection.addRequestProperty("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject(names);
            turnConnection.setDoOutput(true);
            OutputStreamWriter outputStream = new OutputStreamWriter(turnConnection.getOutputStream());
            outputStream.write(jsonObject.toString());
            outputStream.flush();
            outputStream.close();

            if(turnConnection.getResponseCode() == 200)
            {
                createGameScanner = new Scanner(turnConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                while (createGameScanner.hasNextLine())
                {
                    stringBuilder.append(createGameScanner.nextLine());
                }
                String createGameString = stringBuilder.toString();
                boards = gson.fromJson(createGameString,Boards.class);
            }


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
            turnConnection.disconnect();
        }
        return boards;
    }

    public static Guess makeGuess(String gameID, String playerId, Point xy)
    {
        URL guessUrl;
        HttpURLConnection guessConnection = null;
        Scanner guessScanner;
        Map<String,String> names = new LinkedHashMap<>();
        names.put("playerId",playerId);
        names.put("xPos", ""+xy.getX());
        names.put("yPos", ""+xy.getY());
        Guess guess = null;
        Gson gson = new Gson();
        try
        {
            guessUrl = new URL(BASE_URL+"/api/games/" + gameID+"/guess");
            guessConnection = (HttpURLConnection) guessUrl.openConnection();
            guessConnection.setRequestMethod("POST");
            guessConnection.addRequestProperty("Content-Type", "application/json");
            JSONObject jsonObject = new JSONObject(names);
            guessConnection.setDoOutput(true);
            OutputStreamWriter outputStream = new OutputStreamWriter(guessConnection.getOutputStream());
            outputStream.write(jsonObject.toString());
            outputStream.flush();
            outputStream.close();

            if(guessConnection.getResponseCode() == 200)
            {
                guessScanner = new Scanner(guessConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                while (guessScanner.hasNextLine())
                {
                    stringBuilder.append(guessScanner.nextLine());
                }
                String createGameString = stringBuilder.toString();
                guess = gson.fromJson(createGameString,Guess.class);
            }

            Log.i("Guess", guess.hit + "|" + guess.shipSunk);

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
            guessConnection.disconnect();
        }
        return guess;
    }
}
