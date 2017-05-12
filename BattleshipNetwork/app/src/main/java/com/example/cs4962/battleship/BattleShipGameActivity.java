package com.example.cs4962.battleship;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class BattleShipGameActivity extends Activity
{

    String GAME_LIST_TAG = "GAME_LIST_TAG";
    FrameLayout masterFrameLayout,secondFrameLayout;
    GameListFragment gameListFragment;
    BattleShipManager manager;
    String filename1 = "gameList2.txt";
    String filename2 = "games2.txt";
    boolean tablet = false;
    boolean isTurn;
    boolean gameStarted;
    private Handler handler = new Handler();
    int updateBoard = 0;
    boolean viewGame;

    public interface GameDetailListener
    {
        void gameDetailReady();
    }

    public GameDetailListener gameDetailsListener = null;

    public void setGameDetailsListener(GameDetailListener _gameDetailsListener)
    {
        gameDetailsListener = _gameDetailsListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            tablet = isTablet(this);
            LinearLayout mainLayout = new LinearLayout(this);
            if(tablet)
                mainLayout.setOrientation(LinearLayout.HORIZONTAL);
            else
                mainLayout.setOrientation(LinearLayout.VERTICAL);



            masterFrameLayout = new FrameLayout(this);
            masterFrameLayout.setId(10);
            secondFrameLayout = new FrameLayout(this);
            secondFrameLayout.setId(11);

            if(tablet)
            {
                mainLayout.addView(masterFrameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1));
                mainLayout.addView(secondFrameLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));

            }
            else
            {
                mainLayout.addView(masterFrameLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            setContentView(mainLayout);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            gameListFragment = (GameListFragment) getFragmentManager().findFragmentByTag(GAME_LIST_TAG);

            if (gameListFragment == null)
            {
                gameListFragment = GameListFragment.newInstance();
                transaction.add(masterFrameLayout.getId(), gameListFragment, GAME_LIST_TAG); //TODO: use newInstance

            }
            gameListFragment.setOnGameSelectedListener(onGameSelectedListener());

            transaction.commit();

            //Event Listener Implementation
            manager = BattleShipManager.getInstance();
            manager.setOnNewGameListener(onNewGameListener());
            manager.setOnJoinGameLister(onJoinGameListener());

        }
        catch(Exception e)
        {
            Log.e("Exception FUCK ", e.getMessage());
            e.printStackTrace();
        }
    }




    public GameListFragment.OnGameSelectedListener onGameSelectedListener()
    {
        GameListFragment.OnGameSelectedListener listener = new GameListFragment.OnGameSelectedListener()
        {
            @Override
            public void OnGameSelected(BattleshipNetwork.GameList gameName)
            {
                if(gameName.status == GameStatus.WAITING)
                {
                    getName(gameName);
                }
                else if((gameName.status == GameStatus.DONE || gameName.status == GameStatus.PLAYING )&& !manager.myGame(gameName) )
                {
                    getGameDetail(gameName.id,true);
                }
                else if((gameName.status == GameStatus.DONE || gameName.status == GameStatus.PLAYING )&& manager.myGame(gameName))
                {
                    loadData(gameName,manager.getMyGame(gameName));
                }

            }

        };

        return listener;
    }


    public void joinGame(final String gameId, final String playerName)
    {

        AsyncTask<Void,Void,BattleshipNetwork.MyGame> joinGameTask = new AsyncTask<Void, Void, BattleshipNetwork.MyGame>()
        {

            @Override
            protected BattleshipNetwork.MyGame doInBackground(Void... params)
            {
                return BattleshipNetwork.joinGame(gameId, playerName);
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.MyGame myGame)
            {
                super.onPostExecute(myGame);
                BattleShipManager.getInstance().joinGame(myGame, playerName);
            }
        };

        joinGameTask.execute();
    }

    public void getGameDetail(final String gameId, final boolean selected)
    {

        AsyncTask<BattleshipNetwork.GameDetail,BattleshipNetwork.GameDetail,BattleshipNetwork.GameDetail> gameDetailTask = new AsyncTask<BattleshipNetwork.GameDetail, BattleshipNetwork.GameDetail, BattleshipNetwork.GameDetail>()
        {
            @Override
            protected BattleshipNetwork.GameDetail doInBackground(BattleshipNetwork.GameDetail... params)
            {
                return BattleshipNetwork.getGameDetails(gameId);
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.GameDetail gameDetail)
            {
                super.onPostExecute(gameDetail);
                if(selected)
                    showGameDetails(gameDetail);
                else
                    manager.setCurrentGameDetails(gameDetail);

            }
        };


        gameDetailTask.execute();
    }


    public void gameOver()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        gameListFragment = (GameListFragment) getFragmentManager().findFragmentByTag(GAME_LIST_TAG);

        if (gameListFragment == null)
        {
            gameListFragment = GameListFragment.newInstance();
            masterFrameLayout.removeAllViews();
            transaction.replace(masterFrameLayout.getId(), gameListFragment, GAME_LIST_TAG); //TODO: use newInstance

        }
        //transaction.add(masterFrameLayout.getId(), gameListFragment.newInstance(), GAME_LIST_TAG);
        gameListFragment.setOnGameSelectedListener(onGameSelectedListener());

        transaction.commit();

    }
    public BattleShipManager.OnNewGameListener onNewGameListener()
    {
        BattleShipManager.OnNewGameListener listener = new BattleShipManager.OnNewGameListener()
        {
            @Override
            public void onNewGameListener(BattleshipNetwork.MyGame name)
            {
                gameStarted = false;
                callIsTurnService();
                gameListFragment.loadList();
                //TODO need to check if someone has joined the game yet.
                //need to check if connected every 500ms
            }

        };
        return listener;
    }

    private BattleShipManager.OnJoinGameListener onJoinGameListener()
    {
        BattleShipManager.OnJoinGameListener listener = new BattleShipManager.OnJoinGameListener()
        {
            @Override
            public void onJoinListener(BattleshipNetwork.MyGame name)
            {
                gameStarted = true;
                callIsTurnService();
                BattleshipNetwork.MyGame temp = manager.getCurrentGame();
                getBoards(temp.gameId, temp.playerId);
                gameListFragment.loadList();
            }
        };
        return listener;
    }
    private void determineTurn() {
        // Make sure it is the user's turn
        AsyncTask<String, Integer, BattleshipNetwork.Turn> turnTask = new AsyncTask<String, Integer, BattleshipNetwork.Turn>() {
            @Override
            protected BattleshipNetwork.Turn doInBackground(String... params) {
                BattleshipNetwork.MyGame current = manager.getCurrentGame();
                return BattleshipNetwork.isTurn(current.gameId, current.playerId);
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.Turn turn) {
                if (turn == null)
                    return;
                super.onPostExecute(turn);

                if(updateBoard > 0)
                {
                    if(isTurn != turn.isYourTurn)
                        updateBoard = 1;

                }

                isTurn = turn.isYourTurn;
                if(isTurn)
                    gameStarted = true;
                if(gameStarted)
                {
                    if (updateBoard == 0)
                        updateBoard++;
                    else if (updateBoard == 1)
                    {
                        BattleshipNetwork.MyGame temp = manager.getCurrentGame();
                        getBoards(temp.gameId, temp.playerId);
                        updateBoard++;
                    }
                    else
                    {
                        updateBoard++;
                    }
                }
                String winner = turn.winner;
               if (!winner.equals("IN PROGRESS")) {
                   showWinner(winner);
                   handler.removeCallbacks(checkForNewPlayer);
                }

                return;

            }
        };
        turnTask.execute();
    }

    private Runnable checkForNewPlayer = new Runnable() {

        public void run()
        {

            if(!isTurn)
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Waiting for Opponent",Toast.LENGTH_SHORT);
                toast.show();

            }

            determineTurn();
            callIsTurnService();

        }
    };

    private void callIsTurnService() {
        handler.postDelayed(checkForNewPlayer, 1000);
    }

    private void getBoards(final String gameId, final String playerId)
    {
        AsyncTask<Void,Void,BattleshipNetwork.Boards> getBoardTask = new AsyncTask<Void, Void, BattleshipNetwork.Boards>()
        {
            @Override
            protected BattleshipNetwork.Boards doInBackground(Void... params)
            {
                return BattleshipNetwork.getBoard(gameId, playerId);
            }

            @Override
            protected void onPostExecute(BattleshipNetwork.Boards boards)
            {
                super.onPostExecute(boards);
                if(isTurn)
                {
                    manager.updateGameModel(boards);
                    getGameDetail(gameId, false);
                    startGame();
                  //  gameStarted = true;
                }
                if(viewGame)
                {
                    manager.updateGameModel(boards);
                    getGameDetail(gameId, false);
                    startGame();
                }
            }
        };
        getBoardTask.execute();
    }


    public void startGame()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        GameFragment fragment = GameFragment.newInstance(isTurn);
        if(tablet)
        {
            secondFrameLayout.removeAllViews();
            trans.add(secondFrameLayout.getId(),fragment);
        }
        else
        {
            masterFrameLayout.removeAllViews();
            trans.replace(masterFrameLayout.getId(), fragment);
        }


        trans.commit();
    }




    private void loadData(BattleshipNetwork.GameList gameList, BattleshipNetwork.MyGame myGame)
    {
        GameStatus status = gameList.status;
        switch (status)
        {
            case PLAYING:
                callIsTurnService();
                getBoards(myGame.gameId,myGame.playerId);
                break;
            case DONE:
                getBoards(myGame.gameId,myGame.playerId);
                isTurn = false;
                viewGame = true;
                break;
        }
    }


    @Override
    protected void onPause()
    {
        Log.d("onPause", "I am called");
        super.onPause();
        saveMyGames();

    }

    @Override
    protected void onResume()
    {
        Log.d("OnResume", "I am called");
        super.onResume();
        loadMyGames();
    }

    private void saveMyGames()
    {
            ArrayList<BattleshipNetwork.MyGame> gameNames = manager.getMyGames();
            Gson gson = new Gson();
            String jsonPolyPaths = gson.toJson(gameNames);
            try
            {
                File drawingFile = new File(getFilesDir(), filename1);
                drawingFile.createNewFile();
                FileWriter fileWriter = new FileWriter(drawingFile);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                writer.write(jsonPolyPaths);
                writer.close();
            } catch (Exception e)
            {
                Log.e("Persistence", "Error saving GameList file" + e.getMessage());
                e.printStackTrace();
            }

    }

    private void loadMyGames()
    {

        try
        {
            File drawingFile = new File(getFilesDir(), filename1);
            FileReader fileReader = new FileReader(drawingFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String polyLineJason = reader.readLine();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<BattleshipNetwork.MyGame>>(){}.getType();
            ArrayList<BattleshipNetwork.MyGame> gameList = gson.fromJson(polyLineJason, collectionType);
            manager.setMyGames(gameList);
            reader.close();
            drawingFile.delete();
        } catch (Exception e)
        {
            Log.e("Persistence", "Error opening GameList file" + e.getMessage());
            e.printStackTrace();

        }
    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void getName(final BattleshipNetwork.GameList  gameName)
    {
        final AlertDialog.Builder inputWindow = new AlertDialog.Builder(this);

        inputWindow.setTitle("Join a game");
        inputWindow.setMessage("Please enter name");

        final EditText playerName = new EditText(this);
        playerName.setHint("Player Name");
        inputWindow.setView(playerName);

        inputWindow.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Editable pvalue = playerName.getText();
                String pName = pvalue.toString();

                if ( pName != null && !pName.equals("") )
                {
                    BattleShipManager.getInstance().setPlayerName(pName); //TODO: Need to make this use my model
                    joinGame(gameName.id, manager.getPlayerName());
                }
            }
        });

        inputWindow.show();
    }

    public void showGameDetails(final BattleshipNetwork.GameDetail gameName)
    {
        final AlertDialog.Builder inputWindow = new AlertDialog.Builder(this);

        inputWindow.setTitle(gameName.name + " Stats");
        inputWindow.setMessage("\nPlayer 1: " + gameName.player1 +
                "\nPlayer 2: " + gameName.player2 +
                "\nWinner: " + gameName.winner +
                "\nNumber of Missles Fired: " + gameName.missilesLaunched);



        inputWindow.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });

        inputWindow.show();
    }

    public void showWinner(String winner)
    {
        String endMsg = "";
        if (winner.equals(manager.getPlayerName())) {
            // PLAYER WON
            endMsg += "YOU WON";
        }
        else {
            // OPPONENT WON
            endMsg += "YOU LOST";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                gameOver();
                gameListFragment.loadList();
                dialog.dismiss();
            }
        });
        builder.setMessage(endMsg)
                .setTitle("GAME OVER");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
