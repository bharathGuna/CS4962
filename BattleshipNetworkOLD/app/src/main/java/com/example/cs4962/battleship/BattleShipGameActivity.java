package com.example.cs4962.battleship;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
            manager.setOnGameOverListener(onGameOverListener());
        }
        catch(Exception e)
        {
            Log.e("Exception FUCK ", e.getMessage());
            e.printStackTrace();
        }
    }



/*
    public GameListFragment.OnGameSelectedListener onGameSelectedListener()
    {
        GameListFragment.OnGameSelectedListener listener = new GameListFragment.OnGameSelectedListener()
        {
            @Override
            public void OnGameSelected(String gameName)
            {
                manager.setCurrentGame(gameName);
                //TODO: need to load the data for this game
                loadData(gameName);
            }
        };

        return listener;
    }

    private void loadData(String gameName)
    {
        GameStatus status = manager.getGameMode(gameName);
        switch (status)
        {

            case PLAYER1_TURN:
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                int turn = manager.getPlayer2Board(gameName).getMisslesFired();
                trans = getFragmentManager().beginTransaction();
                GameFragment gfragment = GameFragment.newInstance(gameName,turn);
                if(tablet)
                {
                    secondFrameLayout.removeAllViews();
                    trans.add(secondFrameLayout.getId(),gfragment);
                }
                else
                {
                    masterFrameLayout.removeAllViews();
                    trans.replace(masterFrameLayout.getId(), gfragment);
                }
                //masterFrameLayout.removeAllViews();
                //trans.replace(masterFrameLayout.getId(), gfragment);
                trans.commit();
                manager.setGameMode(gameName, status);
                break;
            case PLAYER2_TURN:
                turn = manager.getPlayer1Board(gameName).getMisslesFired();
                trans = getFragmentManager().beginTransaction();
                gfragment = GameFragment.newInstance(gameName,turn);
                if(tablet)
                {
                    secondFrameLayout.removeAllViews();
                    trans.add(secondFrameLayout.getId(),gfragment);
                }
                else
                {
                    masterFrameLayout.removeAllViews();
                    trans.replace(masterFrameLayout.getId(), gfragment);
                }
                //masterFrameLayout.removeAllViews();
                //trans.replace(masterFrameLayout.getId(), gfragment);
                trans.commit();
                manager.setGameMode(gameName, status);
                break;
            case PLAYER1_WON:
                turn = manager.getPlayer2Board(gameName).getMisslesFired();
                trans = getFragmentManager().beginTransaction();
                gfragment = GameFragment.newInstance(gameName,turn);
                if(tablet)
                {
                    secondFrameLayout.removeAllViews();
                    trans.add(secondFrameLayout.getId(),gfragment);
                }
                else
                {
                    masterFrameLayout.removeAllViews();
                    trans.replace(masterFrameLayout.getId(), gfragment);
                }
                //masterFrameLayout.removeAllViews();
                //trans.replace(masterFrameLayout.getId(), gfragment);
                trans.commit();
                manager.setGameMode(gameName, status);
                break;
            case PLAYER2_WON:
                turn = manager.getPlayer1Board(gameName).getMisslesFired();
                trans = getFragmentManager().beginTransaction();
                gfragment = GameFragment.newInstance(gameName,turn);
                if(tablet)
                {
                    secondFrameLayout.removeAllViews();
                    trans.add(secondFrameLayout.getId(),gfragment);
                }
                else
                {
                    masterFrameLayout.removeAllViews();
                    trans.replace(masterFrameLayout.getId(), gfragment);
                }
                //masterFrameLayout.removeAllViews();
                //trans.replace(masterFrameLayout.getId(), gfragment);
                trans.commit();
                manager.setGameMode(gameName, status);
                break;
        }
    }
*/
    public BattleShipManager.OnGameOverListener onGameOverListener()
    {
        BattleShipManager.OnGameOverListener listener = new BattleShipManager.OnGameOverListener()
        {
            @Override
            public void OnGameOverListener(String name)
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
        };
        return listener;

    }
    public BattleShipManager.OnNewGameListener onNewGameListener()
    {
        BattleShipManager.OnNewGameListener listener = new BattleShipManager.OnNewGameListener()
        {
            @Override
            public void onNewGameListener(String name)
            {

            }
        };
        return listener;
    }



    @Override
    protected void onPause()
    {
        Log.d("onPause","I am called");
        super.onPause();
        saveGameList();
        saveGames();

    }

    @Override
    protected void onResume()
    {
        Log.d("OnResume", "I am called");
        super.onResume();
        loadGameList();
        loadGames();
        if(manager.getCurrentGame() != null)
            loadData(manager.getCurrentGame());

    }

    private void saveGameList()
    {
            ArrayList<String> gameNames = manager.getGameNames();
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
    private void saveGames()
    {
        Gson gson = new Gson();
        //String jsonPolyPaths = gson.toJson(polyPaths);
        try
        {
            File drawingFile = new File(getFilesDir(), filename2);
            drawingFile.createNewFile();
            FileWriter fileWriter = new FileWriter(drawingFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for(GameModel gm : manager.getGameModels())
            {
                //Player 1
                //saving board model
                BoardModel player1 = gm.getPlayer1();
                String toSave = gson.toJson(player1.getBoard());
                writer.write(toSave + "\n");
                //saving ArrayList<ShipType> Ships that need to placed
                toSave = gson.toJson(player1.getAvailableShips());
                writer.write(toSave + "\n");
                //saving Arraylist<ShipModel> remaining Ships
                toSave = gson.toJson(player1.getPlacedShips());
                writer.write(toSave + "\n");
                //saving ArrayList<ShipMode> sunk ships
                toSave = gson.toJson(player1.getSunkShip());
                writer.write(toSave + "\n");
                toSave = gson.toJson(player1.getMisslesFired());
                writer.write(toSave+"\n");
                //Player 2
                //saving board model
                BoardModel player2 = gm.getPlayer2();
                toSave = gson.toJson(player2.getBoard());
                writer.write(toSave + "\n");
                //saving ArrayList<ShipType> Ships that need to placed
                toSave = gson.toJson(player2.getAvailableShips());
                writer.write(toSave + "\n");
                //saving Arraylist<ShipModel> remaining Ships
                toSave = gson.toJson(player2.getPlacedShips());
                writer.write(toSave + "\n");
                //saving ArrayList<ShipMode> sunk ships
                toSave = gson.toJson(player2.getSunkShip());
                writer.write(toSave + "\n");
                toSave = gson.toJson(player2.getMisslesFired());
                writer.write(toSave+"\n");
                toSave = gson.toJson(gm.getMode());
                writer.write(toSave + "\n");
                String current = manager.getCurrentGame();
                current = gson.toJson(current);
                writer.write(current + "\n");

            }
            writer.close();
        } catch (Exception e)
        {
            Log.e("Persistence", "Error saving games file" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadGameList()
    {

        try
        {
            File drawingFile = new File(getFilesDir(), filename1);
            FileReader fileReader = new FileReader(drawingFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String polyLineJason = reader.readLine();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> gameList = gson.fromJson(polyLineJason, collectionType);
            manager.setGames(gameList);
            reader.close();
            drawingFile.delete();
        } catch (Exception e)
        {
            Log.e("Persistence", "Error opening GameList file" + e.getMessage());
            e.printStackTrace();

        }
    }

    private void loadGames()
    {
        Gson gson = new Gson();
        //String jsonPolyPaths = gson.toJson(polyPaths);
        ArrayList<GameModel> games = new ArrayList<>();
        try
        {
            File drawingFile = new File(getFilesDir(), filename2);
            FileReader fileReader = new FileReader(drawingFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String first = "";
            String current = "";
            while( (first = reader.readLine()) != null)
            {

                //Player 1
                //reading board model
                String readLine = first;
                Type collectionType = new TypeToken<CellModel[][]>(){}.getType();
                CellModel[][] player1 = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                //read ArrayList<ShipType> Ships that need to placed
                collectionType = new TypeToken<ArrayList<ShipType>>(){}.getType();
                ArrayList<ShipType> availableShips = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                //read Arraylist<ShipModel> remaining Ships
                collectionType = new TypeToken<ArrayList<ShipModel>>(){}.getType();
                ArrayList<ShipModel> ships = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                //read ArrayList<ShipMode> sunk ships
                collectionType = new TypeToken<ArrayList<ShipModel>>(){}.getType();
                ArrayList<ShipModel> sunkShips = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                int misslesFired = gson.fromJson(readLine,Integer.class);
                //Player 2
                readLine = reader.readLine();
                collectionType = new TypeToken<CellModel[][]>(){}.getType();
                CellModel[][] player2 = gson.fromJson(readLine, collectionType);
                readLine = reader.readLine();
                //read ArrayList<ShipType> Ships that need to placed
                collectionType = new TypeToken<ArrayList<ShipType>>(){}.getType();
                ArrayList<ShipType> availableShips2 = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                //read Arraylist<ShipModel> remaining Ships
                collectionType = new TypeToken<ArrayList<ShipModel>>(){}.getType();
                ArrayList<ShipModel> ships2 = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                //read ArrayList<ShipMode> sunk ships
                collectionType = new TypeToken<ArrayList<ShipModel>>(){}.getType();
                ArrayList<ShipModel> sunkShips2 = gson.fromJson(readLine,collectionType);
                readLine = reader.readLine();
                int misslesFired2 = gson.fromJson(readLine,Integer.class);
                readLine = reader.readLine();
                //reading GameStatus
                collectionType = new TypeToken<GameStatus>(){}.getType();
                GameStatus status = gson.fromJson(readLine,collectionType);

                readLine = reader.readLine();
                current = gson.fromJson(readLine,String.class);
                //Creating GameModel
                GameModel model = new GameModel();
                //creating player 1
                model.getPlayer1().setBoard(player1);
                model.getPlayer1().setAvailableShips(availableShips);
                model.getPlayer1().setPlacedShips(ships);
                model.getPlayer1().setSunkShips(sunkShips);
                model.getPlayer1().setMisslesFired(misslesFired);
                //creating player 2
                model.getPlayer2().setBoard(player2);
                model.getPlayer2().setAvailableShips(availableShips2);
                model.getPlayer2().setPlacedShips(ships2);
                model.getPlayer2().setSunkShips(sunkShips2);
                model.getPlayer2().setMisslesFired(misslesFired2);
                model.setMode(status);
                games.add(model);
            }
            manager.setGameModels(games);
            manager.setCurrentGame(current);
            reader.close();
            drawingFile.delete();
        } catch (Exception e)
        {
            Log.e("Persistence", "Error opening games file" + e.getMessage());
            e.printStackTrace();
        }


    }

    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
